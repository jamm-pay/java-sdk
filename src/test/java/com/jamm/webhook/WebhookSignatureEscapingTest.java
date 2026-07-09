package com.jamm.webhook;

import com.api.v1.ChargeMessage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jamm.errors.InvalidSignatureException;
import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Covers webhook signature verification when the signed {@code content} contains characters
 * that Go's JSON encoder escapes but Java's does not: '&' -> &, '<' -> <,
 * '>' -> > (verified against the backend). The signature is computed over the exact
 * transmitted bytes, so verification must be done over those raw bytes — not a re-serialized
 * copy of the parsed content.
 */
class WebhookSignatureEscapingTest {

    private static final String SECRET = "test-secret";

    // Exactly what arrives on the wire: '&' -> &, '>' -> >, '<' -> < (Go json.Marshal).
    // In this Java literal, "\\u0026" is the six literal characters \ u 0 0 2 6.
    private static final String CONTENT_WIRE =
            "{\"id\":\"trx-1\",\"customer\":\"cus-1\",\"status\":\"STATUS_SUCCESS\","
          + "\"description\":\"Plan A \\u003e B \\u003ctag\\u003e\",\"merchant_name\":\"Ben \\u0026 Jerry's\","
          + "\"initial_amount\":300,\"discount\":0,\"final_amount\":300,\"currency\":\"JPY\","
          + "\"created_at\":\"2024-11-29T02:17:05Z\",\"updated_at\":\"2024-11-29T02:17:07Z\"}";

    private static String hmac(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] out = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : out) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String buildBody() {
        // Signature computed over the raw wire bytes, as the backend does.
        String sig = "sha256=" + hmac(CONTENT_WIRE, SECRET);
        return "{\"id\":\"mwh-1\",\"signature\":\"" + sig + "\","
             + "\"event_type\":\"EVENT_TYPE_CHARGE_SUCCESS\","
             + "\"content\":" + CONTENT_WIRE + "}";
    }

    @Test
    void verifyAndParseSucceedsWhenContentHasEscapedCharacters() throws Exception {
        Object content = Webhook.verifyAndParse(buildBody(), SECRET);

        assertInstanceOf(ChargeMessage.class, content);
        ChargeMessage charge = (ChargeMessage) content;
        // Parsed values are un-escaped as expected.
        assertEquals("Ben & Jerry's", charge.getMerchantName());
        assertEquals("Plan A > B <tag>", charge.getDescription());
    }

    @Test
    void verifyAndParseSucceedsWhenContentIsNested() throws Exception {
        // Refund webhooks nest the content ({ transaction: {...}, refund: {...} }). The raw-content
        // extractor must recover the balanced nested object exactly, escapes and all.
        String nestedContent =
                "{\"transaction\":{\"id\":\"trx-2\",\"customer\":\"cus-2\",\"status\":\"STATUS_REFUNDED\","
              + "\"description\":\"A \\u0026 B\",\"final_amount\":300,\"currency\":\"JPY\"},"
              + "\"refund\":{\"refund_id\":\"rfd-1\",\"amount_refunded\":100}}";
        String body = "{\"id\":\"mwh-2\",\"signature\":\"sha256=" + hmac(nestedContent, SECRET) + "\","
                + "\"event_type\":\"EVENT_TYPE_REFUND_SUCCEEDED\","
                + "\"content\":" + nestedContent + ",\"created_at\":\"2024-11-29T02:17:05Z\"}";

        Object content = Webhook.verifyAndParse(body, SECRET);

        assertInstanceOf(ChargeMessage.class, content);
        ChargeMessage charge = (ChargeMessage) content;
        assertEquals("A & B", charge.getDescription());
        assertEquals("rfd-1", charge.getRefund().getRefundId());
    }

    @Test
    void verifyAndParseThrowsOnTamperedContent() {
        // Same signature, but the transmitted content has been altered.
        String tamperedContent = CONTENT_WIRE.replace("300", "1");
        String body = "{\"id\":\"mwh-1\",\"signature\":\"sha256=" + hmac(CONTENT_WIRE, SECRET) + "\","
                + "\"event_type\":\"EVENT_TYPE_CHARGE_SUCCESS\","
                + "\"content\":" + tamperedContent + "}";

        assertThrows(InvalidSignatureException.class, () -> Webhook.verifyAndParse(body, SECRET));
    }

    @Test
    void verifyAndParseRejectsDuplicateContent() {
        // A second "content" whose value the JSON parser would keep (parsers keep the last on
        // duplicate keys) while the signature covers the first — must be rejected outright so a
        // signed value can't be swapped for an unsigned one.
        String forged = CONTENT_WIRE.replace("300", "999");
        String body = "{\"id\":\"mwh-1\",\"signature\":\"sha256=" + hmac(CONTENT_WIRE, SECRET) + "\","
                + "\"event_type\":\"EVENT_TYPE_CHARGE_SUCCESS\","
                + "\"content\":" + CONTENT_WIRE + ",\"content\":" + forged + "}";

        assertThrows(IllegalArgumentException.class, () -> Webhook.verifyAndParse(body, SECRET));
    }

    @Test
    void verifyAndParseThrowsOnWrongSecret() {
        assertThrows(InvalidSignatureException.class,
                () -> Webhook.verifyAndParse(buildBody(), "wrong-secret"));
    }

    @Test
    void verifyAndParseThrowsWhenSignatureMissing() {
        String body = "{\"id\":\"mwh-1\",\"event_type\":\"EVENT_TYPE_CHARGE_SUCCESS\","
                + "\"content\":" + CONTENT_WIRE + "}";
        assertThrows(IllegalArgumentException.class, () -> Webhook.verifyAndParse(body, SECRET));
    }

    @Test
    void verifyAndParseThrowsWhenContentMissing() {
        String body = "{\"id\":\"mwh-1\",\"signature\":\"sha256=" + hmac(CONTENT_WIRE, SECRET) + "\","
                + "\"event_type\":\"EVENT_TYPE_CHARGE_SUCCESS\"}";
        assertThrows(IllegalArgumentException.class, () -> Webhook.verifyAndParse(body, SECRET));
    }

    /**
     * Documents the reason {@link Webhook#verifyAndParse} exists: re-serializing the parsed
     * content back to JSON un-escapes '&'/'<'/'>' and therefore fails verification for legitimate
     * webhooks. Kept as a guard so nobody reintroduces that pattern.
     */
    @Test
    void reSerializedContentPathFailsVerification() {
        String body = buildBody();
        JsonObject payload = JsonParser.parseString(body).getAsJsonObject();
        String signature = payload.get("signature").getAsString();
        String reSerialized = payload.get("content").toString(); // un-escapes & -> &

        assertThrows(InvalidSignatureException.class,
                () -> Webhook.verify(reSerialized, signature, SECRET));
    }
}
