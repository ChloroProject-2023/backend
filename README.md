# dashboard (test service)

Web application for porject

- BI12-074 Đoàn Đình Đăng (Backend)
- BI12-076 Mai Hải Đăng (Frontend API Handling)
- BI12-073 Trần Hải Đăng (Frontend UI)

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

- run *dashboard-security* service to get *JWT* (Running on port 8080)

```shell script
cd dashboard-security
mvn compile quarkus:dev
```

- create new shell and run *dashboard-user* for testing endpoint (Running on port 8081)

```shell script
cd dashboard-security
mvn compile quarkus:dev
```

# Accessing API

- Test on **Postman** for convenient UI
- Test on **swagger-ui** at endpoint **/q/swagger-ui**
    - for *dashboard-security*: > http://localhost:8080/q/swagger-ui
    - for *dashboard-user*: http://localhost:8081/q/swagger-ui

## Register new user

- Method: **POST**: (Permission: All)

```url
http://localhost:8080/users
```

- Request Body:

```json
{
    "username": "username",
    "password": "@Password123", // password must be at least 8 characters, contain at least 1 uppercase letter, 1 lowercase letter, 1 number and 1 special character
    "userDetails" : {
        "firstname": "firstname",
        "lastname": "lastname",
        "email": "email@gmail.com",
    }
}
```

- Future update:

    - verify 2 time entered password (maybe done in Frontend)
    - verify account with google, facebook, github, etc...

## Login to get jwt

- Method: **GET** (Permission: **admin**, **user**)

```url
http://localhost:8080/jwt
```

- This endpoint will return a jwt token for accessing other endpoint

## Get user information

- Method: **GET** (Permission: **admin**, **user**)
    
    Get list of user information

```url
http://localhost:8081/users
```

- Method: **GET** (Permission: **admin**, **user**)
    
    Get a page of 20 users

```url
http://localhost:8081/users/paging/{pageNo}
```

- Method **GET** (Permission: **admin**, **user**)
    
    Search for user started with specific string

```url
http://localhost:8081/users/search/{startsWith}
```

- Method **Post** (Permission: **admin**, **user**)
    
    Update user information (Still fixing)

## Response body

```json
[
    {
        "id": {id},
        "firstname": "firstname",
        "lastname": "lastname",
        "email": "email@gmail.com",
        "createTime": "{time}",
        "users": {
            "id": {id},
            "username": "username",
            "password": "{hashed password}",
            "roles": "user",
            "userDetails": {id}
        }
    }
]
```