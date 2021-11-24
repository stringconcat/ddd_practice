#!/bin/bash
set -e

echo [Install git hooks] installing git hooks to project repo...
cp ./git-hooks/* ./.git/hooks
echo [Install git hooks] git hooks sucessfully installed