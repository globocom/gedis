.PHONY: test build compile snapshot release ci

SBT_CLIENT := $(shell which sbt)

test:
	@$(SBT_CLIENT) clean test

build:
	@$(SBT_CLIENT) clean compile package

compile:
	@$(SBT_CLIENT) clean compile

snapshot:
	@$(SBT_CLIENT) publishSigned

release:
	@$(SBT_CLIENT) sonatypeRelease

ci: clean coverage test coverageReport scapegoat
