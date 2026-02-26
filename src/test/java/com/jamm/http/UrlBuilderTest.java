package com.jamm.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UrlBuilderTest {

    @Test
    void path_returnsBasePath() {
        String url = UrlBuilder.path("/v1/charges").build();
        assertEquals("/v1/charges", url);
    }

    @Test
    void pathParam_appendsEncodedSegment() {
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("cus-123")
                .build();
        assertEquals("/v1/charges/cus-123", url);
    }

    @Test
    void pathParam_encodesSpecialCharacters() {
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("cus-123&456")
                .build();
        assertEquals("/v1/charges/cus-123%26456", url);
    }

    @Test
    void pathParam_encodesSpaces() {
        // Per RFC 3986, spaces in path segments should be encoded as '%20', not '+'
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("cus 123")
                .build();
        assertEquals("/v1/charges/cus%20123", url);
    }

    @Test
    void pathParam_encodesSlashes() {
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("cus/123")
                .build();
        assertEquals("/v1/charges/cus%2F123", url);
    }

    @Test
    void pathParam_ignoresNullValue() {
        String url = UrlBuilder.path("/v1/charges")
                .pathParam(null)
                .build();
        assertEquals("/v1/charges", url);
    }

    @Test
    void pathParam_ignoresEmptyValue() {
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("")
                .build();
        assertEquals("/v1/charges", url);
    }

    @Test
    void queryParam_appendsParameter() {
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("cus-123")
                .queryParam("pageToken", "abc")
                .build();
        assertEquals("/v1/charges/cus-123?pageToken=abc", url);
    }

    @Test
    void queryParam_appendsMultipleParameters() {
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("cus-123")
                .queryParam("pageToken", "abc")
                .queryParam("pageSize", 20)
                .build();
        assertEquals("/v1/charges/cus-123?pageToken=abc&pageSize=20", url);
    }

    @Test
    void queryParam_encodesSpecialCharactersInValue() {
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("cus-123")
                .queryParam("pageToken", "abc=def&ghi")
                .build();
        assertEquals("/v1/charges/cus-123?pageToken=abc%3Ddef%26ghi", url);
    }

    @Test
    void queryParam_encodesSpacesAsPlus() {
        // Query parameters use application/x-www-form-urlencoded where spaces become '+'
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("cus-123")
                .queryParam("filter", "status active")
                .build();
        assertEquals("/v1/charges/cus-123?filter=status+active", url);
    }

    @Test
    void queryParam_encodesSpecialCharactersInName() {
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("cus-123")
                .queryParam("page&token", "abc")
                .build();
        assertEquals("/v1/charges/cus-123?page%26token=abc", url);
    }

    @Test
    void queryParam_ignoresNullValue() {
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("cus-123")
                .queryParam("pageToken", (String) null)
                .build();
        assertEquals("/v1/charges/cus-123", url);
    }

    @Test
    void queryParam_ignoresEmptyValue() {
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("cus-123")
                .queryParam("pageToken", "")
                .build();
        assertEquals("/v1/charges/cus-123", url);
    }

    @Test
    void queryParam_ignoresZeroOrNegativeIntValue() {
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("cus-123")
                .queryParam("pageSize", 0)
                .queryParam("limit", -1)
                .build();
        assertEquals("/v1/charges/cus-123", url);
    }

    @Test
    void queryParam_ignoresNullName() {
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("cus-123")
                .queryParam(null, "abc")
                .build();
        assertEquals("/v1/charges/cus-123", url);
    }

    @Test
    void queryParam_ignoresEmptyName() {
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("cus-123")
                .queryParam("", "abc")
                .build();
        assertEquals("/v1/charges/cus-123", url);
    }

    @Test
    void complexUrl_buildsCorrectly() {
        String url = UrlBuilder.path("/v1/charges")
                .pathParam("cus-123")
                .queryParam("pageToken", "next-page")
                .queryParam("pageSize", 50)
                .queryParam("filter", "status=active&type=recurring")
                .build();
        assertEquals("/v1/charges/cus-123?pageToken=next-page&pageSize=50&filter=status%3Dactive%26type%3Drecurring", url);
    }
}
