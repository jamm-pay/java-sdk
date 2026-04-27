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

import com.jamm.http.RequestOptions;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentServiceTest {

    @Test
    void onSessionPayment_contractWithCharge_callsCorrectEndpoint() {
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
    void onSessionPayment_addCharge_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        OnSessionPaymentRequest req = OnSessionPaymentRequest.newBuilder()
            .setCustomer("cus-123")
            .setCharge(InitialCharge.newBuilder()
                .setPrice(100)
                .setDescription("add charge")
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
    void onSessionPayment_contractWithoutCharge_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        OnSessionPaymentRequest req = OnSessionPaymentRequest.newBuilder()
            .setBuyer(Buyer.newBuilder().setEmail("foo@example.com").build())
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
    void onSessionPayment_oneTime_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        OnSessionPaymentRequest req = OnSessionPaymentRequest.newBuilder()
            .setBuyer(Buyer.newBuilder().setEmail("foo@example.com").build())
            .setCharge(InitialCharge.newBuilder()
                .setPrice(100)
                .setDescription("one time payment")
                .build())
            .setOneTime(true)
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
            .setIdempotencyKey("order-2024-001")
            .build();

        OffSessionPaymentAsyncResponse resp = OffSessionPaymentAsyncResponse.newBuilder()
            .setRequestId("pwf-123")
            .setStatus(AsyncStatus.ASYNC_STATUS_PENDING)
            .setChargeId("trx-123")
            .build();

        when(http.post(eq("/v1/payments/off-session/async"), eq(req), eq(OffSessionPaymentAsyncResponse.class)))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        OffSessionPaymentAsyncResponse result = service.offSessionPaymentAsync(req);

        assertEquals("pwf-123", result.getRequestId());
        assertEquals(AsyncStatus.ASYNC_STATUS_PENDING, result.getStatus());
        assertEquals("trx-123", result.getChargeId());
    }

    @Test
    void offSessionPaymentAsync_blankIdempotencyKey_isAutoFilledWithUuid() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        OffSessionPaymentAsyncRequest req = OffSessionPaymentAsyncRequest.newBuilder()
            .setCustomer("cus-123")
            .setCharge(InitialCharge.newBuilder()
                .setPrice(100)
                .setDescription("auto-fill key test")
                .build())
            .build();

        OffSessionPaymentAsyncResponse resp = OffSessionPaymentAsyncResponse.newBuilder()
            .setRequestId("pwf-auto")
            .setChargeId("trx-auto")
            .build();
        when(http.post(eq("/v1/payments/off-session/async"), any(OffSessionPaymentAsyncRequest.class),
                eq(OffSessionPaymentAsyncResponse.class)))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        service.offSessionPaymentAsync(req);

        ArgumentCaptor<OffSessionPaymentAsyncRequest> captor =
            ArgumentCaptor.forClass(OffSessionPaymentAsyncRequest.class);
        verify(http).post(eq("/v1/payments/off-session/async"), captor.capture(),
            eq(OffSessionPaymentAsyncResponse.class));

        String filled = captor.getValue().getIdempotencyKey();
        assertFalse(filled.isEmpty(), "idempotency_key should be auto-filled when blank");
        assertDoesNotThrow(() -> UUID.fromString(filled), "idempotency_key should be a valid UUID");
    }

    @Test
    void offSessionPaymentAsync_whitespaceIdempotencyKey_isAutoFilledWithUuid() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        OffSessionPaymentAsyncRequest req = OffSessionPaymentAsyncRequest.newBuilder()
            .setCustomer("cus-123")
            .setCharge(InitialCharge.newBuilder()
                .setPrice(100)
                .setDescription("whitespace key test")
                .build())
            .setIdempotencyKey("   ")
            .build();

        OffSessionPaymentAsyncResponse resp = OffSessionPaymentAsyncResponse.newBuilder()
            .setRequestId("pwf-ws")
            .setChargeId("trx-ws")
            .build();
        when(http.post(eq("/v1/payments/off-session/async"), any(OffSessionPaymentAsyncRequest.class),
                eq(OffSessionPaymentAsyncResponse.class)))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        service.offSessionPaymentAsync(req);

        ArgumentCaptor<OffSessionPaymentAsyncRequest> captor =
            ArgumentCaptor.forClass(OffSessionPaymentAsyncRequest.class);
        verify(http).post(eq("/v1/payments/off-session/async"), captor.capture(),
            eq(OffSessionPaymentAsyncResponse.class));

        String filled = captor.getValue().getIdempotencyKey();
        assertFalse(filled.isBlank(), "whitespace-only idempotency_key should be replaced");
        assertDoesNotThrow(() -> UUID.fromString(filled), "idempotency_key should be a valid UUID");
    }

    @Test
    void offSessionPaymentAsync_suppliedIdempotencyKey_isPreserved() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        OffSessionPaymentAsyncRequest req = OffSessionPaymentAsyncRequest.newBuilder()
            .setCustomer("cus-123")
            .setCharge(InitialCharge.newBuilder()
                .setPrice(100)
                .setDescription("explicit key test")
                .build())
            .setIdempotencyKey("order-2024-001")
            .build();

        OffSessionPaymentAsyncResponse resp = OffSessionPaymentAsyncResponse.newBuilder()
            .setRequestId("pwf-explicit")
            .setChargeId("trx-explicit")
            .build();
        when(http.post(eq("/v1/payments/off-session/async"), any(OffSessionPaymentAsyncRequest.class),
                eq(OffSessionPaymentAsyncResponse.class)))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        service.offSessionPaymentAsync(req);

        ArgumentCaptor<OffSessionPaymentAsyncRequest> captor =
            ArgumentCaptor.forClass(OffSessionPaymentAsyncRequest.class);
        verify(http).post(eq("/v1/payments/off-session/async"), captor.capture(),
            eq(OffSessionPaymentAsyncResponse.class));

        assertEquals("order-2024-001", captor.getValue().getIdempotencyKey());
    }

    @Test
    void offSessionPaymentAsync_withMerchant_autoFillsAndPassesRequestOptions() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        OffSessionPaymentAsyncRequest req = OffSessionPaymentAsyncRequest.newBuilder()
            .setCustomer("cus-123")
            .setCharge(InitialCharge.newBuilder()
                .setPrice(100)
                .setDescription("platform async key parity")
                .build())
            .build();

        OffSessionPaymentAsyncResponse resp = OffSessionPaymentAsyncResponse.newBuilder()
            .setRequestId("pwf-platform")
            .setChargeId("trx-platform")
            .build();
        when(http.post(eq("/v1/payments/off-session/async"), any(OffSessionPaymentAsyncRequest.class),
                eq(OffSessionPaymentAsyncResponse.class), any(RequestOptions.class)))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        service.offSessionPaymentAsync(req, "mer-test");

        ArgumentCaptor<OffSessionPaymentAsyncRequest> captor =
            ArgumentCaptor.forClass(OffSessionPaymentAsyncRequest.class);
        verify(http).post(eq("/v1/payments/off-session/async"), captor.capture(),
            eq(OffSessionPaymentAsyncResponse.class), any(RequestOptions.class));

        String filled = captor.getValue().getIdempotencyKey();
        assertFalse(filled.isBlank(), "merchant overload should auto-fill blank idempotency_key");
        assertDoesNotThrow(() -> UUID.fromString(filled), "idempotency_key should be a valid UUID");
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
    void refund_withoutAmount_callsCorrectEndpoint() {
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

    @Test
    void refund_withCancelOnly_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        RefundRequest req = RefundRequest.newBuilder()
            .setChargeId("trx-123")
            .setCancelOnly(true)
            .build();

        RefundResponse resp = RefundResponse.newBuilder()
            .setChargeId("trx-123")
            .setRefundId("rfd-cancel")
            .build();

        when(http.post("/v1/refund", req, RefundResponse.class))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        RefundResponse result = service.refund(req);

        assertEquals("trx-123", result.getChargeId());
        assertEquals("rfd-cancel", result.getRefundId());
    }

    // Platform mode: merchant overload tests

    @Test
    void onSessionPayment_withMerchant_callsWithRequestOptions() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        OnSessionPaymentRequest req = OnSessionPaymentRequest.newBuilder()
            .setCustomer("cus-123")
            .setCharge(InitialCharge.newBuilder().setPrice(1000).setDescription("platform test").build())
            .setRedirect(URL.newBuilder()
                .setSuccessUrl("https://example.com/success")
                .setFailureUrl("https://example.com/failure")
                .build())
            .build();

        OnSessionPaymentResponse resp = OnSessionPaymentResponse.newBuilder()
            .setSuccess(true)
            .build();

        when(http.post(eq("/v1/payments/on-session"), eq(req), eq(OnSessionPaymentResponse.class), any(RequestOptions.class)))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        OnSessionPaymentResponse result = service.onSessionPayment(req, "mer-test");

        assertTrue(result.getSuccess());
        verify(http).post(eq("/v1/payments/on-session"), eq(req), eq(OnSessionPaymentResponse.class), any(RequestOptions.class));
    }

    @Test
    void offSessionPayment_withMerchant_callsWithRequestOptions() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        OffSessionPaymentRequest req = OffSessionPaymentRequest.newBuilder()
            .setCustomer("cus-123")
            .setCharge(InitialCharge.newBuilder().setPrice(500).setDescription("platform off-session").build())
            .build();

        OffSessionPaymentResponse resp = OffSessionPaymentResponse.newBuilder()
            .setCustomer(Customer.newBuilder().setId("cus-123").build())
            .setCharge(ChargeResult.newBuilder().setChargeId("chg-p1").setPaid(true).build())
            .build();

        when(http.post(eq("/v1/payments/off-session"), eq(req), eq(OffSessionPaymentResponse.class), any(RequestOptions.class)))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        OffSessionPaymentResponse result = service.offSessionPayment(req, "mer-test");

        assertEquals("chg-p1", result.getCharge().getChargeId());
        verify(http).post(eq("/v1/payments/off-session"), eq(req), eq(OffSessionPaymentResponse.class), any(RequestOptions.class));
    }

    @Test
    void getCharge_withMerchant_callsWithRequestOptions() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        GetChargeResponse resp = GetChargeResponse.newBuilder()
            .setCharge(ChargeResult.newBuilder().setChargeId("chg-p2").setPaid(true).build())
            .build();

        when(http.get(eq("/v1/charge/chg-p2"), eq(GetChargeResponse.class), any(RequestOptions.class)))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        GetChargeResponse result = service.getCharge("chg-p2", "mer-test");

        assertEquals("chg-p2", result.getCharge().getChargeId());
        verify(http).get(eq("/v1/charge/chg-p2"), eq(GetChargeResponse.class), any(RequestOptions.class));
    }

    @Test
    void getCharges_withMerchant_callsWithRequestOptions() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        GetChargesRequest req = GetChargesRequest.newBuilder()
            .setCustomer("cus-123")
            .build();

        GetChargesResponse resp = GetChargesResponse.newBuilder()
            .setCustomer(Customer.newBuilder().setId("cus-123").build())
            .addCharges(ChargeResult.newBuilder().setChargeId("chg-p3").build())
            .build();

        when(http.get(eq("/v1/charges/cus-123"), eq(GetChargesResponse.class), any(RequestOptions.class)))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        GetChargesResponse result = service.getCharges(req, "mer-test");

        assertEquals(1, result.getChargesCount());
        verify(http).get(eq("/v1/charges/cus-123"), eq(GetChargesResponse.class), any(RequestOptions.class));
    }

    @Test
    void refund_withMerchant_callsWithRequestOptions() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        RefundRequest req = RefundRequest.newBuilder()
            .setChargeId("trx-p1")
            .build();

        RefundResponse resp = RefundResponse.newBuilder()
            .setChargeId("trx-p1")
            .setRefundId("rfd-p1")
            .build();

        when(http.post(eq("/v1/refund"), eq(req), eq(RefundResponse.class), any(RequestOptions.class)))
            .thenReturn(resp);

        PaymentService service = new PaymentService(client);
        RefundResponse result = service.refund(req, "mer-test");

        assertEquals("trx-p1", result.getChargeId());
        assertEquals("rfd-p1", result.getRefundId());
        verify(http).post(eq("/v1/refund"), eq(req), eq(RefundResponse.class), any(RequestOptions.class));
    }
}
