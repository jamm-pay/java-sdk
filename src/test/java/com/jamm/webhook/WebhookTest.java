package com.jamm.webhook;

import com.api.v1.ChargeMessage;
import com.api.v1.ContractMessage;
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
                return buildChargeMessage(eventType, null);
            }

            private String buildChargeMessage(String eventType, String extraFields) {
                return "{" +
                    "\"id\": \"mwh-ct4i88q418in5emhfvcg\"," +
                    "\"signature\": \"sha256=aa7114c09d9275e035675947e0f56e1869b7b6a9d678f304db03da15c5c27beb\"," +
                    "\"created_at\": \"2024-11-29T02:17:07.458580287Z\"," +
                    "\"event_type\": \"" + eventType + "\"," +
                    "\"content\": {" +
                        "\"id\": \"trx-5fc49679-7e5f-465b-b7ec-1b0e076cf208\"," +
                        "\"customer\": \"cus-ct4i7ma418in6j467rjg\"," +
                        "\"status\": \"STATUS_SUCCESS\"," +
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
            }

            @Test
            void parseChargeFail() throws Exception {
                String json = buildChargeMessage("EVENT_TYPE_CHARGE_FAIL");
                Object result = Webhook.parse(json);
                assertInstanceOf(ChargeMessage.class, result);
            }

            @Test
            void parseChargeRefund() throws Exception {
                String json = buildChargeMessage(
                    "EVENT_TYPE_CHARGE_REFUND",
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

            @Test
            void parseChargeCancel() throws Exception {
                String json = buildChargeMessage("EVENT_TYPE_CHARGE_CANCEL");
                Object result = Webhook.parse(json);
                assertInstanceOf(ChargeMessage.class, result);
                ChargeMessage charge = (ChargeMessage) result;
                assertEquals("trx-5fc49679-7e5f-465b-b7ec-1b0e076cf208", charge.getId());
            }

            @Test
            void parseChargeRefundFailed() throws Exception {
                String json = buildChargeMessage(
                    "EVENT_TYPE_CHARGE_REFUND_FAILED",
                    "\"refund_id\": \"rfd-test-123\""
                );
                Object result = Webhook.parse(json);
                assertInstanceOf(ChargeMessage.class, result);
                ChargeMessage charge = (ChargeMessage) result;
                assertEquals("trx-5fc49679-7e5f-465b-b7ec-1b0e076cf208", charge.getId());
                assertTrue(charge.hasRefundId());
                assertEquals("rfd-test-123", charge.getRefundId());
            }

            @Test
            void parseChargeRefundDenied() throws Exception {
                String json = buildChargeMessage("EVENT_TYPE_CHARGE_REFUND_DENIED");
                Object result = Webhook.parse(json);
                assertInstanceOf(ChargeMessage.class, result);
                ChargeMessage charge = (ChargeMessage) result;
                assertEquals("trx-5fc49679-7e5f-465b-b7ec-1b0e076cf208", charge.getId());
            }

            @Test
            void parseChargeCancelAsRefundEvent() throws Exception {
                String json = buildChargeMessage(
                    "EVENT_TYPE_CHARGE_REFUND",
                    "\"amount_refunded\": 300," +
                        "\"jamm_fee\": 0," +
                        "\"original_transaction_jamm_fee\": \"waived\""
                );
                Object result = Webhook.parse(json);
                assertInstanceOf(ChargeMessage.class, result);
                ChargeMessage charge = (ChargeMessage) result;
                assertTrue(charge.hasAmountRefunded());
                assertEquals(300, charge.getAmountRefunded());
                assertTrue(charge.hasJammFee());
                assertEquals(0, charge.getJammFee());
                assertTrue(charge.hasOriginalTransactionJammFee());
                assertEquals("waived", charge.getOriginalTransactionJammFee());
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
}
