[![MIT License][license-shield]][license-url]
[![Maven Central][maven-central-shield]][maven-central-url]

<br />
<div align="center">
  <a href="https://docs.jamm-pay.jp">
    <img src="https://assets.jamm-pay.jp/brand/jamm_logo.png" alt="logo" width="120" height="120">
  </a>
  <h3 align="center">Jamm SDK - Java</h3>
  <p align="center">
    The official Java SDK for Jamm's payment API!
    We strongly recommend using the SDK for backend integration in order to simplify and streamline your development process!
    <br />
    <a href="https://docs.jamm-pay.jp"><strong>Docs »</strong></a>
    ·
    <a href="https://github.com/jamm-pay/java-sdk/issues">Report Bug / Request Feature</a>
    <br />
    <br />
  </p>
</div>

## How to Use

```java
import com.jamm.JammClient;
import com.jamm.config.Environment;
import com.api.v1.*;

// Create a client
try (JammClient client = JammClient.builder()
        .clientId("<your client id>")
        .clientSecret("<your client secret>")
        .environment(Environment.PRODUCTION)
        .build()) {

    // Create an on-session payment
    OnSessionPaymentResponse payment = client.payments().onSessionPayment(
        OnSessionPaymentRequest.newBuilder()
            .setCharge(InitialCharge.newBuilder()
                .setPrice(10000)
                .setDescription("Jamm")
                .build())
            .setRedirect(URL.newBuilder()
                .setSuccessUrl("http://www.example.com/success")
                .setFailureUrl("http://www.example.com/fail")
                .build())
            .setBuyer(Buyer.newBuilder()
                .setName("Taro Taro")
                .setKatakanaFirstName("タロウ")
                .setKatakanaLastName("タロウ")
                .setGender("male")
                .setAddress("東京都渋谷区１−１−１")
                .setEmail("test@jamm-pay.jp")
                .setPhone("09012345678")
                .setBirthDate("2000-01-01")
                .build())
            .build());
}
```

### Off-Session Payment

Once a customer has approved the payment and completed onboarding (KYC, terms of service, payment method setup), you can charge them off-session. The charge is always started asynchronously: the call returns immediately with a `charge_id` while the charge is still `CHARGE_STATUS_PENDING`, and you poll `getCharge` (or wait for the charge webhook) for the result.

```java
OffSessionPaymentAsyncResponse asyncResponse = client.payments().offSessionPaymentAsync(
    OffSessionPaymentAsyncRequest.newBuilder()
        .setCustomer("cus-xxxxxxxx")
        .setCharge(InitialCharge.newBuilder()
            .setPrice(5000)
            .setDescription("Monthly subscription")
            .putMetadata("orderId", "order-123")
            .build())
        .setIdempotencyKey("order-2024-001")
        .build());

String requestId = asyncResponse.getRequestId();
String chargeId = asyncResponse.getChargeId();

ChargeResult charge = awaitCharge(client, chargeId);
if (charge == null) {
    // Still pending — leave the order open and let the webhook resolve it (see below).
} else if (charge.getPaid()) {
    // Settled and paid; fulfil the order.
}
```

#### Waiting for the result

A single `getCharge` straight after the async call reads a charge that has not settled yet, so
`getPaid()` would be `false` for a charge that succeeds a moment later. Poll until the charge leaves
`CHARGE_STATUS_PENDING`, and treat "still pending when the poll gives up" as *unresolved*, never as
failed:

```java
// Returns the settled charge, or null if it is still pending when the budget runs out.
static ChargeResult awaitCharge(JammClient client, String chargeId) throws InterruptedException {
    for (int attempt = 0; attempt < 30; attempt++) {
        ChargeResult charge = client.payments().getCharge(chargeId).getCharge();
        if (charge.getChargeStatus() != ChargeStatus.CHARGE_STATUS_PENDING) {
            return charge;
        }
        Thread.sleep(2000);
    }
    return null;
}
```

A `null` here means "no answer yet" — the charge may still settle. Keep the order awaiting the
`EVENT_TYPE_CHARGE_SUCCESS` / `EVENT_TYPE_CHARGE_FAIL` webhook rather than marking it unpaid, and if
you retry, reuse the same `idempotency_key` so you read the same charge instead of creating a second
one.

