# dashboard (test service)

Web application for porject

- BI12-074 Đoàn Đình Đăng
- BI12-076 Mai Hải Đăng
- BI12-073 Trần Hải Đăng

# Normal build and run

## Requirement

- JAVA_HOME set up (recommended jdk 17)
- Maven set up, (MAVEN_HOME set up for downloading extension)
- Docker set up (future update)
- Mysql running on port 3306 with created schema *user-management* for storing data

## Quick run (Require docker for building images)

```shell script
./quickstart.sh
```
(Still updating and fixing)

## Normal build

- Generate RSA key used for security of the project(for window: open git bash terminal or run in WSL bash terminal)

```shell script
./keygen.sh
```

- run *authentication-jwt* service to get *JWT*

```shell script
cd authentication-jwt
mvn compile quarkus:dev
```

- create new shell and run *user-management* for testing endpoint

```shell script
cd user-management
mvn compile quarkus:dev
```

# Accessing API

- Test on **Postman** for convenient UI
- Test on **swagger-ui** at endpoint */q/swagger-ui*

## *authentication-jwt* is running on port 8082

- open URL method GET:*localhost:8082/jwt* and get the *JWT*

## *user-management* is running on port 8081

- Available endpoint:
  - GET: *localhost:8081/api/list* : list data (unsecured)
  - POST: *localhost:8081/api/list*: create data (input body follows the JSON script - Read Users.java to see the input attributes) (secured)
  - GET: *localhost:8081/api/list/{id}*: get user by id (secured)
  - GET: *localhost:8081/api/list/{firstname}*: get user by first name (secured)
  - GET: *localhost:8081/api/list/{username}*: get user by username (secured)
  - DELETE: *localhost:8081/api/list/{id}*: delete user by id (secured)