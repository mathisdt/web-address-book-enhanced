![license](https://img.shields.io/github/license/mathisdt/web-address-book.svg?style=flat)

# Web Address Book

## Features

- manage families and their members (the ordering of family members is customizable)
- provide family data (which is the same for all family members) _and_ individual data for any family member if applicable
- export a formatted PDF

## Technology

The backend is written in Java and Spring Boot, while the frontend uses Angular.

## Getting started

- if you don't have it yet: download and install Java 21 or later
- download the [lastest release](https://github.com/mathisdt/web-address-book-enhanced/releases/latest) and unpack it
  (use the jar, not the sources.jar)
- create a database for your data - you can use
  [this file](https://github.com/mathisdt/web-address-book-enhanced/blob/master/src/test/resources/schema.sql)
  for schema generation
- create an `application-prod.properties` next to the jar file and define your database connection in it:
  ```
  spring.datasource.url=jdbc:mysql://localhost:3306/yourdatabase
  # OR spring.datasource.url=jdbc:postgresql://localhost:5432/yourdatabase
  spring.datasource.username=yourusername
  spring.datasource.password=yourpassword
  spring.datasource.driver-class-name=com.mysql.jdbc.Driver
  # OR spring.datasource.driver-class-name=org.postgresql.Driver
  ```
- start the application using `java -jar THE-JAR-YOU-DOWNLOADED.jar --spring.profiles.active=prod`
  or `java -jar THE-JAR-YOU-DOWNLOADED.jar --spring.config.location=file:/path/to/application-prod.properties`