Handling the webhook instead of polling frees the calling thread entirely and is the better
integration for anything but a straight port of a synchronous call site — see
[Webhook Verification](#webhook-verification).

> The synchronous `offSessionPayment` method was removed in 3.0.0. Replace each call with `offSessionPaymentAsync` plus a `getCharge` poll or an `EVENT_TYPE_CHARGE_SUCCESS` / `EVENT_TYPE_CHARGE_FAIL` webhook handler — see [Migrating from 2.x](#migrating-from-2x).

#### Retry safety

The `idempotency_key` makes retries safe: submitting the same request with the same key returns the existing charge instead of creating a duplicate. Use a stable value tied to your order (e.g. `order-2024-001`). Keys are ASCII, 1–255 chars of `[A-Za-z0-9_-]`.

If you do not set the field, the SDK auto-fills it with a UUID so each call is still de-duplicated on accidental network retries — but a fresh UUID per call means an explicit retry from your side will create a new charge. Set the key yourself when you need retry control.

### Handling Charge Errors

Failed charges include error details with a Jamm-defined error code and a human-readable message. You can check for errors on any charge returned by `getCharge` or `getCharges`:

```java
import com.api.v1.ChargeError;
import com.api.v1.ChargeStatus;

GetChargeResponse response = client.payments().getCharge(chargeId);
ChargeResult charge = response.getCharge();

if (charge.getChargeStatus() == ChargeStatus.CHARGE_STATUS_FAILURE && charge.hasError()) {
    ChargeError error = charge.getError();
    System.out.println("Error code: " + error.getCode());     // e.g. "ERROR_TYPE_PAYMENT_CHARGE_OVER_LIMIT"
    System.out.println("Error message: " + error.getMessage()); // e.g. "The payment charge exceeds the allowed limit."
}
```

### Refund

Refund a charge. If the same-day cancellation window has not passed, cancels the charge directly. Otherwise, creates a bank transfer refund request. The result is delivered asynchronously via the `refund_succeeded` webhook. You can use `getCharge` to retrieve the latest refund status.

```java
// Full refund
RefundResponse response = client.payments().refund(
    RefundRequest.newBuilder()
        .setChargeId("trx-xxxxxxxx")
        .build());

// Partial refund (amount in JPY)
RefundResponse partialResponse = client.payments().refund(
    RefundRequest.newBuilder()
        .setChargeId("trx-xxxxxxxx")
        .setAmount(500)
        .build());

String refundId = response.getRefundId();

// Poll refund status via GetCharge
GetChargeResponse charge = client.payments().getCharge("trx-xxxxxxxx");
```

### Webhook Verification

```java
import com.jamm.webhook.Webhook;
import com.jamm.webhook.WebhookEvent;
import com.api.v1.ChargeMessage;
import com.api.v1.EventType;

// Parse and verify an incoming webhook
String jsonBody = request.getBody(); // from your HTTP handler

// verifyAndParseEvent checks the HMAC signature over the exact received bytes, then parses and
// returns the event_type together with the content. Prefer it over calling verify/parse
// separately: it cannot be accidentally skipped, and it is not broken by JSON re-serialization.
WebhookEvent event = Webhook.verifyAndParseEvent(jsonBody, clientSecret);

// event_type is the reliable way to tell charge success from fail, and charge from refund events
// (both deserialize to a ChargeMessage).
EventType eventType = event.getEventType();
switch (eventType) {
    case EVENT_TYPE_CHARGE_SUCCESS: {
        ChargeMessage charge = (ChargeMessage) event.getContent();
        // Which API triggered the charge. Charges created before this field was introduced omit
        // it, so guard with hasApiSource() rather than treating UNSPECIFIED as a real value.
        if (charge.hasApiSource()) {
            switch (charge.getApiSource()) {
                case API_SOURCE_OFF_SESSION_SYNC:  break; // synchronous off-session API (not this SDK)
                case API_SOURCE_OFF_SESSION_ASYNC: break; // OffSessionPaymentAsync
                case API_SOURCE_ON_SESSION:        break; // OnSessionPayment
                default: break;                           // API_SOURCE_UNSPECIFIED
            }
        }
        break;
    }
    case EVENT_TYPE_CHARGE_FAIL: {
        ChargeMessage charge = (ChargeMessage) event.getContent();
        if (charge.hasError()) {
            System.out.println("Error code: " + charge.getError().getCode());
            System.out.println("Error message: " + charge.getError().getMessage());
        }
        break;
    }
    case EVENT_TYPE_REFUND_SUCCEEDED: {
        // Transaction fields are flattened onto the charge; refund details live on getRefund().
        ChargeMessage charge = (ChargeMessage) event.getContent();
        if (charge.hasRefundId()) {
            System.out.println("Refund ID: " + charge.getRefundId()); // rfd-...
        }
        System.out.println("Amount refunded: " + charge.getRefund().getAmountRefunded());
        break;
    }
    case EVENT_TYPE_REFUND_FAILED: {
        ChargeMessage charge = (ChargeMessage) event.getContent();
        // A refund that fails at the cancellation step carries no id, and no amount was moved —
        // report the error rather than an amount of 0.
        if (charge.hasRefundId()) {
            System.out.println("Refund ID: " + charge.getRefundId());
        }
        if (charge.getRefund().hasError()) {
            System.out.println("Refund error: " + charge.getRefund().getError().getMessage());
        }
        break;
    }
    default:
        break;
}
```

The refund identifier on a webhook is `RefundInfo.getId()`, reached via `charge.getRefund()`. On the
nested `{ transaction, refund }` payloads the API sends today, the same value is also lifted onto the
flat `charge.getRefundId()` attribute, so either accessor works. **`charge.getRefundId()` is the one
to read if you want a single accessor**: legacy flat payloads set only that field and never build a
`RefundInfo`, so `getRefund().getId()` is empty on them.

Both are distinct from the `RefundResponse.getRefundId()` returned by `payments().refund(...)` shown
earlier: that is the API's response object, not the webhook's. To tie a refund webhook back to your
own records, match on the charge/transaction id rather than on the refund id.

The refund id is **not always present on `EVENT_TYPE_REFUND_FAILED`**: a refund request that was
declined carries it alongside the error, while a refund that fails at the cancellation step carries
only the error. Every `EVENT_TYPE_REFUND_SUCCEEDED` payload observed in testing carries the id, but
the field is optional on the wire in both cases, so the example guards it either way. Call
`hasRefundId()` — or `hasId()` on the nested `RefundInfo` — before treating it as an identifier.

The two enums used above are generated protobuf types:

| accessor | type | notes |
| --- | --- | --- |
| `event.getEventType()` | `com.api.v1.EventType` | top-level enum; import it to reference the constants outside a `switch` |
| `charge.getApiSource()` | `com.api.v1.ChargeMessage.ApiSource` | **nested** inside `ChargeMessage` — there is no top-level `ApiSource` class |

`ApiSource` values: `API_SOURCE_UNSPECIFIED`, `API_SOURCE_OFF_SESSION_SYNC`,
`API_SOURCE_OFF_SESSION_ASYNC`, `API_SOURCE_ON_SESSION`. Availability is not continuous: the field
shipped in 1.5.0, is **absent from 1.6.0 and 1.6.1**, and returns in 2.0.0 — so use **2.0.0 or
newer**.

Java enum `case` labels are unqualified, so the `case` constants themselves need no import.
Referring to either enum by name does — as the example does above with `EventType eventType = …`,
and as any `==` comparison would: `EventType.EVENT_TYPE_CHARGE_SUCCESS`, or
`ChargeMessage.ApiSource.API_SOURCE_ON_SESSION`.

> `Webhook.verifyAndParse(...)` (returning only the content `Object`) is still available for
> backward compatibility.

## Platform Mode

For platform partners managing multiple merchants, initialize the SDK in platform mode. See the [Platform Onboarding Guide](https://docs.jamm-pay.jp/docs/platform-onboarding-guide) for setup details.

```java
// Initialize in platform mode
JammClient client = JammClient.builder()
        .clientId("<your platform client id>")
        .clientSecret("<your platform client secret>")
        .environment(Environment.PRODUCTION)
        .platform(true)
        .build();
```

All service methods accept an optional `merchant` parameter to operate on behalf of a connected merchant:

```java
// Create a customer on behalf of a merchant
MerchantCustomer customer = client.customers().create(
    CreateCustomerRequest.newBuilder()
        .setBuyer(Buyer.newBuilder()
            .setEmail("customer@example.com")
            .setName("Taro Yamada")
            .build())
        .build(),
    "mer-abc123");

// Create a payment with platform fee
OnSessionPaymentResponse payment = client.payments().onSessionPayment(
    OnSessionPaymentRequest.newBuilder()
        .setCharge(InitialCharge.newBuilder()
            .setPrice(10000)
            .setDescription("Order #1234")
            .setPlatformFee(500)
            .build())
        .setRedirect(URL.newBuilder()
            .setSuccessUrl("https://yoursite.com/success")
            .setFailureUrl("https://yoursite.com/failure")
            .build())
        .setBuyer(Buyer.newBuilder()
            .setEmail("customer@example.com")
            .build())
        .build(),
    "mer-abc123");

// Get a charge on behalf of a merchant
GetChargeResponse charge = client.payments().getCharge("trx-xxxxxxxx", "mer-abc123");

// Refund on behalf of a merchant
RefundResponse refund = client.payments().refund(
    RefundRequest.newBuilder()
        .setChargeId("trx-xxxxxxxx")
        .build(),
    "mer-abc123");

// Ping on behalf of a merchant
PingResponse ping = client.healthcheck().ping("mer-abc123");
```

Or using the global configuration:

```java
Jamm.configure("client-id", "client-secret", Environment.PRODUCTION, true);
JammClient client = Jamm.getClient();
```

## Migrating from 2.x

`offSessionPayment` (the synchronous off-session charge) was removed in 3.0.0. The SDK now supports
off-session charges through `offSessionPaymentAsync` only.

The async call returns as soon as the charge is accepted, so the charge result is no longer available
on the response. Read it from `getCharge`, or handle the charge webhook.

2.x — the charge was settled by the time the call returned:

```java
OffSessionPaymentResponse response = client.payments().offSessionPayment(
    OffSessionPaymentRequest.newBuilder()
        .setCustomer("cus-xxxxxxxx")
        .setCharge(InitialCharge.newBuilder().setPrice(5000).setDescription("Monthly subscription").build())
        .build());

boolean paid = response.getCharge().getPaid();
```

3.0.0 — the call returns while the charge is still pending, so the result has to be waited for.
**Do not read `getPaid()` from a single immediate `getCharge`** — it will be `false` for a charge that
settles a moment later. Use the `awaitCharge` poll from
[Waiting for the result](#waiting-for-the-result), and note the third outcome the 2.x code did not
have: *unresolved*.

```java
OffSessionPaymentAsyncResponse response = client.payments().offSessionPaymentAsync(
    OffSessionPaymentAsyncRequest.newBuilder()
        .setCustomer("cus-xxxxxxxx")
        .setCharge(InitialCharge.newBuilder().setPrice(5000).setDescription("Monthly subscription").build())
        .setIdempotencyKey("order-2024-001")
        .build());

ChargeResult charge = awaitCharge(client, response.getChargeId());
if (charge == null) {
    // Unresolved, NOT unpaid: still pending when the poll gave up. Leave the order open for the
    // charge webhook; retrying with the same idempotency_key reads this charge, not a new one.
    throw new IllegalStateException("charge " + response.getChargeId() + " unresolved");
}

boolean paid = charge.getPaid();
```

The synchronous call had two outcomes, paid or not. The async call has three, and collapsing
*unresolved* into *unpaid* is the one migration mistake that costs real money — it double-charges on
the retry, or refuses a customer whose charge succeeded. Handling
`EVENT_TYPE_CHARGE_SUCCESS` / `EVENT_TYPE_CHARGE_FAIL` avoids the question entirely and is the better
long-term integration — see [Webhook Verification](#webhook-verification).

Platform mode migrates the same way: `offSessionPayment(request, merchant)` becomes
`offSessionPaymentAsync(request, merchant)`.

`OffSessionPaymentRequest` and `OffSessionPaymentResponse` remain in the generated `com.api.v1`
package, but no client method accepts or returns them.

## Installation

### Maven

```xml
<dependency>
  <groupId>jp.jamm-pay</groupId>
  <artifactId>jamm-sdk</artifactId>
  <version>3.0.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'jp.jamm-pay:jamm-sdk:3.0.0'
```

The SDK is compiled to Java 8 bytecode, so it runs on Java 8 and any newer runtime (Java 11, 17, 21, …).

If you want to build from source:

```sh
make build
```

Vendoring dependencies by hand (no Maven/Gradle)? See [DEPENDENCIES.md](DEPENDENCIES.md) for the
complete 7-jar runtime set and versions.

## Configuration

```java
JammClient client = JammClient.builder()
        .clientId("your-client-id")
        .clientSecret("your-client-secret")
        .environment(Environment.PRODUCTION)  // PRODUCTION, STAGING, LOCAL, TESTING
        .build();
```

Or use the global configuration:

```java
import com.jamm.Jamm;

Jamm.configure("your-client-id", "your-client-secret", Environment.PRODUCTION);
JammClient client = Jamm.getClient();
```

## Error Handling

The Java SDK throws structured exceptions so you can handle API, authentication, and transport failures separately:

- `ApiException` for non-2xx API responses
- `OAuthException` for token/authentication failures
- `JammException` for network, parsing, or other SDK-level failures

```java
import com.api.v1.Customer;
import com.jamm.JammClient;
import com.jamm.config.Environment;
import com.jamm.errors.ApiException;
import com.jamm.errors.JammException;
import com.jamm.errors.OAuthException;

try (JammClient client = JammClient.builder()
        .clientId("<your client id>")
        .clientSecret("<your client secret>")
        .environment(Environment.STAGING)
        .build()) {

    Customer customer = client.customers().get("cus-xxxxxxxx");

} catch (ApiException e) {
    System.err.println("API error: " + e.getErrorName());
    System.err.println("HTTP status: " + e.getHttpStatus());
    System.err.println("Message: " + e.getMessage());
    System.err.println("Error type: " + e.getErrorType());
    System.err.println("Request ID: " + e.getRequestId());
} catch (OAuthException e) {
    System.err.println("Authentication failed: " + e.getMessage());
} catch (JammException e) {
    System.err.println("SDK error: " + e.getMessage());
}
```

## Development

This SDK is published from the `jamm-pay/java-sdk` repository.

### Requirements

These are for **building the SDK from source**. Consuming the published SDK only requires Java 8+ (see [Installation](#installation)).

- Docker — all Maven builds and tests run in containers, so no host JDK is required. Building/linting use JDK 25; tests run on JDK 8 to exercise the SDK in the actual merchant runtime.

### Available Commands

Commands run via Docker: `make build`/`lint`/`package`/`publish`/`javadoc` use `maven:3.9-eclipse-temurin-25`; `make test`/`e2e` use `maven:3.9-eclipse-temurin-8` (the Java 8 merchant runtime).

```sh
make install       # Download dependencies
make build         # Build the project (skip tests)
make test          # Run unit tests
make e2e           # Run E2E tests (requires MERCHANT_CLIENT_ID and MERCHANT_CLIENT_SECRET)
make clean         # Clean build artifacts
make lint          # Run checkstyle
make check         # Run lint and tests
make package       # Package JAR with sources and javadoc
make publish       # Deploy to Maven Central
make javadoc       # Generate Javadoc
```

### Running Tests

Unit tests:

```sh
make test
```

End-to-end tests:

```sh
make e2e MERCHANT_CLIENT_ID=*** MERCHANT_CLIENT_SECRET=*** ENV=develop
```

Some E2E tests require additional environment variables and will be skipped if not set:

| Variable   | Description                              | Example          |
|------------|------------------------------------------|------------------|
| `CUSTOMER` | Customer ID for off-session payment test | `cus-xxxxxxxx`   |
| `CHARGE`   | Charge ID for refund test                | `trx-xxxxxxxx`   |

### Built With

[![Java][Java.com]][Java-url]

[license-shield]: https://img.shields.io/badge/license-MIT-blue?style=for-the-badge
[license-url]: https://github.com/jamm-pay/java-sdk/blob/main/LICENSE
[maven-central-shield]: https://img.shields.io/badge/maven%20central-jp.jamm--pay%3Ajamm--sdk-brightgreen?style=for-the-badge
[maven-central-url]: https://central.sonatype.com/artifact/jp.jamm-pay/jamm-sdk/overview
[Java.com]: https://img.shields.io/badge/java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://openjdk.org
