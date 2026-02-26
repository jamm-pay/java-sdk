package com.jamm.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamm.errors.OAuthException;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * OAuth2 provider for fetching and caching access tokens.
 * Uses the client credentials grant flow.
 */
public class OAuthProvider implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthProvider.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String TOKEN_ENDPOINT = "/oauth2/token";

    private final String clientId;
    private final String clientSecret;
    private final String oauthBaseUrl;
    private final OkHttpClient httpClient;

    // Token caching
    private String cachedToken;
    private Instant tokenExpiry;
    private final Object tokenLock = new Object();

    // Buffer time before expiry to refresh token (30 seconds)
    private static final long EXPIRY_BUFFER_SECONDS = 30;

    /**
     * Creates a new OAuthProvider.
     *
     * @param clientId      the OAuth client ID
     * @param clientSecret  the OAuth client secret
     * @param oauthBaseUrl  the OAuth server base URL
     * @param connectTimeout connection timeout in milliseconds (must be positive)
     * @param readTimeout    read timeout in milliseconds (must be positive)
     * @throws IllegalArgumentException if clientId, clientSecret, or oauthBaseUrl is null/empty,
     *                                  or if timeout values are not positive
     */
    public OAuthProvider(String clientId, String clientSecret, String oauthBaseUrl,
                         long connectTimeout, long readTimeout) {
        if (clientId == null) {
            throw new IllegalArgumentException("clientId must not be null");
        }
        if (clientSecret == null) {
            throw new IllegalArgumentException("clientSecret must not be null");
        }
        if (oauthBaseUrl == null) {
            throw new IllegalArgumentException("oauthBaseUrl must not be null");
        }

        if (clientId.isEmpty()) {
            throw new IllegalArgumentException("clientId must not be null or empty");
        }
        if (clientSecret.isEmpty()) {
            throw new IllegalArgumentException("clientSecret must not be null or empty");
        }
        if (oauthBaseUrl.isEmpty()) {
            throw new IllegalArgumentException("oauthBaseUrl must not be null or empty");
        }
        if (connectTimeout <= 0) {
            throw new IllegalArgumentException("connectTimeout must be positive");
        }
        if (readTimeout <= 0) {
            throw new IllegalArgumentException("readTimeout must be positive");
        }

        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.oauthBaseUrl = oauthBaseUrl;

        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * Gets a valid access token, fetching a new one if necessary.
     * This method is thread-safe and will cache tokens.
     *
     * @return a valid access token
     * @throws OAuthException if token fetching fails
     */
    public String getToken() {
        synchronized (tokenLock) {
            if (isTokenValid()) {
                return cachedToken;
            }
            return refreshToken();
        }
    }

    /**
     * Forces a token refresh, ignoring any cached token.
     *
     * @return a new access token
     * @throws OAuthException if token fetching fails
     */
    public String refreshToken() {
        synchronized (tokenLock) {
            LOGGER.debug("Fetching new OAuth token from {}", oauthBaseUrl);

            String tokenUrl = oauthBaseUrl + TOKEN_ENDPOINT;

            // Avoid storing plaintext credentials in a local variable
            String encodedCredentials = Base64.getEncoder().encodeToString(
                    (clientId + ":" + clientSecret).getBytes(StandardCharsets.UTF_8)
            );

            // Use FormBody.Builder for proper URL encoding of form parameters
            RequestBody body = new FormBody.Builder()
                    .add("grant_type", "client_credentials")
                    .add("client_id", clientId)
                    .build();

            Request request = new Request.Builder()
                    .url(tokenUrl)
                    .post(body)
                    .header("Authorization", "Basic " + encodedCredentials)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";

                if (!response.isSuccessful()) {
                    throw new OAuthException(
                            "OAuth token request failed",
                            response.code(),
                            responseBody
                    );
                }

                return parseTokenResponse(responseBody, response.code());

            } catch (SocketTimeoutException e) {
                throw new OAuthException("OAuth request timed out", e);
            } catch (IOException e) {
                throw new OAuthException(
                        "An unexpected error occurred while communicating with OAuth server: " + e.getMessage(),
                        e
                );
            }
        }
    }

    /**
     * Clears the cached token, forcing a refresh on the next getToken() call.
     */
    public void clearCache() {
        synchronized (tokenLock) {
            cachedToken = null;
            tokenExpiry = null;
        }
    }

    private boolean isTokenValid() {
        if (cachedToken == null || tokenExpiry == null) {
            return false;
        }
        // Consider token invalid if it will expire within the buffer time
        return Instant.now().plusSeconds(EXPIRY_BUFFER_SECONDS).isBefore(tokenExpiry);
    }

    private String parseTokenResponse(String responseBody, int statusCode) {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(responseBody);

            if (!root.has("access_token")) {
                throw new OAuthException(
                        "Access token not found in OAuth response",
                        statusCode,
                        responseBody
                );
            }

            cachedToken = root.get("access_token").asText();

            // Parse expiry if available
            if (root.has("expires_in")) {
                long expiresIn = root.get("expires_in").asLong();
                tokenExpiry = Instant.now().plusSeconds(expiresIn);
                LOGGER.debug("Token will expire at {}", tokenExpiry);
            } else {
                // Default to 1 hour if not specified
                tokenExpiry = Instant.now().plusSeconds(3600);
            }

            return cachedToken;

        } catch (IOException e) {
            throw new OAuthException(
                    "Failed to parse OAuth response",
                    statusCode,
                    responseBody
            );
        }
    }

    /**
     * Releases resources held by the underlying OkHttpClient.
     * <p>
     * OkHttpClient manages its own connection pool and executor service.
     * This method allows callers (typically {@link com.jamm.JammClient})
     * to explicitly clean up those resources when the provider is no longer needed.
     */
    @Override
    public void close() {
        httpClient.dispatcher().executorService().shutdown();
        httpClient.connectionPool().evictAll();
    }
}
