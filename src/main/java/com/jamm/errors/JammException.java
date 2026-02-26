package com.jamm.errors;

import java.util.Collections;
import java.util.Map;

/**
 * Base exception for all Jamm SDK errors.
 * Contains HTTP response details when available.
 */
public class JammException extends RuntimeException {

    private final Integer httpStatus;
    private final Map<String, String> httpHeaders;
    private final String httpBody;

    /**
     * Creates a new JammException with a message.
     *
     * @param message the error message
     */
    public JammException(String message) {
        this(message, null, null, null, null);
    }

    /**
     * Creates a new JammException with a message and cause.
     *
     * @param message the error message
     * @param cause   the underlying cause
     */
    public JammException(String message, Throwable cause) {
        this(message, cause, null, null, null);
    }

    /**
     * Creates a new JammException with full HTTP response details.
     *
     * @param message     the error message
     * @param httpStatus  the HTTP status code
     * @param httpHeaders the HTTP response headers
     * @param httpBody    the HTTP response body
     */
    public JammException(String message, Integer httpStatus, Map<String, String> httpHeaders, String httpBody) {
        this(message, null, httpStatus, httpHeaders, httpBody);
    }

    /**
     * Creates a new JammException with all details.
     *
     * @param message     the error message
     * @param cause       the underlying cause
     * @param httpStatus  the HTTP status code
     * @param httpHeaders the HTTP response headers
     * @param httpBody    the HTTP response body
     */
    public JammException(String message, Throwable cause, Integer httpStatus,
                         Map<String, String> httpHeaders, String httpBody) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.httpHeaders = httpHeaders;
        this.httpBody = httpBody;
    }

    /**
     * Gets the HTTP status code, if available.
     *
     * @return the HTTP status code, or null if not available
     */
    public Integer getHttpStatus() {
        return httpStatus;
    }

    /**
     * Gets the HTTP response headers, if available.
     *
     * @return an unmodifiable view of the HTTP headers, or null if not available
     */
    public Map<String, String> getHttpHeaders() {
        return httpHeaders != null ? Collections.unmodifiableMap(httpHeaders) : null;
    }

    /**
     * Gets the HTTP response body, if available.
     *
     * @return the HTTP body, or null if not available
     */
    public String getHttpBody() {
        return httpBody;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (httpStatus != null) {
            sb.append("(Status ").append(httpStatus).append(") ");
        }
        sb.append(getMessage());
        return sb.toString();
    }
}
