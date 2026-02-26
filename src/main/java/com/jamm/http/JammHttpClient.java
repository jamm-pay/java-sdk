package com.jamm.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import com.jamm.Jamm;
import com.jamm.auth.OAuthProvider;
import com.jamm.errors.ApiException;
import com.jamm.errors.JammException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HTTP client wrapper for making authenticated API requests.
 * Handles JSON serialization, authentication, and error handling.
 */
public class JammHttpClient implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JammHttpClient.class);
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient httpClient;
    private final OAuthProvider oauthProvider;
    private final String apiBaseUrl;
    private final ObjectMapper objectMapper;
    private final int maxRetries;
    private final long retryInitialDelayMs;
    private final long retryMaxDelayMs;

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
                          int maxRetries, long retryInitialDelayMs, long retryMaxDelayMs) {
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
        this.maxRetries = maxRetries;
        this.retryInitialDelayMs = retryInitialDelayMs;
        this.retryMaxDelayMs = retryMaxDelayMs;

        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(connectTimeoutMs, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeoutMs, TimeUnit.MILLISECONDS)
                .build();

        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
        return executeWithRetry(() -> {
            Request request = buildRequest(path, "GET", null);
            return execute(request, responseType);
        });
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
        return executeWithRetry(() -> {
            Request request = buildRequest(path, "POST", body);
            return execute(request, responseType);
        });
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
        return executeWithRetry(() -> {
            Request request = buildRequest(path, "PUT", body);
            return execute(request, responseType);
        });
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
        return executeWithRetry(() -> {
            Request request = buildRequest(path, "DELETE", null);
            return execute(request, responseType);
        });
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
        return executeWithRetry(() -> {
            Request request = buildRequest(path, "PATCH", body);
            return execute(request, responseType);
        });
    }

    private Request buildRequest(String path, String method, Object body) {
        String url = buildUrl(apiBaseUrl, path);
        String token = oauthProvider.getToken();

        Request.Builder builder = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("X-SDK-Version", "java:" + Jamm.VERSION);

        RequestBody requestBody = null;
        if (body != null) {
            try {
                String json;
                if (body instanceof MessageOrBuilder) {
                    json = JsonFormat.printer().print((MessageOrBuilder) body);
                } else {
                    json = objectMapper.writeValueAsString(body);
                }
                requestBody = RequestBody.create(json, JSON);
            } catch (JsonProcessingException | InvalidProtocolBufferException e) {
                throw new JammException("Failed to serialize request body", e);
            }
        }

        switch (method.toUpperCase()) {
            case "GET":
                builder.get();
                break;
            case "POST":
                builder.post(requestBody != null ? requestBody : RequestBody.create("", JSON));
                break;
            case "PUT":
                builder.put(requestBody != null ? requestBody : RequestBody.create("", JSON));
                break;
            case "DELETE":
                if (requestBody != null) {
                    builder.delete(requestBody);
                } else {
                    builder.delete();
                }
                break;
            case "PATCH":
                builder.patch(requestBody != null ? requestBody : RequestBody.create("", JSON));
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        return builder.build();
    }

    private <T> T execute(Request request, Class<T> responseType) {
        LOGGER.debug("Executing {} {}", request.method(), request.url());

        try (Response response = httpClient.newCall(request).execute()) {
            // NOTE: response.body() may be null for responses with no body (e.g. 204).
            // In that case we intentionally treat it as an empty string.
            // Downstream logic explicitly checks for empty response bodies and returns null,
            // so this is safe and avoids unnecessary null handling.
            String responseBody = response.body() != null ? response.body().string() : "";

            if (!response.isSuccessful()) {
                Map<String, String> headers = new HashMap<>();
                for (String name : response.headers().names()) {
                    headers.put(name, response.header(name));
                }
                throw ApiException.fromResponse(response.code(), headers, responseBody);
            }

            if (responseType == Void.class || responseType == void.class) {
                return null;
            }

            if (responseBody.isEmpty()) {
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
                return objectMapper.readValue(responseBody, responseType);
            } catch (JsonProcessingException | InvalidProtocolBufferException e) {
                throw new JammException("Failed to parse response body: " + e.getMessage(), e);
            }

        } catch (SocketTimeoutException e) {
            throw new JammException("Request timed out", e);
        } catch (IOException e) {
            throw new JammException("Network error: " + e.getMessage(), e);
        }
    }

    private <T> T executeWithRetry(RequestExecutor<T> executor) {
        long delayMs = retryInitialDelayMs;

        for (int attempt = 0; attempt <= maxRetries; attempt++) {
            try {
                return executor.execute();
            } catch (JammException e) {
                // Don't retry on client errors (4xx) except for 429 (rate limit)
                if (e.getHttpStatus() != null) {
                    int status = e.getHttpStatus();
                    if (status >= 400 && status < 500 && status != 429) {
                        throw e;
                    }
                }

                if (attempt == maxRetries) {
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
            }
        }
        // Should not reach here, but required for compilation
        throw new JammException("Failed to execute request after retries");
    }

    /**
     * Gets the ObjectMapper used for JSON serialization.
     *
     * @return the ObjectMapper instance
     */
    ObjectMapper getObjectMapper() {
        return objectMapper;
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
     * Releases resources held by the underlying OkHttpClient.
     * <p>
     * OkHttpClient manages its own connection pool and executor service.
     * This method allows callers to explicitly clean up those resources
     * when the client is no longer needed.
     */
    @Override
    public void close() {
        // Shut down the executor service used by the dispatcher
        httpClient.dispatcher().executorService().shutdown();

        // Evict all connections from the pool
        httpClient.connectionPool().evictAll();
    }
}
