package com.jamm.e2e;

import com.api.v1.Buyer;
import com.api.v1.InitialCharge;
import com.api.v1.OnSessionPaymentRequest;
import com.api.v1.OnSessionPaymentResponse;
import com.api.v1.URL;
import com.jamm.JammClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
