package com.jamm.webhook;

import com.api.v1.ChargeMessage;
import com.api.v1.EventType;
import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Covers the event-aware webhook API ({@link Webhook#parseEvent} / {@link Webhook#verifyAndParseEvent}),
 * which exposes the envelope {@code event_type} alongside the parsed content.
 */
class WebhookEventTest {

    private static final String SECRET = "test-secret";

    private static String body(String eventType, String content) {
        String sig = "sha256=" + hmac(content, SECRET);
        return "{\"id\":\"mwh-1\",\"signature\":\"" + sig + "\","
             + "\"event_type\":\"" + eventType + "\",\"content\":" + content + "}";
    }

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

    @Test
    void parseEvent_chargeSuccess_exposesEventTypeAndContent() throws Exception {
        String content = "{\"id\":\"trx-1\",\"status\":\"STATUS_SUCCESS\",\"final_amount\":297}";
        WebhookEvent event = Webhook.parseEvent(body("EVENT_TYPE_CHARGE_SUCCESS", content));

        assertEquals(EventType.EVENT_TYPE_CHARGE_SUCCESS, event.getEventType());
        assertInstanceOf(ChargeMessage.class, event.getContent());
        ChargeMessage charge = (ChargeMessage) event.getContent();
        assertEquals("trx-1", charge.getId());
        assertFalse(charge.hasError());
    }

    @Test
    void parseEvent_chargeFail_distinguishedByEventType() throws Exception {
        String content = "{\"id\":\"trx-2\",\"status\":\"STATUS_FAILURE\"}";
        WebhookEvent event = Webhook.parseEvent(body("EVENT_TYPE_CHARGE_FAIL", content));

        // Same content type as success — event_type is what distinguishes them.
        assertEquals(EventType.EVENT_TYPE_CHARGE_FAIL, event.getEventType());
        assertInstanceOf(ChargeMessage.class, event.getContent());
    }

    @Test
    void parseEvent_refundSucceeded_exposesRefundEventType() throws Exception {
        String content = "{\"transaction\":{\"id\":\"trx-3\"},\"refund\":{\"id\":\"rfd-1\"}}";
        WebhookEvent event = Webhook.parseEvent(body("EVENT_TYPE_REFUND_SUCCEEDED", content));

        assertEquals(EventType.EVENT_TYPE_REFUND_SUCCEEDED, event.getEventType());
        assertInstanceOf(ChargeMessage.class, event.getContent());
    }

    @Test
    void verifyAndParseEvent_validSignature_returnsEventTypeAndContent() throws Exception {
        String content = "{\"id\":\"trx-4\",\"status\":\"STATUS_SUCCESS\"}";
        WebhookEvent event = Webhook.verifyAndParseEvent(body("EVENT_TYPE_CHARGE_SUCCESS", content), SECRET);

        assertEquals(EventType.EVENT_TYPE_CHARGE_SUCCESS, event.getEventType());
        assertInstanceOf(ChargeMessage.class, event.getContent());
        assertEquals("trx-4", ((ChargeMessage) event.getContent()).getId());
    }

    @Test
    void parse_stillReturnsContentOnly_backwardCompatible() throws Exception {
        String content = "{\"id\":\"trx-5\",\"status\":\"STATUS_SUCCESS\"}";
        Object result = Webhook.parse(body("EVENT_TYPE_CHARGE_SUCCESS", content));

        assertInstanceOf(ChargeMessage.class, result);
        assertEquals("trx-5", ((ChargeMessage) result).getId());
    }

    @Test
    void parseEvent_unsupportedEventType_throws() {
        // Valid EventType enum value the parser doesn't map to content -> default branch.
        String json = body("EVENT_TYPE_TESTING", "{}");
        assertThrows(IllegalArgumentException.class, () -> Webhook.parseEvent(json));
    }
}
