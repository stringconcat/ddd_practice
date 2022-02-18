#!/bin/bash
set -e
currentDir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
rootDir="$currentDir/../../"

echo [Install git hooks] installing git hooks to project repo...
(cd "$rootDir" && cp ./tools/git-hooks/* ./.git/hooks)
echo [Install git hooks] git hooks sucessfully installed