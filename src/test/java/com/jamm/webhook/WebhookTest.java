package com.jamm.webhook;

import com.api.v1.ChargeMessage;
import com.api.v1.ContractMessage;
import com.api.v1.RefundInfo;
import com.api.v1.UserAccountMessage;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.jamm.errors.InvalidSignatureException;

import static org.junit.jupiter.api.Assertions.*;

class WebhookTest {

    private static final String CLIENT_SECRET = "test-secret";

    @Nested
    class ParseTests {

        @Nested
        class ChargeMessages {

            private String buildChargeMessage(String eventType) {
                return buildChargeMessage(eventType, null, "STATUS_SUCCESS");
            }

            private String buildChargeMessage(String eventType, String extraFields) {
                return buildChargeMessage(eventType, extraFields, "STATUS_SUCCESS");
            }

            private String buildChargeMessage(String eventType, String extraFields, String status) {
                return "{" +
                    "\"id\": \"mwh-ct4i88q418in5emhfvcg\"," +
                    "\"signature\": \"sha256=aa7114c09d9275e035675947e0f56e1869b7b6a9d678f304db03da15c5c27beb\"," +
                    "\"created_at\": \"2024-11-29T02:17:07.458580287Z\"," +
                    "\"event_type\": \"" + eventType + "\"," +
                    "\"content\": {" +
                        "\"id\": \"trx-5fc49679-7e5f-465b-b7ec-1b0e076cf208\"," +
                        "\"customer\": \"cus-ct4i7ma418in6j467rjg\"," +
                        "\"status\": \"" + status + "\"," +
                        "\"description\": \"test create nth charge without redirect\"," +
                        "\"merchant_name\": \"Test Merchant 1\"," +
                        "\"initial_amount\": 300," +
                        "\"discount\": 3," +
                        "\"final_amount\": 297," +
                        "\"currency\": \"JPY\"," +
                        "\"processed_at\": \"2024-11-29T02:17:06Z\"," +
                        formatExtraFields(extraFields) +
                        "\"created_at\": \"2024-11-29T02:17:05.595784Z\"," +
                        "\"updated_at\": \"2024-11-29T02:17:07.296241Z\"" +
                    "}" +
                "}";
            }

            private String formatExtraFields(String extraFields) {
                if (extraFields == null || extraFields.isBlank()) {
                    return "";
                }

                String trimmed = extraFields.trim();
                return trimmed.endsWith(",") ? trimmed : trimmed + ",";
            }

            @Test
            void parseChargeCreated() throws Exception {
                String json = buildChargeMessage("EVENT_TYPE_CHARGE_CREATED");
                Object result = Webhook.parse(json);
                assertInstanceOf(ChargeMessage.class, result);
                ChargeMessage charge = (ChargeMessage) result;
                assertEquals("trx-5fc49679-7e5f-465b-b7ec-1b0e076cf208", charge.getId());
                assertEquals("cus-ct4i7ma418in6j467rjg", charge.getCustomer());
                assertEquals(ChargeMessage.Status.STATUS_SUCCESS, charge.getStatus());
                assertEquals("Test Merchant 1", charge.getMerchantName());
                assertEquals(300, charge.getInitialAmount());
                assertEquals(3, charge.getDiscount());
                assertEquals(297, charge.getFinalAmount());
                assertEquals("JPY", charge.getCurrency());
                assertEquals("2024-11-29T02:17:05.595784Z", charge.getCreatedAt());
                assertEquals("2024-11-29T02:17:07.296241Z", charge.getUpdatedAt());
                assertFalse(charge.hasError());
            }

            @Test
            void parseChargeUpdated() throws Exception {
                String json = buildChargeMessage("EVENT_TYPE_CHARGE_UPDATED");
                Object result = Webhook.parse(json);
                assertInstanceOf(ChargeMessage.class, result);
                ChargeMessage charge = (ChargeMessage) result;
                assertEquals("trx-5fc49679-7e5f-465b-b7ec-1b0e076cf208", charge.getId());
                assertEquals("cus-ct4i7ma418in6j467rjg", charge.getCustomer());
            }

