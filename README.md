# FileServer

This repository contains code of FileServer application that works like **dropbox or google drive with local storage**.
* Made by Aleksandr Aleksandrov

## Short overview
**There is no front end implemented for this project - all functionality works on backend server.** 

This repository contains a project structure and all implemented functionality of file server.

The project uses Spring Boot and can be built/deployed with Gradle.

A file-based embedded HSQL database is already configured. The database file is created and stored in the git-ignored `db` dir. See `src/main/resources/application.properties`. Spring Boot automatically executes a database initialization script `src/main/resources/schema.sql` on first startup. 

Local storage folder path - ``src/main/resources/localStorage``

## Technologies and dependencies

  * **Gradle** - Project management
  * **Spring Boot** - Stand-alone, production-grade Spring based Applications
  * **H2** - File-based embedded HSQL database
  * **Lombok** - Build tool, automate logging variables
  * **JPA** - Programming interface specification that describes the management of relational data in enterprise Java applications.
  * **Hibernate** - Specification of JPA
  * **OpenAPI/Swagger** - Is an Interface Description Language for describing RESTful APIs
  * **Postman** -  To test RESTful APIs
  * **JUnit** - Unit testing framework

## API Specification and END POINTS example

FileServer's API endpoints and their behaviour is pre-specified, see `spec/api-spec.yaml`. The API is described as an OpenAPI/Swagger 2.0 specification.

A static HTML file (`spec/api-spec.html`) has been generated from the spec for more comfortable reading.

**_Examples of end points:_**

### Directory END POINTS

* ``/api/dir`` - **GET** - List top-level directory contents.
    * _Output example_:
  ```
      [
                [
                    {
                        "id": 1,
                        "name": "test-directory-v1",
                        "createdOn": "2015-03-31",
                        "lastAccessedOn": null,
                        "size": 10,
                        "directoryPath": "src\\main\\resources\\localStorage\\test-directory-v1",
                        "parentDirectory": null
                    },
                    {
                        "id": 2,
                        "name": "test-sub-dir-v1",
                        "createdOn": "2015-03-31",
                        "lastAccessedOn": null,
                        "size": 10,
                        "directoryPath": "src\\main\\resources\\localStorage\\test-directory-v1\\test-sub-dir-v1",
                        "parentDirectory": {
                            "id": 1,
                            "name": "test-directory-v1",
                            "createdOn": "2015-03-31",
                            "lastAccessedOn": null,
                            "size": 10,
                            "directoryPath": "src\\main\\resources\\localStorage\\test-directory-v1",
                            "parentDirectory": null
                        }
                    }
                ]
        ]
    ```

* ``/api/dir/{id}`` - **GET** - List directory contents by id.
    * _Output example_:
    ```
      [
          {
              "id": 1,
              "name": "test-directory-v1",
              "createdOn": "2015-03-31",
              "lastAccessedOn": null,
              "size": 10,
              "directoryPath": "src\\main\\resources\\localStorage\\test-directory-v1",
              "parentDirectory": null
          }
      ]
     ```
* ``/api/dir`` - **POST** - Create new top-level directory.
    * _Directory Dto example_:
    ```
          {
              "name": "test-directory-v1",
              "createdOn": "2015-03-31",
              "lastAccessedOn:": "2018-05-14",
              "size": 10,
              "parentDirectory": null
          }
    ```
  
* ``/api/dir/{id}`` - **DELETE** - Delete **empty** directory with given ID
* ``/api/dir/{id}`` - **POST** - Create a new subdirectory under directory with given ID.
* _Subdirectory Dto example_:
```
{
    "name": "test-sub-dir-v1",
    "createdOn": "2015-03-31",
    "lastAccessedOn:": "2018-05-14",
    "size": 10,
    "parentDirectory": null
}
```
* ``api/dir/{id}/file`` - **POST** - Create a new file under directory with given ID.
    * Example of form-data
    ```
    key: file
    value: yourFileHere
    ```
### Files END POINTS
* ``api/file/{id}`` - **GET** - Download file with given ID.
    * _You can run this in your browser and file will be downloaded_
    
* ``api/file/{id}`` - **DELETE** - Delete file with given ID.
## Deploying

The server can should be deployed using Gradle's `bootRun` task. Using the included Gradle wrapper:

```
./gradlew bootRun
```

This will start an embedded Tomcat server on port 8080 (by default).