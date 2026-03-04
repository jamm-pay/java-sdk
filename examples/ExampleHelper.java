import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import com.jamm.JammClient;
import com.jamm.config.Environment;
import com.jamm.errors.ApiException;
import com.jamm.errors.JammException;
import com.jamm.errors.OAuthException;

final class ExampleHelper {
    @FunctionalInterface
    interface ExampleAction {
        void run(JammClient client) throws Exception;
    }

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

    static void run(ExampleAction action) throws Exception {
        try (JammClient client = createClientFromEnv()) {
            action.run(client);
        } catch (ApiException e) {
            System.err.println("API request failed: " + e);
            String errorType = e.getErrorType();
            if (errorType != null && !errorType.isBlank() && !"UNSPECIFIED".equals(errorType)) {
                System.err.println("Error type: " + errorType);
            }
            if (e.getHttpBody() != null && !e.getHttpBody().isBlank()) {
                System.err.println("Response body: " + e.getHttpBody());
            }
            System.exit(1);
        } catch (OAuthException e) {
            System.err.println("Authentication failed: " + e);
            if (e.getHttpBody() != null && !e.getHttpBody().isBlank()) {
                System.err.println("Response body: " + e.getHttpBody());
            }
            System.exit(1);
        } catch (JammException e) {
            System.err.println("SDK request failed: " + e);
            if (e.getHttpBody() != null && !e.getHttpBody().isBlank()) {
                System.err.println("Response body: " + e.getHttpBody());
            }
            System.exit(1);
        }
    }
}
