package com.jamm.healthcheck;

import com.api.v1.PingResponse;
import com.jamm.JammClient;
import com.jamm.http.JammHttpClient;

class HealthcheckService {

    private static final String HEALTHCHECK_PATH = "/v1/healthcheck";

    private final JammHttpClient http;

    HealthcheckService(JammClient client) {
        this.http = client.getHttpClient();
    }

    PingResponse ping() {
        return http.get(HEALTHCHECK_PATH, PingResponse.class);
    }
}
