#!/bin/bash
set -e
currentDir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
rootDir="$currentDir/../"

(cd "$rootDir" && exec docker-compose down -v)
(cd "$rootDir" && exec docker-compose rm -f)