set -e

imageTag=$1

if [ -z "$1" ]
  then
    echo 'No imageTag provided. Latest will be used.'
    imageTag=latest
fi

imageFullName=restaurant/shop:$imageTag

echo [Shop STARTING] building "$imageFullName"...

echo [Shop] remove old image "$imageFullName"...
(docker rmi -f "$imageFullName")

echo [Shop] creating docker image "$imageFullName"...
(docker build -t "$imageFullName" "${BASH_SOURCE%/*}")

echo [Shop FINISHED] image "$imageFullName" has been built