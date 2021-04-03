# FileServer

FileServer is a test assignment created for Cybernetica programmer internship applicants. Please do not share solutions to this assignment publicly.


## Overview

This is a solution template. It contains a standard project structure and some implemented functionality. Some of the code might need improvements. 

The project uses Spring Boot 2.0 and can be built/deployed with Gradle. Spring Boot and its *starter* dependencies include a lot of functionality out-of-the-box. See the [Spring Boot Reference Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/index.html) for more information.

A file-based embedded HSQL database is already configured. The database file is created and stored in the git-ignored `db` dir. See `src/main/resources/application.properties`. Spring Boot automatically executes a database initialization script `src/main/resources/schema.sql` on first startup. 


## Deploying

The server can and should be deployed using Gradle's `bootRun` task. Using the included Gradle wrapper:

```
./gradlew bootRun
```

This will start an embedded Tomcat server on port 8080 (by default). FileServer's file download service for example can then be found on `http://localhost:8080/api/file/...` 


## API Specification

FileServer's API endpoints and their behaviour is pre-specified, see `spec/api-spec.yaml`. The API is described as an OpenAPI/Swagger 2.0 specification. For more information, see the [OpenAPI 2.0 Specification](https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md).

A static HTML file (`spec/api-spec.html`) has been generated from the spec for more comfortable reading.


## Notes

Your task is to implement the missing functionality so that this application conforms to the given specification. 

You can change this solution template as well (e.g. the database structure) but the final solution must not require any manual configuration -- the application must be deployable by simply running Gradle's `bootRun` task.
