package com.jamm.webhook;

import com.api.v1.ChargeMessage;
import com.api.v1.ContractMessage;
import com.api.v1.EventType;
import com.api.v1.RefundInfo;
import com.api.v1.UserAccountMessage;
import com.jamm.errors.InvalidSignatureException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Webhook utilities for parsing and verifying incoming webhook messages from Jamm.
 *
 * <p>The webhook payload is a JSON object with snake_case field names:
 * <pre>{@code
 * {
 *   "id": "mwh-...",
 *   "signature": "sha256=...",
 *   "event_type": "EVENT_TYPE_CHARGE_SUCCESS",
 *   "content": { ... },
 *   "created_at": "2024-01-01T00:00:00Z"
 * }
 * }</pre>
 *
 * <p>Example usage:
 * <pre>{@code
 * // In your webhook endpoint handler
 * String jsonBody = request.getBody();
 *
 * // Prefer verifyAndParse / verifyAndParseEvent below — they verify over the exact
 * // received bytes. If you verify manually, do it over the raw content bytes from the
 * // request body, not a re-serialized copy (re-serialization un-escapes &, <, > and
 * // breaks the signature).
 *
 * // Parse the webhook message
 * Object content = Webhook.parse(jsonBody);
 *
 * if (content instanceof ChargeMessage) {
 *     ChargeMessage charge = (ChargeMessage) content;
 *     // Handle charge event
 * } else if (content instanceof ContractMessage) {
 *     ContractMessage contract = (ContractMessage) content;
 *     // Handle contract event
 * }
 * }</pre>
 */
public final class Webhook {

    private static final String HMAC_SHA256 = "HmacSHA256";

    private Webhook() {
        // Utility class, prevent instantiation
    }

    /**
     * Parses the incoming webhook JSON data and returns the appropriate message content.
     *
     * <p>This method does NOT verify the signature. You should call {@link #verify(String, String, String)}
     * before using this method to ensure the webhook is authentic.
     *
     * @param json the raw JSON string from the webhook request body
     * @return the parsed content object (ChargeMessage, ContractMessage, or UserAccountMessage)
     * @throws InvalidProtocolBufferException if the JSON cannot be parsed
     * @throws IllegalArgumentException if the json is null/empty, or the event type is unsupported or missing
     */
    public static Object parse(String json) throws InvalidProtocolBufferException {
        return parseEvent(json).getContent();
    }

    /**
     * Parses the incoming webhook JSON and returns both the {@code event_type} and the parsed
     * content. Like {@link #parse(String)}, but also exposes the event type — the reliable way to
     * distinguish {@code EVENT_TYPE_CHARGE_SUCCESS} from {@code EVENT_TYPE_CHARGE_FAIL}, or charge
     * events from refund events (both deserialize to a {@code ChargeMessage}).
     *
     * <p>This method does NOT verify the signature. Prefer {@link #verifyAndParseEvent(String, String)}.
     *
     * @param json the raw JSON string from the webhook request body
     * @return the event type and parsed content
     * @throws InvalidProtocolBufferException if the content cannot be parsed
     * @throws IllegalArgumentException if the json is null/empty, or the event type is unsupported or missing
     */
    public static WebhookEvent parseEvent(String json) throws InvalidProtocolBufferException {
        if (json == null || json.isEmpty()) {
            throw new IllegalArgumentException("json cannot be null or empty");
        }

        JsonObject rootNode = parseObject(json);

        // Extract event type using proper JSON parsing
        String eventTypeString = extractEventType(rootNode);
        EventType eventType = parseEventType(eventTypeString);

        // Extract content JSON using proper JSON parsing
        String contentJson = extractContentJson(rootNode);

        Object content;
        switch (eventType) {
            case EVENT_TYPE_CHARGE_CREATED:
            case EVENT_TYPE_CHARGE_UPDATED:
            case EVENT_TYPE_CHARGE_SUCCESS:
            case EVENT_TYPE_CHARGE_FAIL:
                content = mergeContent(contentJson, ChargeMessage.newBuilder());
                break;

            case EVENT_TYPE_REFUND_SUCCEEDED:
            case EVENT_TYPE_REFUND_FAILED:
                content = parseRefundContent(contentJson);
                break;

            case EVENT_TYPE_CONTRACT_ACTIVATED:
                content = mergeContent(contentJson, ContractMessage.newBuilder());
                break;

            case EVENT_TYPE_USER_ACCOUNT_DELETED:
                content = mergeContent(contentJson, UserAccountMessage.newBuilder());
                break;

            default:
                throw new IllegalArgumentException("Unsupported event type: " + eventType);
        }
        return new WebhookEvent(eventType, content);
    }

