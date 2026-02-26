package com.jamm.e2e;

import com.api.v1.Buyer;
import com.api.v1.CreateCustomerRequest;
import com.api.v1.Customer;
import com.api.v1.DeleteCustomerResponse;
import com.api.v1.MerchantCustomer;
import com.api.v1.UpdateCustomerRequest;
import com.jamm.JammClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerE2ETest {

    @Test
    void create_get_update_delete_customer() {
        try (JammClient client = E2ETestHelper.createClient()) {
            String email = E2ETestHelper.randomEmail("java-customer-e2e");

            CreateCustomerRequest createRequest = CreateCustomerRequest.newBuilder()
                    .setBuyer(Buyer.newBuilder()
                            .setEmail(email)
                            .setName("Java SDK E2E Customer")
                            .setKatakanaLastName("ヤマダ")
                            .setKatakanaFirstName("タロウ")
                            .setPhone("07012345678")
                            .setAddress("Tokyo")
                            .setBirthDate("1995-01-01")
                            .setGender("male")
                            .setForceKyc(false)
                            .putMetadata("suite", "java-e2e")
                            .build())
                    .build();

            MerchantCustomer created = client.customers().create(createRequest);
            assertNotNull(created);
            assertNotNull(created.getCustomer());
            assertNotNull(created.getMerchant());
            assertTrue(!created.getCustomer().getId().isEmpty());

            String customerId = created.getCustomer().getId();

            Customer fetched = client.customers().get(customerId);
            assertNotNull(fetched);
            assertEquals(customerId, fetched.getId());

            String updatedEmail = E2ETestHelper.randomEmail("java-customer-updated");
            UpdateCustomerRequest updateRequest = UpdateCustomerRequest.newBuilder()
                    .setCustomer(customerId)
                    .setEmail(updatedEmail)
                    .build();

            MerchantCustomer updated = client.customers().update(updateRequest);
            assertNotNull(updated);
            assertNotNull(updated.getCustomer());
            assertEquals(customerId, updated.getCustomer().getId());
            assertEquals(updatedEmail, updated.getCustomer().getEmail());

            DeleteCustomerResponse deleted = client.customers().delete(customerId);
            assertNotNull(deleted);
            assertTrue(deleted.getAccepted());
        }
    }
}
