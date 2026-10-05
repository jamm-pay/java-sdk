package com.jamm;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;

/**
 * Resolves the API version a client sends. Kept apart from {@link ApiVersion}, which is generated.
 */
public final class ApiVersionResolver {

    // The regex pins the shape (uuuu alone accepts signed/5+ digit years); STRICT rejects 2026-02-30.
    private static final Pattern API_VERSION_PATTERN = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");
    private static final DateTimeFormatter API_VERSION_FORMAT =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private ApiVersionResolver() {}

    /**
     * Resolves a requested API version: empty means {@link ApiVersion#VALUE}; malformed or newer is rejected.
     *
     * @param requested the requested version, or null
     * @return the version to send
     * @throws IllegalArgumentException if malformed or newer than {@link ApiVersion#VALUE}
     */
    public static String resolve(String requested) {
        if (requested == null || requested.isEmpty()) {
            return ApiVersion.VALUE;
        }
        if (!API_VERSION_PATTERN.matcher(requested).matches()) {
            throw invalid(requested);
        }
        try {
            LocalDate.parse(requested, API_VERSION_FORMAT);
        } catch (DateTimeParseException e) {
            throw invalid(requested);
        }
        if (requested.compareTo(ApiVersion.VALUE) > 0) {
            throw new IllegalArgumentException("apiVersion " + requested
                    + " is newer than this SDK supports (" + ApiVersion.VALUE + "); upgrade the SDK to use it");
        }
        return requested;
    }

    private static IllegalArgumentException invalid(String requested) {
        return new IllegalArgumentException("invalid apiVersion \"" + requested + "\": expected YYYY-MM-DD");
    }
}
