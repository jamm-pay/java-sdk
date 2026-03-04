# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

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
