package com.jamm.payment;

import com.api.v1.GetChargeResponse;
import com.api.v1.GetChargesRequest;
import com.api.v1.GetChargesResponse;
import com.api.v1.OffSessionPaymentAsyncRequest;
import com.api.v1.OffSessionPaymentAsyncResponse;
import com.api.v1.OffSessionPaymentRequest;
import com.api.v1.OffSessionPaymentResponse;
import com.api.v1.OnSessionPaymentRequest;
import com.api.v1.OnSessionPaymentResponse;
import com.api.v1.RefundRequest;
import com.api.v1.RefundResponse;
import com.jamm.JammClient;

public final class PaymentClient {

    private final PaymentService service;

    public PaymentClient(JammClient client) {
        this.service = new PaymentService(client);
    }

    /**
     * On Session Payment - Provides a unified interface for creating payment sessions.
     * This API intelligently routes requests to the appropriate payment strategy.
     *
     * @param request the on-session payment request
     * @return response containing payment session details
     */
    public OnSessionPaymentResponse onSessionPayment(OnSessionPaymentRequest request) {
        return service.onSessionPayment(request);
    }

    /**
     * Off Session Payment - Charge customer in synchronous request.
     * The customer must already be created, and must have completed Jamm onboarding
     * including terms of service acceptance, KYC, and payment method setup.
     *
     * @param request the off-session payment request
     * @return response containing customer and charge information
     */
    public OffSessionPaymentResponse offSessionPayment(OffSessionPaymentRequest request) {
        return service.offSessionPayment(request);
    }

    /**
     * Off Session Payment Async - Start an off-session charge asynchronously.
     * Returns request tracking information and charge ID for polling with GetCharge.
     *
     * @param request the off-session async payment request
     * @return response containing async request tracking information
     */
    public OffSessionPaymentAsyncResponse offSessionPaymentAsync(OffSessionPaymentAsyncRequest request) {
        return service.offSessionPaymentAsync(request);
    }

    /**
     * Get a charge by ID.
     *
     * @param chargeId the charge ID
     * @return response containing charge and customer information
     */
    public GetChargeResponse getCharge(String chargeId) {
        return service.getCharge(chargeId);
    }

    /**
     * Get charges for a customer. The response is paginated.
     *
     * @param request the get charges request containing customer ID and pagination info
     * @return response containing list of charges and pagination info
     */
    public GetChargesResponse getCharges(GetChargesRequest request) {
        return service.getCharges(request);
    }

    /**
     * Refund a charge. If the same-day cancellation window has not passed,
     * cancels the charge directly. Otherwise, creates a bank transfer refund request.
     * The result is delivered asynchronously via the {@code charge_refund} webhook.
     * Use {@link #getCharge(String)} to retrieve the latest refund status.
     *
     * @param request the refund request containing charge ID and optional amount
     * @return response containing charge ID and refund ID
     */
    public RefundResponse refund(RefundRequest request) {
        return service.refund(request);
    }
}
