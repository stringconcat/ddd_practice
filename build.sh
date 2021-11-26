#!/bin/sh
./gradlew build -PallWarningsAsErrors=true
./application/buildImage.sh