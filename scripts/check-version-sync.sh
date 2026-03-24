#!/usr/bin/env bash
# Verify that the version in pom.xml matches the version in README.md installation examples.
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

POM_VERSION=$(mvn -f "$PROJECT_DIR/pom.xml" help:evaluate -Dexpression=project.version -q -DforceStdout)
README="$PROJECT_DIR/README.md"

errors=0

# Check Maven example
if ! grep -qF "<version>${POM_VERSION}</version>" "$README"; then
  echo "ERROR: README.md Maven example does not match pom.xml version ${POM_VERSION}"
  errors=$((errors + 1))
fi

# Check Gradle example
if ! grep -qF "implementation 'jp.jamm-pay:jamm-sdk:${POM_VERSION}'" "$README"; then
  echo "ERROR: README.md Gradle example does not match pom.xml version ${POM_VERSION}"
  errors=$((errors + 1))
fi

if [ "$errors" -gt 0 ]; then
  echo "FAIL: README.md version(s) out of sync with pom.xml (${POM_VERSION})"
  exit 1
fi

echo "OK: README.md versions match pom.xml (${POM_VERSION})"
