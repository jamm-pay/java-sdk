package com.jamm.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import com.jamm.Jamm;
import com.jamm.auth.OAuthProvider;
import com.jamm.errors.ApiException;
import com.jamm.errors.JammException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * HTTP client wrapper for making authenticated API requests.
 * Handles JSON serialization, authentication, and error handling.
 */
public class JammHttpClient implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JammHttpClient.class);
    private static final String MERCHANT_HEADER = "Jamm-Merchant";
    private static final Pattern MERCHANT_ID_PATTERN = Pattern.compile("^mer-[0-9A-Za-z_-]+$");

    private final OAuthProvider oauthProvider;
    private final String apiBaseUrl;
    private final int connectTimeoutMs;
    private final int readTimeoutMs;
    private final Gson gson;
    private final int maxRetries;
    private final long retryInitialDelayMs;
    private final long retryMaxDelayMs;
    private final boolean platformMode;

    /**
     * Creates a new JammHttpClient.
     *
     * @param oauthProvider      the OAuth provider for authentication
     * @param apiBaseUrl         the API base URL
     * @param connectTimeoutMs   connection timeout in milliseconds
     * @param readTimeoutMs      read timeout in milliseconds
     * @param maxRetries         maximum number of retry attempts
     * @param retryInitialDelayMs initial delay between retries in milliseconds
     * @param retryMaxDelayMs    maximum delay between retries in milliseconds
     */
    public JammHttpClient(OAuthProvider oauthProvider, String apiBaseUrl,
                          long connectTimeoutMs, long readTimeoutMs,
                          int maxRetries, long retryInitialDelayMs, long retryMaxDelayMs,
                          boolean platformMode) {
        if (oauthProvider == null) {
            throw new IllegalArgumentException("oauthProvider must not be null");
        }
        if (apiBaseUrl == null || apiBaseUrl.isEmpty()) {
            throw new IllegalArgumentException("apiBaseUrl must not be null or empty");
        }
        if (connectTimeoutMs <= 0) {
            throw new IllegalArgumentException("connectTimeoutMs must be positive");
        }
        if (readTimeoutMs <= 0) {
            throw new IllegalArgumentException("readTimeoutMs must be positive");
        }
        if (maxRetries < 0) {
            throw new IllegalArgumentException("maxRetries must be non-negative");
        }
        if (maxRetries > 0) {
            if (retryInitialDelayMs <= 0) {
                throw new IllegalArgumentException("retryInitialDelayMs must be positive when retries are enabled");
            }
            if (retryMaxDelayMs < retryInitialDelayMs) {
                throw new IllegalArgumentException("retryMaxDelayMs must be >= retryInitialDelayMs");
            }
        }

        this.oauthProvider = oauthProvider;
        this.apiBaseUrl = apiBaseUrl;
        this.connectTimeoutMs = (int) Math.min(connectTimeoutMs, Integer.MAX_VALUE);
        this.readTimeoutMs = (int) Math.min(readTimeoutMs, Integer.MAX_VALUE);
        this.maxRetries = maxRetries;
        this.retryInitialDelayMs = retryInitialDelayMs;
        this.retryMaxDelayMs = retryMaxDelayMs;
        this.platformMode = platformMode;

        // Non-proto bodies/responses only (proto uses JsonFormat). gson ignores unknown
        // response fields by default; disableHtmlEscaping keeps &, <, > literal in request JSON.
        this.gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    /**
     * Executes a GET request.
     *
     * @param path         the API path (relative to base URL)
     * @param responseType the expected response type
     * @param <T>          the response type
     * @return the parsed response
     * @throws ApiException if the request fails
     */
    public <T> T get(String path, Class<T> responseType) {
        return get(path, responseType, RequestOptions.none());
    }

    /**
     * Executes a GET request with per-request options.
     *
     * @param path         the API path (relative to base URL)
     * @param responseType the expected response type
     * @param options      per-request options (e.g., merchant for platform mode)
     * @param <T>          the response type
     * @return the parsed response
     * @throws ApiException if the request fails
     */
    public <T> T get(String path, Class<T> responseType, RequestOptions options) {
        RequestOptions opts = safeOptions(options);
        validateMerchant(opts.getMerchant());
        return executeWithRetry(true,
                () -> execute(path, "GET", null, opts.getMerchant(), responseType));
    }

    /**
     * Executes a POST request.
     *
     * @param path         the API path (relative to base URL)
     * @param body         the request body object
     * @param responseType the expected response type
     * @param <T>          the response type
     * @return the parsed response
     * @throws ApiException if the request fails
     */
    public <T> T post(String path, Object body, Class<T> responseType) {
        return post(path, body, responseType, RequestOptions.none());
    }

    /**
     * Executes a POST request with per-request options.
     *
     * @param path         the API path (relative to base URL)
     * @param body         the request body object
     * @param responseType the expected response type
     * @param options      per-request options (e.g., merchant for platform mode)
     * @param <T>          the response type
     * @return the parsed response
     * @throws ApiException if the request fails
     */
    public <T> T post(String path, Object body, Class<T> responseType, RequestOptions options) {
        RequestOptions opts = safeOptions(options);
        validateMerchant(opts.getMerchant());
        return executeWithRetry(false,
                () -> execute(path, "POST", body, opts.getMerchant(), responseType));
    }

    /**
     * Executes a PUT request.
     *
     * @param path         the API path (relative to base URL)
     * @param body         the request body object
     * @param responseType the expected response type
     * @param <T>          the response type
     * @return the parsed response
     * @throws ApiException if the request fails
     */
    public <T> T put(String path, Object body, Class<T> responseType) {
        return put(path, body, responseType, RequestOptions.none());
    }

    /**
     * Executes a PUT request with per-request options.
     *
     * @param path         the API path (relative to base URL)
     * @param body         the request body object
     * @param responseType the expected response type
     * @param options      per-request options (e.g., merchant for platform mode)
     * @param <T>          the response type
     * @return the parsed response
     * @throws ApiException if the request fails
     */
    public <T> T put(String path, Object body, Class<T> responseType, RequestOptions options) {
        RequestOptions opts = safeOptions(options);
        validateMerchant(opts.getMerchant());
        return executeWithRetry(true,
                () -> execute(path, "PUT", body, opts.getMerchant(), responseType));
    }

    /**
     * Executes a DELETE request.
     *
     * @param path         the API path (relative to base URL)
     * @param responseType the expected response type
     * @param <T>          the response type
     * @return the parsed response
     * @throws ApiException if the request fails
     */
    public <T> T delete(String path, Class<T> responseType) {
        return delete(path, responseType, RequestOptions.none());
    }

    /**
     * Executes a DELETE request with per-request options.
     *
     * @param path         the API path (relative to base URL)
     * @param responseType the expected response type
     * @param options      per-request options (e.g., merchant for platform mode)
     * @param <T>          the response type
     * @return the parsed response
     * @throws ApiException if the request fails
     */
    public <T> T delete(String path, Class<T> responseType, RequestOptions options) {
        RequestOptions opts = safeOptions(options);
        validateMerchant(opts.getMerchant());
        return executeWithRetry(true,
                () -> execute(path, "DELETE", null, opts.getMerchant(), responseType));
    }

    /**
     * Executes a PATCH request.
     *
     * @param path         the API path (relative to base URL)
     * @param body         the request body object
     * @param responseType the expected response type
     * @param <T>          the response type
     * @return the parsed response
     * @throws ApiException if the request fails
     */
    public <T> T patch(String path, Object body, Class<T> responseType) {
        return patch(path, body, responseType, RequestOptions.none());
    }

    /**
     * Executes a PATCH request with per-request options.
     *
     * @param path         the API path (relative to base URL)
     * @param body         the request body object
     * @param responseType the expected response type
     * @param options      per-request options (e.g., merchant for platform mode)
     * @param <T>          the response type
     * @return the parsed response
     * @throws ApiException if the request fails
     */
    public <T> T patch(String path, Object body, Class<T> responseType, RequestOptions options) {
        RequestOptions opts = safeOptions(options);
        validateMerchant(opts.getMerchant());
        return executeWithRetry(false,
                () -> execute(path, "PATCH", body, opts.getMerchant(), responseType));
    }

    private static RequestOptions safeOptions(RequestOptions options) {
        return options != null ? options : RequestOptions.none();
    }

    private void validateMerchant(String merchant) {
        if (merchant == null) {
            return;
        }
        if (!platformMode) {
            throw new JammException("merchant parameter can only be used in platform mode");
        }
        if (!MERCHANT_ID_PATTERN.matcher(merchant).matches()) {
            throw new JammException("invalid merchant id format");
        }
    }

    private <T> T execute(String path, String method, Object body, String merchant, Class<T> responseType) {
        String url = buildUrl(apiBaseUrl, path);
        byte[] payload = serializeBody(body);

        LOGGER.debug("Executing {} {}", method, url);

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            forceRequestMethod(conn, method.toUpperCase());
            conn.setConnectTimeout(connectTimeoutMs);
            conn.setReadTimeout(readTimeoutMs);
            conn.setInstanceFollowRedirects(false);

            conn.setRequestProperty("Authorization", "Bearer " + oauthProvider.getToken());
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("X-SDK-Version", "java:" + Jamm.VERSION);
            if (merchant != null) {
                conn.setRequestProperty(MERCHANT_HEADER, merchant);
            }

            // POST/PUT/PATCH always send a body (empty if none); DELETE only when one is provided.
            boolean sendBody = payload != null
                    && !"GET".equalsIgnoreCase(method)
                    && !("DELETE".equalsIgnoreCase(method) && payload.length == 0);
            if (sendBody) {
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(payload.length);
                try (OutputStream out = conn.getOutputStream()) {
                    out.write(payload);
                }
            }

            int code = conn.getResponseCode();
            String responseBody = readResponseBody(conn, code);

            if (code < 200 || code >= 300) {
                throw ApiException.fromResponse(
                        code, collectHeaders(conn), responseBody, method, conn.getURL().getPath());
            }

            if (responseType == Void.class || responseType == void.class || responseBody.isEmpty()) {
                return null;
            }

            try {
                if (Message.class.isAssignableFrom(responseType)) {
                    Message.Builder builder = createProtoBuilder(responseType);
                    JsonFormat.parser().ignoringUnknownFields().merge(responseBody, builder);
                    @SuppressWarnings("unchecked")
                    T parsed = (T) builder.build();
                    return parsed;
                }
                return gson.fromJson(responseBody, responseType);
            } catch (JsonParseException | InvalidProtocolBufferException e) {
                throw new JammException("Failed to parse response body: " + e.getMessage(), e);
            }

        } catch (SocketTimeoutException e) {
            if (conn != null) {
                conn.disconnect();
            }
            throw new JammException("Request timed out", e);
        } catch (IOException e) {
            if (conn != null) {
                conn.disconnect();
            }
            throw new JammException("Network error: " + e.getMessage(), e);
        }
    }

    private byte[] serializeBody(Object body) {
        if (body == null) {
            return new byte[0];
        }
        try {
            String json = body instanceof MessageOrBuilder
                    ? JsonFormat.printer().print((MessageOrBuilder) body)
                    : gson.toJson(body);
            return json.getBytes(StandardCharsets.UTF_8);
        } catch (InvalidProtocolBufferException e) {
            throw new JammException("Failed to serialize request body", e);
        }
    }

    /**
     * Reads the response (2xx) or error (>=400) stream fully as a UTF-8 string, draining it so the
     * underlying connection can be reused. Returns "" when there is no body (e.g. 204).
     */
    private static String readResponseBody(HttpURLConnection conn, int code) throws IOException {
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

    private static Map<String, String> collectHeaders(HttpURLConnection conn) {
        Map<String, String> headers = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : conn.getHeaderFields().entrySet()) {
            // The status line is keyed under null; skip it. Keep the last value for a repeated
            // header, matching the previous OkHttp behaviour (Headers.get returns the last).
            if (entry.getKey() != null && entry.getValue() != null && !entry.getValue().isEmpty()) {
                headers.put(entry.getKey(), entry.getValue().get(entry.getValue().size() - 1));
            }
        }
        return headers;
    }

    /**
     * Sets the request method. {@link HttpURLConnection#setRequestMethod} rejects {@code PATCH};
     * fall back to overriding the private {@code method} field (the Jamm API only issues GET/POST,
     * so this path is exercised only by the generic {@code patch()} helper).
     */
    private static void forceRequestMethod(HttpURLConnection conn, String method) {
        try {
            conn.setRequestMethod(method);
        } catch (ProtocolException e) {
            try {
                Object target = conn;
                Field delegate = findField(conn.getClass(), "delegate");
                if (delegate != null) {
                    delegate.setAccessible(true);
                    Object delegateConn = delegate.get(conn);
                    if (delegateConn instanceof HttpURLConnection) {
                        target = delegateConn;
                    }
                }
                Field methodField = findField(target.getClass(), "method");
                if (methodField == null) {
                    throw new NoSuchFieldException("method");
                }
                methodField.setAccessible(true);
                methodField.set(target, method);
            } catch (ReflectiveOperationException | RuntimeException re) {
                // RuntimeException covers InaccessibleObjectException on JDK 9+ (java.net not open).
                throw new JammException("Unsupported HTTP method: " + method, re);
            }
        }
    }

    private static Field findField(Class<?> type, String name) {
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            try {
                return c.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                // walk up the hierarchy
            }
        }
        return null;
    }

    /**
     * Executes a request with retry handling.
     *
     * @param idempotent whether the request is safe to retry. Non-idempotent requests
     *                   (POST/PATCH, e.g. creating a charge) are not retried on timeout/5xx
     *                   because the server may have already processed them, which would
     *                   duplicate the charge. A 401 still triggers a single token refresh
     *                   for any method, since a 401 means the request was rejected before
     *                   processing.
     */
    private <T> T executeWithRetry(boolean idempotent, RequestExecutor<T> executor) {
        long delayMs = retryInitialDelayMs;
        boolean authRefreshed = false;
        int attempt = 0;

        while (true) {
            try {
                return executor.execute();
            } catch (JammException e) {
                Integer status = e.getHttpStatus();

                // Reactively refresh the token once on 401: the cached token may be stale
                // (rotated secret, clock skew beyond the refresh buffer). Safe for any method
                // because a 401 is rejected before the request is processed.
                if (status != null && status == 401 && !authRefreshed) {
                    authRefreshed = true;
                    oauthProvider.clearCache();
                    LOGGER.debug("Got 401, refreshing OAuth token and retrying once");
                    continue;
                }

                // Don't retry on client errors (4xx) except for 429 (rate limit).
                if (status != null && status >= 400 && status < 500 && status != 429) {
                    throw e;
                }

                // Only retry idempotent requests; retrying a POST/PATCH could duplicate a charge.
                if (!idempotent || attempt >= maxRetries) {
                    throw e;
                }

                LOGGER.warn("Request failed (attempt {}/{}), retrying in {}ms: {}",
                        attempt + 1, maxRetries + 1, delayMs, e.getMessage());

                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new JammException("Request interrupted", ie);
                }

                // Exponential backoff with overflow protection:
                // ensure delay multiplication cannot overflow long.
                long nextDelay;
                if (delayMs > retryMaxDelayMs / 2) {
                    nextDelay = retryMaxDelayMs;
                } else {
                    nextDelay = delayMs * 2;
                }
                delayMs = Math.min(nextDelay, retryMaxDelayMs);
                attempt++;
            }
        }
    }


    private <T> Message.Builder createProtoBuilder(Class<T> responseType) {
        try {
            return (Message.Builder) responseType.getMethod("newBuilder").invoke(null);
        } catch (ReflectiveOperationException e) {
            throw new JammException(
                    "Failed to create protobuf builder for response type: " + responseType.getName(), e);
        }
    }

    /**
     * Builds a complete URL from a base URL and path, ensuring proper formatting.
     *
     * @param baseUrl the base URL
     * @param path    the path to append
     * @return the complete URL
     */
    private String buildUrl(String baseUrl, String path) {
        if (path == null || path.isEmpty()) {
            return baseUrl;
        }

        boolean baseEndsWithSlash = baseUrl.endsWith("/");
        boolean pathStartsWithSlash = path.startsWith("/");

        if (baseEndsWithSlash && pathStartsWithSlash) {
            // Remove duplicate slash
            return baseUrl + path.substring(1);
        } else if (!baseEndsWithSlash && !pathStartsWithSlash) {
            // Add missing slash
            return baseUrl + "/" + path;
        } else {
            // One has slash, one doesn't - just concatenate
            return baseUrl + path;
        }
    }

    @FunctionalInterface
    private interface RequestExecutor<T> {
        T execute();
    }

    /**
     * No-op. The client uses {@link HttpURLConnection}, which holds no pool or executor to release;
     * retained for API compatibility and the {@link AutoCloseable} contract.
     */
    @Override
    public void close() {
        // Nothing to release.
    }
}
