set -e

imageTag=$1

if [ -z "$1" ]
  then
    echo 'No imageTag provided. Latest will be used.'
    imageTag=latest
fi

imageFullName=restaurant/kitchen:$imageTag

echo [Kitchen STARTING] building "$imageFullName"...

echo [Kitchen] remove old image "$imageFullName"...
(docker rmi -f "$imageFullName")

echo [Kitchen] creating docker image "$imageFullName"...
(docker build -t "$imageFullName" "${BASH_SOURCE%/*}")

echo [Kitchen FINISHED] image "$imageFullName" has been built