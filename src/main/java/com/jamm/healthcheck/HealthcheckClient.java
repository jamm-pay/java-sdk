package com.jamm.healthcheck;

import com.api.v1.PingResponse;
import com.jamm.JammClient;

/**
 * Client for healthcheck operations.
 *
 * <p>In platform mode, {@link #ping(String)} accepts a {@code merchant} parameter
 * to verify connectivity for a specific merchant's scope.
 */
public final class HealthcheckClient {

    private final HealthcheckService service;

    public HealthcheckClient(JammClient client) {
        this.service = new HealthcheckService(client);
    }

    /**
     * Ping the Jamm server to check connection.
     *
     * @return PingResponse containing the ok status
     */
    public PingResponse ping() {
        return service.ping();
    }

    /**
     * Ping the Jamm server on behalf of a merchant (platform mode).
     * Verifies connectivity for the specified merchant's scope.
     *
     * @param merchant the merchant ID (format: "mer-*")
     * @return PingResponse containing the ok status
     */
    public PingResponse ping(String merchant) {
        return service.ping(merchant);
    }
}
