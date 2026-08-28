package com.jamm.webhook;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.api.v1.ChargeMessage;
import com.api.v1.ContractMessage;
import com.api.v1.EventType;
import com.api.v1.UserAccountMessage;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jamm.errors.InvalidSignatureException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Decodes recorded webhook payloads and asserts the resulting field values, so that a change in
 * what the API sends — a renamed or dropped field — fails here rather than silently surfacing as
 * an empty accessor.
 *
 * <p>Also verifies HMAC signatures against conformance vectors carrying the exact signed bytes.
 *
 * <p>The payload records are maintained alongside the API and are not part of the released
 * artifact; these tests skip when they are unavailable.
 */
class RecordedWebhookTest {

    private static final String RECORDS_DIR_PROPERTY = "webhook.records.dir";

    private static final String CHARGE_ID = "trx-00000000000000000000";
    private static final String CUSTOMER_ID = "cus-00000000000000000000";
    private static final String REFUND_ID = "rfd-00000000000000000000";

    @Nested
    @DisplayName("every recorded payload decodes")
    class Breadth {

        @ParameterizedTest(name = "{0}")
        @MethodSource("com.jamm.webhook.RecordedWebhookTest#allRecords")
        void decodesWithoutThrowing(String name, String body) {
            assertDoesNotThrow(() -> Webhook.parseEvent(body),
                    name + " must decode; a throw here means the SDK is out of sync with the API");
        }
    }

    @Nested
    @DisplayName("api_source decodes to the right value")
    class ApiSource {

        @Test
        void onSession() throws Exception {
            ChargeMessage charge = charge("charge_success_api_source.json");
            assertTrue(charge.hasApiSource());
            assertEquals(ChargeMessage.ApiSource.API_SOURCE_ON_SESSION, charge.getApiSource());
        }

        @Test
        void offSessionAsyncOnNestedRefund() throws Exception {
            ChargeMessage charge = charge("refund_succeeded_nested_api_source.json");
            assertTrue(charge.hasApiSource());
            assertEquals(ChargeMessage.ApiSource.API_SOURCE_OFF_SESSION_ASYNC, charge.getApiSource());
        }

        @Test
        void absentWhenTheRecordOmitsIt() throws Exception {
            ChargeMessage charge = charge("charge_success_without_api_source.json");
            assertFalse(charge.hasApiSource(),
                    "a record without api_source must not report one");
            assertEquals(ChargeMessage.ApiSource.API_SOURCE_UNSPECIFIED, charge.getApiSource());
        }

        @Test
        void decodesFromADeliveredCapture() throws Exception {
            ChargeMessage charge = charge("delivered_charge_success.json");
            assertEquals(ChargeMessage.ApiSource.API_SOURCE_OFF_SESSION_SYNC, charge.getApiSource());
        }
    }

    @Nested
    @DisplayName("refund id decodes from the nested wrapper")
    class RefundId {

        @ParameterizedTest(name = "{0}")
        @MethodSource("com.jamm.webhook.RecordedWebhookTest#refundRecords")
        void surfacesTheRfdId(String name) throws Exception {
            ChargeMessage charge = charge(name);
            assertEquals(CHARGE_ID, charge.getId(),
                    "the nested transaction must be flattened onto the charge");
            assertTrue(charge.hasRefund(), name + " carries a refund object");
            assertEquals(REFUND_ID, charge.getRefund().getId(),
                    "backend sends content.refund.id; an empty value means the SDK is reading a "
                            + "field the backend no longer emits");
        }

        /** The flat refund_id attribute mirrors refund.id, matching the Go, Node and Ruby SDKs. */
        @ParameterizedTest(name = "{0}")
        @MethodSource("com.jamm.webhook.RecordedWebhookTest#refundRecords")
        void alsoSurfacesTheIdOnTheFlatAttribute(String name) throws Exception {
            ChargeMessage charge = charge(name);
            assertEquals(REFUND_ID, charge.getRefundId(),
                    "charge.getRefundId() must mirror charge.getRefund().getId()");
        }

        @Test
        void flatAttributeStaysEmptyWhenTheEventHasNoRefundId() throws Exception {
            ChargeMessage charge = charge("refund_failed_no_refund_id.json");
            assertFalse(charge.hasRefundId());
            assertTrue(charge.getRefundId().isEmpty());
        }

