# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [3.0.0] - 2026-09-02

Major version because the synchronous off-session charge method was removed — see **Removed** below.

### Removed

- **Breaking: `PaymentClient.offSessionPayment(...)` (both overloads) is gone.** Off-session charges are now supported through `offSessionPaymentAsync(...)` only. Replace each call with `offSessionPaymentAsync` and read the charge result from `getCharge(response.getChargeId())` or from the `EVENT_TYPE_CHARGE_SUCCESS` / `EVENT_TYPE_CHARGE_FAIL` webhook — the async response carries `request_id`, `charge_id` and `status`, not the settled charge. Platform mode migrates identically (`offSessionPayment(request, merchant)` → `offSessionPaymentAsync(request, merchant)`). See [Migrating from 2.x](README.md#migrating-from-2x). The `OffSessionPaymentRequest` / `OffSessionPaymentResponse` protobuf types still exist in `com.api.v1`, but no client method accepts or returns them.

- `PaymentOffSessionExample.java` — the synchronous example. `PlatformPaymentOffSessionExample.java` is replaced by `PlatformPaymentOffSessionAsyncExample.java`.

## [2.0.0] - 2026-08-26

Major version because the refund identifier accessors changed shape — see **Changed** below.
Everything else in this release is additive.

### Added

- Charge webhooks now expose the merchant `metadata` map attached to the charge at creation, echoed back verbatim, via `getMetadataMap()` / `getMetadataOrDefault(...)`. This lets you correlate a webhook against your own order without a `GetCharge` round-trip. Available on all six charge events, including `REFUND_SUCCEEDED` and `REFUND_FAILED`, which deliver the charge nested under `transaction`. A charge created without metadata decodes to an empty map.

- **`api_source` is back on charge webhooks** — `charge.getApiSource()` / `hasApiSource()` return a `ChargeMessage.ApiSource` (`API_SOURCE_OFF_SESSION_SYNC` / `API_SOURCE_OFF_SESSION_ASYNC` / `API_SOURCE_ON_SESSION`) telling you which API triggered the charge. The field shipped in 1.5.0, was removed in 1.6.0 by a pre-release rollback, and is restored here — so `1.6.0` and `1.6.1` have no `ApiSource` type at all and `getApiSource()` does not compile against them. Note the enum is nested inside `ChargeMessage`, not a top-level `com.api.v1` type. Charges predating the field omit it; guard with `hasApiSource()`.

- **Refund webhooks now also populate the flat `charge.getRefundId()`** (`ChargeMessage` field 21) with the same `rfd-` value as `charge.getRefund().getId()`, matching the Go, Node and Ruby SDKs. Previously the Java SDK left field 21 unset on every refund event, so `getRefundId()` compiled and silently returned an empty string. Failure events that carry no refund id leave it unset; guard with `hasRefundId()`.

### Changed

- **Breaking: `RefundInfo.getRefundId()` / `hasRefundId()` are again `getId()` / `hasId()`** — matching the `content.refund.id` the backend actually delivers. This completes the rename first made in 1.5.1 and reverted in 1.6.0 by the same rollback as `api_source`. **Upgrading from 1.6.0/1.6.1 is a compile break** at every `getRefund().getRefundId()` call site; replace with `getRefund().getId()`. Merchants on 1.6.0/1.6.1 should upgrade regardless: those versions discard the delivered `refund.id` as an unknown field, leaving `refund_id` unset, so `getRefundId()` silently returns an empty string for every refund webhook.

## [1.6.1] - 2026-07-17

### Changed

- Internal pipeline changes, no effect on Java SDK behavior

## [1.6.0] - 2026-07-07

### Changed

- **Replaced Jackson with gson for JSON handling** — dropped the `jackson-databind` / `jackson-annotations` dependencies (and their transitive `jackson-core`) in favour of `gson`, which was already on the classpath transitively via `protobuf-java-util` and is now a direct dependency. Integrators who vendor dependencies by hand no longer have to reconcile the SDK's Jackson version against their own. No public API changes. The only behavioural difference is `ApiException.getBody()`: JSON numbers in the untyped map are now `Double` (gson's default) rather than `Integer`/`Long` — the typed `getCode()` / `getErrorCode()` accessors are unaffected. Webhook signature verification is unchanged: it still hashes the exact received `content` bytes.

### Removed

- **Dropped the OkHttp dependency** (`okhttp`, and its transitive `okio` and Kotlin standard-library jars — 8 jars total). The HTTP transport now uses the JDK's built-in `java.net.HttpURLConnection`, so the SDK ships with **no runtime HTTP dependency to vendor**. No public API changes; timeouts, retries, authentication, and error handling behave as before. (OkHttp remains a test-only dependency via `mockwebserver`.)

