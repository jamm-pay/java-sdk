package com.jamm.errors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Map;

/**
 * Exception thrown when an API request fails.
 * Contains structured error information from the API response.
 */
public class ApiException extends JammException {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ErrorCode errorCode;
    private final String errorType;
    private final Map<String, Object> body;
    private final String requestMethod;
    private final String requestPath;
    private final String requestId;

    /**
     * Creates a new ApiException with all details.
     *
     * @param message     the error message
     * @param errorCode   the ConnectRPC error code
     * @param errorType   the Jamm error type
     * @param httpStatus  the HTTP status code
     * @param httpHeaders the HTTP response headers
     * @param httpBody    the HTTP response body
     * @param body        the parsed response body as a map
     */
    public ApiException(String message, ErrorCode errorCode, String errorType,
                        Integer httpStatus, Map<String, String> httpHeaders,
                        String httpBody, Map<String, Object> body) {
        this(message, errorCode, errorType, httpStatus, httpHeaders, httpBody, body, null, null, extractRequestId(httpHeaders));
    }

    /**
     * Creates a new ApiException with request context.
     *
     * @param message       the error message
     * @param errorCode     the ConnectRPC error code
     * @param errorType     the Jamm error type
     * @param httpStatus    the HTTP status code
     * @param httpHeaders   the HTTP response headers
     * @param httpBody      the HTTP response body
     * @param body          the parsed response body as a map
     * @param requestMethod the HTTP request method
     * @param requestPath   the request path
     * @param requestId     the upstream request identifier, if present
     */
    public ApiException(String message, ErrorCode errorCode, String errorType,
                        Integer httpStatus, Map<String, String> httpHeaders,
                        String httpBody, Map<String, Object> body,
                        String requestMethod, String requestPath, String requestId) {
        super(message, httpStatus, httpHeaders, httpBody);
        this.errorCode = errorCode;
        this.errorType = errorType;
        this.body = body;
        this.requestMethod = requestMethod;
        this.requestPath = requestPath;
        this.requestId = requestId;
    }

    /**
     * Creates an ApiException from an HTTP error response.
     *
     * @param httpStatus  the HTTP status code
     * @param httpHeaders the HTTP response headers
     * @param httpBody    the HTTP response body
     * @return a new ApiException
     */
    public static ApiException fromResponse(int httpStatus, Map<String, String> httpHeaders, String httpBody) {
        return fromResponse(httpStatus, httpHeaders, httpBody, null, null);
    }

    /**
     * Creates an ApiException from an HTTP error response with request context.
     *
     * @param httpStatus    the HTTP status code
     * @param httpHeaders   the HTTP response headers
     * @param httpBody      the HTTP response body
     * @param requestMethod the HTTP request method
     * @param requestPath   the request path
     * @return a new ApiException
     */
    public static ApiException fromResponse(int httpStatus, Map<String, String> httpHeaders, String httpBody,
                                            String requestMethod, String requestPath) {
        ErrorCode errorCode = ErrorCode.UNKNOWN;
        String errorType = "UNSPECIFIED";
        String message = "An API error occurred";
        Map<String, Object> bodyMap = null;

        if (httpBody != null && !httpBody.isEmpty()) {
            try {
                JsonNode root = OBJECT_MAPPER.readTree(httpBody);

                // Extract error code
                if (root.has("code")) {
                    JsonNode codeNode = root.get("code");
                    if (codeNode.isInt()) {
                        errorCode = ErrorCode.fromCode(codeNode.asInt());
                    } else if (codeNode.isTextual()) {
                        errorCode = ErrorCode.fromName(codeNode.asText());
                    }
                }

                // Extract message
                if (root.has("message")) {
                    message = root.get("message").asText();
                }

                // Extract error type from details
                if (root.has("details") && root.get("details").isArray()) {
                    for (JsonNode detail : root.get("details")) {
                        if (detail.has("debug")) {
                            errorType = detail.get("debug").asText();
                            break;
                        }
                    }
                }

                // Parse full body
                bodyMap = OBJECT_MAPPER.convertValue(root,
                        new TypeReference<Map<String, Object>>() {});
            } catch (Exception e) {
                // If parsing fails, use defaults
                message = httpBody;
            }
        }

        return new ApiException(
                message,
                errorCode,
                errorType,
                httpStatus,
                httpHeaders,
                httpBody,
                bodyMap,
                requestMethod,
                requestPath,
                extractRequestId(httpHeaders)
        );
    }

    /**
     * Gets the ConnectRPC error code.
     *
     * @return the error code
     */
    public ErrorCode getErrorCode() {
        return errorCode;
    }

    /**
     * Gets the Jamm error type.
     *
     * @return the error type
     */
    public String getErrorType() {
        return errorType;
    }

    /**
     * Gets the parsed response body.
     *
     * @return an unmodifiable view of the body as a map, or null if not available
     */
    public Map<String, Object> getBody() {
        return body != null ? Collections.unmodifiableMap(body) : null;
    }

    /**
     * Gets the numeric error code value.
     *
     * @return the error code number
     */
    public int getCode() {
        return errorCode != null ? errorCode.getCode() : 0;
    }

    /**
     * Gets the human-readable error name.
     *
     * @return the error name
     */
    public String getErrorName() {
        return errorCode != null ? errorCode.getName() : "UNKNOWN";
    }

    /**
     * Gets the HTTP request method for the failed request.
     *
     * @return the HTTP method, or null if not available
     */
    public String getRequestMethod() {
        return requestMethod;
    }

    /**
     * Gets the request path for the failed request.
     *
     * @return the request path, or null if not available
     */
    public String getRequestPath() {
        return requestPath;
    }

    /**
     * Gets the upstream request ID associated with the failed request.
     *
     * @return the request ID, or null if not available
     */
    public String getRequestId() {
        return requestId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean hasContext = false;

        if (errorCode != null) {
            sb.append("(").append(errorCode.getName());
            hasContext = true;
        }

        if (getHttpStatus() != null) {
            if (!hasContext) {
                sb.append("(");
                hasContext = true;
            } else {
                sb.append(", ");
            }
            sb.append("HTTP ").append(getHttpStatus());
        }

        if (requestMethod != null || requestPath != null) {
            if (!hasContext) {
                sb.append("(");
                hasContext = true;
            } else {
                sb.append(", ");
            }

            if (requestMethod != null) {
                sb.append(requestMethod);
            }
            if (requestMethod != null && requestPath != null) {
                sb.append(" ");
            }
            if (requestPath != null) {
                sb.append(requestPath);
            }
        }

        if (hasContext) {
            sb.append(") ");
        }

        sb.append(getMessage());

        if (requestId != null && !requestId.isBlank()) {
            sb.append(" [request_id=").append(requestId).append("]");
        }

        return sb.toString();
    }

    private static String extractRequestId(Map<String, String> httpHeaders) {
        if (httpHeaders == null || httpHeaders.isEmpty()) {
            return null;
        }

        for (Map.Entry<String, String> entry : httpHeaders.entrySet()) {
            if ("x-request-id".equalsIgnoreCase(entry.getKey()) || "request-id".equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }

        return null;
    }
}
