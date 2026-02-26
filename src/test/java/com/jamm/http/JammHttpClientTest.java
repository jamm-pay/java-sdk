package com.jamm.http;

import com.api.v1.Buyer;
import com.api.v1.Customer;
import com.jamm.auth.OAuthProvider;
import com.jamm.errors.ApiException;
import com.jamm.errors.ErrorCode;
import com.jamm.errors.JammException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JammHttpClientTest {

    private MockWebServer mockServer;
    private OAuthProvider mockOAuthProvider;
    private JammHttpClient httpClient;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        mockOAuthProvider = mock(OAuthProvider.class);
        when(mockOAuthProvider.getToken()).thenReturn("test-bearer-token");

        String baseUrl = mockServer.url("").toString();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        httpClient = new JammHttpClient(
                mockOAuthProvider,
                baseUrl,
                5000,  // connect timeout
                5000,  // read timeout
                0,     // max retries
                1000,  // retry initial delay
                30000  // retry max delay
        );
    }

    @AfterEach
    void tearDown() throws IOException {
        mockServer.shutdown();
    }

    // GET tests
    @Test
    void testGetRequest() throws InterruptedException {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"id\":\"123\",\"name\":\"Test\"}"));

        TestResponse response = httpClient.get("/api/test", TestResponse.class);

        assertNotNull(response);
        assertEquals("123", response.id);
        assertEquals("Test", response.name);

        RecordedRequest request = mockServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("GET", request.getMethod());
        assertEquals("/api/test", request.getPath());
        assertEquals("Bearer test-bearer-token", request.getHeader("Authorization"));
        assertEquals("application/json", request.getHeader("Content-Type"));
        String sdkVersion = request.getHeader("X-SDK-Version");
        assertNotNull(sdkVersion, "X-SDK-Version header should be present");
        assertTrue(sdkVersion.matches("java:.+"), "X-SDK-Version should follow java:<version> format");
    }

    // POST tests
    @Test
    void testPostRequest() throws InterruptedException {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(201)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"id\":\"456\",\"name\":\"Created\"}"));

        TestRequest requestBody = new TestRequest("new-item");
        TestResponse response = httpClient.post("/api/test", requestBody, TestResponse.class);

        assertNotNull(response);
        assertEquals("456", response.id);
        assertEquals("Created", response.name);

        RecordedRequest request = mockServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("POST", request.getMethod());
        assertTrue(request.getBody().readUtf8().contains("new-item"));
    }

    // PUT tests
    @Test
    void testPutRequest() throws InterruptedException {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\":\"123\",\"name\":\"Updated\"}"));

        TestRequest requestBody = new TestRequest("updated-item");
        TestResponse response = httpClient.put("/api/test/123", requestBody, TestResponse.class);

        assertNotNull(response);
        assertEquals("Updated", response.name);

        RecordedRequest request = mockServer.takeRequest(1, TimeUnit.SECONDS);
        assertEquals("PUT", request.getMethod());
    }

    // DELETE tests
    @Test
    void testDeleteRequest() throws InterruptedException {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(204));

        Void response = httpClient.delete("/api/test/123", Void.class);

        assertNull(response);

        RecordedRequest request = mockServer.takeRequest(1, TimeUnit.SECONDS);
        assertEquals("DELETE", request.getMethod());
        assertEquals("/api/test/123", request.getPath());
    }

    // PATCH tests
    @Test
    void testPatchRequest() throws InterruptedException {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\":\"123\",\"name\":\"Patched\"}"));

        TestRequest requestBody = new TestRequest("patched-item");
        TestResponse response = httpClient.patch("/api/test/123", requestBody, TestResponse.class);

        assertNotNull(response);
        assertEquals("Patched", response.name);

        RecordedRequest request = mockServer.takeRequest(1, TimeUnit.SECONDS);
        assertEquals("PATCH", request.getMethod());
    }

    // Error handling tests
    @Test
    void testNotFoundError() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(404)
                .setBody("{\"code\":5,\"message\":\"Resource not found\"}"));

        ApiException ex = assertThrows(ApiException.class,
                () -> httpClient.get("/api/missing", TestResponse.class));

        assertEquals(404, ex.getHttpStatus());
        assertEquals(ErrorCode.NOT_FOUND, ex.getErrorCode());
        assertEquals("Resource not found", ex.getMessage());
    }

    @Test
    void testBadRequestError() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(400)
                .setBody("{\"code\":3,\"message\":\"Invalid input\"}"));

        ApiException ex = assertThrows(ApiException.class,
                () -> httpClient.post("/api/test", new TestRequest(""), TestResponse.class));

        assertEquals(400, ex.getHttpStatus());
        assertEquals(ErrorCode.INVALID_ARGUMENT, ex.getErrorCode());
    }

    @Test
    void testServerError() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("{\"code\":13,\"message\":\"Internal server error\"}"));

        ApiException ex = assertThrows(ApiException.class,
                () -> httpClient.get("/api/test", TestResponse.class));

        assertEquals(500, ex.getHttpStatus());
        assertEquals(ErrorCode.INTERNAL, ex.getErrorCode());
    }

    @Test
    void testUnauthorizedError() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(401)
                .setBody("{\"code\":16,\"message\":\"Unauthenticated\"}"));

        ApiException ex = assertThrows(ApiException.class,
                () -> httpClient.get("/api/test", TestResponse.class));

        assertEquals(401, ex.getHttpStatus());
        assertEquals(ErrorCode.UNAUTHENTICATED, ex.getErrorCode());
    }

    // Retry tests
    @Test
    void testRetryOnServerError() throws InterruptedException {
        // Create client with retries enabled
        String baseUrl = mockServer.url("").toString();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        JammHttpClient retryClient = new JammHttpClient(
                mockOAuthProvider,
                baseUrl,
                5000,
                5000,
                2,    // 2 retries
                100,  // 100ms initial delay
                1000  // 1s max delay
        );

        // First two requests fail, third succeeds
        mockServer.enqueue(new MockResponse().setResponseCode(500).setBody("{\"code\":13}"));
        mockServer.enqueue(new MockResponse().setResponseCode(500).setBody("{\"code\":13}"));
        mockServer.enqueue(new MockResponse().setResponseCode(200).setBody("{\"id\":\"123\",\"name\":\"Success\"}"));

        TestResponse response = retryClient.get("/api/test", TestResponse.class);

        assertEquals("Success", response.name);
        assertEquals(3, mockServer.getRequestCount());
    }

    @Test
    void testNoRetryOnClientError() {
        // Create client with retries enabled
        String baseUrl = mockServer.url("").toString();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        JammHttpClient retryClient = new JammHttpClient(
                mockOAuthProvider,
                baseUrl,
                5000,
                5000,
                2,    // 2 retries
                100,
                1000
        );

        // Client error should not be retried
        mockServer.enqueue(new MockResponse().setResponseCode(400).setBody("{\"code\":3}"));

        assertThrows(ApiException.class, () -> retryClient.get("/api/test", TestResponse.class));

        // Only 1 request should be made (no retries for 4xx)
        assertEquals(1, mockServer.getRequestCount());
    }

    @Test
    void testRetryOn429RateLimit() {
        String baseUrl = mockServer.url("").toString();
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        JammHttpClient retryClient = new JammHttpClient(
                mockOAuthProvider,
                baseUrl,
                5000,
                5000,
                1,    // 1 retry
                100,
                1000
        );

        // 429 should be retried
        mockServer.enqueue(new MockResponse().setResponseCode(429).setBody("{\"code\":8}"));
        mockServer.enqueue(new MockResponse().setResponseCode(200).setBody("{\"id\":\"123\",\"name\":\"Success\"}"));

        TestResponse response = retryClient.get("/api/test", TestResponse.class);

        assertEquals("Success", response.name);
        assertEquals(2, mockServer.getRequestCount());
    }

    @Test
    void testEmptyResponse() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(""));

        TestResponse response = httpClient.get("/api/test", TestResponse.class);

        assertNull(response);
    }

    @Test
    void testMalformedJsonResponse() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("this is not valid json"));

        JammException ex = assertThrows(JammException.class,
                () -> httpClient.get("/api/test", TestResponse.class));

        assertTrue(ex.getMessage().startsWith("Failed to parse response body:"));
    }

    // URL path handling tests
    @Test
    void testPathWithoutLeadingSlash() throws InterruptedException {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\":\"123\",\"name\":\"Test\"}"));

        // Path without leading slash should still work
        TestResponse response = httpClient.get("api/test", TestResponse.class);

        assertNotNull(response);

        RecordedRequest request = mockServer.takeRequest(1, TimeUnit.SECONDS);
        assertEquals("/api/test", request.getPath());
    }

    @Test
    void testPathWithLeadingSlash() throws InterruptedException {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\":\"123\",\"name\":\"Test\"}"));

        TestResponse response = httpClient.get("/api/test", TestResponse.class);

        assertNotNull(response);

        RecordedRequest request = mockServer.takeRequest(1, TimeUnit.SECONDS);
        assertEquals("/api/test", request.getPath());
    }

    @Test
    void testBaseUrlWithTrailingSlash() throws InterruptedException {
        // Create client with base URL that has trailing slash
        String baseUrl = mockServer.url("").toString();
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }

        JammHttpClient clientWithTrailingSlash = new JammHttpClient(
                mockOAuthProvider,
                baseUrl,
                5000,
                5000,
                0,
                1000,
                30000
        );

        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\":\"123\",\"name\":\"Test\"}"));

        TestResponse response = clientWithTrailingSlash.get("/api/test", TestResponse.class);

        assertNotNull(response);

        RecordedRequest request = mockServer.takeRequest(1, TimeUnit.SECONDS);
        // Should not have double slash
        assertEquals("/api/test", request.getPath());
    }

    // Retry delay validation tests
    @Test
    void testNegativeMaxRetries() {
        String baseUrl = mockServer.url("").toString();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new JammHttpClient(mockOAuthProvider, baseUrl, 5000, 5000, -1, 1000, 30000));
        assertTrue(ex.getMessage().contains("maxRetries"));
    }

    @Test
    void testZeroRetryInitialDelayWithRetriesEnabled() {
        String baseUrl = mockServer.url("").toString();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new JammHttpClient(mockOAuthProvider, baseUrl, 5000, 5000, 1, 0, 30000));
        assertTrue(ex.getMessage().contains("retryInitialDelayMs"));
    }

    @Test
    void testNegativeRetryInitialDelayWithRetriesEnabled() {
        String baseUrl = mockServer.url("").toString();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new JammHttpClient(mockOAuthProvider, baseUrl, 5000, 5000, 1, -100, 30000));
        assertTrue(ex.getMessage().contains("retryInitialDelayMs"));
    }

    @Test
    void testRetryMaxDelayLessThanInitialDelay() {
        String baseUrl = mockServer.url("").toString();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new JammHttpClient(mockOAuthProvider, baseUrl, 5000, 5000, 1, 1000, 500));
        assertTrue(ex.getMessage().contains("retryMaxDelayMs"));
    }

    @Test
    void testZeroRetriesAllowsAnyDelayValues() {
        String baseUrl = mockServer.url("").toString();
        // Should not throw - delay values are ignored when retries are disabled
        JammHttpClient client = new JammHttpClient(mockOAuthProvider, baseUrl, 5000, 5000, 0, 0, 0);
        assertNotNull(client);
    }

    // Null and empty validation tests
    @Test
    void testNullOAuthProvider() {
        String baseUrl = mockServer.url("").toString();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new JammHttpClient(null, baseUrl, 5000, 5000, 0, 1000, 30000));
        assertTrue(ex.getMessage().contains("oauthProvider"));
    }

    @Test
    void testNullApiBaseUrl() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new JammHttpClient(mockOAuthProvider, null, 5000, 5000, 0, 1000, 30000));
        assertTrue(ex.getMessage().contains("apiBaseUrl"));
    }

    @Test
    void testEmptyApiBaseUrl() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new JammHttpClient(mockOAuthProvider, "", 5000, 5000, 0, 1000, 30000));
        assertTrue(ex.getMessage().contains("apiBaseUrl"));
    }

    @Test
    void testZeroConnectTimeout() {
        String baseUrl = mockServer.url("").toString();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new JammHttpClient(mockOAuthProvider, baseUrl, 0, 5000, 0, 1000, 30000));
        assertTrue(ex.getMessage().contains("connectTimeoutMs"));
    }

    @Test
    void testNegativeConnectTimeout() {
        String baseUrl = mockServer.url("").toString();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new JammHttpClient(mockOAuthProvider, baseUrl, -1, 5000, 0, 1000, 30000));
        assertTrue(ex.getMessage().contains("connectTimeoutMs"));
    }

    @Test
    void testZeroReadTimeout() {
        String baseUrl = mockServer.url("").toString();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new JammHttpClient(mockOAuthProvider, baseUrl, 5000, 0, 0, 1000, 30000));
        assertTrue(ex.getMessage().contains("readTimeoutMs"));
    }

    @Test
    void testNegativeReadTimeout() {
        String baseUrl = mockServer.url("").toString();
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new JammHttpClient(mockOAuthProvider, baseUrl, 5000, -1, 0, 1000, 30000));
        assertTrue(ex.getMessage().contains("readTimeoutMs"));
    }

    // Protobuf serialization tests
    @Test
    void testProtobufRequestBodySerialization() throws InterruptedException {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"id\":\"cust-1\",\"email\":\"test@example.com\"}"));

        Buyer buyer = Buyer.newBuilder()
                .setEmail("buyer@example.com")
                .setName("Test Buyer")
                .setPhone("09012345678")
                .build();

        httpClient.post("/api/test", buyer, TestResponse.class);

        RecordedRequest request = mockServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        String body = request.getBody().readUtf8();

        // Verify the body was serialized via JsonFormat (proto JSON), not Jackson
        assertTrue(body.contains("\"email\""), "Proto JSON should contain email field");
        assertTrue(body.contains("buyer@example.com"), "Proto JSON should contain email value");
        assertTrue(body.contains("\"name\""), "Proto JSON should contain name field");
        assertTrue(body.contains("Test Buyer"), "Proto JSON should contain name value");
        assertTrue(body.contains("\"phone\""), "Proto JSON should contain phone field");
        assertTrue(body.contains("09012345678"), "Proto JSON should contain phone value");
    }

    @Test
    void testProtobufResponseParsing() {
        String protoJson = "{\"id\":\"cust-123\",\"email\":\"parsed@example.com\",\"activated\":true}";
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(protoJson));

        Customer customer = httpClient.get("/api/customers/cust-123", Customer.class);

        assertNotNull(customer);
        assertEquals("cust-123", customer.getId());
        assertEquals("parsed@example.com", customer.getEmail());
        assertTrue(customer.getActivated());
    }

    @Test
    void testProtobufResponseIgnoresUnknownFields() {
        // JSON with an extra field not in the Customer proto definition
        String protoJson = "{\"id\":\"cust-456\",\"email\":\"unknown@example.com\",\"unknownField\":\"should-be-ignored\"}";
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody(protoJson));

        Customer customer = httpClient.get("/api/customers/cust-456", Customer.class);

        assertNotNull(customer);
        assertEquals("cust-456", customer.getId());
        assertEquals("unknown@example.com", customer.getEmail());
        // No exception thrown — unknown fields are silently ignored
    }

    // Helper classes for testing
    static class TestRequest {
        public String name;

        public TestRequest() {}

        public TestRequest(String name) {
            this.name = name;
        }
    }

    static class TestResponse {
        public String id;
        public String name;
    }
}
