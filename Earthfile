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

build-and-release-on-github:
    ARG --required GITHUB_TOKEN
    ARG PATTERN_TO_RELEASE
    BUILD +build
    FROM ubuntu:latest
    WORKDIR /project
    COPY .git .git
    COPY +build-java/target target
    RUN apt update && apt install -y curl gpg
    RUN curl -fsSL https://cli.github.com/packages/githubcli-archive-keyring.gpg | gpg --dearmor -o /usr/share/keyrings/githubcli-archive-keyring.gpg
    RUN echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" | tee /etc/apt/sources.list.d/github-cli.list > /dev/null
    RUN apt update && apt install -y gh
    RUN --push release_timestamp=$(date '+%Y-%m-%d @ %H:%M')
    RUN --push release_timestamp_terse=$(date '+%Y-%m-%d-%H-%M')
    RUN --push release_hash=$(git rev-parse --short HEAD)
    RUN --push echo $GITHUB_TOKEN | gh auth login --with-token
    RUN --push gh release create "release-$release_timestamp_terse-$release_hash" --target "$release_hash" --title "Release $release_timestamp" --notes "built from commit $release_hash"
    RUN --push if [ -n "$PATTERN_TO_RELEASE" ]; then gh release upload "release-$release_timestamp_terse-$release_hash" $(ls $PATTERN_TO_RELEASE); else echo "no PATTERN_TO_RELEASE was given, so no files were attached to the release"; fi
