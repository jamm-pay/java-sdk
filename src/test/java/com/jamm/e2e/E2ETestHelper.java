package com.jamm.e2e;

import com.jamm.JammClient;
import com.jamm.config.Environment;
import org.junit.jupiter.api.Assumptions;

import java.util.Locale;
import java.util.UUID;

final class E2ETestHelper {

    private E2ETestHelper() {
    }

    static JammClient createClient() {
        String clientId = System.getenv("MERCHANT_CLIENT_ID");
        String clientSecret = System.getenv("MERCHANT_CLIENT_SECRET");

        Assumptions.assumeTrue(
                clientId != null && !clientId.isBlank()
                        && clientSecret != null && !clientSecret.isBlank(),
                "Skipping E2E tests: set MERCHANT_CLIENT_ID and MERCHANT_CLIENT_SECRET");

        String env = envOrDefault("ENV", "local");

        return JammClient.builder()
                .environment(Environment.fromString(env))
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
    }

    static JammClient createPlatformClient() {
        String clientId = System.getenv("PLATFORM_CLIENT_ID");
        String clientSecret = System.getenv("PLATFORM_CLIENT_SECRET");

        Assumptions.assumeTrue(
                clientId != null && !clientId.isBlank()
                        && clientSecret != null && !clientSecret.isBlank(),
                "Skipping platform E2E tests: set PLATFORM_CLIENT_ID and PLATFORM_CLIENT_SECRET");

        String env = envOrDefault("ENV", "local");

        return JammClient.builder()
                .environment(Environment.fromString(env))
                .clientId(clientId)
                .clientSecret(clientSecret)
                .platform(true)
                .build();
    }

    static String requireEnv(String key) {
        String value = System.getenv(key);
        Assumptions.assumeTrue(value != null && !value.isBlank(),
                "Skipping: required env var " + key + " not set");
        return value;
    }

    static String envOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }

    static String randomEmail(String prefix) {
        String normalizedPrefix = prefix.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
        return normalizedPrefix + "+" + UUID.randomUUID() + "@jamm-pay.jp";
    }
}
