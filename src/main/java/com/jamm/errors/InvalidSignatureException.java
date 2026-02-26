package com.jamm.errors;

/**
 * Exception thrown when webhook signature verification fails.
 * This indicates that the webhook payload may have been tampered with
 * or was not signed with the expected secret.
 */
public class InvalidSignatureException extends RuntimeException {

    /**
     * Creates a new InvalidSignatureException with a default message.
     */
    public InvalidSignatureException() {
        super("Invalid webhook signature");
    }

    /**
     * Creates a new InvalidSignatureException with a custom message.
     *
     * @param message the error message
     */
    public InvalidSignatureException(String message) {
        super(message);
    }

    /**
     * Creates a new InvalidSignatureException with a message and cause.
     *
     * @param message the error message
     * @param cause   the underlying cause
     */
    public InvalidSignatureException(String message, Throwable cause) {
        super(message, cause);
    }
}
