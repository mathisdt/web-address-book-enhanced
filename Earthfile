VERSION 0.8

build:
    BUILD +build-angular
    BUILD +build-java

build-angular:
    FROM node:22-alpine3.20
    WORKDIR /project
    COPY src/frontend ./
    RUN TZ=Europe/Berlin npm install
    RUN TZ=Europe/Berlin npm run build
    SAVE ARTIFACT dist/frontend/browser

build-java:
    FROM maven:3.9.9-eclipse-temurin-21-alpine
    WORKDIR /project
    COPY .git .git
    COPY pom.xml ./
    COPY src/main src/main
    COPY src/test src/test
    COPY +build-angular/browser src/main/resources/static/
    RUN TZ=Europe/Berlin mvn clean verify -U --no-transfer-progress
    SAVE ARTIFACT target AS LOCAL target
