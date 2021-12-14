FROM bellsoft/liberica-openjre-alpine:17
ARG JAR_FILE=build/libs/mock-server.jar
COPY ${JAR_FILE} mock-server.jar
ENTRYPOINT ["java","-jar","mock-server.jar"]