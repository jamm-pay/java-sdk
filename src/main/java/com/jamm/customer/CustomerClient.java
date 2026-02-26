package com.jamm.customer;

import com.api.v1.CreateCustomerRequest;
import com.api.v1.Customer;
import com.api.v1.DeleteCustomerResponse;
import com.api.v1.GetContractResponse;
import com.api.v1.MerchantCustomer;
import com.api.v1.UpdateCustomerRequest;
import com.jamm.JammClient;

/**
 * Client for customer-related operations.
 */
public final class CustomerClient {

    private final CustomerService service;

    public CustomerClient(JammClient client) {
        this.service = new CustomerService(client);
    }

    /**
     * Creates a new customer.
     *
     * @param request the create customer request containing customer details
     * @return the created merchant customer
     */
    public MerchantCustomer create(CreateCustomerRequest request) {
        return service.create(request);
    }

    /**
     * Gets a customer by ID or email.
     *
     * @param customerIdOrEmail the customer ID (e.g., "cus-123") or email address
     * @return the customer information
     */
    public Customer get(String customerIdOrEmail) {
        return service.get(customerIdOrEmail);
    }

    /**
     * Gets the contract associated with a customer.
     *
     * @param customerId the customer ID (e.g., "cus-123")
     * @return the contract response containing contract details
     */
    public GetContractResponse getContract(String customerId) {
        return service.getContract(customerId);
    }

    /**
     * Updates an existing customer.
     *
     * @param request the update customer request containing the customer ID and fields to update
     * @return the updated merchant customer
     */
    public MerchantCustomer update(UpdateCustomerRequest request) {
        return service.update(request);
    }

    /**
     * Deletes a customer.
     *
     * @param customerId the customer ID to delete (e.g., "cus-123")
     * @return the delete customer response
     */
    public DeleteCustomerResponse delete(String customerId) {
        return service.delete(customerId);
    }
}
