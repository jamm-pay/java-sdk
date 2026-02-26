package com.jamm;

import com.jamm.auth.OAuthProvider;
import com.jamm.config.Environment;
import com.jamm.http.JammHttpClient;

/**
 * Main client for interacting with the Jamm API.
 * Use the {@link Builder} to create instances.
 *
 * <p>Example usage:
 * <pre>{@code
 * JammClient client = JammClient.builder()
 *     .clientId("your-client-id")
 *     .clientSecret("your-client-secret")
 *     .environment(Environment.PRODUCTION)
 *     .build();
 *
 * // Use the client...
 * }</pre>
 */
public class JammClient implements AutoCloseable {

    private final String clientId;
    private final Environment environment;
    private final OAuthProvider oauthProvider;
    private final JammHttpClient httpClient;
    private final com.jamm.customer.CustomerClient customerClient;
    private final com.jamm.healthcheck.HealthcheckClient healthcheckClient;
    private final com.jamm.payment.PaymentClient paymentClient;

    // Configuration
    private final long connectTimeoutMs;
    private final long readTimeoutMs;
    private final int maxRetries;

    private JammClient(Builder builder) {
        this.clientId = builder.clientId;
        this.environment = builder.environment;
        this.connectTimeoutMs = builder.connectTimeoutMs;
        this.readTimeoutMs = builder.readTimeoutMs;
        this.maxRetries = builder.maxRetries;

        // Initialize OAuth provider
        this.oauthProvider = new OAuthProvider(
                clientId,
                builder.clientSecret,
                environment.getOauthBaseUrl(),
                connectTimeoutMs,
                readTimeoutMs
        );

        // Initialize HTTP client
        this.httpClient = new JammHttpClient(
                oauthProvider,
                environment.getApiBaseUrl(),
                connectTimeoutMs,
                readTimeoutMs,
                maxRetries,
                builder.retryInitialDelayMs,
                builder.retryMaxDelayMs
        );


        this.customerClient = new com.jamm.customer.CustomerClient(this);
        this.healthcheckClient = new com.jamm.healthcheck.HealthcheckClient(this);
        this.paymentClient = new com.jamm.payment.PaymentClient(this);
    }

    /**
     * Creates a new builder for JammClient.
     *
     * @return a new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Gets the client ID.
     *
     * @return the client ID
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Gets the configured environment.
     *
     * @return the environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * Gets the OAuth provider for token management.
     *
     * @return the OAuth provider
     */
    public OAuthProvider getOAuthProvider() {
        return oauthProvider;
    }

    /**
     * Gets the HTTP client for making API requests.
     *
     * @return the HTTP client
     */
    public JammHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * Gets the connection timeout in milliseconds.
     *
     * @return the connection timeout
     */
    public long getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    /**
     * Gets the read timeout in milliseconds.
     *
     * @return the read timeout
     */
    public long getReadTimeoutMs() {
        return readTimeoutMs;
    }

    /**
     * Gets the maximum number of retries.
     *
     * @return the max retries
     */
    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * Closes underlying resources used by this client.
     * <p>
     * This will release HTTP connection pools and executor services held
     * by the underlying {@link JammHttpClient}.
     */
    @Override
    public void close() {
        httpClient.close();
    }

    /**
     * Access the customer client.
     *
     * @return the customer client
     */
    public com.jamm.customer.CustomerClient customers() {
        return customerClient;
    }

    /**
     * Access the healthcheck client.
     *
     * @return the healthcheck client
     */
    public com.jamm.healthcheck.HealthcheckClient healthcheck() {
        return healthcheckClient;
    }

    /**
     * Access the payment client.
     *
     * @return the payment client
     */
    public com.jamm.payment.PaymentClient payments() {
        return paymentClient;
    }

    /**
     * Builder for creating JammClient instances.
     */
    public static class Builder {
        private String clientId;
        private String clientSecret;
        private Environment environment = Environment.PRODUCTION;
        private long connectTimeoutMs = 30_000; // 30 seconds
        private long readTimeoutMs = 90_000;    // 90 seconds
        private int maxRetries = 0;
        private long retryInitialDelayMs = 1000;  // 1 second
        private long retryMaxDelayMs = 30_000;    // 30 seconds

        /**
         * Sets the OAuth client ID.
         *
         * @param clientId the client ID
         * @return this builder
         */
        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        /**
         * Sets the OAuth client secret.
         *
         * @param clientSecret the client secret
         * @return this builder
         */
        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        /**
         * Sets the API environment.
         *
         * @param environment the environment
         * @return this builder
         */
        public Builder environment(Environment environment) {
            this.environment = environment;
            return this;
        }

        /**
         * Sets the API environment from a string name.
         *
         * @param envName the environment name (e.g., "production", "staging", "local")
         * @return this builder
         */
        public Builder environment(String envName) {
            this.environment = Environment.fromString(envName);
            return this;
        }

        /**
         * Sets the connection timeout.
         *
         * @param timeoutMs timeout in milliseconds
         * @return this builder
         */
        public Builder connectTimeout(long timeoutMs) {
            this.connectTimeoutMs = timeoutMs;
            return this;
        }

        /**
         * Sets the read timeout.
         *
         * @param timeoutMs timeout in milliseconds
         * @return this builder
         */
        public Builder readTimeout(long timeoutMs) {
            this.readTimeoutMs = timeoutMs;
            return this;
        }

        /**
         * Sets the maximum number of retry attempts for failed requests.
         *
         * @param maxRetries maximum retries (0 means no retries)
         * @return this builder
         */
        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        /**
         * Sets the initial delay between retry attempts.
         *
         * @param delayMs initial delay in milliseconds
         * @return this builder
         */
        public Builder retryInitialDelay(long delayMs) {
            this.retryInitialDelayMs = delayMs;
            return this;
        }

        /**
         * Sets the maximum delay between retry attempts (for exponential backoff).
         *
         * @param delayMs maximum delay in milliseconds
         * @return this builder
         */
        public Builder retryMaxDelay(long delayMs) {
            this.retryMaxDelayMs = delayMs;
            return this;
        }

        /**
         * Builds the JammClient instance.
         *
         * @return a new JammClient
         * @throws IllegalArgumentException if required parameters are missing
         */
        public JammClient build() {
            if (clientId == null || clientId.isEmpty()) {
                throw new IllegalArgumentException("clientId is required");
            }
            if (clientSecret == null || clientSecret.isEmpty()) {
                throw new IllegalArgumentException("clientSecret is required");
            }
            if (environment == null) {
                throw new IllegalArgumentException("environment is required");
            }
            return new JammClient(this);
        }
    }
}
