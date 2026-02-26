package com.jamm.e2e;

import com.api.v1.PingResponse;
import com.jamm.JammClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HealthcheckE2ETest {

    @Test
    void ping_returnsOk() {
        try (JammClient client = E2ETestHelper.createClient()) {
            PingResponse response = client.healthcheck().ping();

            assertNotNull(response);
            assertTrue(response.getOk());
        }
    }
}
