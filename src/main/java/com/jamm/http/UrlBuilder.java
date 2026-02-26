package com.jamm.http;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class for building URLs with proper encoding of path and query parameters.
 * Uses a builder pattern for fluent API.
 *
 * <p>Example usage:
 * <pre>{@code
 * String url = UrlBuilder.path("/v1/charges")
 *     .pathParam("cus-123")
 *     .queryParam("pageToken", "next")
 *     .queryParam("pageSize", 20)
 *     .build();
 * // Result: /v1/charges/cus-123?pageToken=next&pageSize=20
 * }</pre>
 */
public final class UrlBuilder {

    private final StringBuilder pathBuilder;
    private final Map<String, String> queryParams;

    private UrlBuilder(String basePath) {
        this.pathBuilder = new StringBuilder(basePath);
        this.queryParams = new LinkedHashMap<>(); // Preserve insertion order
    }

    /**
     * Creates a new UrlBuilder with the given base path.
     *
     * @param basePath the base path (e.g., "/v1/charges")
     * @return a new UrlBuilder instance
     */
    public static UrlBuilder path(String basePath) {
        return new UrlBuilder(basePath);
    }

    /**
     * Appends a path segment with proper URL encoding.
     * Uses RFC 3986 compliant encoding where spaces are encoded as '%20'.
     *
     * @param segment the path segment to append (will be URL-encoded)
     * @return this builder for chaining
     */
    public UrlBuilder pathParam(String segment) {
        if (segment != null && !segment.isEmpty()) {
            if (pathBuilder.length() > 0 && pathBuilder.charAt(pathBuilder.length() - 1) != '/') {
                pathBuilder.append('/');
            }
            pathBuilder.append(encodePathSegment(segment));
        }
        return this;
    }

    /**
     * Adds a query parameter. Null or empty values are ignored.
     *
     * @param name the parameter name
     * @param value the parameter value (will be URL-encoded)
     * @return this builder for chaining
     */
    public UrlBuilder queryParam(String name, String value) {
        if (name != null && !name.isEmpty() && value != null && !value.isEmpty()) {
            queryParams.put(name, value);
        }
        return this;
    }

    /**
     * Adds a query parameter with an integer value. Values <= 0 are ignored.
     *
     * @param name the parameter name
     * @param value the parameter value
     * @return this builder for chaining
     */
    public UrlBuilder queryParam(String name, int value) {
        if (name != null && !name.isEmpty() && value > 0) {
            queryParams.put(name, String.valueOf(value));
        }
        return this;
    }

    /**
     * Builds the final URL string.
     *
     * @return the complete URL with encoded path and query parameters
     */
    public String build() {
        StringBuilder result = new StringBuilder(pathBuilder);

        if (!queryParams.isEmpty()) {
            result.append('?');
            boolean first = true;
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                if (!first) {
                    result.append('&');
                }
                result.append(encodeQueryParam(entry.getKey()));
                result.append('=');
                result.append(encodeQueryParam(entry.getValue()));
                first = false;
            }
        }

        return result.toString();
    }

    /**
     * URL-encodes a path segment using RFC 3986 compliant encoding.
     * Spaces are encoded as '%20' (not '+' as in form encoding).
     *
     * @param value the string to encode
     * @return the URL-encoded string suitable for path segments
     */
    private static String encodePathSegment(String value) {
        // URLEncoder uses application/x-www-form-urlencoded which encodes spaces as '+'.
        // For path segments per RFC 3986, spaces must be '%20'.
        return URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
    }

    /**
     * URL-encodes a query parameter using application/x-www-form-urlencoded encoding.
     * Spaces are encoded as '+' which is standard for query parameters.
     *
     * @param value the string to encode
     * @return the URL-encoded string suitable for query parameters
     */
    private static String encodeQueryParam(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
