package com.jamm.e2e;

import com.api.v1.Buyer;
import com.api.v1.CreateCustomerRequest;
import com.api.v1.Customer;
import com.api.v1.MerchantCustomer;
import com.api.v1.UpdateCustomerRequest;
import com.api.v1.DeleteCustomerResponse;
import com.jamm.JammClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlatformCustomerE2ETest {

    @Test
    void create_customer_with_merchant() {
        String merchant = E2ETestHelper.requireEnv("PLATFORM_MERCHANT");

        try (JammClient client = E2ETestHelper.createPlatformClient()) {
            String email = E2ETestHelper.randomEmail("java-platform-customer");

            CreateCustomerRequest request = CreateCustomerRequest.newBuilder()
                    .setBuyer(Buyer.newBuilder()
                            .setEmail(email)
                            .setName("Java Platform E2E Customer")
                            .setKatakanaLastName("テスト")
                            .setKatakanaFirstName("ユーザー")
                            .setAddress("Tokyo, Minato-ku, 1-2-3")
                            .setBirthDate("1990-01-01")
                            .setGender("male")
                            .setForceKyc(false)
                            .putMetadata("suite", "java-platform-e2e")
                            .build())
                    .build();

            MerchantCustomer created = client.customers().create(request, merchant);
            assertNotNull(created);
            assertNotNull(created.getCustomer());
            assertFalse(created.getCustomer().getId().isEmpty());
        }
    }

    @Test
    void get_customer_with_merchant() {
        String merchant = E2ETestHelper.requireEnv("PLATFORM_MERCHANT");
        String customerId = E2ETestHelper.requireEnv("PLATFORM_CUSTOMER");

        try (JammClient client = E2ETestHelper.createPlatformClient()) {
            Customer customer = client.customers().get(customerId, merchant);
            assertNotNull(customer);
            assertEquals(customerId, customer.getId());
        }
    }

    @Test
    void update_customer_with_merchant() {
        String merchant = E2ETestHelper.requireEnv("PLATFORM_MERCHANT");
        String customerId = E2ETestHelper.requireEnv("PLATFORM_CUSTOMER");

        try (JammClient client = E2ETestHelper.createPlatformClient()) {
            String updatedEmail = E2ETestHelper.randomEmail("java-platform-updated");

            UpdateCustomerRequest request = UpdateCustomerRequest.newBuilder()
                    .setCustomer(customerId)
                    .setEmail(updatedEmail)
                    .build();

            MerchantCustomer updated = client.customers().update(request, merchant);
            assertNotNull(updated);
            assertEquals(customerId, updated.getCustomer().getId());
        }
    }

    @Test
    void create_and_delete_customer_with_merchant() {
        String merchant = E2ETestHelper.requireEnv("PLATFORM_MERCHANT");

        try (JammClient client = E2ETestHelper.createPlatformClient()) {
            String email = E2ETestHelper.randomEmail("java-platform-delete");

            MerchantCustomer created = client.customers().create(
                    CreateCustomerRequest.newBuilder()
                            .setBuyer(Buyer.newBuilder()
                                    .setEmail(email)
                                    .setForceKyc(false)
                                    .build())
                            .build(),
                    merchant);

            String customerId = created.getCustomer().getId();

            DeleteCustomerResponse deleted = client.customers().delete(customerId, merchant);
            assertNotNull(deleted);
            assertTrue(deleted.getAccepted());
        }
    }
}
