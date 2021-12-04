set -e

imageTag=$1

if [ -z "$1" ]
  then
    echo 'No imageTag provided. Latest will be used.'
    imageTag=latest
fi

imageFullName=restaurant/mock-server:$imageTag

echo [Mock Server STARTING] building "$imageFullName"...

echo [Mock Server] remove old image "$imageFullName"...
(docker rmi -f "$imageFullName")

echo [Mock Server] creating docker image "$imageFullName"...
(docker build -t "$imageFullName" "${BASH_SOURCE%/*}")

echo [Mock Server FINISHED] image "$imageFullName" has been built