package com.jamm.errors;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    // JammException tests
    @Test
    void testJammExceptionWithMessage() {
        JammException ex = new JammException("Test error");
        assertEquals("Test error", ex.getMessage());
        assertNull(ex.getHttpStatus());
        assertNull(ex.getHttpHeaders());
        assertNull(ex.getHttpBody());
    }

    @Test
    void testJammExceptionWithCause() {
        RuntimeException cause = new RuntimeException("Root cause");
        JammException ex = new JammException("Test error", cause);
        assertEquals("Test error", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void testJammExceptionWithHttpDetails() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        JammException ex = new JammException("Test error", 500, headers, "{\"error\":\"internal\"}");
        assertEquals("Test error", ex.getMessage());
        assertEquals(500, ex.getHttpStatus());
        assertEquals(headers, ex.getHttpHeaders());
        assertEquals("{\"error\":\"internal\"}", ex.getHttpBody());
    }

    @Test
    void testJammExceptionToString() {
        JammException ex1 = new JammException("Test error");
        assertEquals("Test error", ex1.toString());

        JammException ex2 = new JammException("Test error", 404, null, null);
        assertEquals("(Status 404) Test error", ex2.toString());
    }

    // OAuthException tests
    @Test
    void testOAuthExceptionWithMessage() {
        OAuthException ex = new OAuthException("OAuth failed");
        assertEquals("OAuth failed", ex.getMessage());
    }

    @Test
    void testOAuthExceptionWithHttpDetails() {
        OAuthException ex = new OAuthException("Token expired", 401, "{\"error\":\"invalid_token\"}");
        assertEquals("Token expired", ex.getMessage());
        assertEquals(401, ex.getHttpStatus());
        assertEquals("{\"error\":\"invalid_token\"}", ex.getHttpBody());
    }

    // ApiException tests
    @Test
    void testApiExceptionFromResponse() {
        String responseBody = "{\"code\":5,\"message\":\"Customer not found\",\"details\":[{\"debug\":\"CUSTOMER_NOT_FOUND\"}]}";
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        ApiException ex = ApiException.fromResponse(404, headers, responseBody);

        assertEquals("Customer not found", ex.getMessage());
        assertEquals(ErrorCode.NOT_FOUND, ex.getErrorCode());
        assertEquals(5, ex.getCode());
        assertEquals("NOT_FOUND", ex.getErrorName());
        assertEquals("CUSTOMER_NOT_FOUND", ex.getErrorType());
        assertEquals(404, ex.getHttpStatus());
    }

    @Test
    void testApiExceptionFromResponseWithStringCode() {
        String responseBody = "{\"code\":\"INVALID_ARGUMENT\",\"message\":\"Invalid request\"}";

        ApiException ex = ApiException.fromResponse(400, null, responseBody);

        assertEquals("Invalid request", ex.getMessage());
        assertEquals(ErrorCode.INVALID_ARGUMENT, ex.getErrorCode());
    }

    @Test
    void testApiExceptionFromResponseEmpty() {
        ApiException ex = ApiException.fromResponse(500, null, "");

        assertEquals("An API error occurred", ex.getMessage());
        assertEquals(ErrorCode.UNKNOWN, ex.getErrorCode());
    }

    @Test
    void testApiExceptionFromResponseInvalidJson() {
        ApiException ex = ApiException.fromResponse(500, null, "not json");

        assertEquals("not json", ex.getMessage());
        assertEquals(ErrorCode.UNKNOWN, ex.getErrorCode());
    }

    @Test
    void testApiExceptionToString() {
        String responseBody = "{\"code\":7,\"message\":\"Access denied\"}";
        ApiException ex = ApiException.fromResponse(403, null, responseBody);

        assertEquals("(PERMISSION_DENIED, HTTP 403) Access denied", ex.toString());
    }

    @Test
    void testApiExceptionToStringWithRequestContext() {
        String responseBody = "{\"code\":5,\"message\":\"Customer not found\"}";
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Request-Id", "req-123");

        ApiException ex = ApiException.fromResponse(404, headers, responseBody, "GET", "/api/v1/customers/cus-123");

        assertEquals(
                "(NOT_FOUND, HTTP 404, GET /api/v1/customers/cus-123) Customer not found [request_id=req-123]",
                ex.toString()
        );
        assertEquals("GET", ex.getRequestMethod());
        assertEquals("/api/v1/customers/cus-123", ex.getRequestPath());
        assertEquals("req-123", ex.getRequestId());
    }

    @Test
    void testApiExceptionToStringWithPartialRequestContext() {
        String responseBody = "{\"message\":\"Missing route\"}";

        ApiException ex = ApiException.fromResponse(404, null, responseBody, null, "/api/v1/missing");

        assertEquals("(UNKNOWN, HTTP 404, /api/v1/missing) Missing route", ex.toString());
    }

    // InvalidSignatureException tests
    @Test
    void testInvalidSignatureExceptionDefault() {
        InvalidSignatureException ex = new InvalidSignatureException();
        assertEquals("Invalid webhook signature", ex.getMessage());
    }

    @Test
    void testInvalidSignatureExceptionWithMessage() {
        InvalidSignatureException ex = new InvalidSignatureException("Signature mismatch");
        assertEquals("Signature mismatch", ex.getMessage());
    }

    @Test
    void testInvalidSignatureExceptionWithCause() {
        RuntimeException cause = new RuntimeException("Crypto error");
        InvalidSignatureException ex = new InvalidSignatureException("Verification failed", cause);
        assertEquals("Verification failed", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }

    // Immutability tests
    @Test
    void testJammExceptionHeadersAreImmutable() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        JammException ex = new JammException("Test error", 500, headers, "{\"error\":\"internal\"}");
        Map<String, String> returnedHeaders = ex.getHttpHeaders();

        assertThrows(UnsupportedOperationException.class, () -> returnedHeaders.put("New-Header", "value"));
    }

    @Test
    void testJammExceptionNullHeadersReturnsNull() {
        JammException ex = new JammException("Test error", 500, null, "{\"error\":\"internal\"}");
        assertNull(ex.getHttpHeaders());
    }

    @Test
    void testApiExceptionBodyIsImmutable() {
        String responseBody = "{\"code\":5,\"message\":\"Not found\",\"extra\":\"data\"}";
        ApiException ex = ApiException.fromResponse(404, null, responseBody);

        Map<String, Object> body = ex.getBody();
        assertNotNull(body);

        assertThrows(UnsupportedOperationException.class, () -> body.put("new-key", "value"));
    }

    @Test
    void testApiExceptionNullBodyReturnsNull() {
        ApiException ex = ApiException.fromResponse(500, null, "");
        assertNull(ex.getBody());
    }
}
