FROM bellsoft/liberica-openjre-alpine:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} shop-application.jar
ENTRYPOINT ["java","-jar","shop-application.jar"]