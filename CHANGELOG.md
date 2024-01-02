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
