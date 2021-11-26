set -e

imageTag=$1

if [ -z "$1" ]
  then
    echo 'No imageTag provided. Latest will be used.'
    imageTag=latest
fi

imageFullName=restaurant/mainapp:$imageTag

echo [Main App STARTING] building "$imageFullName"...

echo [Main App] remove old image "$imageFullName"...
(docker rmi -f "$imageFullName")

echo [Main App] creating docker image "$imageFullName"...
(docker build -t "$imageFullName" "${BASH_SOURCE%/*}")

echo [Main App FINISHED] image "$imageFullName" has been built