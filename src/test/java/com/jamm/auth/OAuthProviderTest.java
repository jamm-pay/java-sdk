package com.jamm.auth;

import com.jamm.errors.OAuthException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class OAuthProviderTest {

    private MockWebServer mockServer;
    private OAuthProvider oauthProvider;

    @BeforeEach
    void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        String baseUrl = mockServer.url("").toString();
        // Remove trailing slash
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }

        oauthProvider = new OAuthProvider(
                "test-client-id",
                "test-client-secret",
                baseUrl,
                5000,
                5000
        );
    }

    @AfterEach
    void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    void testGetTokenSuccess() throws InterruptedException {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"access_token\":\"test-token-123\",\"expires_in\":3600,\"token_type\":\"Bearer\"}"));

        String token = oauthProvider.getToken();

        assertEquals("test-token-123", token);

        // Verify request
        RecordedRequest request = mockServer.takeRequest(1, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("POST", request.getMethod());
        assertEquals("/oauth2/token", request.getPath());

        // Verify Basic auth header
        String authHeader = request.getHeader("Authorization");
        assertNotNull(authHeader);
        assertTrue(authHeader.startsWith("Basic "));
        String credentials = new String(Base64.getDecoder().decode(authHeader.substring(6)));
        assertEquals("test-client-id:test-client-secret", credentials);

        // Verify Content-Type
        assertTrue(request.getHeader("Content-Type").startsWith("application/x-www-form-urlencoded"));

        // Verify body
        String body = request.getBody().readUtf8();
        assertTrue(body.contains("grant_type=client_credentials"));
        assertTrue(body.contains("client_id=test-client-id"));
    }

    @Test
    void testGetTokenCaching() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-Type", "application/json")
                .setBody("{\"access_token\":\"cached-token\",\"expires_in\":3600}"));

        // First call - should fetch token
        String token1 = oauthProvider.getToken();
        assertEquals("cached-token", token1);

        // Second call - should use cached token (no new request)
        String token2 = oauthProvider.getToken();
        assertEquals("cached-token", token2);

        // Only one request should have been made
        assertEquals(1, mockServer.getRequestCount());
    }

    @Test
    void testRefreshTokenForcesNewRequest() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"access_token\":\"token-1\",\"expires_in\":3600}"));
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"access_token\":\"token-2\",\"expires_in\":3600}"));

        String token1 = oauthProvider.getToken();
        assertEquals("token-1", token1);

        String token2 = oauthProvider.refreshToken();
        assertEquals("token-2", token2);

        assertEquals(2, mockServer.getRequestCount());
    }

    @Test
    void testClearCacheForcesNewRequest() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"access_token\":\"token-1\",\"expires_in\":3600}"));
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"access_token\":\"token-2\",\"expires_in\":3600}"));

        String token1 = oauthProvider.getToken();
        assertEquals("token-1", token1);

        oauthProvider.clearCache();

        String token2 = oauthProvider.getToken();
        assertEquals("token-2", token2);

        assertEquals(2, mockServer.getRequestCount());
    }

    @Test
    void testGetTokenUnauthorized() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(401)
                .setBody("{\"error\":\"invalid_client\"}"));

        OAuthException ex = assertThrows(OAuthException.class, () -> oauthProvider.getToken());

        assertEquals(401, ex.getHttpStatus());
        assertTrue(ex.getMessage().contains("OAuth token request failed"));
    }

    @Test
    void testGetTokenServerError() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("{\"error\":\"internal_error\"}"));

        OAuthException ex = assertThrows(OAuthException.class, () -> oauthProvider.getToken());

        assertEquals(500, ex.getHttpStatus());
    }

    @Test
    void testGetTokenMissingAccessToken() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"token_type\":\"Bearer\",\"expires_in\":3600}"));

        OAuthException ex = assertThrows(OAuthException.class, () -> oauthProvider.getToken());

        assertTrue(ex.getMessage().contains("Access token not found"));
    }

    @Test
    void testGetTokenInvalidJson() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("not valid json"));

        OAuthException ex = assertThrows(OAuthException.class, () -> oauthProvider.getToken());

        assertTrue(ex.getMessage().contains("Failed to parse"));
    }

    @Test
    void testGetTokenWithoutExpiresIn() {
        mockServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"access_token\":\"token-no-expiry\"}"));

        String token = oauthProvider.getToken();

        assertEquals("token-no-expiry", token);
    }

    // Input validation tests
    @Test
    void testConstructorNullClientId() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new OAuthProvider(null, "secret", "https://oauth.example.com", 5000, 5000));
        assertTrue(ex.getMessage().contains("clientId"));
    }

    @Test
    void testConstructorEmptyClientId() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new OAuthProvider("", "secret", "https://oauth.example.com", 5000, 5000));
        assertTrue(ex.getMessage().contains("clientId"));
    }

    @Test
    void testConstructorNullClientSecret() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new OAuthProvider("client-id", null, "https://oauth.example.com", 5000, 5000));
        assertTrue(ex.getMessage().contains("clientSecret"));
    }

    @Test
    void testConstructorEmptyClientSecret() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new OAuthProvider("client-id", "", "https://oauth.example.com", 5000, 5000));
        assertTrue(ex.getMessage().contains("clientSecret"));
    }

    @Test
    void testConstructorNullOauthBaseUrl() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new OAuthProvider("client-id", "secret", null, 5000, 5000));
        assertTrue(ex.getMessage().contains("oauthBaseUrl"));
    }

    @Test
    void testConstructorEmptyOauthBaseUrl() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new OAuthProvider("client-id", "secret", "", 5000, 5000));
        assertTrue(ex.getMessage().contains("oauthBaseUrl"));
    }

    @Test
    void testConstructorZeroConnectTimeout() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new OAuthProvider("client-id", "secret", "https://oauth.example.com", 0, 5000));
        assertTrue(ex.getMessage().contains("connectTimeout"));
    }

    @Test
    void testConstructorNegativeConnectTimeout() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new OAuthProvider("client-id", "secret", "https://oauth.example.com", -1, 5000));
        assertTrue(ex.getMessage().contains("connectTimeout"));
    }

    @Test
    void testConstructorZeroReadTimeout() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new OAuthProvider("client-id", "secret", "https://oauth.example.com", 5000, 0));
        assertTrue(ex.getMessage().contains("readTimeout"));
    }

    @Test
    void testConstructorNegativeReadTimeout() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                new OAuthProvider("client-id", "secret", "https://oauth.example.com", 5000, -1));
        assertTrue(ex.getMessage().contains("readTimeout"));
    }
}
