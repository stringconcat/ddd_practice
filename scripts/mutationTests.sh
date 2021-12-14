#!/bin/bash
set -e
currentDir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
rootDir="$currentDir/../"

(cd "$rootDir" && exec ./gradlew ":shop:domain:pitest" ":shop:usecase:pitest" ":kitchen:usecase:pitest" ":kitchen:domain:pitest")
