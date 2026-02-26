package com.jamm.healthcheck;

import com.api.v1.PingResponse;
import com.jamm.JammClient;
import com.jamm.http.JammHttpClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HealthcheckServiceTest {

    @Test
    void ping_callsCorrectEndpoint() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        PingResponse resp = PingResponse.newBuilder()
            .setOk(true)
            .build();

        when(http.get("/v1/healthcheck", PingResponse.class))
            .thenReturn(resp);

        HealthcheckService service = new HealthcheckService(client);
        PingResponse result = service.ping();

        assertTrue(result.getOk());
    }
}
