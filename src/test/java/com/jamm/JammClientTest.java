package com.jamm;

import com.jamm.config.Environment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JammClientTest {

    @Test
    void testBuilderWithRequiredFields() {
        JammClient client = JammClient.builder()
                .clientId("test-client-id")
                .clientSecret("test-client-secret")
                .environment(Environment.PRODUCTION)
                .build();

        assertEquals("test-client-id", client.getClientId());
        assertEquals(Environment.PRODUCTION, client.getEnvironment());
    }

    @Test
    void testBuilderWithAllFields() {
        JammClient client = JammClient.builder()
                .clientId("test-client-id")
                .clientSecret("test-client-secret")
                .environment(Environment.STAGING)
                .connectTimeout(15000)
                .readTimeout(60000)
                .maxRetries(3)
                .retryInitialDelay(500)
                .retryMaxDelay(10000)
                .build();

        assertEquals("test-client-id", client.getClientId());
        assertEquals(Environment.STAGING, client.getEnvironment());
        assertEquals(15000, client.getConnectTimeoutMs());
        assertEquals(60000, client.getReadTimeoutMs());
        assertEquals(3, client.getMaxRetries());
    }

    @Test
    void testBuilderWithEnvironmentString() {
        JammClient client = JammClient.builder()
                .clientId("test-client-id")
                .clientSecret("test-client-secret")
                .environment("production")
                .build();

        assertEquals(Environment.PRODUCTION.getApiBaseUrl(), client.getEnvironment().getApiBaseUrl());
    }

    @Test
    void testBuilderWithStagingEnvironmentString() {
        JammClient client = JammClient.builder()
                .clientId("test-client-id")
                .clientSecret("test-client-secret")
                .environment("staging")
                .build();

        assertEquals(Environment.STAGING.getApiBaseUrl(), client.getEnvironment().getApiBaseUrl());
    }

    @Test
    void testBuilderDefaultValues() {
        JammClient client = JammClient.builder()
                .clientId("test-client-id")
                .clientSecret("test-client-secret")
                .build();

        // Default environment is PRODUCTION
        assertEquals(Environment.PRODUCTION, client.getEnvironment());
        // Default timeouts
        assertEquals(30000, client.getConnectTimeoutMs());
        assertEquals(90000, client.getReadTimeoutMs());
        // Default retries
        assertEquals(0, client.getMaxRetries());
    }

    @Test
    void testBuilderMissingClientId() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                JammClient.builder()
                        .clientSecret("test-secret")
                        .build());

        assertTrue(ex.getMessage().contains("clientId"));
    }

    @Test
    void testBuilderEmptyClientId() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                JammClient.builder()
                        .clientId("")
                        .clientSecret("test-secret")
                        .build());

        assertTrue(ex.getMessage().contains("clientId"));
    }

    @Test
    void testBuilderMissingClientSecret() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                JammClient.builder()
                        .clientId("test-id")
                        .build());

        assertTrue(ex.getMessage().contains("clientSecret"));
    }

    @Test
    void testBuilderEmptyClientSecret() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                JammClient.builder()
                        .clientId("test-id")
                        .clientSecret("")
                        .build());

        assertTrue(ex.getMessage().contains("clientSecret"));
    }

    @Test
    void testPlatformModeDefault() {
        JammClient client = JammClient.builder()
                .clientId("test-id")
                .clientSecret("test-secret")
                .build();

        assertFalse(client.isPlatformMode());
    }

    @Test
    void testPlatformModeEnabled() {
        JammClient client = JammClient.builder()
                .clientId("test-id")
                .clientSecret("test-secret")
                .environment(Environment.DEVELOP)
                .platform(true)
                .build();

        assertTrue(client.isPlatformMode());
    }

    @Test
    void testPlatformModeDisabledExplicitly() {
        JammClient client = JammClient.builder()
                .clientId("test-id")
                .clientSecret("test-secret")
                .platform(false)
                .build();

        assertFalse(client.isPlatformMode());
    }

    @Test
    void testGetOAuthProvider() {
        JammClient client = JammClient.builder()
                .clientId("test-client-id")
                .clientSecret("test-client-secret")
                .environment(Environment.PRODUCTION)
                .build();

        assertNotNull(client.getOAuthProvider());
    }

    @Test
    void testGetHttpClient() {
        JammClient client = JammClient.builder()
                .clientId("test-client-id")
                .clientSecret("test-client-secret")
                .environment(Environment.PRODUCTION)
                .build();

        assertNotNull(client.getHttpClient());
    }
}
