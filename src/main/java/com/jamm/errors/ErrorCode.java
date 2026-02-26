package com.jamm.errors;

/**
 * ConnectRPC error codes.
 * Maps numeric error codes to human-readable names.
 *
 * @see <a href="https://github.com/connectrpc/connect-go/blob/main/code.go">ConnectRPC Error Codes</a>
 */
public enum ErrorCode {
    OK(0, "OK"),
    CANCELED(1, "CANCELED"),
    UNKNOWN(2, "UNKNOWN"),
    INVALID_ARGUMENT(3, "INVALID_ARGUMENT"),
    DEADLINE_EXCEEDED(4, "DEADLINE_EXCEEDED"),
    NOT_FOUND(5, "NOT_FOUND"),
    ALREADY_EXISTS(6, "ALREADY_EXISTS"),
    PERMISSION_DENIED(7, "PERMISSION_DENIED"),
    RESOURCE_EXHAUSTED(8, "RESOURCE_EXHAUSTED"),
    FAILED_PRECONDITION(9, "FAILED_PRECONDITION"),
    ABORTED(10, "ABORTED"),
    OUT_OF_RANGE(11, "OUT_OF_RANGE"),
    UNIMPLEMENTED(12, "UNIMPLEMENTED"),
    INTERNAL(13, "INTERNAL"),
    UNAVAILABLE(14, "UNAVAILABLE"),
    DATA_LOSS(15, "DATA_LOSS"),
    UNAUTHENTICATED(16, "UNAUTHENTICATED");

    private final int code;
    private final String name;

    ErrorCode(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Gets the numeric error code.
     *
     * @return the error code number
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets the human-readable error name.
     *
     * @return the error name
     */
    public String getName() {
        return name;
    }

    /**
     * Converts a numeric code to an ErrorCode enum value.
     *
     * @param code the numeric error code
     * @return the corresponding ErrorCode, or UNKNOWN if not found
     */
    public static ErrorCode fromCode(int code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.code == code) {
                return errorCode;
            }
        }
        return UNKNOWN;
    }

    /**
     * Converts a string name to an ErrorCode enum value.
     *
     * @param name the error name
     * @return the corresponding ErrorCode, or UNKNOWN if not found
     */
    public static ErrorCode fromName(String name) {
        if (name == null) {
            return UNKNOWN;
        }
        for (ErrorCode errorCode : values()) {
            if (errorCode.name.equalsIgnoreCase(name)) {
                return errorCode;
            }
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return name;
    }
}
