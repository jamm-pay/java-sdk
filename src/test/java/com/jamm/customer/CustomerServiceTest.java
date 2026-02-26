package com.jamm.customer;

import com.api.v1.Buyer;
import com.api.v1.Contract;
import com.api.v1.CreateCustomerRequest;
import com.api.v1.CreateCustomerResponse;
import com.api.v1.Customer;
import com.api.v1.MerchantCustomer;
import com.api.v1.GetCustomerResponse;
import com.api.v1.GetContractResponse;
import com.api.v1.UpdateCustomerRequest;
import com.api.v1.UpdateCustomerResponse;
import com.api.v1.DeleteCustomerResponse;
import com.jamm.JammClient;
import com.jamm.http.JammHttpClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class CustomerServiceTest {

    @Test
    void createCustomer_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        CreateCustomerRequest req = CreateCustomerRequest.newBuilder()
            .setBuyer(Buyer.newBuilder().setEmail("a@b.com").build())
            .build();

        CreateCustomerResponse resp = CreateCustomerResponse.newBuilder()
            .setCustomer(MerchantCustomer.newBuilder()
                .setCustomer(Customer.newBuilder().setId("cus-1").build())
                .build())
            .build();

        when(http.post("/v1/customers", req, CreateCustomerResponse.class))
            .thenReturn(resp);

        CustomerService service = new CustomerService(client);
        MerchantCustomer result = service.create(req);

        assertEquals("cus-1", result.getCustomer().getId());
    }

    @Test
    void getCustomer_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        GetCustomerResponse resp = GetCustomerResponse.newBuilder()
            .setCustomer(Customer.newBuilder().setId("cus-2").build())
            .build();

        when(http.get("/v1/customers/cus-2", GetCustomerResponse.class))
            .thenReturn(resp);

        CustomerService service = new CustomerService(client);
        Customer result = service.get("cus-2");

        assertEquals("cus-2", result.getId());
    }

    @Test
    void getContract_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        GetContractResponse resp = GetContractResponse.newBuilder()
            .setCustomer(MerchantCustomer.newBuilder()
                .setCustomer(Customer.newBuilder().setId("cus-5").build())
                .build())
            .setContract(Contract.newBuilder().setId("con-1").build())
            .build();

        when(http.get("/v1/customers/cus-5/contract", GetContractResponse.class))
            .thenReturn(resp);

        CustomerService service = new CustomerService(client);
        GetContractResponse result = service.getContract("cus-5");

        assertEquals("cus-5", result.getCustomer().getCustomer().getId());
        assertEquals("con-1", result.getContract().getId());
    }

    @Test
    void updateCustomer_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        UpdateCustomerRequest req = UpdateCustomerRequest.newBuilder()
            .setCustomer("cus-3")
            .setEmail("new@b.com")
            .build();

        UpdateCustomerResponse resp = UpdateCustomerResponse.newBuilder()
            .setCustomer(MerchantCustomer.newBuilder()
                .setCustomer(Customer.newBuilder().setId("cus-3").build())
                .build())
            .build();

        when(http.put("/v1/customers/cus-3", req, UpdateCustomerResponse.class))
            .thenReturn(resp);

        CustomerService service = new CustomerService(client);
        MerchantCustomer result = service.update(req);

        assertEquals("cus-3", result.getCustomer().getId());
    }

    @Test
    void deleteCustomer_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        DeleteCustomerResponse resp = DeleteCustomerResponse.newBuilder()
            .setAccepted(true)
            .build();

        when(http.delete("/v1/customers/cus-4", DeleteCustomerResponse.class))
            .thenReturn(resp);

        CustomerService service = new CustomerService(client);
        DeleteCustomerResponse result = service.delete("cus-4");

        assertEquals(true, result.getAccepted());
    }
}
