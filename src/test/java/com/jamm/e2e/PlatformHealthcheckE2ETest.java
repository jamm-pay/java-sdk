package com.jamm.e2e;

import com.api.v1.PingResponse;
import com.jamm.JammClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlatformHealthcheckE2ETest {

    @Test
    void ping_with_merchant() {
        String merchant = E2ETestHelper.requireEnv("PLATFORM_MERCHANT");

        try (JammClient client = E2ETestHelper.createPlatformClient()) {
            PingResponse response = client.healthcheck().ping(merchant);
            assertNotNull(response);
            assertTrue(response.getOk());
        }
    }

    @Test
    void ping_without_merchant_in_platform_mode() {
        try (JammClient client = E2ETestHelper.createPlatformClient()) {
            PingResponse response = client.healthcheck().ping();
            assertNotNull(response);
            assertTrue(response.getOk());
        }
    }
}
