package com.jamm.compat;

import com.api.v1.ChargeMessage;
import com.api.v1.PingResponse;
import com.jamm.Jamm;
import com.jamm.webhook.Webhook;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * Shared backward-compatibility smoke suite for the published {@code jp.jamm-pay:jamm-sdk} artifact.
 *
 * <p>Each {@code compatibility/<version>/} directory is a tiny Maven project whose {@code pom.xml}
 * pins {@code jamm-sdk} to one published version and adds this file as a test source
 * ({@code build-helper-maven-plugin}). Maven therefore compiles and runs the <em>same</em>
 * assertions against every pinned version's jar. This mirrors the Node/Ruby/PHP harnesses, whose
 * dynamic runtimes let one suite run against every version; Java binds at compile time instead, so
 * the version directories share this source rather than importing it at runtime.
 *
 * <p>Unlike the dynamic-language suites, this one does not reflectively capability-gate every call:
 * across the pinned range (1.1.3–1.5.2) the surface it touches — {@code Jamm.configure},
 * {@code Webhook.verify}, {@code Webhook.parse}, {@code healthcheck().ping()} — is signature-stable,
 * so the suite compiles directly against each jar. The only gate is the live healthcheck, which is
 * skipped unless {@code MERCHANT_CLIENT_*} are set (it is the sole check that touches the API).
 *
 * <p>The {@code webhook.parse} checks are the headline: they feed each version current-day backend
 * records (carrying {@code api_source} and the nested refund wrapper) and assert the core
 * {@link ChargeMessage} still decodes. A version that <em>throws</em> on these records fails here —
 * that failure is the signal it is out of sync with the API, not an accepted outcome. Because the
 * pinned versions are published and immutable, {@code make report} records per-version PASS/FAIL for
 * an informational PR comment rather than blocking the merge.
 */
@DisplayName("jamm-sdk backward-compat")
class CompatSuite {

    // Shared decoded-core assertions: every parse-capable version must decode these from each
    // fixture regardless of the newer fields it carries.
    private static final String EXPECTED_ID = "trx-00000000000000000000";
    private static final String EXPECTED_CUSTOMER = "cus-00000000000000000000";

    // Mirrors the e2e credential env vars. When unset OR empty, the live-API healthcheck is skipped
    // so the offline checks still run (e.g. in CI without a reachable backend). CI passes
    // `MERCHANT_CLIENT_ID=` when the secret is missing, which surfaces as an empty string, so both
    // the null and empty forms are treated as "no creds".
    private static final String CLIENT_ID = envOr("MERCHANT_CLIENT_ID", "compat-client-id");
    private static final String CLIENT_SECRET = envOr("MERCHANT_CLIENT_SECRET", "compat-client-secret");
    private static final boolean HAS_API_CREDS =
            !isBlank(System.getenv("MERCHANT_CLIENT_ID")) && !isBlank(System.getenv("MERCHANT_CLIENT_SECRET"));

    // Environment the live healthcheck configures against. The SDK's local env host is
    // api.jamm.test; override with ENV (e.g. develop) when running against a remote cluster.
    private static final String ENV_NAME = envOr("ENV", "local");

    // The API base URL "local" resolves to, asserted by the config round-trip. Environment exposes
    // no name reader, so the resolved host is the observable proof the env string round-trips.
    private static final String LOCAL_API_BASE_URL = "https://api.jamm.test";

    // Payload for the webhook.verify contract check. The signature is NOT hardcoded: verify()
    // HMAC-signs this exact string with the configured client_secret, so the suite recomputes the
    // expected signature for whatever secret it uses. Fixed pseudo IDs, not real merchant data.
    private static final String WEBHOOK_CONTENT_JSON =
            "{\"customer\":\"cus-000000000000000000\","
          + "\"created_at\":\"2024-11-29T02:16:12.168127Z\","
          + "\"activated_at\":\"2024-11-29T02:16:18.040142301Z\","
          + "\"merchant_name\":\"TestMerchant1\"}";

