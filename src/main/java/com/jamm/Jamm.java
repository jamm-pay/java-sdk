package com.jamm;

import com.jamm.config.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Global configuration and convenience class for the Jamm SDK.
 * Provides static access to a default client instance.
 *
 * <p>For simple use cases, you can configure a global client:
 * <pre>{@code
 * Jamm.configure("your-client-id", "your-client-secret", Environment.PRODUCTION);
 *
 * // Then use the global client
 * JammClient client = Jamm.getClient();
 * }</pre>
 *
 * <p>For more control, create client instances directly using {@link JammClient#builder()}.
 */
public final class Jamm {

    /** SDK version, read from jamm-sdk.properties (injected by Maven resource filtering). */
    public static final String VERSION = loadVersion();

    // The default client is declared volatile to ensure safe publication.
    // All writes occur inside synchronized blocks (configure/setClient/reset),
    // so readers in getClient() will never observe a partially constructed instance.
    private static volatile JammClient defaultClient;
    private static final Object LOCK = new Object();

    private Jamm() {
        // Prevent instantiation
    }

    private static String loadVersion() {
        try (InputStream in = Jamm.class.getClassLoader()
                .getResourceAsStream("jamm-sdk.properties")) {
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                String version = props.getProperty("version");
                if (version != null && !version.isEmpty() && !version.startsWith("${")) {
                    return version;
                }
            }
        } catch (IOException ignored) {
            // fall through to default
        }
        return "unknown";
    }

    /**
     * Configures the global default client.
     *
     * @param clientId     the OAuth client ID
     * @param clientSecret the OAuth client secret
     * @param environment  the API environment
     */
    public static void configure(String clientId, String clientSecret, Environment environment) {
        synchronized (LOCK) {
            defaultClient = JammClient.builder()
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .environment(environment)
                    .build();
        }
    }

    /**
     * Configures the global default client in platform mode.
     * Platform mode enables making API calls on behalf of connected merchants.
     *
     * @param clientId     the OAuth client ID
     * @param clientSecret the OAuth client secret
     * @param environment  the API environment
     * @param platform     true to enable platform mode
     */
    public static void configure(String clientId, String clientSecret,
                                 Environment environment, boolean platform) {
        synchronized (LOCK) {
            defaultClient = JammClient.builder()
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .environment(environment)
                    .platform(platform)
                    .build();
        }
    }

    /**
     * Configures the global default client with environment string.
     *
     * @param clientId     the OAuth client ID
     * @param clientSecret the OAuth client secret
     * @param envName      the environment name (e.g., "production", "staging", "local")
     */
    public static void configure(String clientId, String clientSecret, String envName) {
        configure(clientId, clientSecret, Environment.fromString(envName));
    }

    /**
     * Configures the global default client in platform mode with environment string.
     *
     * @param clientId     the OAuth client ID
     * @param clientSecret the OAuth client secret
     * @param envName      the environment name (e.g., "production", "staging", "local")
     * @param platform     true to enable platform mode
     */
    public static void configure(String clientId, String clientSecret,
                                 String envName, boolean platform) {
        configure(clientId, clientSecret, Environment.fromString(envName), platform);
    }

    /**
     * Gets the global default client.
     *
     * @return the default client
     * @throws IllegalStateException if the client has not been configured
     */
    // Thread-safe: relies on volatile + synchronized publication in configure().
    public static JammClient getClient() {
        JammClient client = defaultClient;
        if (client == null) {
            throw new IllegalStateException(
                    "Jamm client has not been configured. " +
                    "Call Jamm.configure() first or create a JammClient instance directly."
            );
        }
        return client;
    }

    /**
     * Sets a custom client as the global default.
     *
     * @param client the client to use as default
     */
    public static void setClient(JammClient client) {
        synchronized (LOCK) {
            defaultClient = client;
        }
    }

    /**
     * Checks if the global client has been configured.
     *
     * @return true if configured, false otherwise
     */
    public static boolean isConfigured() {
        return defaultClient != null;
    }

    /**
     * Resets the global client configuration.
     * Primarily useful for testing.
     */
    public static void reset() {
        synchronized (LOCK) {
            defaultClient = null;
        }
    }
}
