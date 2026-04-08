# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
