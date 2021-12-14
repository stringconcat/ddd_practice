FROM bellsoft/liberica-openjre-alpine:17
ARG JAR_FILE=build/libs/kitchen-application.jar
COPY ${JAR_FILE} kitchen-application.jar
ENTRYPOINT ["java","-jar","kitchen-application.jar"]