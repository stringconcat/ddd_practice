#!/bin/bash
set -e
currentDir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
rootDir="$currentDir/../"

(cd "$rootDir" && exec ./gradlew -p tests/performance clean gatlingRun\
 -DstartDocker=false -DenvFile=`pwd`/docker/env/local.env)