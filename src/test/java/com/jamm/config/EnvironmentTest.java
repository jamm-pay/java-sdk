package com.jamm.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EnvironmentTest {

    @Test
    void testProductionEnvironment() {
        Environment env = Environment.PRODUCTION;
        assertEquals("https://merchant-identity.jamm-pay.jp", env.getOauthBaseUrl());
        assertEquals("https://api.jamm-pay.jp", env.getApiBaseUrl());
    }

    @Test
    void testStagingEnvironment() {
        Environment env = Environment.STAGING;
        assertEquals("https://merchant-identity.develop.jamm-pay.jp", env.getOauthBaseUrl());
        assertEquals("https://api.develop.jamm-pay.jp", env.getApiBaseUrl());
    }

    @Test
    void testLocalEnvironment() {
        Environment env = Environment.LOCAL;
        assertEquals("https://merchant-identity.develop.jamm-pay.jp", env.getOauthBaseUrl());
        assertEquals("https://api.jamm.test", env.getApiBaseUrl());
    }

    @Test
    void testTestingEnvironment() {
        Environment env = Environment.TESTING;
        assertEquals("https://merchant-identity.testing.jamm-pay.jp", env.getOauthBaseUrl());
        assertEquals("https://api.testing.jamm-pay.jp", env.getApiBaseUrl());
    }

    @ParameterizedTest
    @ValueSource(strings = {"prd", "prod", "production"})
    void testFromStringProduction(String envName) {
        Environment env = Environment.fromString(envName);
        assertEquals(Environment.PRODUCTION.getOauthBaseUrl(), env.getOauthBaseUrl());
        assertEquals(Environment.PRODUCTION.getApiBaseUrl(), env.getApiBaseUrl());
    }

    @ParameterizedTest
    @ValueSource(strings = {"staging", "develop"})
    void testFromStringStaging(String envName) {
        Environment env = Environment.fromString(envName);
        assertEquals(Environment.STAGING.getOauthBaseUrl(), env.getOauthBaseUrl());
        assertEquals(Environment.STAGING.getApiBaseUrl(), env.getApiBaseUrl());
    }

    @Test
    void testFromStringLocal() {
        Environment env = Environment.fromString("local");
        assertEquals(Environment.LOCAL.getOauthBaseUrl(), env.getOauthBaseUrl());
        assertEquals(Environment.LOCAL.getApiBaseUrl(), env.getApiBaseUrl());
    }

    @ParameterizedTest
    @ValueSource(strings = {"testing", "test"})
    void testFromStringTesting(String envName) {
        Environment env = Environment.fromString(envName);
        assertEquals(Environment.TESTING.getOauthBaseUrl(), env.getOauthBaseUrl());
        assertEquals(Environment.TESTING.getApiBaseUrl(), env.getApiBaseUrl());
    }

    @Test
    void testFromStringNull() {
        Environment env = Environment.fromString(null);
        assertEquals(Environment.PRODUCTION, env);
    }

    @Test
    void testCustomEnvironment() {
        Environment env = Environment.custom("sandbox");
        assertEquals("https://merchant-identity.sandbox.jamm-pay.jp", env.getOauthBaseUrl());
        assertEquals("https://api.sandbox.jamm-pay.jp", env.getApiBaseUrl());
    }

    @Test
    void testFromStringCustom() {
        Environment env = Environment.fromString("qa");
        assertEquals("https://merchant-identity.qa.jamm-pay.jp", env.getOauthBaseUrl());
        assertEquals("https://api.qa.jamm-pay.jp", env.getApiBaseUrl());
    }

    // Constructor validation tests
    @Test
    void testConstructorNullOauthBaseUrl() {
        assertThrows(NullPointerException.class, () ->
                new Environment(null, "https://api.example.com"));
    }

    @Test
    void testConstructorNullApiBaseUrl() {
        assertThrows(NullPointerException.class, () ->
                new Environment("https://oauth.example.com", null));
    }

    @Test
    void testConstructorEmptyOauthBaseUrl() {
        assertThrows(IllegalArgumentException.class, () ->
                new Environment("", "https://api.example.com"));
    }

    @Test
    void testConstructorEmptyApiBaseUrl() {
        assertThrows(IllegalArgumentException.class, () ->
                new Environment("https://oauth.example.com", ""));
    }

    @Test
    void testCustomNullEnvName() {
        assertThrows(NullPointerException.class, () ->
                Environment.custom(null));
    }

    @Test
    void testCustomEmptyEnvName() {
        assertThrows(IllegalArgumentException.class, () ->
                Environment.custom(""));
    }
}
