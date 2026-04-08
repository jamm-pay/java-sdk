package com.jamm.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RequestOptionsTest {

    @Test
    void withMerchant_storesMerchantId() {
        RequestOptions options = RequestOptions.withMerchant("mer-abc123");
        assertEquals("mer-abc123", options.getMerchant());
    }

    @Test
    void none_returnsNullMerchant() {
        RequestOptions options = RequestOptions.none();
        assertNull(options.getMerchant());
    }

    @Test
    void withMerchant_nullThrowsIllegalArgument() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> RequestOptions.withMerchant(null));
        assertTrue(ex.getMessage().contains("merchant must not be null"));
    }
}
