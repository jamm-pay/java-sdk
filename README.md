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
                .setPhone("010-1234-5678")
                .setBirthDate("2000-01-01")
                .build())
            .build());
}
```

### Off-Session Payment

Once a customer has approved the payment and completed onboarding (KYC, terms of service, payment method setup), you can charge them directly:

```java
// Charge an existing customer
OffSessionPaymentResponse response = client.payments().offSessionPayment(
    OffSessionPaymentRequest.newBuilder()
        .setCustomer("cus-xxxxxxxx")
        .setCharge(InitialCharge.newBuilder()
            .setPrice(5000)
            .setDescription("Monthly subscription")
            .putMetadata("orderId", "order-123")
            .build())
        .build());

// Access the charge result
ChargeResult charge = response.getCharge();
```

### Off-Session Payment (Async)

You can also start an asynchronous off-session payment and use the returned `charge_id` to poll charge status:

```java
OffSessionPaymentAsyncResponse asyncResponse = client.payments().offSessionPaymentAsync(
    OffSessionPaymentAsyncRequest.newBuilder()
        .setCustomer("cus-xxxxxxxx")
        .setCharge(InitialCharge.newBuilder()
            .setPrice(5000)
            .setDescription("Monthly subscription async")
            .build())
        .setIdempotencyKey("order-2024-001")
        .build());

String requestId = asyncResponse.getRequestId();
String chargeId = asyncResponse.getChargeId();

GetChargeResponse charge = client.payments().getCharge(chargeId);
```

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
import com.api.v1.ChargeMessage;

// Parse and verify an incoming webhook
String jsonBody = request.getBody(); // from your HTTP handler

// Verify the signature
ObjectMapper mapper = new ObjectMapper();
JsonNode payload = mapper.readTree(jsonBody);
String signature = payload.get("signature").asText();
String contentJson = payload.get("content").toString();
Webhook.verify(contentJson, signature, clientSecret);

// Parse the message
Object content = Webhook.parse(jsonBody);
if (content instanceof ChargeMessage) {
    ChargeMessage charge = (ChargeMessage) content;
    // Handle charge event...

    // For EVENT_TYPE_CHARGE_FAIL webhooks, access error details:
    if (charge.hasError()) {
        System.out.println("Error code: " + charge.getError().getCode());
        System.out.println("Error message: " + charge.getError().getMessage());
    }
}
```

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

## Installation

### Maven

```xml
<dependency>
  <groupId>jp.jamm-pay</groupId>
  <artifactId>jamm-sdk</artifactId>
  <version>1.4.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'jp.jamm-pay:jamm-sdk:1.4.0'
```

If you want to build from source:

```sh
make build
```

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

- Java 11 or above
- Docker (for running Maven builds and tests)

### Available Commands

All commands run via Docker using `maven:3.9-eclipse-temurin-25`:

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
