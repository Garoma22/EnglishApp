#FROM ubuntu:latest
#LABEL authors="volga"
#
#ENTRYPOINT ["top", "-b"]


FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY englishApp-1.0-SNAPSHOT.war /app/ea.jar
ENTRYPOINT ["java", "-jar", "/app/ea.jar"]