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

/**
 * Client for payment-related operations.
 *
 * <p>In platform mode, all methods have overloads accepting a {@code merchant} parameter
 * to operate on behalf of a connected merchant.
 */
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
     * On Session Payment on behalf of a merchant (platform mode).
     *
     * @param request  the on-session payment request
     * @param merchant the merchant ID (format: "mer-*")
     * @return response containing payment session details
     */
    public OnSessionPaymentResponse onSessionPayment(OnSessionPaymentRequest request, String merchant) {
        return service.onSessionPayment(request, merchant);
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
     * Off Session Payment on behalf of a merchant (platform mode).
     *
     * @param request  the off-session payment request
     * @param merchant the merchant ID (format: "mer-*")
     * @return response containing customer and charge information
     */
    public OffSessionPaymentResponse offSessionPayment(OffSessionPaymentRequest request, String merchant) {
        return service.offSessionPayment(request, merchant);
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
     * Off Session Payment Async on behalf of a merchant (platform mode).
     *
     * @param request  the off-session async payment request
     * @param merchant the merchant ID (format: "mer-*")
     * @return response containing async request tracking information
     */
    public OffSessionPaymentAsyncResponse offSessionPaymentAsync(OffSessionPaymentAsyncRequest request, String merchant) {
        return service.offSessionPaymentAsync(request, merchant);
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
     * Get a charge by ID on behalf of a merchant (platform mode).
     *
     * @param chargeId the charge ID
     * @param merchant the merchant ID (format: "mer-*")
     * @return response containing charge and customer information
     */
    public GetChargeResponse getCharge(String chargeId, String merchant) {
        return service.getCharge(chargeId, merchant);
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
     * Get charges for a customer on behalf of a merchant (platform mode).
     *
     * @param request  the get charges request containing customer ID and pagination info
     * @param merchant the merchant ID (format: "mer-*")
     * @return response containing list of charges and pagination info
     */
    public GetChargesResponse getCharges(GetChargesRequest request, String merchant) {
        return service.getCharges(request, merchant);
    }

    /**
     * Refund a charge. If the same-day cancellation window has not passed,
     * cancels the charge directly. Otherwise, creates a bank transfer refund request.
     * The result is delivered asynchronously via the {@code refund_succeeded} webhook.
     * Use {@link #getCharge(String)} to retrieve the latest refund status.
     *
     * <p>Use {@link RefundRequest.Builder#setCancelOnly(boolean)} to set the cancel-only flag
     * (wire/JSON field {@code cancel_only}). When {@code true}, only cancellation is attempted
     * without falling back to a bank transfer refund. Defaults to {@code false}
     * (fallback enabled).
     *
     * @param request the refund request containing charge ID, optional amount,
     *                and optional cancel-only flag
     * @return response containing charge ID and refund ID
     */
    public RefundResponse refund(RefundRequest request) {
        return service.refund(request);
    }

    /**
     * Refund a charge on behalf of a merchant (platform mode).
     *
     * @param request  the refund request containing charge ID, optional amount,
     *                 and optional cancel-only flag
     * @param merchant the merchant ID (format: "mer-*")
     * @return response containing charge ID and refund ID
     */
    public RefundResponse refund(RefundRequest request, String merchant) {
        return service.refund(request, merchant);
    }
}
