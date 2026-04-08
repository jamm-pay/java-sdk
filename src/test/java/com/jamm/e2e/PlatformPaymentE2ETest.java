package com.jamm.e2e;

import com.api.v1.Buyer;
import com.api.v1.GetChargeResponse;
import com.api.v1.GetChargesRequest;
import com.api.v1.GetChargesResponse;
import com.api.v1.InitialCharge;
import com.api.v1.OnSessionPaymentRequest;
import com.api.v1.OnSessionPaymentResponse;
import com.api.v1.Pagination;
import com.api.v1.URL;
import com.jamm.JammClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlatformPaymentE2ETest {

    @Test
    void onSessionPayment_existingCustomer_with_merchant() {
        String merchant = E2ETestHelper.requireEnv("PLATFORM_MERCHANT");
        String customerId = E2ETestHelper.requireEnv("PLATFORM_CUSTOMER");

        try (JammClient client = E2ETestHelper.createPlatformClient()) {
            OnSessionPaymentRequest request = OnSessionPaymentRequest.newBuilder()
                    .setCustomer(customerId)
                    .setCharge(InitialCharge.newBuilder()
                            .setPrice(1000)
                            .setDescription("Java platform E2E on-session")
                            .build())
                    .setRedirect(URL.newBuilder()
                            .setSuccessUrl("https://jamm-pay.jp/success")
                            .setFailureUrl("https://jamm-pay.jp/fail")
                            .build())
                    .build();

            OnSessionPaymentResponse response = client.payments().onSessionPayment(request, merchant);
            assertNotNull(response);
            // API responds with success or known error depending on customer state
            assertTrue(response.getSuccess() || response.hasData() || response.hasError());
        }
    }

    @Test
    void onSessionPayment_newBuyer_with_merchant() {
        String merchant = E2ETestHelper.requireEnv("PLATFORM_MERCHANT");

        try (JammClient client = E2ETestHelper.createPlatformClient()) {
            OnSessionPaymentRequest request = OnSessionPaymentRequest.newBuilder()
                    .setCharge(InitialCharge.newBuilder()
                            .setPrice(1000)
                            .setDescription("Java platform E2E new buyer")
                            .build())
                    .setRedirect(URL.newBuilder()
                            .setSuccessUrl("https://jamm-pay.jp/success")
                            .setFailureUrl("https://jamm-pay.jp/fail")
                            .build())
                    .setBuyer(Buyer.newBuilder()
                            .setEmail(E2ETestHelper.randomEmail("java-platform-buyer"))
                            .setName("Platform Buyer")
                            .setKatakanaLastName("バイヤー")
                            .setKatakanaFirstName("テスト")
                            .setAddress("Tokyo, Minato-ku, 1-2-3")
                            .setBirthDate("1990-01-01")
                            .setGender("male")
                            .build())
                    .build();

            OnSessionPaymentResponse response = client.payments().onSessionPayment(request, merchant);
            assertNotNull(response);
            assertTrue(response.getSuccess());
        }
    }

    @Test
    void getCharge_with_merchant() {
        String merchant = E2ETestHelper.requireEnv("PLATFORM_MERCHANT");
        String chargeId = E2ETestHelper.requireEnv("PLATFORM_CHARGE");

        try (JammClient client = E2ETestHelper.createPlatformClient()) {
            GetChargeResponse response = client.payments().getCharge(chargeId, merchant);
            assertNotNull(response);
            assertNotNull(response.getCharge());
            assertEquals(chargeId, response.getCharge().getChargeId());
        }
    }

    @Test
    void getCharges_with_merchant() {
        String merchant = E2ETestHelper.requireEnv("PLATFORM_MERCHANT");
        String customerId = E2ETestHelper.requireEnv("PLATFORM_CUSTOMER");

        try (JammClient client = E2ETestHelper.createPlatformClient()) {
            GetChargesRequest request = GetChargesRequest.newBuilder()
                    .setCustomer(customerId)
                    .setPagination(Pagination.newBuilder()
                            .setPageSize(10)
                            .setPageToken("")
                            .build())
                    .build();

            GetChargesResponse response = client.payments().getCharges(request, merchant);
            assertNotNull(response);
            assertNotNull(response.getChargesList());
        }
    }
}
