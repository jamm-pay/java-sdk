package com.jamm.errors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ErrorCodeTest {

    @ParameterizedTest
    @CsvSource({
            "0, OK",
            "1, CANCELED",
            "2, UNKNOWN",
            "3, INVALID_ARGUMENT",
            "4, DEADLINE_EXCEEDED",
            "5, NOT_FOUND",
            "6, ALREADY_EXISTS",
            "7, PERMISSION_DENIED",
            "8, RESOURCE_EXHAUSTED",
            "9, FAILED_PRECONDITION",
            "10, ABORTED",
            "11, OUT_OF_RANGE",
            "12, UNIMPLEMENTED",
            "13, INTERNAL",
            "14, UNAVAILABLE",
            "15, DATA_LOSS",
            "16, UNAUTHENTICATED"
    })
    void testFromCode(int code, String expectedName) {
        ErrorCode errorCode = ErrorCode.fromCode(code);
        assertEquals(code, errorCode.getCode());
        assertEquals(expectedName, errorCode.getName());
    }

    @Test
    void testFromCodeUnknown() {
        ErrorCode errorCode = ErrorCode.fromCode(999);
        assertEquals(ErrorCode.UNKNOWN, errorCode);
    }

    @Test
    void testFromCodeNegative() {
        ErrorCode errorCode = ErrorCode.fromCode(-1);
        assertEquals(ErrorCode.UNKNOWN, errorCode);
    }

    @ParameterizedTest
    @CsvSource({
            "OK, 0",
            "CANCELED, 1",
            "UNKNOWN, 2",
            "INVALID_ARGUMENT, 3",
            "NOT_FOUND, 5",
            "PERMISSION_DENIED, 7",
            "INTERNAL, 13",
            "UNAUTHENTICATED, 16"
    })
    void testFromName(String name, int expectedCode) {
        ErrorCode errorCode = ErrorCode.fromName(name);
        assertEquals(expectedCode, errorCode.getCode());
    }

    @Test
    void testFromNameCaseInsensitive() {
        assertEquals(ErrorCode.NOT_FOUND, ErrorCode.fromName("not_found"));
        assertEquals(ErrorCode.NOT_FOUND, ErrorCode.fromName("Not_Found"));
        assertEquals(ErrorCode.NOT_FOUND, ErrorCode.fromName("NOT_FOUND"));
    }

    @Test
    void testFromNameNull() {
        assertEquals(ErrorCode.UNKNOWN, ErrorCode.fromName(null));
    }

    @Test
    void testFromNameInvalid() {
        assertEquals(ErrorCode.UNKNOWN, ErrorCode.fromName("INVALID_ERROR_CODE"));
    }

    @Test
    void testToString() {
        assertEquals("NOT_FOUND", ErrorCode.NOT_FOUND.toString());
        assertEquals("INTERNAL", ErrorCode.INTERNAL.toString());
    }

    @Test
    void testAllErrorCodesHaveUniqueValues() {
        ErrorCode[] codes = ErrorCode.values();
        for (int i = 0; i < codes.length; i++) {
            for (int j = i + 1; j < codes.length; j++) {
                assertNotEquals(codes[i].getCode(), codes[j].getCode(),
                        "Duplicate code found: " + codes[i] + " and " + codes[j]);
            }
        }
    }
}
