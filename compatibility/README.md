# Java SDK backward-compatibility tests

This directory verifies that previously **published** versions of the
`jp.jamm-pay:jamm-sdk` artifact still work against the current API — i.e. that
the SDKs and the backend stay in sync. The pre-release/latest SDK lives in
`../src` and is covered by `../src/test` + `*E2ETest`; this directory covers the
**released** versions pulled from Maven Central.

## Layout

```
packages/sdk/
  compatibility/
    webhooks/          # language-neutral backend webhook records; shared by every SDK harness
  java/compatibility/
    shared/
      src/test/java/com/jamm/compat/CompatSuite.java  # the shared smoke suite (single source of truth)
    templates/
      pom.xml.tmpl     # source template for each version directory
    <version>/
      pom.xml          # pins jamm-sdk@<version> + adds ../shared as a test source
    Makefile
```

Each `<version>/` directory is a tiny Maven project whose `pom.xml` pins
`jamm-sdk` to one published version and adds the shared suite
(`../shared/src/test/java`) as a test source via `build-helper-maven-plugin`.
Maven therefore compiles and runs the **same** assertions against every pinned
version's jar. Depending on `jamm-sdk` in the version `pom.xml` — not in the
shared suite — is what makes Maven resolve it to that directory's version.

Per-version `target/` output is git-ignored on purpose: these builds pull real
published versions from the registry and are out of scope for our vulnerability
checks.

Unlike the Node/Ruby/PHP harnesses — whose dynamic runtimes let one suite run
against every version and reflectively capability-gate each call — Java binds at
compile time, so the version directories share this source rather than importing
it at runtime. Across the pinned range the surface the suite touches
(`Jamm.configure`, `Webhook.verify`, `Webhook.parse`, `healthcheck().ping()`) is
signature-stable, so the suite compiles directly against each jar; the only
runtime gate is the live healthcheck, skipped unless `MERCHANT_CLIENT_*` are set.

## What is tested

| Check                          | Touches API | Notes                                                          |
| ------------------------------ | ----------- | -------------------------------------------------------------- |
| `Jamm.configure` + environment | no          | the `"local"` env string round-trips to `https://api.jamm.test` |
| `healthcheck().ping()`         | yes         | skipped unless `MERCHANT_CLIENT_*` are set                     |
| `Webhook.verify`               | no          | recomputes the HMAC signature; asserts the contract            |
| `Webhook.parse`                | no          | forward-compat: parses backend records carrying newer fields (see below) |

### Forward-compatibility: `Webhook.parse` against current-day records

`../../compatibility/webhooks/*.json` are webhook payloads shaped as the backend
sends them, covering both shapes the harness cares about:

- `charge_success_api_source.json` — a flat charge carrying `api_source`
  (`ChargeMessage` field 23). The backend marshals webhook content with Go's
  `json.Marshal` (not `protojson`), so the enum goes out as its numeric value
  (`"api_source": 3`); there is no enum-string form on the wire.
- `charge_success_without_api_source.json` — the same flat charge **without**
  `api_source` (the reverted backend; see the note below).
- `refund_succeeded_nested_api_source.json` — the nested refund wrapper
  (`content.transaction` + `content.refund`) **with** `api_source`.
- `refund_succeeded_nested_no_api_source.json` — the same nested wrapper
  **without** `api_source`.

The suite asserts every version **must** decode the core `ChargeMessage`
(`id`, `customer`) and ignore fields it predates. A version that *throws* on
these records **fails** the test — that failure is the signal it is out of sync
with the API, not an accepted outcome (mirrors the Node/Ruby harnesses).

Beyond the `api_source` axis, the parse test also covers the remaining backend
events that decode to a `ChargeMessage` — the charge lifecycle events and the
`Error`-bearing `charge_fail_error.json` / `refund_failed_nested_error.json` (the
only records exercising the proto `Error` decode path) — plus a `ContractMessage`
check for `contract_activated.json` and every `ChargeMessage.Status` enum value
(0–6) injected into a charge record. `user_account_deleted.json` is **not** covered
here: `UserAccountMessage` may postdate some pinned versions in this range, and a
direct type reference to a class absent from an older jar would break that version's
whole compilation (this suite binds at compile time). It stays covered by the Node
harness. See `../../compatibility/webhooks/README.md`.

> [!IMPORTANT]
> The `api_source` / nested-refund webhook feature was **rolled back on `main`**
> for release (PR #2316). These fixtures are retained deliberately — covering
> both the with- and without-`api_source` shapes — so this harness is the
> ready-made signal for which published versions tolerate the contract once it
> re-lands.

Verified locally against the installed jars (the authoritative matrix is
whatever `make report` reports):

| version        | flat charge (± `api_source`) | nested refund wrapper                                         |
| -------------- | ---------------------------- | ------------------------------------------------------------- |
| 1.1.3          | ✅ parses (unknown fields ignored) | ❌ `EVENT_TYPE_REFUND_SUCCEEDED` unknown (renamed in 1.2.0)   |
| 1.2.0 – 1.4.1  | ✅ parses                     | ❌ wrapper not flattened → core fields empty (flatten landed in 1.5.0) |
| 1.5.0 – 1.6.1  | ✅ parses                     | ✅ flattens `transaction`/`refund` onto the `ChargeMessage`   |

The actionable finding: **no published version before `1.5.0` handles the nested
refund webhook wrapper.** Flat charges — even carrying `api_source` — parse on
every version, because the Java SDK's charge parse has always used
`ignoringUnknownFields()`. Because these versions are published and immutable,
CI runs the report as an informational PR comment rather than a merge blocker.

## Running

Everything runs in the same pinned Maven image as the parent SDK Makefile, so no
host JDK is required. The live `healthcheck` check needs credentials and a
reachable backend; without them it is skipped, so the offline checks still run.

```sh
# from packages/sdk/java/compatibility
make install                 # resolve all pinned versions into the ~/.m2 cache
make test                    # run the suite against every version (offline; healthcheck skipped)

# with a reachable backend (e.g. the develop cluster):
make test \
  ENV=develop \
  MERCHANT_CLIENT_ID=...  \
  MERCHANT_CLIENT_SECRET=...
```

`make report` is the CI variant: it writes per-version `PASS`/`FAIL` to
`compat-report.tsv` and always exits 0.

## Pinned versions

The latest 12 versions published to Maven Central (the installable source of
truth): `1.1.3, 1.2.0, 1.2.1, 1.2.2, 1.3.0, 1.4.0, 1.4.1, 1.5.0, 1.5.1, 1.5.2,
1.6.0, 1.6.1`.

To add a newly released version:

```sh
make add VER=1.6.0           # scaffolds 1.6.0/ from templates/
make install && make test
```