    /**
     * Verifies the signature and parses an incoming webhook in one step. This is the
     * recommended entry point: it removes the risk of parsing an unverified (possibly
     * forged) webhook, and it verifies over the exact received {@code content} bytes so
     * verification is not broken by JSON re-serialization.
     *
     * <p>The Jamm backend computes the HMAC over the raw {@code content} bytes it transmits.
     * Those bytes are produced by Go's JSON encoder, which escapes {@code &}, {@code <} and
     * {@code >} as {@code &amp;}, {@code &lt;}, {@code &gt;}. Re-serializing the parsed
     * {@code content} (parsing it and serializing again) un-escapes those characters, so the
     * recomputed digest no longer matches. This method avoids that by slicing the raw
     * {@code content} substring out of {@code rawBody} verbatim for verification.
     *
     * @param rawBody      the raw, unmodified webhook request body
     * @param clientSecret the merchant client secret used for HMAC verification
     * @return the parsed content object (ChargeMessage, ContractMessage, or UserAccountMessage)
     * @throws InvalidSignatureException if the signature does not match
     * @throws InvalidProtocolBufferException if the content cannot be parsed
     * @throws IllegalArgumentException if rawBody or clientSecret is null/empty, or the payload
     *                                  is missing the {@code signature} or {@code content} field
     */
    public static Object verifyAndParse(String rawBody, String clientSecret)
            throws InvalidProtocolBufferException {
        verifySignature(rawBody, clientSecret);
        return parse(rawBody);
    }

    /**
     * Verifies the signature and parses in one step, returning both the {@code event_type} and the
     * content. Same guarantees as {@link #verifyAndParse(String, String)} (verification is done over
     * the exact received {@code content} bytes), but also exposes the event type.
     *
     * @param rawBody      the raw, unmodified webhook request body
     * @param clientSecret the merchant client secret used for HMAC verification
     * @return the event type and parsed content
     * @throws InvalidSignatureException if the signature does not match
     * @throws InvalidProtocolBufferException if the content cannot be parsed
     * @throws IllegalArgumentException if rawBody or clientSecret is null/empty, or the payload
     *                                  is missing the {@code signature} or {@code content} field
     */
    public static WebhookEvent verifyAndParseEvent(String rawBody, String clientSecret)
            throws InvalidProtocolBufferException {
        verifySignature(rawBody, clientSecret);
        return parseEvent(rawBody);
    }

    /**
     * Verifies the webhook HMAC signature over the exact received {@code content} bytes.
     */
    private static void verifySignature(String rawBody, String clientSecret) {
        if (rawBody == null || rawBody.isEmpty()) {
            throw new IllegalArgumentException("rawBody cannot be null or empty");
        }
        if (clientSecret == null || clientSecret.isEmpty()) {
            throw new IllegalArgumentException("clientSecret cannot be null or empty");
        }

        JsonObject rootNode = parseObject(rawBody);

        JsonElement signatureNode = rootNode.get("signature");
        if (signatureNode == null || !signatureNode.isJsonPrimitive()) {
            throw new IllegalArgumentException("Webhook message does not contain 'signature' field");
        }

        String rawContent = extractRawContent(rawBody);
        verify(rawContent, signatureNode.getAsString(), clientSecret);
    }

    /**
     * Extracts the raw {@code content} substring from the webhook body verbatim, without
     * decoding or re-serializing it, so the exact signed bytes are recovered for HMAC
     * verification. Scans the top-level object for the {@code content} field and returns its
     * value's exact span from {@code rawBody} — including any {@code \\u0026}-style escapes the
     * backend emitted, which a JSON-tree round-trip would collapse.
     */
    private static String extractRawContent(String rawBody) {
        int n = rawBody.length();
        int i = skipWhitespace(rawBody, 0);
        if (i >= n || rawBody.charAt(i) != '{') {
            throw new IllegalArgumentException("Webhook body must be a JSON object");
        }
        i++; // past '{'
        String content = null;
        while (true) {
            i = skipWhitespace(rawBody, i);
            if (i >= n || rawBody.charAt(i) == '}') {
                break;
            }
            if (rawBody.charAt(i) != '"') {
                throw new IllegalArgumentException("Invalid JSON format: expected field name");
            }
            int keyEnd = skipString(rawBody, i); // index just past the key's closing quote
            String key = rawBody.substring(i + 1, keyEnd - 1);
            i = skipWhitespace(rawBody, keyEnd);
            if (i >= n || rawBody.charAt(i) != ':') {
                throw new IllegalArgumentException("Invalid JSON format: expected ':'");
            }
            i = skipWhitespace(rawBody, i + 1);
            int valueStart = i;
            int valueEnd = skipValue(rawBody, i);
            if ("content".equals(key)) {
                // Reject a duplicate top-level "content". We sign the first occurrence, but the
                // JSON parser keeps the last on duplicate keys — a mismatch an attacker could use
                // to have one value verified and a different one deserialized.
                if (content != null) {
                    throw new IllegalArgumentException("Webhook body contains multiple 'content' fields");
                }
                content = rawBody.substring(valueStart, valueEnd);
            }
            i = skipWhitespace(rawBody, valueEnd);
            if (i < n && rawBody.charAt(i) == ',') {
                i++;
            }
        }
        if (content == null) {
            throw new IllegalArgumentException("Webhook message does not contain 'content' field");
        }
        return content;
    }

