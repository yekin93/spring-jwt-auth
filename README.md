# Spring Boot JWT Authentication

A Spring Boot 3 application implementing a secure authentication system
using JWT Access Tokens and Refresh Tokens.

This project is built to demonstrate modern authentication and
authorization practices commonly used in real-world backend applications.

---

## 🚀 Features

- User registration and login
- JWT Access Token authentication
- Refresh Token (database-based)
- Role-based authorization
- Secured REST endpoints
- Global exception handling
- Custom UserDetails implementation

---

## 🧱 Tech Stack

- Java 23
- Spring Boot 3
- Spring Security
- Spring Data JPA (Hibernate)
- MySQL
- Maven

---

## 🔐 Authentication Flow

1. User logs in with credentials
2. Access Token and Refresh Token are generated
3. Access Token is sent with API requests
4. When the Access Token expires:
   - A new Access Token is requested using the Refresh Token
5. Refresh Token is validated against the database

---

## 📌 API Endpoints (Overview)

### Authentication
- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/refresh`

### User
- `GET /api/users`

### Role
- `GET /api/roles`

> Some endpoints are protected using role-based authorization.

---

## ⚙️ Configuration

Before running the application, create the following file:

```bash
src/main/resources/application.properties

You can use the example configuration provided here:
src/main/resources/application.properties.example




```bash
src/main/resources/application.properties
