package com.jamm.e2e;

import com.api.v1.AsyncStatus;
import com.api.v1.Buyer;
import com.api.v1.InitialCharge;
import com.api.v1.OffSessionPaymentRequest;
import com.api.v1.OffSessionPaymentResponse;
import com.api.v1.OffSessionPaymentAsyncRequest;
import com.api.v1.OffSessionPaymentAsyncResponse;
import com.api.v1.OnSessionPaymentRequest;
import com.api.v1.OnSessionPaymentResponse;
import com.api.v1.RefundRequest;
import com.api.v1.RefundResponse;
import com.api.v1.URL;
import com.jamm.JammClient;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentE2ETest {

    @Test
    void onSessionPayment_contractWithCharge_createsSessionAndReusesCustomer() {
        try (JammClient client = E2ETestHelper.createClient()) {
            String email = E2ETestHelper.randomEmail("java-payment-e2e");

            OnSessionPaymentRequest request = OnSessionPaymentRequest.newBuilder()
                    .setBuyer(Buyer.newBuilder()
                            .setEmail(email)
                            .setForceKyc(true)
                            .putMetadata("suite", "java-e2e")
                            .build())
                    .setCharge(InitialCharge.newBuilder()
                            .setPrice(100)
                            .setDescription("Java SDK payment E2E")
                            .putMetadata("case", "on-session")
                            .build())
                    .setRedirect(URL.newBuilder()
                            .setSuccessUrl("https://example.com/success")
                            .setFailureUrl("https://example.com/failure")
                            .build())
                    .build();

            OnSessionPaymentResponse first = client.payments().onSessionPayment(request);
            OnSessionPaymentResponse second = client.payments().onSessionPayment(request);

            assertTrue(first.getSuccess());
            assertTrue(first.hasData());
            assertTrue(first.getData().hasCustomer());
            assertTrue(first.getData().hasContract());
            assertTrue(first.getData().hasCharge());
            assertTrue(first.getData().hasPaymentLink());
            assertFalse(first.getData().getOneTime());

            assertTrue(second.getSuccess());
            assertTrue(second.hasData());
            assertTrue(second.getData().hasCustomer());
            assertTrue(second.getData().hasContract());
            assertTrue(second.getData().hasCharge());
            assertTrue(second.getData().hasPaymentLink());
            assertFalse(second.getData().getOneTime());

            assertEquals(first.getData().getCustomer().getId(), second.getData().getCustomer().getId());
        }
    }

    @Test
    void onSessionPayment_contractWithoutCharge_succeeds() {
        try (JammClient client = E2ETestHelper.createClient()) {
            String email = E2ETestHelper.randomEmail("java-contract-only");

            OnSessionPaymentRequest request = OnSessionPaymentRequest.newBuilder()
                    .setBuyer(Buyer.newBuilder()
                            .setEmail(email)
                            .setForceKyc(true)
                            .build())
                    .build();

            OnSessionPaymentResponse response = client.payments().onSessionPayment(request);

            assertTrue(response.getSuccess());
            assertTrue(response.hasData());
            assertTrue(response.getData().hasContract());
            assertFalse(response.getData().hasCharge());
            assertTrue(response.getData().hasCustomer());
            assertTrue(response.getData().hasPaymentLink());
            assertFalse(response.getData().getOneTime());
        }
    }

    @Test
    void onSessionPayment_addCharge_forExistingCustomer() {
        String customerId = System.getenv("CUSTOMER");
        Assumptions.assumeTrue(
                customerId != null && !customerId.isBlank(),
                "Skipping: set CUSTOMER env var (e.g. cus-xxxxxxxx)");

        try (JammClient client = E2ETestHelper.createClient()) {
            OnSessionPaymentRequest request = OnSessionPaymentRequest.newBuilder()
                    .setCustomer(customerId)
                    .setCharge(InitialCharge.newBuilder()
                            .setPrice(100)
                            .setDescription("Java SDK add charge E2E")
                            .putMetadata("case", "add-charge")
                            .build())
                    .setRedirect(URL.newBuilder()
                            .setSuccessUrl("https://example.com/success")
                            .setFailureUrl("https://example.com/failure")
                            .build())
                    .build();

            OnSessionPaymentResponse response = client.payments().onSessionPayment(request);

            assertTrue(response.getSuccess());
            assertTrue(response.hasData());
            assertFalse(response.getData().hasContract());
            assertTrue(response.getData().hasCharge());
            assertTrue(response.getData().hasCustomer());
            assertTrue(response.getData().hasPaymentLink());
            assertFalse(response.getData().getOneTime());
        }
    }

    @Test
    void onSessionPayment_oneTime_succeeds() {
        try (JammClient client = E2ETestHelper.createClient()) {
            String email = E2ETestHelper.randomEmail("java-one-time");

            OnSessionPaymentRequest request = OnSessionPaymentRequest.newBuilder()
                    .setBuyer(Buyer.newBuilder()
                            .setEmail(email)
                            .setForceKyc(true)
                            .build())
                    .setCharge(InitialCharge.newBuilder()
                            .setPrice(100)
                            .setDescription("Java SDK one-time payment E2E")
                            .putMetadata("case", "one-time")
                            .build())
                    .setOneTime(true)
                    .setRedirect(URL.newBuilder()
                            .setSuccessUrl("https://example.com/success")
                            .setFailureUrl("https://example.com/failure")
                            .build())
                    .build();

            OnSessionPaymentResponse response = client.payments().onSessionPayment(request);

            assertTrue(response.getSuccess());
            assertTrue(response.hasData());
            assertFalse(response.getData().hasContract());
            assertTrue(response.getData().hasCharge());
            assertTrue(response.getData().hasCustomer());
            assertTrue(response.getData().hasPaymentLink());
            assertTrue(response.getData().getOneTime());
        }
    }

    @Test
    void offSessionPayment_chargesExistingCustomer() {
        String customerId = System.getenv("CUSTOMER");
        Assumptions.assumeTrue(
                customerId != null && !customerId.isBlank(),
                "Skipping: set CUSTOMER env var (e.g. cus-xxxxxxxx)");

        try (JammClient client = E2ETestHelper.createClient()) {
            OffSessionPaymentRequest request = OffSessionPaymentRequest.newBuilder()
                    .setCustomer(customerId)
                    .setCharge(InitialCharge.newBuilder()
                            .setPrice(100)
                            .setDescription("Java SDK offSessionPayment E2E")
                            .putMetadata("case", "off-session")
                            .build())
                    .build();

            OffSessionPaymentResponse response = client.payments().offSessionPayment(request);

            assertEquals(customerId, response.getCustomer().getId());
            assertFalse(response.getCharge().getChargeId().isEmpty());
            assertTrue(response.getCharge().getPaid());
        }
    }

    @Test
    void offSessionPaymentAsync_startsAsyncCharge() {
        String customerId = System.getenv("CUSTOMER");
        Assumptions.assumeTrue(
                customerId != null && !customerId.isBlank(),
                "Skipping: set CUSTOMER env var (e.g. cus-xxxxxxxx)");

        try (JammClient client = E2ETestHelper.createClient()) {
            OffSessionPaymentAsyncRequest request = OffSessionPaymentAsyncRequest.newBuilder()
                    .setCustomer(customerId)
                    .setCharge(InitialCharge.newBuilder()
                            .setPrice(100)
                            .setDescription("Java SDK offSessionPaymentAsync E2E")
                            .putMetadata("case", "off-session-async")
                            .build())
                    .build();

            OffSessionPaymentAsyncResponse response = client.payments().offSessionPaymentAsync(request);

            assertFalse(response.getRequestId().isEmpty());
            assertFalse(response.getChargeId().isEmpty());
            assertTrue(response.getChargeId().startsWith("trx-"));
            assertEquals(AsyncStatus.ASYNC_STATUS_PENDING, response.getStatus());
        }
    }

    @Test
    void refund_full_returnsRefundId() {
        String chargeId = System.getenv("CHARGE_FULL_REFUND");
        Assumptions.assumeTrue(
                chargeId != null && !chargeId.isBlank(),
                "Skipping: set CHARGE_FULL_REFUND env var");

        try (JammClient client = E2ETestHelper.createClient()) {
            RefundRequest request = RefundRequest.newBuilder()
                    .setChargeId(chargeId)
                    .build();

            RefundResponse response = client.payments().refund(request);

            assertEquals(chargeId, response.getChargeId());
            assertFalse(response.getRefundId().isEmpty());
        }
    }

    @Test
    void refund_partial_returnsRefundId() {
        String chargeId = System.getenv("CHARGE_PARTIAL_REFUND");
        Assumptions.assumeTrue(
                chargeId != null && !chargeId.isBlank(),
                "Skipping: set CHARGE_PARTIAL_REFUND env var");

        String partialRefundAmount = E2ETestHelper.envOrDefault("PARTIAL_REFUND_AMOUNT", "50");
        int amount;
        try {
            amount = Integer.parseInt(partialRefundAmount);
        } catch (NumberFormatException e) {
            Assumptions.assumeTrue(
                    false,
                    "Skipping: PARTIAL_REFUND_AMOUNT must be a valid integer, but was '" + partialRefundAmount + "'");
            return;
        }

        try (JammClient client = E2ETestHelper.createClient()) {
            RefundRequest request = RefundRequest.newBuilder()
                    .setChargeId(chargeId)
                    .setAmount(amount)
                    .build();

            RefundResponse response = client.payments().refund(request);

            assertEquals(chargeId, response.getChargeId());
            assertFalse(response.getRefundId().isEmpty());
        }
    }
}
