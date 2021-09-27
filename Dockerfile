
FROM openjdk:11.0.8-jre-slim

ARG SDI_VERSION=0.0.1-SNAPSHOT
EXPOSE 8200
COPY ./app.jar /app/java-clinical-microservice.jar

CMD [ "java", "-jar", "/app/java-clinical-microservice.jar", "--spring.datasource.initialization-mode=always" ]