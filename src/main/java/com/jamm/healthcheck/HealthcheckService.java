package com.jamm.healthcheck;

import com.api.v1.PingResponse;
import com.jamm.JammClient;
import com.jamm.http.JammHttpClient;
import com.jamm.http.RequestOptions;

class HealthcheckService {

    private static final String HEALTHCHECK_PATH = "/v1/healthcheck";

    private final JammHttpClient http;

    HealthcheckService(JammClient client) {
        this.http = client.getHttpClient();
    }

    PingResponse ping() {
        return http.get(HEALTHCHECK_PATH, PingResponse.class);
    }

    PingResponse ping(String merchant) {
        return http.get(HEALTHCHECK_PATH, PingResponse.class,
                RequestOptions.withMerchant(merchant));
    }
}
