# Web Address Book

## Features

- manage families and their members (the ordering of family members is customizable)
- provide family data (which is the same for all family members) _and_ individual data for any family member if applicable
- export a formatted PDF

## Getting started

- if you don't have it yet: download and install Java 21 or later
- download the [latest release](https://github.com/mathisdt/web-address-book-enhanced/releases/latest)
- create a database for your data (don't worry about creating tables etc. - Liquibase will take care of that)
- create an `application-prod.properties` next to the jar file and define your database connection in it:
  ```
  spring.datasource.url=jdbc:mysql://localhost:3306/yourdatabase
  # OR: spring.datasource.url=jdbc:postgresql://localhost:5432/yourdatabase
  spring.datasource.username=yourusername
  spring.datasource.password=yourpassword
  spring.datasource.driver-class-name=com.mysql.jdbc.Driver
  # OR: spring.datasource.driver-class-name=org.postgresql.Driver
  ```
- if you want, you can also overwrite the HTML used for generating the report in that file - this is the default:
  ```
  wab.report.title=Address Book
  # see https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/format/DateTimeFormatter.html#patterns for possible placeholders
  wab.report.date-format=dd/MM/yyyy
  # available placeholder: {date} / note: images can be embedded using "data:" URLs inside of <img> "src" attributes
  wab.report.header=<h1><u>Address Book</u></h1>Here you can find the addresses as of {date}.<br/><br/>
  # available placeholders: {lastName} {address} {contacts} {persons}
  wab.report.family=<div style="border-top:1px solid"><div style="display:inline-block; width: 20%; vertical-align:top; padding-top:5px; padding-bottom:5px">{lastName}</div><div style="display:inline-block; vertical-align:top; padding-top:5px; padding-bottom:5px">{address}</div><div style="margin-left: 12%; padding-bottom:5px">{contacts}</div>{persons}</div>
  # available placeholders: {name} {birthday} {contacts}
  wab.report.person=<div style="margin-left: 12%; border-top:1px solid; padding-top:5px; padding-bottom:5px"><div style="display:inline-block; width: 15%; vertical-align:top">{name}</div> <div style="display:inline-block; width: 10%; vertical-align:top">{birthday}</div>{contacts}</div>
  ```
- start the application using `java -jar THE-JAR-YOU-DOWNLOADED.jar --spring.profiles.active=prod`
  or `java -jar THE-JAR-YOU-DOWNLOADED.jar --spring.config.location=file:/path/to/application-prod.properties`

## Technology

The backend is written in Java and Spring Boot, while the frontend uses Angular.

## License

This project is licensed under GPL v3. If you submit or contribute changes, these are automatically licensed
under GPL v3 as well. If you don't want that, please don't submit the contribution (e.g. pull request)!
