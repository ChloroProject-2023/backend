# dashboard

Web application for project

- [Đoàn Đình Đăng](https://github.com/dangdd2003) (Backend - Server-side)
- [Bùi Huy Hoàng](https://github.com/bhhoang) (Backend)
- [Mai Hải Đăng](https://github.com/Vivarium69420) (Frontend API Handling - Client-side)
- [Trần Hải Đăng](https://github.com/thdgg) (Frontend UI)

(for Website service, redirect to [dashboard-UI](https://github.com/ChloroProject-2023/dashboard-UI) repository)

# Normal build and run

## Requirement

- JAVA_HOME set up (recommended jdk 17, most of dependencies and classes work well with jdk 17)
- Maven set up, (MAVEN_HOME set up for downloading extension,recommend at least version 3.9.6)
- Docker set up (for running in docker)
- Mysql/Mariadb running on port 3306 with created schema _user-management_ for data persistence.

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

- Run _dashboard-security_ service to get _JWT_ (Running on port 8080)

```shell script
cd dashboard-security
mvn compile quarkus:dev
```

- Create new shell and run _dashboard-user_ for testing endpoint (Running on port 8081)

```shell script
cd dashboard-user
mvn compile quarkus:dev
```

# Access API - Endpoint

- Please refer to [API doc]() page.
