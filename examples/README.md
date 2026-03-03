# Java SDK Examples

These examples mirror the Ruby and Node SDK samples and use the Java SDK's current API surface.

## Prerequisites

Set environment variables before running examples:

```bash
export ENV=develop
export MERCHANT_CLIENT_ID=***
export MERCHANT_CLIENT_SECRET=***

# Required by selected examples
export CUSTOMER=cus-***
export CHARGE=trx-***
export CHARGE_FULL_REFUND=trx-***
export CHARGE_PARTIAL_REFUND=trx-***
export PARTIAL_REFUND_AMOUNT=50
export EMAIL=java.test@jamm-pay.jp

# Optional: only needed when using RefundExample for a partial refund
export AMOUNT=50
```

## Available examples

- `QuickstartExample.java`
- `HealthcheckPingExample.java`
- `CustomerCreateExample.java`
- `CustomerGetExample.java`
- `CustomerGetContractExample.java`
- `CustomerUpdateExample.java`
- `CustomerDeleteExample.java`
- `PaymentOnSessionExample.java` - legacy existing-customer on-session charge example
- `PaymentOnSessionContractWithChargeExample.java`
- `PaymentOnSessionContractWithoutChargeExample.java`
- `PaymentOnSessionAddChargeExample.java`
- `PaymentOnSessionOneTimePaymentExample.java`
- `PaymentOffSessionExample.java` - direct off-session charge for an existing customer
- `PaymentOffSessionAsyncExample.java` - asynchronous off-session charge plus follow-up `getCharge`
- `ChargeGetExample.java`
- `ChargeListExample.java`
- `RefundExample.java` - full refund by default, or partial refund when `AMOUNT` is set
- `RefundFullExample.java`
- `RefundPartialExample.java`

## Build and run

From `packages/sdk/java`:

```bash
mvn -q -DskipTests package
mvn -q -DincludeScope=runtime -Dmdep.outputFile=target/classpath.txt dependency:build-classpath
javac -cp "target/classes:$(cat target/classpath.txt)" examples/*.java -d target/examples
java -cp "target/examples:target/classes:$(cat target/classpath.txt)" QuickstartExample
```

Replace `QuickstartExample` with any class name above (for example `PaymentOnSessionExample`).
