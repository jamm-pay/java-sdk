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
import com.jamm.http.JammHttpClient;
import com.jamm.http.UrlBuilder;

class PaymentService {

    private static final String PAYMENTS_BASE_PATH = "/v1/payments";
    private static final String CHARGE_BASE_PATH = "/v1/charge";
    private static final String CHARGES_BASE_PATH = "/v1/charges";
    private static final String REFUND_BASE_PATH = "/v1/refund";

    private final JammHttpClient http;

    PaymentService(JammClient client) {
        this.http = client.getHttpClient();
    }

    /**
     * On Session Payment - Provides a unified interface for creating payment sessions.
     * This API intelligently routes requests to the appropriate payment strategy.
     *
     * @param request the on-session payment request
     * @return response containing payment session details
     */
    OnSessionPaymentResponse onSessionPayment(OnSessionPaymentRequest request) {
        return http.post(PAYMENTS_BASE_PATH + "/on-session", request, OnSessionPaymentResponse.class);
    }

    /**
     * Off Session Payment - Charge customer in synchronous request.
     * The customer must already be created, and must have completed Jamm onboarding
     * including terms of service acceptance, KYC, and payment method setup.
     *
     * @param request the off-session payment request
     * @return response containing customer and charge information
     */
    OffSessionPaymentResponse offSessionPayment(OffSessionPaymentRequest request) {
        return http.post(PAYMENTS_BASE_PATH + "/off-session", request, OffSessionPaymentResponse.class);
    }

    /**
     * Off Session Payment Async - Start an off-session charge asynchronously.
     * Returns request tracking information and charge ID for polling with GetCharge.
     *
     * @param request the off-session async payment request
     * @return response containing async request tracking information
     */
    OffSessionPaymentAsyncResponse offSessionPaymentAsync(OffSessionPaymentAsyncRequest request) {
        return http.post(PAYMENTS_BASE_PATH + "/off-session/async", request, OffSessionPaymentAsyncResponse.class);
    }

    /**
     * Get a charge by ID.
     *
     * @param chargeId the charge ID
     * @return response containing charge and customer information
     */
    GetChargeResponse getCharge(String chargeId) {
        String path = UrlBuilder.path(CHARGE_BASE_PATH)
                .pathParam(chargeId)
                .build();
        return http.get(path, GetChargeResponse.class);
    }

    /**
     * Get charges for a customer. The response is paginated.
     *
     * @param request the get charges request containing customer ID and pagination info
     * @return response containing list of charges and pagination info
     */
    GetChargesResponse getCharges(GetChargesRequest request) {
        UrlBuilder builder = UrlBuilder.path(CHARGES_BASE_PATH)
                .pathParam(request.getCustomer());

        if (request.hasPagination()) {
            builder.queryParam("pageToken", request.getPagination().getPageToken())
                    .queryParam("pageSize", request.getPagination().getPageSize());
        }

        return http.get(builder.build(), GetChargesResponse.class);
    }

    /**
     * Refund a charge. If the same-day cancellation window has not passed,
     * cancels the charge directly. Otherwise, creates a bank transfer refund request.
     * The result is delivered asynchronously via the {@code charge_refund} webhook.
     * Use {@link PaymentClient#getCharge(String)} to retrieve the latest refund status.
     *
     * @param request the refund request containing charge ID and optional amount
     * @return response containing charge ID and refund ID
     */
    RefundResponse refund(RefundRequest request) {
        return http.post(REFUND_BASE_PATH, request, RefundResponse.class);
    }
}