            @Test
            void parseChargeSuccess() throws Exception {
                String json = buildChargeMessage("EVENT_TYPE_CHARGE_SUCCESS");
                Object result = Webhook.parse(json);
                assertInstanceOf(ChargeMessage.class, result);
                ChargeMessage charge = (ChargeMessage) result;
                assertEquals("trx-5fc49679-7e5f-465b-b7ec-1b0e076cf208", charge.getId());
                assertEquals(297, charge.getFinalAmount());
                assertFalse(charge.hasError());
            }

            @Test
            void parseChargeFail() throws Exception {
                String json = buildChargeMessage(
                    "EVENT_TYPE_CHARGE_FAIL",
                    "\"error\": {" +
                        "\"code\": \"ERROR_TYPE_PAYMENT_CHARGE_OVER_LIMIT\"," +
                        "\"message\": \"The payment charge exceeds the allowed limit.\"," +
                        "\"details\": [{\"type\": \"google.protobuf.Value\", \"value\": \"test\", \"debug\": \"ERROR_TYPE_PAYMENT_CHARGE_OVER_LIMIT\"}]" +
                    "},",
                    "STATUS_FAILURE"
                );
                Object result = Webhook.parse(json);
                assertInstanceOf(ChargeMessage.class, result);
                ChargeMessage charge = (ChargeMessage) result;
                assertEquals(ChargeMessage.Status.STATUS_FAILURE, charge.getStatus());
                assertTrue(charge.hasError());
                assertEquals("ERROR_TYPE_PAYMENT_CHARGE_OVER_LIMIT", charge.getError().getCode());
                assertEquals("The payment charge exceeds the allowed limit.", charge.getError().getMessage());
            }

            // Refund/cancel webhooks deliver the nested {transaction, refund} shape.
            // The transaction holds the charge fields; the refund holds the rfd- id and amounts.
            private String buildRefundMessage(String eventType) {
                return "{" +
                    "\"id\": \"mwh-ct4i88q418in5emhfvcg\"," +
                    "\"signature\": \"sha256=aa7114c09d9275e035675947e0f56e1869b7b6a9d678f304db03da15c5c27beb\"," +
                    "\"created_at\": \"2024-11-29T02:17:07.458580287Z\"," +
                    "\"event_type\": \"" + eventType + "\"," +
                    "\"content\": {" +
                        "\"transaction\": {" +
                            "\"id\": \"trx-5fc49679-7e5f-465b-b7ec-1b0e076cf208\"," +
                            "\"customer\": \"cus-ct4i7ma418in6j467rjg\"," +
                            "\"status\": 6," +
                            "\"merchant_name\": \"Test Merchant 1\"," +
                            "\"initial_amount\": 300," +
                            "\"discount\": 3," +
                            "\"final_amount\": 297," +
                            "\"currency\": \"JPY\"" +
                        "}," +
                        "\"refund\": {" +
                            "\"refund_id\": \"rfd-test-123\"," +
                            "\"amount_refunded\": 297," +
                            "\"jamm_fee\": 3," +
                            "\"consumption_tax\": 0," +
                            "\"original_transaction_fee_waived\": false," +
                            "\"processed_at\": \"2024-11-29T02:17:07Z\"" +
                        "}" +
                    "}" +
                "}";
            }

            @Test
            void parseRefundSucceeded() throws Exception {
                Object result = Webhook.parse(buildRefundMessage("EVENT_TYPE_REFUND_SUCCEEDED"));
                assertInstanceOf(ChargeMessage.class, result);
                ChargeMessage charge = (ChargeMessage) result;
                // Transaction fields survive the flattening.
                assertEquals("trx-5fc49679-7e5f-465b-b7ec-1b0e076cf208", charge.getId());
                assertEquals("cus-ct4i7ma418in6j467rjg", charge.getCustomer());
                assertEquals(ChargeMessage.Status.STATUS_REFUNDED, charge.getStatus());
                assertEquals(297, charge.getFinalAmount());
                // Refund details are populated on the nested RefundInfo.
                assertTrue(charge.hasRefund());
                RefundInfo refund = charge.getRefund();
                assertEquals("rfd-test-123", refund.getRefundId());
                assertEquals(297, refund.getAmountRefunded());
                assertEquals(3, refund.getJammFee());
            }

