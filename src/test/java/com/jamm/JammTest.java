package com.jamm;

import com.jamm.config.Environment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JammTest {

    @BeforeEach
    @AfterEach
    void resetJamm() {
        Jamm.reset();
    }

    @Test
    void testVersion() {
        assertNotNull(Jamm.VERSION);
        assertNotEquals("unknown", Jamm.VERSION, "VERSION should be loaded from jamm-sdk.properties");
        assertTrue(Jamm.VERSION.matches("\\d+\\.\\d+\\.\\d+.*"),
                "VERSION should follow semver pattern, got: " + Jamm.VERSION);
    }

    @Test
    void testConfigureWithEnvironment() {
        Jamm.configure("test-id", "test-secret", Environment.PRODUCTION);

        assertTrue(Jamm.isConfigured());
        JammClient client = Jamm.getClient();
        assertNotNull(client);
        assertEquals("test-id", client.getClientId());
        assertEquals(Environment.PRODUCTION, client.getEnvironment());
    }

    @Test
    void testConfigureWithEnvironmentString() {
        Jamm.configure("test-id", "test-secret", "staging");

        assertTrue(Jamm.isConfigured());
        JammClient client = Jamm.getClient();
        assertEquals(Environment.STAGING.getApiBaseUrl(), client.getEnvironment().getApiBaseUrl());
    }

    @Test
    void testGetClientNotConfigured() {
        assertFalse(Jamm.isConfigured());

        IllegalStateException ex = assertThrows(IllegalStateException.class, Jamm::getClient);

        assertTrue(ex.getMessage().contains("not been configured"));
    }

    @Test
    void testSetClient() {
        JammClient customClient = JammClient.builder()
                .clientId("custom-id")
                .clientSecret("custom-secret")
                .environment(Environment.LOCAL)
                .build();

        Jamm.setClient(customClient);

        assertTrue(Jamm.isConfigured());
        assertEquals(customClient, Jamm.getClient());
        assertEquals("custom-id", Jamm.getClient().getClientId());
    }

    @Test
    void testReset() {
        Jamm.configure("test-id", "test-secret", Environment.PRODUCTION);
        assertTrue(Jamm.isConfigured());

        Jamm.reset();

        assertFalse(Jamm.isConfigured());
    }

    @Test
    void testConfigureOverwritesPrevious() {
        Jamm.configure("first-id", "first-secret", Environment.PRODUCTION);
        assertEquals("first-id", Jamm.getClient().getClientId());

        Jamm.configure("second-id", "second-secret", Environment.STAGING);
        assertEquals("second-id", Jamm.getClient().getClientId());
        assertEquals(Environment.STAGING.getApiBaseUrl(), Jamm.getClient().getEnvironment().getApiBaseUrl());
    }

    @Test
    void testIsConfiguredInitiallyFalse() {
        assertFalse(Jamm.isConfigured());
    }
}
