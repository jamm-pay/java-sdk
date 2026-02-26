package com.jamm.healthcheck;

import com.api.v1.PingResponse;
import com.jamm.JammClient;

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
}