            @Test
            void parseRefundFailed() throws Exception {
                Object result = Webhook.parse(buildRefundMessage("EVENT_TYPE_REFUND_FAILED"));
                assertInstanceOf(ChargeMessage.class, result);
                ChargeMessage charge = (ChargeMessage) result;
                assertEquals("trx-5fc49679-7e5f-465b-b7ec-1b0e076cf208", charge.getId());
                assertTrue(charge.hasRefund());
                assertEquals("rfd-test-123", charge.getRefund().getRefundId());
            }

            @Test
            void parseRefundWithoutRefundId() throws Exception {
                // Same-day cancel webhooks omit refund_id (backend sends it nil for cancel-as-refund).
                String json = "{" +
                    "\"event_type\": \"EVENT_TYPE_REFUND_SUCCEEDED\"," +
                    "\"content\": {" +
                        "\"transaction\": {" +
                            "\"id\": \"trx-5fc49679-7e5f-465b-b7ec-1b0e076cf208\"," +
                            "\"status\": 6," +
                            "\"final_amount\": 297" +
                        "}," +
                        "\"refund\": {" +
                            "\"amount_refunded\": 297," +
                            "\"original_transaction_fee_waived\": true" +
                        "}" +
                    "}" +
                "}";
                ChargeMessage charge = (ChargeMessage) Webhook.parse(json);
                assertEquals("trx-5fc49679-7e5f-465b-b7ec-1b0e076cf208", charge.getId());
                assertTrue(charge.hasRefund());
                assertEquals("", charge.getRefund().getRefundId());
                assertFalse(charge.getRefund().hasRefundId());
                assertEquals(297, charge.getRefund().getAmountRefunded());
            }

            @Test
            void parseRefundLegacyFlat() throws Exception {
                // Older payloads sent refund fields flat on the content; the fallback still parses them.
                String json = buildChargeMessage(
                    "EVENT_TYPE_REFUND_SUCCEEDED",
                    "\"amount_refunded\": 300," +
                        "\"jamm_fee\": 200," +
                        "\"original_transaction_jamm_fee\": \"not_waived\""
                );
                Object result = Webhook.parse(json);
                assertInstanceOf(ChargeMessage.class, result);
                ChargeMessage charge = (ChargeMessage) result;
                assertTrue(charge.hasAmountRefunded());
                assertEquals(300, charge.getAmountRefunded());
                assertTrue(charge.hasJammFee());
                assertEquals(200, charge.getJammFee());
                assertTrue(charge.hasOriginalTransactionJammFee());
                assertEquals("not_waived", charge.getOriginalTransactionJammFee());
            }
        }

        @Nested
        class ContractMessages {

            private static final String CONTRACT_MESSAGE = "{" +
                "\"id\": \"mwh-ct4i7si418in5emhfvc0\"," +
                "\"signature\": \"sha256=24ca91d9c527718af98042ab19997e34d277a311ae99662240cd1cfeffb75591\"," +
                "\"created_at\": \"2024-11-29T02:16:18.156236916Z\"," +
                "\"event_type\": \"EVENT_TYPE_CONTRACT_ACTIVATED\"," +
                "\"content\": {" +
                    "\"customer\": \"cus-ct4i7ma418in6j467rjg\"," +
                    "\"created_at\": \"2024-11-29T02:16:12.168127Z\"," +
                    "\"activated_at\": \"2024-11-29T02:16:18.040142301Z\"," +
                    "\"merchant_name\": \"Test Merchant 1\"" +
                "}" +
            "}";

            @Test
            void parseContractActivated() throws Exception {
                Object result = Webhook.parse(CONTRACT_MESSAGE);
                assertInstanceOf(ContractMessage.class, result);
                ContractMessage contract = (ContractMessage) result;
                assertEquals("cus-ct4i7ma418in6j467rjg", contract.getCustomer());
                assertEquals("2024-11-29T02:16:12.168127Z", contract.getCreatedAt());
                assertEquals("2024-11-29T02:16:18.040142301Z", contract.getActivatedAt());
                assertEquals("Test Merchant 1", contract.getMerchantName());
            }
        }