    private static int skipWhitespace(String s, int i) {
        while (i < s.length()) {
            char c = s.charAt(i);
            if (c != ' ' && c != '\t' && c != '\n' && c != '\r') {
                break;
            }
            i++;
        }
        return i;
    }

    /**
     * Given {@code s.charAt(i) == '"'}, returns the index just past the string's closing quote,
     * honoring backslash escapes.
     */
    private static int skipString(String s, int i) {
        int n = s.length();
        i++; // past opening quote
        while (i < n) {
            char c = s.charAt(i);
            if (c == '\\') {
                i += 2;
                continue;
            }
            if (c == '"') {
                return i + 1;
            }
            i++;
        }
        throw new IllegalArgumentException("Invalid JSON format: unterminated string");
    }

    /**
     * Returns the index just past the complete JSON value starting at {@code i}: a string, a
     * balanced object/array (respecting nested strings), or a bare primitive.
     */
    private static int skipValue(String s, int i) {
        int n = s.length();
        char c = s.charAt(i);
        if (c == '"') {
            return skipString(s, i);
        }
        if (c == '{' || c == '[') {
            int depth = 0;
            boolean inString = false;
            for (; i < n; i++) {
                char ch = s.charAt(i);
                if (inString) {
                    if (ch == '\\') {
                        i++;
                    } else if (ch == '"') {
                        inString = false;
                    }
                } else if (ch == '"') {
                    inString = true;
                } else if (ch == '{' || ch == '[') {
                    depth++;
                } else if (ch == '}' || ch == ']') {
                    depth--;
                    if (depth == 0) {
                        return i + 1;
                    }
                }
            }
            throw new IllegalArgumentException("Invalid JSON format: unbalanced value");
        }
        // primitive: number, true, false, null — runs until a structural delimiter
        while (i < n) {
            char ch = s.charAt(i);
            if (ch == ',' || ch == '}' || ch == ']'
                    || ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r') {
                break;
            }
            i++;
        }
        return i;
    }

    /**
     * Merges the {@code content} JSON into the given builder (ignoring unknown fields) and builds it.
     */
    private static Message mergeContent(String contentJson, Message.Builder builder)
            throws InvalidProtocolBufferException {
        JsonFormat.parser().ignoringUnknownFields().merge(contentJson, builder);
        return builder.build();
    }

    /**
     * Parses a refund webhook's {@code content} into a {@link ChargeMessage}.
     *
     * <p>The current backend format nests the charge under {@code transaction} and the refund
     * details under {@code refund}:
     * <pre>{@code { "transaction": { ...ChargeMessage... }, "refund": { "id": "rfd-...", ... } } }</pre>
     * Older payloads sent the transaction fields flat. Both are flattened into a single
     * {@code ChargeMessage} (with its {@code RefundInfo} populated) so consumers read one shape.
     */
    private static ChargeMessage parseRefundContent(String contentJson)
            throws InvalidProtocolBufferException {
        JsonObject contentNode;
        try {
            contentNode = JsonParser.parseString(contentJson).getAsJsonObject();
        } catch (JsonParseException | IllegalStateException e) {
            throw new IllegalArgumentException("Invalid refund content JSON: " + e.getMessage(), e);
        }

        ChargeMessage.Builder charge = ChargeMessage.newBuilder();
        JsonElement transactionNode = contentNode.get("transaction");
        if (transactionNode != null && !transactionNode.isJsonNull()) {
            JsonFormat.parser().ignoringUnknownFields().merge(transactionNode.toString(), charge);
            JsonElement refundNode = contentNode.get("refund");
            if (refundNode != null && !refundNode.isJsonNull()) {
                RefundInfo.Builder refund = RefundInfo.newBuilder();
                JsonFormat.parser().ignoringUnknownFields().merge(refundNode.toString(), refund);
                charge.setRefund(refund.build());
                // Lift the refund id onto the flat refund_id attribute too, so callers reading the
                // charge directly see it without descending into the wrapper. Matches the Go, Node
                // and Ruby SDKs. Failure events may omit the id, hence the guard.
                if (refund.hasId()) {
                    charge.setRefundId(refund.getId());
                }
            }
        } else {
            JsonFormat.parser().ignoringUnknownFields().merge(contentJson, charge);
        }
        return charge.build();
    }

