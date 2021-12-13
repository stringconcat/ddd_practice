#!/bin/bash
set -e
currentDir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
rootDir="$currentDir/../"

(cd "$rootDir" && exec docker-compose -f ./docker/docker-compose.yml \
--env-file ./docker/env/debug.env --project-name=ddd_debug stop)