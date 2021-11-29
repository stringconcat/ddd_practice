#!/bin/bash
set -e
currentDir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
rootDir="$currentDir/../"

(cd "$rootDir" && exec ./scripts/stop.sh)
(cd "$rootDir" && exec docker-compose rm -f)