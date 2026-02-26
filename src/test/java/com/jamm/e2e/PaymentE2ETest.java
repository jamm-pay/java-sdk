package com.jamm.e2e;

import com.api.v1.AsyncStatus;
import com.api.v1.Buyer;
import com.api.v1.InitialCharge;
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
    void onSessionPayment_createsSessionAndReusesCustomer() {
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
            assertTrue(first.getData().hasPaymentLink());

            assertTrue(second.getSuccess());
            assertTrue(second.hasData());
            assertTrue(second.getData().hasCustomer());
            assertTrue(second.getData().hasPaymentLink());

            assertEquals(first.getData().getCustomer().getId(), second.getData().getCustomer().getId());
        }
    }

    @Test
    void onSessionPayment_withoutRedirect_succeeds() {
        try (JammClient client = E2ETestHelper.createClient()) {
            String email = E2ETestHelper.randomEmail("java-payment-no-redirect");

            OnSessionPaymentRequest request = OnSessionPaymentRequest.newBuilder()
                    .setBuyer(Buyer.newBuilder()
                            .setEmail(email)
                            .setForceKyc(true)
                            .build())
                    .setCharge(InitialCharge.newBuilder()
                            .setPrice(100)
                            .setDescription("Java SDK payment without redirect")
                            .build())
                    .build();

            OnSessionPaymentResponse response = client.payments().onSessionPayment(request);

            assertTrue(response.getSuccess());
            assertTrue(response.hasData());
            assertTrue(response.getData().hasPaymentLink());
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
    void refund_refundsCharge() {
        String chargeId = System.getenv("CHARGE");
        Assumptions.assumeTrue(
                chargeId != null && !chargeId.isBlank(),
                "Skipping: set CHARGE env var (e.g. trx-xxxxxxxx)");

        try (JammClient client = E2ETestHelper.createClient()) {
            RefundRequest request = RefundRequest.newBuilder()
                    .setChargeId(chargeId)
                    .build();

            RefundResponse response = client.payments().refund(request);

            assertEquals(chargeId, response.getChargeId());
            assertFalse(response.getRefundId().isEmpty());
        }
    }
}