        @Test
        void refundFailedCarriesTheError() throws Exception {
            ChargeMessage charge = charge("refund_failed_nested_error.json");
            assertTrue(charge.getRefund().hasError());
        }

        /**
         * A failure on the cancel-first path (which a full refund takes) carries {@code refund.id}
         * alongside the error, so the id matches the one the Refund call returned. It did not
         * before JAMM-4407: the backend fed that external id through an internal-id lookup, which
         * never matched, and the field was omitted.
         */
        @Test
        void cancelPathFailureCarriesTheRefundId() throws Exception {
            ChargeMessage charge = charge("refund_failed_cancel.json");
            assertTrue(charge.hasRefund());
            assertTrue(charge.getRefund().hasId(),
                    "the cancel-first failure path carries the rfd- the caller was handed");
            assertEquals(REFUND_ID, charge.getRefund().getId());
            assertEquals(REFUND_ID, charge.getRefundId(),
                    "the flat attribute must mirror it, as on the denied-request path");
            assertTrue(charge.getRefund().hasError());
            assertEquals("refund_failed", charge.getRefund().getError().getCode());
        }

        /**
         * {@code refund.id} is optional on the wire, so a record without one must still decode.
         * Guard before reading it rather than assuming presence.
         */
        @Test
        void aRefundEventWithoutAnIdStillDecodes() throws Exception {
            ChargeMessage charge = charge("refund_failed_no_refund_id.json");
            assertTrue(charge.hasRefund());
            assertFalse(charge.getRefund().hasId());
            assertTrue(charge.getRefund().getId().isEmpty());
            assertTrue(charge.getRefund().hasError());
        }
    }

    @Nested
    @DisplayName("other content types")
    class ContentTypes {

        @Test
        void chargeFailCarriesTheError() throws Exception {
            ChargeMessage charge = charge("charge_fail_error.json");
            assertTrue(charge.hasError());
            assertFalse(charge.getError().getCode().isEmpty());
        }

        @Test
        void metadataRoundTrips() throws Exception {
            ChargeMessage charge = charge("charge_success_metadata.json");
            assertEquals(2, charge.getMetadataCount());
            assertTrue(charge.containsMetadata("order_id"));
        }

        @Test
        void contractActivated() throws Exception {
            WebhookEvent event = Webhook.parseEvent(read("contract_activated.json"));
            assertEquals(EventType.EVENT_TYPE_CONTRACT_ACTIVATED, event.getEventType());
            ContractMessage contract = assertInstanceOf(ContractMessage.class, event.getContent());
            assertEquals(CUSTOMER_ID, contract.getCustomer());
        }

        @Test
        void userAccountDeleted() throws Exception {
            WebhookEvent event = Webhook.parseEvent(read("user_account_deleted.json"));
            assertEquals(EventType.EVENT_TYPE_USER_ACCOUNT_DELETED, event.getEventType());
            assertInstanceOf(UserAccountMessage.class, event.getContent());
        }
    }

    /**
     * The vectors carry the exact bytes that were signed, so they prove verification hashes what
     * was received rather than a re-serialization of it. The escaped vector is the one that
     * matters: the API's JSON encoder escapes {@code &}, {@code <} and {@code >}.
     */
    @Nested
    @DisplayName("signature conformance vectors")
    class Signatures {

        @ParameterizedTest(name = "{0}")
        @MethodSource("com.jamm.webhook.RecordedWebhookTest#signatureVectors")
        void verifiesAndRejectsTampering(String name, String body, String tampered, String secret) {
            assertDoesNotThrow(() -> Webhook.verifyAndParseEvent(body, secret),
                    name + " must verify against the secret the backend signed with");

            assertThrows(InvalidSignatureException.class,
                    () -> Webhook.verifyAndParseEvent(body, "not-the-secret"),
                    name + " must reject a wrong secret");

            assertThrows(InvalidSignatureException.class,
                    () -> Webhook.verifyAndParseEvent(tampered, secret),
                    name + " must reject content bytes that differ from what was signed");
        }
    }

    // ---- fixture loading -------------------------------------------------------------------

