#!/bin/bash
set -e
currentDir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
rootDir="$currentDir/../"

(cd "$rootDir" && exec docker-compose -f ./docker/docker-compose.yml --env-file \
        ./docker/env/local.env --project-name=ddd_local --profile local down -v)
(cd "$rootDir" && exec docker-compose -f ./docker/docker-compose.yml --env-file \
        ./docker/env/local.env --project-name=ddd_local --profile local rm -f)