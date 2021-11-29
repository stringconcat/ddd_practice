#!/bin/bash
set -e
currentDir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
rootDir="$currentDir/../"

(cd "$rootDir" && exec ./scripts/build.sh)
(cd "$rootDir" && exec ./application/buildImage.sh)
(cd "$rootDir" && exec docker-compose up -d)