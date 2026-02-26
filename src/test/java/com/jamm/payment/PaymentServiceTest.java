package com.jamm.payment;

import com.api.v1.Buyer;
import com.api.v1.AsyncStatus;
import com.api.v1.ChargeResult;
import com.api.v1.Customer;
import com.api.v1.GetChargeResponse;
import com.api.v1.GetChargesRequest;
import com.api.v1.GetChargesResponse;
import com.api.v1.InitialCharge;
import com.api.v1.OffSessionPaymentAsyncRequest;
import com.api.v1.OffSessionPaymentAsyncResponse;
import com.api.v1.OffSessionPaymentRequest;
import com.api.v1.OffSessionPaymentResponse;
import com.api.v1.OnSessionPaymentRequest;
import com.api.v1.OnSessionPaymentResponse;
import com.api.v1.Pagination;
import com.api.v1.RefundRequest;
import com.api.v1.RefundResponse;
import com.api.v1.URL;
import com.jamm.JammClient;
import com.jamm.http.JammHttpClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaymentServiceTest {

    @Test
    void onSessionPayment_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        OnSessionPaymentRequest req = OnSessionPaymentRequest.newBuilder()
            .setBuyer(Buyer.newBuilder().setEmail("foo@example.com").build())
            .setCharge(InitialCharge.newBuilder()
                .setPrice(100)
                .setDescription("test charge")
                .build())
            .setRedirect(URL.newBuilder()
                .setSuccessUrl("https://example.com/success")
                .setFailureUrl("https://example.com/failure")
                .build())
            .build();

        OnSessionPaymentResponse resp = OnSessionPaymentResponse.newBuilder()
            .setSuccess(true)
            .build();

        when(http.post("/v1/payments/on-session", req, OnSessionPaymentResponse.class))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        OnSessionPaymentResponse result = service.onSessionPayment(req);

        assertTrue(result.getSuccess());
    }

    @Test
    void offSessionPayment_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        OffSessionPaymentRequest req = OffSessionPaymentRequest.newBuilder()
            .setCustomer("cus-123")
            .setCharge(InitialCharge.newBuilder()
                .setPrice(100)
                .setDescription("test charge")
                .build())
            .build();

        OffSessionPaymentResponse resp = OffSessionPaymentResponse.newBuilder()
            .setCustomer(Customer.newBuilder().setId("cus-123").build())
            .setCharge(ChargeResult.newBuilder()
                .setChargeId("chg-123")
                .setPaid(true)
                .build())
            .build();

        when(http.post("/v1/payments/off-session", req, OffSessionPaymentResponse.class))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        OffSessionPaymentResponse result = service.offSessionPayment(req);

        assertEquals("cus-123", result.getCustomer().getId());
        assertEquals("chg-123", result.getCharge().getChargeId());
        assertTrue(result.getCharge().getPaid());
    }

    @Test
    void offSessionPaymentAsync_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        OffSessionPaymentAsyncRequest req = OffSessionPaymentAsyncRequest.newBuilder()
            .setCustomer("cus-123")
            .setCharge(InitialCharge.newBuilder()
                .setPrice(100)
                .setDescription("test async charge")
                .build())
            .build();

        OffSessionPaymentAsyncResponse resp = OffSessionPaymentAsyncResponse.newBuilder()
            .setRequestId("pwf-123")
            .setStatus(AsyncStatus.ASYNC_STATUS_PENDING)
            .setChargeId("trx-123")
            .build();

        when(http.post("/v1/payments/off-session/async", req, OffSessionPaymentAsyncResponse.class))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        OffSessionPaymentAsyncResponse result = service.offSessionPaymentAsync(req);

        assertEquals("pwf-123", result.getRequestId());
        assertEquals(AsyncStatus.ASYNC_STATUS_PENDING, result.getStatus());
        assertEquals("trx-123", result.getChargeId());
    }

    @Test
    void getCharge_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        GetChargeResponse resp = GetChargeResponse.newBuilder()
            .setCharge(ChargeResult.newBuilder()
                .setChargeId("chg-123")
                .setPaid(true)
                .build())
            .setCustomer(Customer.newBuilder().setId("cus-123").build())
            .build();

        when(http.get("/v1/charge/chg-123", GetChargeResponse.class))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        GetChargeResponse result = service.getCharge("chg-123");

        assertEquals("chg-123", result.getCharge().getChargeId());
        assertEquals("cus-123", result.getCustomer().getId());
    }

    @Test
    void getCharges_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        GetChargesRequest req = GetChargesRequest.newBuilder()
            .setCustomer("cus-123")
            .build();

        GetChargesResponse resp = GetChargesResponse.newBuilder()
            .setCustomer(Customer.newBuilder().setId("cus-123").build())
            .addCharges(ChargeResult.newBuilder()
                .setChargeId("chg-123")
                .setPaid(true)
                .build())
            .setPagination(Pagination.newBuilder()
                .setPageSize(10)
                .setPageToken("next-page")
                .build())
            .build();

        when(http.get("/v1/charges/cus-123", GetChargesResponse.class))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        GetChargesResponse result = service.getCharges(req);

        assertEquals("cus-123", result.getCustomer().getId());
        assertEquals(1, result.getChargesCount());
        assertEquals("chg-123", result.getCharges(0).getChargeId());
    }

    @Test
    void getCharges_withPagination_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        GetChargesRequest req = GetChargesRequest.newBuilder()
            .setCustomer("cus-123")
            .setPagination(Pagination.newBuilder()
                .setPageSize(20)
                .setPageToken("page-token")
                .build())
            .build();

        GetChargesResponse resp = GetChargesResponse.newBuilder()
            .setCustomer(Customer.newBuilder().setId("cus-123").build())
            .addCharges(ChargeResult.newBuilder()
                .setChargeId("chg-456")
                .setPaid(true)
                .build())
            .build();

        when(http.get("/v1/charges/cus-123?pageToken=page-token&pageSize=20", GetChargesResponse.class))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        GetChargesResponse result = service.getCharges(req);

        assertEquals("cus-123", result.getCustomer().getId());
        assertEquals("chg-456", result.getCharges(0).getChargeId());
    }

    @Test
    void refund_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        RefundRequest req = RefundRequest.newBuilder()
            .setChargeId("trx-123")
            .build();

        RefundResponse resp = RefundResponse.newBuilder()
            .setChargeId("trx-123")
            .setRefundId("rfd-456")
            .build();

        when(http.post("/v1/refund", req, RefundResponse.class))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        RefundResponse result = service.refund(req);

        assertEquals("trx-123", result.getChargeId());
        assertEquals("rfd-456", result.getRefundId());
    }

    @Test
    void refund_withAmount_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        RefundRequest req = RefundRequest.newBuilder()
            .setChargeId("trx-123")
            .setAmount(500)
            .build();

        RefundResponse resp = RefundResponse.newBuilder()
            .setChargeId("trx-123")
            .setRefundId("rfd-789")
            .build();

        when(http.post("/v1/refund", req, RefundResponse.class))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        RefundResponse result = service.refund(req);

        assertEquals("trx-123", result.getChargeId());
        assertEquals("rfd-789", result.getRefundId());
    }
}