- **Dropped `protovalidate` and `proto-google-common-protos`** (and their transitive CEL, ANTLR4, re2j, threeten-extra, SnakeYAML, and annotation jars — ~15 jars, including the Guava stack that `protobuf-java-util` no longer needs). The generated protobuf classes previously embedded `google.api.http` routing annotations and `buf.validate` field-constraint options, which pulled these in. The SDK routes via hardcoded REST paths and never runs a validator, so those options are dead weight; `make gen` now regenerates `lib/proto` with them stripped from the descriptors (see `packages/backends/api/proto` — `cmd/protostrip` + `buf.gen.java.yaml`). No public API or behaviour changes. **This brings the runtime dependency set to 7 jars** (from ~33): `protobuf-java`, `protobuf-java-util`, `gson`, `slf4j-api`, `javax.annotation-api`, `jsr305`, `error_prone_annotations`.

### Added

- **`Webhook.verifyAndParseEvent(...)` and `Webhook.parseEvent(...)`** — return a `WebhookEvent` exposing both the envelope's `event_type` (`getEventType()`) and the parsed `content` (`getContent()`). Previously `event_type` was consumed internally and not exposed, so callers could not reliably distinguish `EVENT_TYPE_CHARGE_SUCCESS` from `EVENT_TYPE_CHARGE_FAIL`, or charge events from refund events (both deserialize to `ChargeMessage`). The existing `verifyAndParse` / `parse` (returning only the content) are unchanged.

### Removed

- **Dropped the gRPC/Netty dependencies** (`grpc-stub`, `grpc-protobuf`, `grpc-netty-shaded` and their transitive Netty stack, ~11.5 MB). The SDK communicates over REST and never used gRPC — the dependency existed only because generated `*ServiceGrpc.java` service stubs were compiled into the jar. Those stubs are no longer compiled, and the generated `com.api.v1.*ServiceGrpc` classes are no longer present in the artifact. This significantly shrinks the footprint for integrators who vendor dependencies manually.

## [1.5.3] - 2026-07-06

### Fixed

- **SDK now compiles to Java 8 bytecode** (was Java 11), so it can be consumed on Java 8 build/runtime toolchains. Forward-compatible with Java 11/17/21+; no API or behaviour changes. Internal `String.isBlank()` and `URLEncoder.encode(String, Charset)` usages were replaced with Java 8-safe equivalents.

### Removed

- **Dropped the unused `grpc-ecosystem-protoc-gen-openapiv2` dependency** (Java 21-only, not referenced at compile or runtime) — the entire runtime classpath is now Java 8-compatible.

## [1.5.2] - 2026-06-24

### Security

- **Bumped `jackson-databind` / `jackson-annotations` 2.16.1 → 2.18.8** — resolves two high-severity advisories in `jackson-databind`: a `PolymorphicTypeValidator` bypass via generic type parameters allowing arbitrary class instantiation, and an array-subtype allowlist bypass in `BasicPolymorphicTypeValidator` (`allowIfSubTypeIsArray`). No API changes.

## [1.5.1] - 2026-06-23

### Changed

- **Refund webhook `refund_id` field renamed to `id`** — on `EVENT_TYPE_REFUND_SUCCEEDED` / `EVENT_TYPE_REFUND_FAILED`, the refund object's identifier is now `content.refund.id` (was `content.refund.refund_id`), consistent with `content.transaction.id`. `RefundInfo.getRefundId()` / `hasRefundId()` are now `getId()` / `hasId()`. Update any code reading the refund identifier.

## [1.5.0] - 2026-06-22

### Added