    // config — present in every published version. Round-trips the "local" env string through the
    // global client and asserts it resolves to the local API host.
    @Test
    @DisplayName("config: Jamm.configure round-trips the local environment")
    void configRoundTripsLocalEnvironment() {
        Jamm.configure(CLIENT_ID, CLIENT_SECRET, "local");
        assertEquals(LOCAL_API_BASE_URL, Jamm.getClient().getEnvironment().getApiBaseUrl());
    }

    // healthcheck — hits the live API (ENV, default api.jamm.test), so it needs creds and a
    // reachable backend; skipped otherwise.
    @Test
    @DisplayName("healthcheck: ping reaches the API and reports ok")
    void healthcheckPingsApi() {
        Assumptions.assumeTrue(HAS_API_CREDS, "MERCHANT_CLIENT_* not set; live API check skipped");

        Jamm.configure(CLIENT_ID, CLIENT_SECRET, ENV_NAME);
        PingResponse res = Jamm.getClient().healthcheck().ping();
        assertNotNull(res);
        assertTrue(res.getOk());
    }

    // webhook.verify — offline signing-contract check: recompute the signature the SDK expects,
    // confirm verify() accepts it, then confirm a tampered (but well-formed) signature is rejected.
    @Test
    @DisplayName("webhook.verify: accepts a valid signature and rejects a tampered one")
    void webhookVerifyAcceptsValidAndRejectsTampered() throws Exception {
        String digest = hmacSha256Hex(CLIENT_SECRET, WEBHOOK_CONTENT_JSON);

        assertDoesNotThrow(() ->
                Webhook.verify(WEBHOOK_CONTENT_JSON, "sha256=" + digest, CLIENT_SECRET));

        assertThrows(Exception.class, () ->
                Webhook.verify(WEBHOOK_CONTENT_JSON, "sha256=" + "0".repeat(64), CLIENT_SECRET));
    }

    // webhook.parse forward-compat — pretends the backend is sending current-day records that carry
    // api_source (ChargeMessage field 23) and the nested refund wrapper. Every version MUST decode
    // the core ChargeMessage and ignore fields it predates. A version that throws fails here — that
    // failure is the signal it is out of sync with the API, not an accepted outcome (mirrors the
    // Node/Ruby harnesses). Each charge/refund shape is covered both with and without api_source so
    // the post-revert backend (no api_source emitted) is exercised alongside the re-landed shape.
    @ParameterizedTest(name = "webhook.parse tolerates {0}")
    @ValueSource(strings = {
            "charge_success_api_source.json",
            "charge_success_without_api_source.json",
            "refund_succeeded_nested_api_source.json",
            "refund_succeeded_nested_no_api_source.json",
    })
    @DisplayName("webhook.parse: tolerates a current-day backend record")
    void webhookParseToleratesBackendRecord(String file) throws Exception {
        String json = Files.readString(testdataPath(file));

        Object result = Webhook.parse(json);
        assertInstanceOf(ChargeMessage.class, result, file);

        ChargeMessage charge = (ChargeMessage) result;
        assertEquals(EXPECTED_ID, charge.getId(), file);
        assertEquals(EXPECTED_CUSTOMER, charge.getCustomer(), file);
    }

    // Fixtures live in the language-neutral packages/sdk/compatibility/testdata/ directory so every
    // SDK harness consumes the same backend records. The version pom passes the directory via the
    // compat.testdata.dir system property; the fallback resolves it relative to the module dir.
    private static Path testdataPath(String file) {
        String dir = System.getProperty("compat.testdata.dir");
        if (dir == null || dir.isEmpty()) {
            dir = Path.of("..", "..", "..", "compatibility", "testdata").toString();
        }
        return Path.of(dir, file);
    }

    private static String hmacSha256Hex(String secret, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] out = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder(out.length * 2);
        for (byte b : out) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static String envOr(String name, String fallback) {
        String value = System.getenv(name);
        return isBlank(value) ? fallback : value;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }
}