        @Nested
        class UserAccountMessages {

            private static final String USER_ACCOUNT_MESSAGE = "{" +
                "\"id\": \"mwh-ct4i7si418in5emhfvc0\"," +
                "\"signature\": \"sha256=24ca91d9c527718af98042ab19997e34d277a311ae99662240cd1cfeffb75591\"," +
                "\"created_at\": \"2024-11-29T02:16:18.156236916Z\"," +
                "\"event_type\": \"EVENT_TYPE_USER_ACCOUNT_DELETED\"," +
                "\"content\": {" +
                    "\"customer\": \"cus-ct4i7ma418in6j467rjg\"," +
                    "\"email\": \"foo@bar.com\"," +
                    "\"deleted_at\": \"2024-11-29T02:16:12.168127Z\"," +
                    "\"merchant_name\": \"Test Merchant 1\"" +
                "}" +
            "}";

            @Test
            void parseUserAccountDeleted() throws Exception {
                Object result = Webhook.parse(USER_ACCOUNT_MESSAGE);
                assertInstanceOf(UserAccountMessage.class, result);
                UserAccountMessage userAccount = (UserAccountMessage) result;
                assertEquals("cus-ct4i7ma418in6j467rjg", userAccount.getCustomer());
                assertEquals("foo@bar.com", userAccount.getEmail());
                assertEquals("2024-11-29T02:16:12.168127Z", userAccount.getDeletedAt());
                assertEquals("Test Merchant 1", userAccount.getMerchantName());
            }
        }

        @Test
        void parseUnsupportedEventType() {
            String json = "{" +
                "\"id\": \"mwh-test\"," +
                "\"signature\": \"sha256=test\"," +
                "\"created_at\": \"2024-11-29T02:16:18.156236916Z\"," +
                "\"event_type\": \"EVENT_TYPE_UNSPECIFIED\"," +
                "\"content\": {}" +
            "}";

            assertThrows(IllegalArgumentException.class, () -> Webhook.parse(json));
        }
    }

    @Nested
    class ParseValidationTests {

        @Test
        void parseNullJson() {
            assertThrows(IllegalArgumentException.class, () -> Webhook.parse(null));
        }

        @Test
        void parseEmptyJson() {
            assertThrows(IllegalArgumentException.class, () -> Webhook.parse(""));
        }

        @Test
        void parseInvalidJson() {
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> Webhook.parse("not valid json"));
            assertTrue(ex.getMessage().contains("Invalid JSON format"));
        }

