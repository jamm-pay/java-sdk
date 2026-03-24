.PHONY: install build test clean publish e2e lint package install-local javadoc check check-version-sync examples-build example help

# Docker configuration
DOCKER_IMAGE := maven:3.9-eclipse-temurin-11
WORKDIR := /app
EXAMPLE ?= QuickstartExample

# Run Maven via Docker
# Mount current directory and Maven cache for faster builds
MVN := docker run --rm \
	-v "$(shell pwd)":/app \
	-v "$(HOME)/.m2":/root/.m2 \
	-w $(WORKDIR) \
	$(DOCKER_IMAGE) \
	mvn

# Default target
all: build

# Install dependencies
install:
	$(MVN) dependency:resolve

# Build the project
build:
	$(MVN) clean package -DskipTests

# Run unit tests
test:
	$(MVN) test

# Run end-to-end tests (requires environment variables)
e2e:
	docker run --rm \
		-v "$(shell pwd)":/app \
		-v "$(HOME)/.m2":/root/.m2 \
		-w $(WORKDIR) \
		-e ENV=$(ENV) \
		-e MERCHANT_CLIENT_ID=$(MERCHANT_CLIENT_ID) \
		-e MERCHANT_CLIENT_SECRET=$(MERCHANT_CLIENT_SECRET) \
		-e CUSTOMER=$(CUSTOMER) \
		-e CHARGE_FULL_REFUND=$(CHARGE_FULL_REFUND) \
		-e CHARGE_PARTIAL_REFUND=$(CHARGE_PARTIAL_REFUND) \
		-e PARTIAL_REFUND_AMOUNT=$(PARTIAL_REFUND_AMOUNT) \
		$(DOCKER_IMAGE) \
		mvn test -Dtest=*E2ETest

# Clean build artifacts
clean:
	$(MVN) clean

# Run linting/checkstyle
lint:
	$(MVN) checkstyle:check

# Package for release (includes sources and javadoc)
package:
	$(MVN) clean package

# Deploy to Maven Central (requires GPG setup and credentials)
publish:
	$(MVN) clean deploy -P release

# Install to local Maven repository
install-local:
	$(MVN) clean install -DskipTests

# Generate Javadoc
javadoc:
	$(MVN) javadoc:javadoc

# Verify README versions match pom.xml
check-version-sync:
	@bash scripts/check-version-sync.sh

# Run all checks before committing
check: lint test check-version-sync

# Compile Java examples under examples/ into target/examples
examples-build:
	docker run --rm \
		-v "$(shell pwd)":/app \
		-v "$(HOME)/.m2":/root/.m2 \
		-w $(WORKDIR) \
		$(DOCKER_IMAGE) \
		sh -lc 'mvn -q -DskipTests package && \
			mvn -q -DincludeScope=runtime -Dmdep.outputFile=target/classpath.txt dependency:build-classpath && \
			javac -cp "target/classes:$$(cat target/classpath.txt)" examples/*.java -d target/examples'

# Run one example class, defaulting to QuickstartExample.
# Usage: make example EXAMPLE=PaymentOnSessionExample
example: examples-build
	docker run --rm \
		-v "$(shell pwd)":/app \
		-v "$(HOME)/.m2":/root/.m2 \
		-w $(WORKDIR) \
		-e ENV=$(ENV) \
		-e MERCHANT_CLIENT_ID=$(MERCHANT_CLIENT_ID) \
		-e MERCHANT_CLIENT_SECRET=$(MERCHANT_CLIENT_SECRET) \
		-e CUSTOMER=$(CUSTOMER) \
		-e CHARGE=$(CHARGE) \
		-e AMOUNT=$(AMOUNT) \
		-e CHARGE_FULL_REFUND=$(CHARGE_FULL_REFUND) \
		-e CHARGE_PARTIAL_REFUND=$(CHARGE_PARTIAL_REFUND) \
		-e PARTIAL_REFUND_AMOUNT=$(PARTIAL_REFUND_AMOUNT) \
		-e EMAIL=$(EMAIL) \
		$(DOCKER_IMAGE) \
		sh -lc 'java -cp "target/examples:target/classes:$$(cat target/classpath.txt)" $(EXAMPLE)'

# Display help
help:
	@echo "Available targets:"
	@echo "  install       - Download dependencies"
	@echo "  build         - Build the project (skip tests)"
	@echo "  test          - Run unit tests"
	@echo "  e2e           - Run end-to-end tests (requires MERCHANT_CLIENT_ID and MERCHANT_CLIENT_SECRET, optional ENV)"
	@echo "  clean         - Clean build artifacts"
	@echo "  lint          - Run checkstyle"
	@echo "  package       - Package JAR with sources and javadoc"
	@echo "  publish       - Deploy to Maven Central"
	@echo "  install-local - Install to local Maven repository"
	@echo "  javadoc       - Generate Javadoc"
	@echo "  check         - Run lint and tests"
	@echo "  examples-build - Compile all examples in examples/"
	@echo "  example       - Run one example (EXAMPLE=QuickstartExample by default)"
	@echo ""
	@echo "Example usage:"
	@echo "  make example EXAMPLE=QuickstartExample ENV=develop MERCHANT_CLIENT_ID=*** MERCHANT_CLIENT_SECRET=*** EMAIL=you@example.com"
	@echo "  make example EXAMPLE=PaymentOnSessionAddChargeExample ENV=develop MERCHANT_CLIENT_ID=*** MERCHANT_CLIENT_SECRET=*** CUSTOMER=cus-***"
	@echo "  make example EXAMPLE=RefundExample ENV=develop MERCHANT_CLIENT_ID=*** MERCHANT_CLIENT_SECRET=*** CHARGE=trx-***"
	@echo "  make example EXAMPLE=RefundExample ENV=develop MERCHANT_CLIENT_ID=*** MERCHANT_CLIENT_SECRET=*** CHARGE=trx-*** AMOUNT=50"
	@echo "  make example EXAMPLE=RefundPartialExample ENV=develop MERCHANT_CLIENT_ID=*** MERCHANT_CLIENT_SECRET=*** CHARGE_PARTIAL_REFUND=trx-*** PARTIAL_REFUND_AMOUNT=50"
	@echo ""
	@echo "All commands run via Docker using $(DOCKER_IMAGE)"
