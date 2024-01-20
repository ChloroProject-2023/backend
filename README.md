# dashboard - Backend

- Backend service - Server for Group Project 2023 - 2024

- for Website service, redirect to [dashboard-UI](https://github.com/ChloroProject-2023/dashboard-UI) repository.

# Normal build and run

## Requirement

- **Java** set up (JAVA_HOME set up, recommended JDK 17, most dependencies work well with jdk 17)
- **Maven** set up (MAVEN_HOME set up for downloading extension,recommend at least version 3.9.6)
- **Docker** set up (for running in docker)
- **MySQL/MariaDB** running on port 3306 with created schema _user-management_ for data persistence (recommended _MySQL_)

## Run in docker (Recommended)

```shell script
./quickstart.sh
```

(Still updating and fixing)

## Normal build

- Generate RSA key used for project's security (every time this script generates a new key, all the previous JWT will die)

```shell script
./keygen.sh
```

- Run _dashboard-security_ service to get _JWT_ (Running on port 0.0.0.0:8080)

```shell script
cd dashboard-security
mvn compile quarkus:dev
```

- Create new shell and run _dashboard-user_ for testing endpoint (Running on port 0.0.0.0:8081)

```shell script
cd dashboard-user
mvn compile quarkus:dev
```

# Access API - Endpoint

- Please refer to [API doc]() page.

# Contribution

- Author
  - <a href="https://github.com/dangdd2003" target="_blank">Đoàn Đình Đăng</a> (Backend - Service Creator/Developer)
- Contributors
  - <a href="https://github.com/bhhoang" target="_blank">Bùi Huy Hoàng</a> (Querying Database Supporter)
  - <a href="https://github.com/Vivarium69420" target="_blank">Mai Hải Đăng</a> (Frontend Client - API Handler)
  - <a href="https://github.com/thdgg" target="_blank">Trần Hải Đăng</a> (Frontend Service - API Handler & UI/UX Designer)
