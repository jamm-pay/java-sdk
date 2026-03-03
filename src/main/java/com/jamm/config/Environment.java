package com.jamm.config;

import java.util.Locale;
import java.util.Objects;

/**
 * Environment configuration for the Jamm SDK.
 * Defines the API endpoints for different deployment environments.
 */
public class Environment {

    /**
     * Production environment.
     */
    public static final Environment PRODUCTION = new Environment(
            "https://merchant-identity.jamm-pay.jp",
            "https://api.jamm-pay.jp"
    );

    /**
     * Staging environment.
     */
    public static final Environment STAGING = new Environment(
            "https://merchant-identity.staging.jamm-pay.jp",
            "https://api.staging.jamm-pay.jp"
    );

    /**
     * Development environment.
     */
    public static final Environment DEVELOP = new Environment(
            "https://merchant-identity.develop.jamm-pay.jp",
            "https://api.develop.jamm-pay.jp"
    );

    /**
     * Local development environment.
     */
    public static final Environment LOCAL = new Environment(
            "https://merchant-identity.develop.jamm-pay.jp",
            "https://api.jamm.test"
    );

    /**
     * Testing environment.
     */
    public static final Environment TESTING = new Environment(
            "https://merchant-identity.testing.jamm-pay.jp",
            "https://api.testing.jamm-pay.jp"
    );

    private final String oauthBaseUrl;
    private final String apiBaseUrl;

    /**
     * Creates a new Environment with the specified URLs.
     *
     * @param oauthBaseUrl the OAuth server base URL (must not be null or empty)
     * @param apiBaseUrl   the API base URL (must not be null or empty)
     * @throws NullPointerException if oauthBaseUrl or apiBaseUrl is null
     * @throws IllegalArgumentException if oauthBaseUrl or apiBaseUrl is empty
     */
    public Environment(String oauthBaseUrl, String apiBaseUrl) {
        Objects.requireNonNull(oauthBaseUrl, "oauthBaseUrl must not be null");
        Objects.requireNonNull(apiBaseUrl, "apiBaseUrl must not be null");
        if (oauthBaseUrl.isEmpty()) {
            throw new IllegalArgumentException("oauthBaseUrl must not be empty");
        }
        if (apiBaseUrl.isEmpty()) {
            throw new IllegalArgumentException("apiBaseUrl must not be empty");
        }
        this.oauthBaseUrl = oauthBaseUrl;
        this.apiBaseUrl = apiBaseUrl;
    }

    /**
     * Gets the OAuth base URL for this environment.
     *
     * @return the OAuth base URL
     */
    public String getOauthBaseUrl() {
        return oauthBaseUrl;
    }

    /**
     * Gets the API base URL for this environment.
     *
     * @return the API base URL
     */
    public String getApiBaseUrl() {
        return apiBaseUrl;
    }

    /**
     * Creates a custom environment with the specified environment name.
     * URLs are generated based on the standard naming convention.
     *
     * @param envName the environment name (e.g., "develop", "staging")
     * @return a new Environment instance with custom URLs
     * @throws NullPointerException if envName is null
     * @throws IllegalArgumentException if envName is empty
     */
    public static Environment custom(String envName) {
        Objects.requireNonNull(envName, "envName must not be null");
        if (envName.isEmpty()) {
            throw new IllegalArgumentException("envName must not be empty");
        }
        String oauthUrl = String.format("https://merchant-identity.%s.jamm-pay.jp", envName);
        String apiUrl = String.format("https://api.%s.jamm-pay.jp", envName);
        return new Environment(oauthUrl, apiUrl);
    }

    /**
     * Parses an environment from a string name.
     * Supports: "prd", "prod", "production", "staging", "develop", "local", "testing", "test"
     *
     * @param envName the environment name
     * @return the corresponding Environment
     */
    public static Environment fromString(String envName) {
        if (envName == null) {
            return PRODUCTION;
        }

        switch (envName.toLowerCase(Locale.ROOT)) {
            case "prd":
            case "prod":
            case "production":
                return PRODUCTION;
            case "local":
                return LOCAL;
            case "staging":
                return STAGING;
            case "develop":
                return DEVELOP;
            case "testing":
            case "test":
                return TESTING;
            default:
                return custom(envName);
        }
    }
}
