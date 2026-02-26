import com.api.v1.PingResponse;
import com.jamm.JammClient;

public final class HealthcheckPingExample {
    private HealthcheckPingExample() {
    }

    public static void main(String[] args) throws Exception {
        try (JammClient client = ExampleHelper.createClientFromEnv()) {
            PingResponse response = client.healthcheck().ping();
            ExampleHelper.printProto(response);
        }
    }
}
