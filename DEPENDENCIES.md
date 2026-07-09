# Dependencies

The Jamm Java SDK calls the Jamm REST API over the JDK's built-in
`java.net.HttpURLConnection`; request/response types are Protobuf, serialized to/from JSON.

It is intentionally lean for integrators who **vendor dependencies by hand** (no Maven/Gradle
resolution, flat classpath). There is **no HTTP or JSON library to reconcile** with your
application — the SDK shares no library with a typical host stack (it uses `HttpURLConnection`
rather than OkHttp/Apache HttpClient, and gson rather than Jackson/JSONIC).

## Runtime dependencies — the complete set (7 jars)

| Library | Version | Role |
|---|---|---|
| `com.google.protobuf:protobuf-java` | 4.34.1 | API request/response types (the API contract) |
| `com.google.protobuf:protobuf-java-util` | 4.34.1 | Protobuf ↔ JSON marshalling |
| `com.google.code.gson:gson` | 2.8.9 | JSON parsing for OAuth tokens, webhooks, and error bodies |
| `org.slf4j:slf4j-api` | 2.0.11 | Logging facade (no transitive deps) — you supply the binding |
| `javax.annotation:javax.annotation-api` | 1.3.2 | `@Generated` on generated types |
| `com.google.code.findbugs:jsr305` | 3.0.2 | Nullability annotations (transitive of `protobuf-java-util`) |
| `com.google.errorprone:error_prone_annotations` | 2.18.0 | Annotations (transitive of `protobuf-java-util`) |

That is the entire flat classpath. There is **no** `okhttp`/`okio`/`kotlin`, **no** Jackson, and
**no** `protovalidate` / `proto-google-common-protos` / Guava / CEL / ANTLR — earlier SDK versions
pulled those in; they are no longer required.

`slf4j-api` is only a facade. If your application already has an SLF4J binding (Logback, Log4j2,
`slf4j-simple`, …) the SDK logs through it; otherwise SLF4J is a no-op and you can omit a binding.

## Test-only dependencies (not vendored by consumers)

JUnit, Mockito, WireMock, and OkHttp's `mockwebserver` are `test`-scoped — used only to build and
test the SDK from source, never shipped or required at runtime.

## Regenerating the exact runtime classpath

To reproduce the list above for a given build (e.g. to fetch the jars):

```sh
mvn -DincludeScope=runtime -Dmdep.outputFile=classpath.txt dependency:build-classpath
```

Versions are pinned in [`pom.xml`](pom.xml); this document reflects SDK **1.6.0**.