        @Test
        void parseMissingEventType() {
            String json = "{\"content\": {}}";
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> Webhook.parse(json));
            assertTrue(ex.getMessage().contains("event_type"));
        }

        @Test
        void parseMissingContent() {
            String json = "{\"event_type\": \"EVENT_TYPE_CHARGE_CREATED\"}";
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> Webhook.parse(json));
            assertTrue(ex.getMessage().contains("content"));
        }

        @Test
        void parseUnknownEventType() {
            String json = "{\"event_type\": \"UNKNOWN_EVENT\", \"content\": {}}";
            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> Webhook.parse(json));
            assertTrue(ex.getMessage().contains("Unknown event type"));
        }
    }

    @Nested
    class VerifyTests {

        @Test
        void verifyValidSignature() {
            String json = "{\"test\":\"data\"}";
            // Pre-computed signature for the json with CLIENT_SECRET
            String signature = "sha256=" + computeHmacSha256(json, CLIENT_SECRET);

            assertDoesNotThrow(() -> Webhook.verify(json, signature, CLIENT_SECRET));
        }

        @Test
        void verifyInvalidSignature() {
            String json = "{\"test\":\"data\"}";
            String invalidSignature = "sha256=invalid";

            assertThrows(InvalidSignatureException.class,
                    () -> Webhook.verify(json, invalidSignature, CLIENT_SECRET));
        }

        @Test
        void verifyNullJson() {
            assertThrows(IllegalArgumentException.class,
                    () -> Webhook.verify(null, "sha256=test", CLIENT_SECRET));
        }

        @Test
        void verifyEmptyJson() {
            assertThrows(IllegalArgumentException.class,
                    () -> Webhook.verify("", "sha256=test", CLIENT_SECRET));
        }

        @Test
        void verifyNullSignature() {
            assertThrows(IllegalArgumentException.class,
                    () -> Webhook.verify("{}", null, CLIENT_SECRET));
        }

        @Test
        void verifyEmptySignature() {
            assertThrows(IllegalArgumentException.class,
                    () -> Webhook.verify("{}", "", CLIENT_SECRET));
        }

        @Test
        void verifyNullSecret() {
            assertThrows(IllegalArgumentException.class,
                    () -> Webhook.verify("{}", "sha256=test", null));
        }

        @Test
        void verifyEmptySecret() {
            assertThrows(IllegalArgumentException.class,
                    () -> Webhook.verify("{}", "sha256=test", ""));
        }

        /**
         * Helper method to compute HMAC SHA-256 for test verification.
         */
        private String computeHmacSha256(String data, String secret) {
            try {
                javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
                javax.crypto.spec.SecretKeySpec secretKeySpec =
                        new javax.crypto.spec.SecretKeySpec(
                                secret.getBytes(java.nio.charset.StandardCharsets.UTF_8),
                                "HmacSHA256");
                mac.init(secretKeySpec);
                byte[] hmacBytes = mac.doFinal(
                        data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                for (byte b : hmacBytes) {
                    sb.append(String.format("%02x", b));
                }
                return sb.toString();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Verifies against a real backend-produced fixture: the {@code content} bytes, HMAC signature,
     * and secret were generated by the Go backend (TestMerchant01) — see
     * packages/backends/api/workflows/merchant/webhook. This proves the SDK's verification matches
     * the backend's exact JSON serialization, not just a signature the test computed itself.
     */
    @Nested
    class RealBackendFixture {

        // TestMerchant01 client secret used by the backend to sign this webhook.
        private static final String SECRET = "chukmvsfibedaiarp3jv7lp45qkuu3uikgmkuil02mr0n6840k5";

        // Exact transmitted content bytes and the signature the backend produced over them.
        private static final String CONTENT =
                "{\"customer\":\"cus-ct4i7ma418in6j467rjg\","
              + "\"created_at\":\"2024-11-29T02:16:12.168127Z\","
              + "\"activated_at\":\"2024-11-29T02:16:18.040142301Z\","
              + "\"merchant_name\":\"TestMerchant1\"}";
        private static final String SIGNATURE =
                "sha256=cc30ff809e21445817412de797ed2ca7cde7dea3f0844dcf20753edaaffe252c";

        private String webhookBody() {
            return "{\"id\":\"mwh-real\",\"signature\":\"" + SIGNATURE + "\","
                 + "\"event_type\":\"EVENT_TYPE_CONTRACT_ACTIVATED\","
                 + "\"content\":" + CONTENT + "}";
        }

        @Test
        void verifyAcceptsRealBackendSignature() {
            assertDoesNotThrow(() -> Webhook.verify(CONTENT, SIGNATURE, SECRET));
        }

        @Test
        void verifyRejectsRealSignatureWithWrongSecret() {
            assertThrows(InvalidSignatureException.class,
                    () -> Webhook.verify(CONTENT, SIGNATURE, "wrong-secret"));
        }

        @Test
        void verifyAndParseAcceptsRealBackendWebhook() throws Exception {
            Object content = Webhook.verifyAndParse(webhookBody(), SECRET);

            assertInstanceOf(ContractMessage.class, content);
            ContractMessage contract = (ContractMessage) content;
            assertEquals("cus-ct4i7ma418in6j467rjg", contract.getCustomer());
            assertEquals("TestMerchant1", contract.getMerchantName());
            assertEquals("2024-11-29T02:16:18.040142301Z", contract.getActivatedAt());
        }

        @Test
        void verifyAndParseRejectsRealWebhookWithWrongSecret() {
            assertThrows(InvalidSignatureException.class,
                    () -> Webhook.verifyAndParse(webhookBody(), "wrong-secret"));
        }
    }
}
