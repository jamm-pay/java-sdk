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
import com.jamm.http.RequestOptions;
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

    OnSessionPaymentResponse onSessionPayment(OnSessionPaymentRequest request) {
        return http.post(PAYMENTS_BASE_PATH + "/on-session", request,
                OnSessionPaymentResponse.class);
    }

    OnSessionPaymentResponse onSessionPayment(OnSessionPaymentRequest request, String merchant) {
        return http.post(PAYMENTS_BASE_PATH + "/on-session", request,
                OnSessionPaymentResponse.class, RequestOptions.withMerchant(merchant));
    }

    OffSessionPaymentResponse offSessionPayment(OffSessionPaymentRequest request) {
        return http.post(PAYMENTS_BASE_PATH + "/off-session", request,
                OffSessionPaymentResponse.class);
    }

    OffSessionPaymentResponse offSessionPayment(OffSessionPaymentRequest request, String merchant) {
        return http.post(PAYMENTS_BASE_PATH + "/off-session", request,
                OffSessionPaymentResponse.class, RequestOptions.withMerchant(merchant));
    }

    OffSessionPaymentAsyncResponse offSessionPaymentAsync(OffSessionPaymentAsyncRequest request) {
        return http.post(PAYMENTS_BASE_PATH + "/off-session/async", request,
                OffSessionPaymentAsyncResponse.class);
    }

    OffSessionPaymentAsyncResponse offSessionPaymentAsync(OffSessionPaymentAsyncRequest request, String merchant) {
        return http.post(PAYMENTS_BASE_PATH + "/off-session/async", request,
                OffSessionPaymentAsyncResponse.class, RequestOptions.withMerchant(merchant));
    }

    GetChargeResponse getCharge(String chargeId) {
        String path = UrlBuilder.path(CHARGE_BASE_PATH)
                .pathParam(chargeId)
                .build();
        return http.get(path, GetChargeResponse.class);
    }

    GetChargeResponse getCharge(String chargeId, String merchant) {
        String path = UrlBuilder.path(CHARGE_BASE_PATH)
                .pathParam(chargeId)
                .build();
        return http.get(path, GetChargeResponse.class, RequestOptions.withMerchant(merchant));
    }

    GetChargesResponse getCharges(GetChargesRequest request) {
        return http.get(buildChargesPath(request), GetChargesResponse.class);
    }

    GetChargesResponse getCharges(GetChargesRequest request, String merchant) {
        return http.get(buildChargesPath(request), GetChargesResponse.class,
                RequestOptions.withMerchant(merchant));
    }

    RefundResponse refund(RefundRequest request) {
        return http.post(REFUND_BASE_PATH, request, RefundResponse.class);
    }

    RefundResponse refund(RefundRequest request, String merchant) {
        return http.post(REFUND_BASE_PATH, request, RefundResponse.class,
                RequestOptions.withMerchant(merchant));
    }

    private String buildChargesPath(GetChargesRequest request) {
        UrlBuilder builder = UrlBuilder.path(CHARGES_BASE_PATH)
                .pathParam(request.getCustomer());

        if (request.hasPagination()) {
            builder.queryParam("pageToken", request.getPagination().getPageToken())
                    .queryParam("pageSize", request.getPagination().getPageSize());
        }

        return builder.build();
    }
}
