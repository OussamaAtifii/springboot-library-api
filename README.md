# 📚 Library API

A RESTful API built with **Spring Boot** for managing authors, books, and reviews. The project follows a **feature-based
architecture** and includes authentication, validation, filtering, pagination, and unit testing.

## 🚀 Tech Stack

- Java 21
- Spring Boot
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- Maven
- JUnit 5
- Mockito
- MockMvc

## ✨ Features

- CRUD operations for Authors, Books, and Reviews
- JWT Authentication
- Pagination and filtering
- Request validation
- Global exception handling
- Unit tests for controllers and services

## 📂 Project Structure

```
./library

├── auth
├── author
├── book
├── review
├── common
│   └── config
└── constants
```

Each feature contains its own:

- Controller
- Service
- Repository
- DTOs
- Mapper
- Exceptions
- Model

## 🧪 Testing

The project includes unit tests using **JUnit 5**, **Mockito**, and **MockMvc** to verify:

- Controller endpoints
- Service logic
- Validation
- Exception handling