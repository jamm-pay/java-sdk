package com.jamm.customer;

import com.api.v1.CreateCustomerRequest;
import com.api.v1.CreateCustomerResponse;
import com.api.v1.GetCustomerResponse;
import com.api.v1.GetContractResponse;
import com.api.v1.UpdateCustomerRequest;
import com.api.v1.UpdateCustomerResponse;
import com.api.v1.DeleteCustomerResponse;
import com.api.v1.Customer;
import com.api.v1.MerchantCustomer;
import com.jamm.JammClient;
import com.jamm.http.JammHttpClient;
import com.jamm.http.RequestOptions;

class CustomerService {

    private static final String CUSTOMER_BASE_PATH = "/v1/customers";

    private final JammHttpClient http;

    CustomerService(JammClient client) {
        this.http = client.getHttpClient();
    }

    MerchantCustomer create(CreateCustomerRequest request) {
        CreateCustomerResponse resp =
                http.post(CUSTOMER_BASE_PATH, request, CreateCustomerResponse.class);
        return resp.getCustomer();
    }

    MerchantCustomer create(CreateCustomerRequest request, String merchant) {
        CreateCustomerResponse resp =
                http.post(CUSTOMER_BASE_PATH, request, CreateCustomerResponse.class,
                        RequestOptions.withMerchant(merchant));
        return resp.getCustomer();
    }

    Customer get(String idOrEmail) {
        GetCustomerResponse resp =
                http.get(CUSTOMER_BASE_PATH + "/" + idOrEmail, GetCustomerResponse.class);
        return resp.getCustomer();
    }

    Customer get(String idOrEmail, String merchant) {
        GetCustomerResponse resp =
                http.get(CUSTOMER_BASE_PATH + "/" + idOrEmail, GetCustomerResponse.class,
                        RequestOptions.withMerchant(merchant));
        return resp.getCustomer();
    }

    GetContractResponse getContract(String customerId) {
        return http.get(CUSTOMER_BASE_PATH + "/" + customerId + "/contract",
                GetContractResponse.class);
    }

    GetContractResponse getContract(String customerId, String merchant) {
        return http.get(CUSTOMER_BASE_PATH + "/" + customerId + "/contract",
                GetContractResponse.class, RequestOptions.withMerchant(merchant));
    }

    MerchantCustomer update(UpdateCustomerRequest request) {
        UpdateCustomerResponse resp =
                http.put(CUSTOMER_BASE_PATH + "/" + request.getCustomer(),
                        request,
                        UpdateCustomerResponse.class);
        return resp.getCustomer();
    }

    MerchantCustomer update(UpdateCustomerRequest request, String merchant) {
        UpdateCustomerResponse resp =
                http.put(CUSTOMER_BASE_PATH + "/" + request.getCustomer(),
                        request,
                        UpdateCustomerResponse.class,
                        RequestOptions.withMerchant(merchant));
        return resp.getCustomer();
    }

    DeleteCustomerResponse delete(String customerId) {
        return http.delete(CUSTOMER_BASE_PATH + "/" + customerId, DeleteCustomerResponse.class);
    }

    DeleteCustomerResponse delete(String customerId, String merchant) {
        return http.delete(CUSTOMER_BASE_PATH + "/" + customerId,
                DeleteCustomerResponse.class, RequestOptions.withMerchant(merchant));
    }
}
