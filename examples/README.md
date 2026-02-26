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
export EMAIL=java.test@jamm-pay.jp
```

## Available examples

- `QuickstartExample.java`
- `HealthcheckPingExample.java`
- `CustomerGetExample.java`
- `CustomerGetContractExample.java`
- `CustomerUpdateExample.java`
- `CustomerDeleteExample.java`
- `PaymentOnSessionExample.java`
- `PaymentOffSessionExample.java`
- `PaymentOffSessionAsyncExample.java`
- `ChargeGetExample.java`
- `ChargeListExample.java`
- `RefundExample.java`

## Build and run

From `packages/sdk/java`:

```bash
mvn -q -DskipTests package
mvn -q -DincludeScope=runtime -Dmdep.outputFile=target/classpath.txt dependency:build-classpath
javac -cp "target/classes:$(cat target/classpath.txt)" examples/*.java -d target/examples
java -cp "target/examples:target/classes:$(cat target/classpath.txt)" QuickstartExample
```

Replace `QuickstartExample` with any class name above (for example `PaymentOnSessionExample`).
