import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import com.jamm.JammClient;
import com.jamm.config.Environment;

final class ExampleHelper {
    private ExampleHelper() {
    }

    static JammClient createClientFromEnv() {
        String env = requiredEnv("ENV");
        String clientId = requiredEnv("MERCHANT_CLIENT_ID");
        String clientSecret = requiredEnv("MERCHANT_CLIENT_SECRET");

        return JammClient.builder()
            .environment(Environment.fromString(env))
            .clientId(clientId)
            .clientSecret(clientSecret)
            .build();
    }

    static String requiredEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Missing required env var: " + key);
        }
        return value;
    }

    static String envOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }

    static void printProto(MessageOrBuilder message) throws Exception {
        String json = JsonFormat.printer().print(message);
        System.out.println(json);
    }
}