    /**
     * Verifies the HMAC SHA-256 signature of webhook data.
     *
     * <p>The Jamm backend computes the signature over the {@code content} JSON object
     * (not the entire webhook payload) and includes it in the payload's {@code signature}
     * field. To verify, extract the {@code content} JSON and {@code signature} value
     * from the webhook payload, then pass them to this method.
     *
     * @param json the JSON string to verify (typically the {@code content} field from the webhook payload)
     * @param signature the signature value from the webhook payload's {@code signature} field (format: "sha256=...")
     * @param clientSecret the merchant client secret used for HMAC verification
     * @throws InvalidSignatureException if the signature does not match
     * @throws IllegalArgumentException if any parameter is null or empty
     */
    public static void verify(String json, String signature, String clientSecret) {
        if (json == null || json.isEmpty()) {
            throw new IllegalArgumentException("json cannot be null or empty");
        }
        if (signature == null || signature.isEmpty()) {
            throw new IllegalArgumentException("signature cannot be null or empty");
        }
        if (clientSecret == null || clientSecret.isEmpty()) {
            throw new IllegalArgumentException("clientSecret cannot be null or empty");
        }

        try {
            String computed = computeSignature(json, clientSecret);
            String expected = "sha256=" + computed;

            if (!secureCompare(expected, signature)) {
                throw new InvalidSignatureException("Digests do not match");
            }
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to compute HMAC signature", e);
        }
    }

    /**
     * Extracts the event_type value from the parsed JSON node.
     * Supports both snake_case ("event_type") and camelCase ("eventType") field names.
     */
    private static String extractEventType(JsonObject rootNode) {
        JsonElement eventTypeNode = rootNode.get("event_type");
        if (eventTypeNode == null || eventTypeNode.isJsonNull()) {
            eventTypeNode = rootNode.get("eventType");
        }
        if (eventTypeNode == null || !eventTypeNode.isJsonPrimitive()) {
            throw new IllegalArgumentException("Webhook message does not contain 'event_type' field");
        }
        return eventTypeNode.getAsString();
    }

    /**
     * Extracts the content field as a JSON string from the parsed JSON node.
     */
    private static String extractContentJson(JsonObject rootNode) {
        JsonElement contentNode = rootNode.get("content");
        if (contentNode == null || contentNode.isJsonNull()) {
            throw new IllegalArgumentException("Webhook message does not contain 'content' field");
        }
        return contentNode.toString();
    }

    /**
     * Parses {@code json} into a JSON object, or throws {@link IllegalArgumentException} if it is
     * not valid JSON or not an object.
     */
    private static JsonObject parseObject(String json) {
        try {
            return JsonParser.parseString(json).getAsJsonObject();
        } catch (JsonParseException | IllegalStateException e) {
            throw new IllegalArgumentException("Invalid JSON format: " + e.getMessage(), e);
        }
    }

    /**
     * Parses the event type string to the EventType enum.
     */
    private static EventType parseEventType(String eventTypeString) {
        try {
            return EventType.valueOf(eventTypeString);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown event type: " + eventTypeString, e);
        }
    }

    /**
     * Computes the HMAC SHA-256 signature for the given data.
     *
     * @param data the data to sign
     * @param secret the secret key
     * @return the hex-encoded signature
     */
    private static String computeSignature(String data, String secret)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secretKeySpec = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hmacBytes);
    }

    /**
     * Converts a byte array to a hex string.
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Securely compares two strings in constant time to prevent timing attacks.
     *
     * @param a first string
     * @param b second string
     * @return true if the strings are equal, false otherwise
     */
    private static boolean secureCompare(String a, String b) {
        if (a == null || b == null) {
            return false;
        }

        byte[] aBytes = a.getBytes(StandardCharsets.UTF_8);
        byte[] bBytes = b.getBytes(StandardCharsets.UTF_8);

        if (aBytes.length != bBytes.length) {
            return false;
        }

        int result = 0;
        for (int i = 0; i < aBytes.length; i++) {
            result |= aBytes[i] ^ bBytes[i];
        }
        return result == 0;
    }
}
