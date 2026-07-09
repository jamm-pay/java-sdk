package com.jamm.auth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.jamm.errors.OAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

/**
 * OAuth2 provider for fetching and caching access tokens.
 * Uses the client credentials grant flow.
 */
public class OAuthProvider implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(OAuthProvider.class);
    private static final String TOKEN_ENDPOINT = "/oauth2/token";

    private final String clientId;
    private final String clientSecret;
    private final String oauthBaseUrl;
    private final int connectTimeout;
    private final int readTimeout;

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
        this.connectTimeout = (int) Math.min(connectTimeout, Integer.MAX_VALUE);
        this.readTimeout = (int) Math.min(readTimeout, Integer.MAX_VALUE);
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

            byte[] formBody = ("grant_type=client_credentials&client_id=" + urlEncode(clientId))
                    .getBytes(StandardCharsets.UTF_8);

            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(tokenUrl).openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(connectTimeout);
                conn.setReadTimeout(readTimeout);
                conn.setInstanceFollowRedirects(false);
                conn.setRequestProperty("Authorization", "Basic " + encodedCredentials);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(formBody.length);
                try (OutputStream out = conn.getOutputStream()) {
                    out.write(formBody);
                }

                int code = conn.getResponseCode();
                String responseBody = readBody(conn, code);

                if (code < 200 || code >= 300) {
                    throw new OAuthException("OAuth token request failed", code, responseBody);
                }

                return parseTokenResponse(responseBody, code);

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

    private static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (IOException e) {
            // UTF-8 is always supported.
            throw new IllegalStateException(e);
        }
    }

    private static String readBody(HttpURLConnection conn, int code) throws IOException {
        InputStream stream = code >= 400 ? conn.getErrorStream() : conn.getInputStream();
        if (stream == null) {
            return "";
        }
        try (InputStream in = stream) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] chunk = new byte[8192];
            int read;
            while ((read = in.read(chunk)) != -1) {
                buffer.write(chunk, 0, read);
            }
            return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
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
        JsonObject root;
        try {
            root = JsonParser.parseString(responseBody).getAsJsonObject();
        } catch (JsonParseException | IllegalStateException e) {
            throw new OAuthException(
                    "Failed to parse OAuth response",
                    statusCode,
                    responseBody
            );
        }

        JsonElement accessToken = root.get("access_token");
        if (accessToken == null || !accessToken.isJsonPrimitive()) {
            throw new OAuthException(
                    "Access token not found in OAuth response",
                    statusCode,
                    responseBody
            );
        }

        cachedToken = accessToken.getAsString();

        // Parse expiry if it is present and numeric; otherwise default to 1 hour.
        JsonElement expiresIn = root.get("expires_in");
        if (expiresIn != null && expiresIn.isJsonPrimitive() && expiresIn.getAsJsonPrimitive().isNumber()) {
            tokenExpiry = Instant.now().plusSeconds(expiresIn.getAsLong());
            LOGGER.debug("Token will expire at {}", tokenExpiry);
        } else {
            tokenExpiry = Instant.now().plusSeconds(3600);
        }

        return cachedToken;
    }

    /**
     * No-op. Uses {@link HttpURLConnection}, which holds no pool or executor to release; retained
     * for API compatibility and the {@link AutoCloseable} contract.
     */
    @Override
    public void close() {
        // Nothing to release.
    }
}
