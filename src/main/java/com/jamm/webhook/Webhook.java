package com.jamm.webhook;

import com.api.v1.ChargeMessage;
import com.api.v1.ContractMessage;
import com.api.v1.EventType;
import com.api.v1.RefundInfo;
import com.api.v1.UserAccountMessage;
import com.jamm.errors.InvalidSignatureException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
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
 * // The signature is in the webhook payload's "signature" field and is
 * // computed over the "content" JSON. Extract and verify it:
 * ObjectMapper mapper = new ObjectMapper();
 * JsonNode payload = mapper.readTree(jsonBody);
 * String signature = payload.get("signature").asText();
 * String contentJson = payload.get("content").toString();
 * Webhook.verify(contentJson, signature, clientSecret);
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
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
        if (json == null || json.isEmpty()) {
            throw new IllegalArgumentException("json cannot be null or empty");
        }

        JsonNode rootNode;
        try {
            rootNode = OBJECT_MAPPER.readTree(json);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid JSON format: " + e.getMessage(), e);
        }

        // Extract event type using proper JSON parsing
        String eventTypeString = extractEventType(rootNode);
        EventType eventType = parseEventType(eventTypeString);

        // Extract content JSON using proper JSON parsing
        String contentJson = extractContentJson(rootNode);

        switch (eventType) {
            case EVENT_TYPE_CHARGE_CREATED:
            case EVENT_TYPE_CHARGE_UPDATED:
            case EVENT_TYPE_CHARGE_SUCCESS:
            case EVENT_TYPE_CHARGE_FAIL:
                ChargeMessage.Builder chargeBuilder = ChargeMessage.newBuilder();
                JsonFormat.parser().ignoringUnknownFields().merge(contentJson, chargeBuilder);
                return chargeBuilder.build();

            case EVENT_TYPE_REFUND_SUCCEEDED:
            case EVENT_TYPE_REFUND_FAILED:
                return parseRefundContent(contentJson);

            case EVENT_TYPE_CONTRACT_ACTIVATED:
                ContractMessage.Builder contractBuilder = ContractMessage.newBuilder();
                JsonFormat.parser().ignoringUnknownFields().merge(contentJson, contractBuilder);
                return contractBuilder.build();

            case EVENT_TYPE_USER_ACCOUNT_DELETED:
                UserAccountMessage.Builder userAccountBuilder = UserAccountMessage.newBuilder();
                JsonFormat.parser().ignoringUnknownFields().merge(contentJson, userAccountBuilder);
                return userAccountBuilder.build();

            default:
                throw new IllegalArgumentException("Unsupported event type: " + eventType);
        }
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
     * {@code content} (e.g. {@code JsonNode.toString()}) un-escapes those characters, so the
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
        if (rawBody == null || rawBody.isEmpty()) {
            throw new IllegalArgumentException("rawBody cannot be null or empty");
        }
        if (clientSecret == null || clientSecret.isEmpty()) {
            throw new IllegalArgumentException("clientSecret cannot be null or empty");
        }

        JsonNode rootNode;
        try {
            rootNode = OBJECT_MAPPER.readTree(rawBody);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid JSON format: " + e.getMessage(), e);
        }

        JsonNode signatureNode = rootNode.get("signature");
        if (signatureNode == null || signatureNode.isNull()) {
            throw new IllegalArgumentException("Webhook message does not contain 'signature' field");
        }

        String rawContent = extractRawContent(rawBody);
        verify(rawContent, signatureNode.asText(), clientSecret);
        return parse(rawBody);
    }

    /**
     * Extracts the raw {@code content} substring from the webhook body verbatim, without
     * decoding or re-serializing it, so the exact signed bytes are recovered for HMAC
     * verification. Uses a streaming parser to locate the top-level {@code content} value.
     */
    private static String extractRawContent(String rawBody) {
        try (JsonParser parser = OBJECT_MAPPER.getFactory().createParser(rawBody)) {
            if (parser.nextToken() != JsonToken.START_OBJECT) {
                throw new IllegalArgumentException("Webhook body must be a JSON object");
            }
            while (parser.nextToken() == JsonToken.FIELD_NAME) {
                String field = parser.currentName();
                parser.nextToken(); // advance to the field's value
                if ("content".equals(field)) {
                    int start = (int) parser.currentTokenLocation().getCharOffset();
                    parser.skipChildren();
                    int end = (int) parser.currentLocation().getCharOffset();
                    return rawBody.substring(start, end);
                }
                parser.skipChildren();
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid JSON format: " + e.getMessage(), e);
        }
        throw new IllegalArgumentException("Webhook message does not contain 'content' field");
    }

    /**
     * Parses a refund webhook's {@code content} into a {@link ChargeMessage}.
     *
     * <p>The current backend format nests the charge under {@code transaction} and the refund
     * details under {@code refund}:
     * <pre>{@code { "transaction": { ...ChargeMessage... }, "refund": { "refund_id": "rfd-...", ... } } }</pre>
     * Older payloads sent the transaction fields flat. Both are flattened into a single
     * {@code ChargeMessage} (with its {@code RefundInfo} populated) so consumers read one shape.
     */
    private static ChargeMessage parseRefundContent(String contentJson)
            throws InvalidProtocolBufferException {
        JsonNode contentNode;
        try {
            contentNode = OBJECT_MAPPER.readTree(contentJson);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid refund content JSON: " + e.getMessage(), e);
        }

        ChargeMessage.Builder charge = ChargeMessage.newBuilder();
        JsonNode transactionNode = contentNode.get("transaction");
        if (transactionNode != null && !transactionNode.isNull()) {
            JsonFormat.parser().ignoringUnknownFields().merge(transactionNode.toString(), charge);
            JsonNode refundNode = contentNode.get("refund");
            if (refundNode != null && !refundNode.isNull()) {
                RefundInfo.Builder refund = RefundInfo.newBuilder();
                JsonFormat.parser().ignoringUnknownFields().merge(refundNode.toString(), refund);
                charge.setRefund(refund.build());
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
    private static String extractEventType(JsonNode rootNode) {
        JsonNode eventTypeNode = rootNode.get("event_type");
        if (eventTypeNode == null || eventTypeNode.isNull()) {
            eventTypeNode = rootNode.get("eventType");
        }
        if (eventTypeNode == null || eventTypeNode.isNull()) {
            throw new IllegalArgumentException("Webhook message does not contain 'event_type' field");
        }
        return eventTypeNode.asText();
    }

    /**
     * Extracts the content field as a JSON string from the parsed JSON node.
     */
    private static String extractContentJson(JsonNode rootNode) {
        JsonNode contentNode = rootNode.get("content");
        if (contentNode == null || contentNode.isNull()) {
            throw new IllegalArgumentException("Webhook message does not contain 'content' field");
        }
        return contentNode.toString();
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
