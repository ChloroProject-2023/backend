# Changelog (version 1.0)

## Mon, 21, Nov/2023 (init project)

### Feature

- **JWT security:**
  - **service:** init signature and data encryption service.
  - **endpoint:** init endpoint for requesting JWT.
- **User service:**
  - **service:** handling user information using JPA Streamer and Hibernation.
  - **endpoint:** init CUD (create, update, delete) for managing user information.
  - **exception:** custom exception and handling attributes exception.

## Wed, 13, Dec/2023

### Feature

- **User service:**
  - **model:** init new models to manage entities used for storing model deep learning, user's resources, ...
  - **service:** create CRUD for managing model information.

## Thu, 14, Dec/2023

- **User service:**
  - **service:** add some service for requesting endpoint.

## Sun, 17, Dec/2023

- **User service:**
  - **service:** add service for ratings.
  - **endpoint:** add endpoint to get ratings from models.

## Wed, 20, Dec/2023

- **User service:**
  - **models:** fix definitions and variables' name.
  - **service:** fix logic code.

## Thu, 21, Dec/2023

- **User service:**
  - **service:** remove JPAStream, implement project uniquely with Hibernate ORM.
- **JWT security:**
  - **service:** fix the expiration date after signing a key.

## Fri, 29, Dec/2023

- **User service:**
  - **service** & **endpoint**: finish testing and optimizing CRUD system of users and models.

## Sun, 31, Dec/2023

- **User service:**
  - **service**: finish CRUD for resources and inference (state or result of running models).
  - **endpoint:** finish basic CRUD endpoint for all entities.

## Sat, 6, Jan/2024

- **User service:**
  - **service & endpoint**: add top 10 queries.

## Sun, 7, Jan/2024

- **User service:**
  - **service:** add models' average stars to APIs.

### Feature

- **File service:**
  - **service & endpoint:** initialize upload file feature (still testing and optimizing)

## Sun, 14, Jan/2024

- **User service:**
  - **service & endpoint:** optimize file service for upload model, resource, avatar. Initialize train - evaluate machine learning service (test).

## Tue, 16, Jan/2024

- **User service:**
  - **entities:** optimize entities' relationship, fix description and comment - change to text type (before varchar 255), fix
    stack overflow error when using @Data from lombok.
  - **file service:** optimize upload and download file service.
  - **service & endpoint:** optimize all CRUD services with exception handler.

## Fri, 19, Jan/2024

### Feature

- **user service:**
  - **inference service:** initialize training and running inference service from machine learning API.
