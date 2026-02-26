package com.jamm.errors;

import java.util.Map;

/**
 * Exception thrown when OAuth authentication fails.
 * This can occur during token fetching or refresh operations.
 */
public class OAuthException extends JammException {

    /**
     * Creates a new OAuthException with a message.
     *
     * @param message the error message
     */
    public OAuthException(String message) {
        super(message);
    }

    /**
     * Creates a new OAuthException with a message and cause.
     *
     * @param message the error message
     * @param cause   the underlying cause
     */
    public OAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new OAuthException with HTTP response details.
     *
     * @param message    the error message
     * @param httpStatus the HTTP status code
     * @param httpBody   the HTTP response body
     */
    public OAuthException(String message, Integer httpStatus, String httpBody) {
        super(message, httpStatus, null, httpBody);
    }

    /**
     * Creates a new OAuthException with full HTTP response details.
     *
     * @param message     the error message
     * @param httpStatus  the HTTP status code
     * @param httpHeaders the HTTP response headers
     * @param httpBody    the HTTP response body
     */
    public OAuthException(String message, Integer httpStatus, Map<String, String> httpHeaders, String httpBody) {
        super(message, null, httpStatus, httpHeaders, httpBody);
    }
}
