#!/bin/bash
set -e
currentDir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
rootDir="$currentDir/../"

(cd "$rootDir" && exec ./scripts/build.sh)
(cd "$rootDir" && exec ./shop/application/buildImage.sh)
(cd "$rootDir" && exec ./kitchen/application/buildImage.sh)
(cd "$rootDir" && exec ./tests/mock-server/buildImage.sh)
(cd "$rootDir" && exec ./gradlew -p tests/e2e test allureReport  --continue --clean)