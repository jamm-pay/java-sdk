.PHONY: ls install test report clean add

# Maven runs in the same pinned image as the parent SDK Makefile so no host JDK is required.
DOCKER_IMAGE := maven:3.9-eclipse-temurin-25

# The shared webhooks lives at packages/sdk/compatibility/webhooks -- one level above java/ -- so
# mount packages/sdk (this dir's great-grandparent) at /app and work under java/compatibility. That
# keeps ../../../compatibility/webhooks reachable from every version dir, on host and in CI alike.
SDK_ROOT := $(shell cd ../.. && pwd)
WORKDIR  := /app/java/compatibility

# Pinned version directories -- everything except shared/ and templates/.
VERSIONS := $(filter-out shared/ templates/,$(wildcard */))

# Run Maven in Docker, mounting the SDK tree and the ~/.m2 cache (faster repeat runs). Credentials
# and ENV are forwarded so the live healthcheck can run when supplied; without them it is skipped.
DOCKER_RUN := docker run --rm \
	-v "$(SDK_ROOT)":/app \
	-v "$(HOME)/.m2":/root/.m2 \
	-w $(WORKDIR) \
	-e MERCHANT_CLIENT_ID=$(MERCHANT_CLIENT_ID) \
	-e MERCHANT_CLIENT_SECRET=$(MERCHANT_CLIENT_SECRET) \
	-e ENV=$(ENV) \
	$(DOCKER_IMAGE)

# List the version directories under test.
ls:
	@echo "Compat versions:" $(VERSIONS:/=)

# Resolve dependencies for each pinned version into the shared ~/.m2 cache.
install:
	@$(DOCKER_RUN) bash -c 'set -e; for d in $(VERSIONS); do \
		echo "== install $$d =="; \
		( cd $$d && mvn -q -B dependency:resolve ); \
	done'

# Run the shared suite against every pinned version. Runs all versions even if one fails, then
# exits non-zero if any did -- so a single broken version is visible without masking the rest.
test:
	@$(DOCKER_RUN) bash -c 'rc=0; for d in $(VERSIONS); do \
		echo "== test $$d =="; \
		( cd $$d && mvn -q -B test ) || rc=1; \
	done; exit $$rc'

# CI variant of `test`: runs every version, writes per-version status to compat-report.tsv
# ("<version>\t<PASS|FAIL>"), and always exits 0. The workflow turns the file into a PR comment,
# so the merge is not blocked when an older published version is out of sync with the current API.
report:
	@$(DOCKER_RUN) bash -c 'rm -f compat-report.tsv; for d in $(VERSIONS); do \
		v=$${d%/}; \
		echo "== test $$v =="; \
		if ( cd $$d && mvn -q -B test ); then status=PASS; else status=FAIL; fi; \
		printf "%s\t%s\n" "$$v" "$$status" >> compat-report.tsv; \
	done; echo "== summary =="; cat compat-report.tsv'

# Remove per-version Maven build output.
clean:
	@for d in $(VERSIONS); do rm -rf $$d/target; done
	@rm -f compat-report.tsv

# Scaffold a new version directory from the template: make add VER=1.6.0
add:
	@test -n "$(VER)" || { echo "usage: make add VER=x.y.z"; exit 1; }
	@mkdir -p "$(VER)"
	@sed 's/__VERSION__/$(VER)/g' templates/pom.xml.tmpl > "$(VER)/pom.xml"
	@echo "Created $(VER)/ -- run 'make install' then 'make test'."
