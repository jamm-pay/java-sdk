package com.jamm.errors;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.Map;

/**
 * Exception thrown when an API request fails.
 * Contains structured error information from the API response.
 */
public class ApiException extends JammException {

    private static final Gson GSON = new Gson();
    private static final java.lang.reflect.Type MAP_TYPE =
            new TypeToken<Map<String, Object>>() {}.getType();

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
                JsonObject root = JsonParser.parseString(httpBody).getAsJsonObject();

                // Extract error code
                if (root.has("code")) {
                    JsonElement codeNode = root.get("code");
                    if (codeNode.isJsonPrimitive() && codeNode.getAsJsonPrimitive().isNumber()) {
                        errorCode = ErrorCode.fromCode(codeNode.getAsInt());
                    } else if (codeNode.isJsonPrimitive() && codeNode.getAsJsonPrimitive().isString()) {
                        errorCode = ErrorCode.fromName(codeNode.getAsString());
                    }
                }

                // Extract message
                if (root.has("message") && !root.get("message").isJsonNull()) {
                    message = root.get("message").getAsString();
                }

                // Extract error type from details
                if (root.has("details") && root.get("details").isJsonArray()) {
                    for (JsonElement detail : root.getAsJsonArray("details")) {
                        if (detail.isJsonObject()) {
                            JsonElement debug = detail.getAsJsonObject().get("debug");
                            if (debug != null && debug.isJsonPrimitive()) {
                                errorType = debug.getAsString();
                                break;
                            }
                        }
                    }
                }

                // Parse full body
                bodyMap = GSON.fromJson(root, MAP_TYPE);
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

        if (requestId != null && !requestId.trim().isEmpty()) {
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