    static Stream<org.junit.jupiter.params.provider.Arguments> allRecords() throws IOException {
        List<org.junit.jupiter.params.provider.Arguments> out = new ArrayList<>();
        for (Path p : listRecords()) {
            out.add(org.junit.jupiter.params.provider.Arguments.of(
                    p.getFileName().toString(), readPath(p)));
        }
        return out.stream();
    }

    static Stream<String> refundRecords() {
        return Stream.of(
                "refund_succeeded_nested_api_source.json",
                "refund_succeeded_nested_no_api_source.json",
                "refund_failed_nested_error.json",
                "refund_failed_cancel.json",
                "delivered_refund_succeeded_nested.json");
    }

    /** Rebuilds each delivered body verbatim: envelope fields, raw content bytes, signature. */
    static Stream<org.junit.jupiter.params.provider.Arguments> signatureVectors() throws IOException {
        Path file = compatDir().resolve("signatures").resolve("vectors.json");
        assertTrue(Files.isRegularFile(file), "missing signature vectors at " + file.toAbsolutePath());

        JsonObject root = JsonParser.parseString(readPath(file)).getAsJsonObject();
        List<org.junit.jupiter.params.provider.Arguments> out = new ArrayList<>();
        for (JsonElement element : root.getAsJsonArray("vectors")) {
            JsonObject vector = element.getAsJsonObject();
            JsonObject envelope = vector.getAsJsonObject("envelope");

            StringBuilder body = new StringBuilder("{");
            for (String key : envelope.keySet()) {
                body.append('"').append(key).append("\":\"")
                        .append(envelope.get(key).getAsString()).append("\",");
            }
            String content = vector.get("content").getAsString();
            String signature = vector.get("signature").getAsString();
            String envelopePrefix = body.toString();
            String signed = envelopePrefix + "\"content\":" + content
                    + ",\"signature\":\"" + signature + "\"}";

            // Same JSON, one byte more: a space before content's closing brace. Still parses, but
            // the bytes no longer match what was signed.
            String tamperedContent = content.substring(0, content.length() - 1) + " }";
            String tampered = envelopePrefix + "\"content\":" + tamperedContent
                    + ",\"signature\":\"" + signature + "\"}";

            out.add(org.junit.jupiter.params.provider.Arguments.of(
                    vector.get("name").getAsString(), signed, tampered,
                    vector.get("secret").getAsString()));
        }
        return out.stream();
    }

    private static ChargeMessage charge(String name) throws IOException {
        return assertInstanceOf(ChargeMessage.class, Webhook.parseEvent(read(name)).getContent(),
                name + " must decode to a ChargeMessage");
    }

    private static String read(String name) throws IOException {
        Path file = webhooksDir().resolve(name);
        assertTrue(Files.isRegularFile(file), "missing fixture " + file.toAbsolutePath());
        return readPath(file);
    }

    private static String readPath(Path file) throws IOException {
        return new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
    }

    private static List<Path> listRecords() throws IOException {
        List<Path> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(webhooksDir(), "*.json")) {
            for (Path p : stream) {
                files.add(p);
            }
        }
        assertFalse(files.isEmpty(), "no fixtures found in " + webhooksDir().toAbsolutePath());
        files.sort(null);
        return files;
    }

    private static Path webhooksDir() {
        Path dir = compatDir().resolve("webhooks");
        assertTrue(Files.isDirectory(dir), "missing payload directory " + dir.toAbsolutePath());
        return dir;
    }

    private static Path compatDir() {
        return Paths.get(System.getProperty(RECORDS_DIR_PROPERTY));
    }

    /**
     * Skips the whole class when the payload records are not present, and fails loudly when they
     * are declared but unreadable — a declared-but-missing directory means a broken build setup,
     * not an absent optional input.
     */
    @BeforeAll
    static void requirePayloadRecords() {
        String dir = System.getProperty(RECORDS_DIR_PROPERTY);
        Assumptions.assumeTrue(dir != null && !dir.isEmpty(),
                "webhook payload records are not part of this checkout; skipping these tests");
        assertTrue(Files.isDirectory(Paths.get(dir)),
                RECORDS_DIR_PROPERTY + " is set to a missing directory: "
                        + Paths.get(dir).toAbsolutePath());
    }
}
