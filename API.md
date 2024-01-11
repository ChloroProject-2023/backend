# API Document

- Document for using service's endpoints.

## Table of Endpoints

## Security Service

- Service for signing and encrypting JWT.
- Running default on:
  - Host: `0.0.0.0`
  - Port: `8080`
- Header Authentication.

### JWT Resource

- **POST:** `/jwt`
  - Role Allowed: _admin, user_
  - Response Body: PLAIN_TEXT of JWT

## User Service

- **CRUD** service for users, models managements.
- Running default on:
  - Host: `0.0.0.0`
  - Port: `8081`
- Header Authentication.

### User Resource

- **GET:** `/users`

  - Role Allowed: _admin, user_
  - Response body: list of users

- **GET:** `/users/count`

  - Role Allowed: _admin, user_
  - Response body: `{number of users}`

- **GET:** `/users/paging`

  - Role Allowed: _admin, user_
  - Request Parameters:
    - Query: `{pageNo}: integer`
    - Query: `{pageSize}: integer`
  - Response body: list of users

- **GET:** `/users/profile`

  - Role Allowed: _admin, user_
  - Request Parameters:
    - Query: `{username}: string`
  - Response body: single user

- **GET:** `/users/search`

  - Role Allowed: _admin, user_
  - Request Parameters:
    - Query: `{keyword}: string`
  - Response body: list of users

- **GET:** `/users/{id}`

  - Role Allowed: _admin, user_
  - Request Parameters:
    - Path: `{id}: integer`
  - Response body: single user

- **POST:** `/users/create`

  - Permit All
  - Request Body:

  ```json
  {
    "username": "username",
    "password": "@Password12345",
    "userDetail": {
      "firstname": "firstname",
      "lastname": "lastname",
      "email": "email@example.com"
    }
  }
  ```

  - Response Body: don't care about it, it's suck!

- **PUT:** `/users/update`

  - Role Allowed: _admin, user_
  - Request Parameters:
    - Query: `{id}: integer`
  - Request Body:

  ```json
  {
  "roles": "{admin/user}",
  "userDetail": {
    "firstname": "new_firstname",
    "lastname": "new_lastname",
    "email": "new_email@example.com"
  }
  ```

- **PUT:** `/uses/updatePassword`

  - Role Allowed: _admin, user_
  - Request Parameters:
    - Query: `{id}: integer`
    - Query: `{oldPassword}: string`
    - Query: `{newPassword}: string`

**DELETE**: `/users/delete`

- Role Allowed: _admin, user_
- Request Parameters:
  - Query: `{username}: string`

**DELETE**: `/users/delete/{id}`

- Role Allowed: _admin, user_
- Request Parameters:
  - Path: `{id}: integer`

---

- **_Response Body (Single)_**

```json
{
  "id": 1,
  "username": "username",
  "password": "$2a$10$Borm.iyHy/rMHNwd1g9PoOpFDu4IjWwYSHL4F7Anls8dvTEj8K6aG",
  "role": "admin",
  "firstname": "firstname",
  "lastname": "lastname",
  "email": "email@example.com",
  "createTime": "YYYY-MM-DD hh:mm:ss[.nnnnnnn] [+|-]hh:mm"
}
```

- **_Response Body (List)_**

  ```json
  [
    {
      "id": 1,
      "username": "username1",
      "password": "$2a$10$Borm.iyHy/rMHNwd1g9PoOpFDu4IjWwYSHL4F7Anls8dvTEj8K6aG",
      "role": "admin",
      "firstname": "firstname1",
      "lastname": "lastname1",
      "email": "email2@example.com",
      "createTime": "YYYY-MM-DD hh:mm:ss[.nnnnnnn] [+|-]hh:mm"
    },
    {
      "id": 2,
      "username": "username2",
      "password": "$2a$10$Borm.iyHy/rMHNwd1g9PoOpFDu4IjWwYSHL4F7Anls8dvTEj8K6aG",
      "role": "user",
      "firstname": "firstname2",
      "lastname": "lastname2",
      "email": "email2@example.com",
      "createTime": "YYYY-MM-DD hh:mm:ss[.nnnnnnn] [+|-]hh:mm"
    },
    ...
  ]
  ```

### Model Resource

### Rating Resource

### Inference Resource

### Resource

### File Resource