- **`api_source` on charge webhooks** — `ChargeMessage` now carries `getApiSource()`, identifying which API triggered the charge: `API_SOURCE_OFF_SESSION_SYNC`, `API_SOURCE_OFF_SESSION_ASYNC`, or `API_SOURCE_ON_SESSION` (`API_SOURCE_UNSPECIFIED` otherwise). See the [Webhook Verification](README.md#webhook-verification) example.

### Fixed

- **Refund/cancel webhooks now expose `refund_id` and the original transaction** — `EVENT_TYPE_REFUND_SUCCEEDED` / `EVENT_TYPE_REFUND_FAILED` payloads use a nested `{ transaction, refund }` shape. `Webhook.parse` previously flat-merged the content, dropping the `transaction` block. It now flattens the transaction fields onto the `ChargeMessage` and populates `getRefund()` (a `RefundInfo` carrying the `rfd-` `getRefundId()`, `getAmountRefunded()`, etc.). Legacy flat refund payloads are still parsed.

## [1.4.1] - 2026-06-03

### Fixed

- **`getCharges` pagination** — paginated `getCharges` calls now send `pagination.pageToken` / `pagination.pageSize` query params instead of the flat `pageToken` / `pageSize`, which the API rejected with HTTP 500. Non-paginated calls were unaffected.

## [1.4.0] - 2026-04-27

### Added

- **Idempotency key for async off-session payments** — `OffSessionPaymentAsyncRequest` now accepts an `idempotency_key` field. Submitting a retry with the same key returns the existing charge instead of creating a duplicate.
  - The SDK auto-fills the field with a UUID when omitted, so charges are always de-duplicated on accidental network retries
  - Set the key yourself (ASCII, 1–255 chars of `[A-Za-z0-9_-]`) when you need explicit retry control
  - See the updated [Off-Session Payment (Async)](README.md#off-session-payment-async) example

## [1.3.0] - 2026-04-08

### Added

- **Platform mode** — make API calls on behalf of connected merchants by passing a `merchant` parameter
  - Initialize with `JammClient.Builder.platform(true)` or `Jamm.configure(clientId, clientSecret, environment, true)`
  - All customer, payment, and healthcheck methods support an optional `merchant` overload (e.g., `client.customers().create(request, "mer-123")`)
  - See the [Platform Onboarding Guide](https://docs.jamm-pay.jp/docs/platform-onboarding-guide) for setup details

## [1.2.2] - 2026-04-06

### Changed

- Upgraded build environment to JDK 25 (SDK still targets JDK 11 — no changes required for merchants)

## [1.2.1] - 2026-04-06

### Added

- Added `ChargeError` field on `ChargeResult` — contains `code` and `message` for failed charges (present only when `charge_status` is `CHARGE_STATUS_FAILURE`)
- Added `Error` field on `ChargeMessage` webhook payload — contains error details for `EVENT_TYPE_CHARGE_FAIL` webhooks
- Added `EVENT_TYPE_CHARGE_FAIL` webhook support for async off-session payments

## [1.2.0] - 2026-03-24

### Changed

- **Breaking:** Renamed refund webhook event types — `EVENT_TYPE_CHARGE_CANCEL`, `EVENT_TYPE_CHARGE_REFUND` → `EVENT_TYPE_REFUND_SUCCEEDED`; `EVENT_TYPE_CHARGE_REFUND_FAILED`, `EVENT_TYPE_CHARGE_REFUND_DENIED` → `EVENT_TYPE_REFUND_FAILED`
  - **Migrate:** Replace `EVENT_TYPE_CHARGE_CANCEL` and `EVENT_TYPE_CHARGE_REFUND` with `EVENT_TYPE_REFUND_SUCCEEDED`; replace `EVENT_TYPE_CHARGE_REFUND_FAILED` and `EVENT_TYPE_CHARGE_REFUND_DENIED` with `EVENT_TYPE_REFUND_FAILED`.
- Updated all documentation references from `charge_refund` webhook to `refund_succeeded`

## [1.1.3] - 2026-03-05

### Added

- Added `cancel_only` parameter to `RefundRequest` — when set to `true`, only attempts cancellation without falling back to bank transfer refund

## [1.1.2] - 2026-03-04

### Added

- Added `EVENT_TYPE_CHARGE_REFUND_FAILED` and `EVENT_TYPE_CHARGE_REFUND_DENIED` webhook event types
- Added `RefundInfo` message with refund-specific details (`refund_id`, `amount_refunded`, `jamm_fee`, `consumption_tax`, `processed_at`)
- Added `refund_id` and `STATUS_REFUNDED` fields to `ChargeMessage`

### Changed

- Loosened phone number validation to accept 10-11 digit Japanese phone numbers (landlines, IP phones, mobile)

## [1.1.1] - 2026-03-03

### Changed

- Aligned the protobuf Java runtime dependency with the regenerated proto code
- Added `jamm_fee` to the `charge_refund` webhook payload model
- Expanded examples and tests to cover on-session flow variants and refund scenarios
- Improved Java SDK error diagnostics and example failure output with richer `ApiException` context

## [1.1.0] - 2026-02-26

### Added

- Supported Off-Session Payment Async API (`client.payments().offSessionPaymentAsync()`)
- Supported Refund API (`client.payments().refund()`) for full and partial refunds

## [1.0.0] - 2026-02-12

### Added

- Supported Customer, Payment, and Webhook APIs
- Added SDK version in client transport
- Added examples and E2E tests
- Published to Maven Central
