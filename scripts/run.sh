#!/bin/bash
set -e
currentDir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
rootDir="$currentDir/../"

(cd "$rootDir" && exec ./scripts/build.sh)
(cd "$rootDir" && exec ./shop/application/buildImage.sh)
(cd "$rootDir" && exec ./kitchen/application/buildImage.sh)
(cd "$rootDir" && exec ./tests/mock-server/buildImage.sh)
(cd "$rootDir" && exec docker-compose up -d --remove-orphans)

echo 'Portainer GUI is available at http://localhost:9000/#/dashboard'
echo 'pgadmin is available at http://localhost:15432/ Login: restaurant@stringconcat.com:restaurant Database password is restaurant'
python -mwebbrowser http://localhost:9000/#/dashboard || true