package com.jamm.webhook;

import com.api.v1.ChargeMessage;
import com.api.v1.ContractMessage;
import com.api.v1.EventType;
import com.api.v1.UserAccountMessage;
import com.jamm.errors.InvalidSignatureException;
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
            case EVENT_TYPE_REFUND_SUCCEEDED:
            case EVENT_TYPE_REFUND_FAILED:
                ChargeMessage.Builder chargeBuilder = ChargeMessage.newBuilder();
                JsonFormat.parser().ignoringUnknownFields().merge(contentJson, chargeBuilder);
                return chargeBuilder.build();

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
