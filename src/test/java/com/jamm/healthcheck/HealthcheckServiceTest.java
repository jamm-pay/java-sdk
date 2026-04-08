package com.jamm.healthcheck;

import com.api.v1.PingResponse;
import com.jamm.JammClient;
import com.jamm.http.JammHttpClient;
import com.jamm.http.RequestOptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

    @Test
    void ping_withMerchant_callsWithRequestOptions() {
        JammHttpClient http = mock(JammHttpClient.class);
        JammClient client = mock(JammClient.class);
        when(client.getHttpClient()).thenReturn(http);

        PingResponse resp = PingResponse.newBuilder()
            .setOk(true)
            .build();

        when(http.get(eq("/v1/healthcheck"), eq(PingResponse.class), any(RequestOptions.class)))
            .thenReturn(resp);

        HealthcheckService service = new HealthcheckService(client);
        PingResponse result = service.ping("mer-test123");

        assertTrue(result.getOk());
        verify(http).get(eq("/v1/healthcheck"), eq(PingResponse.class), any(RequestOptions.class));
    }
}
