# UpRooms

UpRooms is an enterprise-grade RESTful API developed to streamline meeting room management, user administration, and booking workflows. Built with modern Java practices and the Spring Boot framework, it emphasizes scalability, maintainability, and robust security.

## 🚀 Project Overview

The UpRooms system provides a centralized backend solution for managing an organization's room inventory and meeting schedules. It is engineered with a strict layered architecture to ensure separation of concerns and facilitates easy integration for frontend applications.

## 🌟 Core Features

- **User Management**: Comprehensive role-based access control (RBAC) supporting `ADMIN` and `COLLABORATOR` roles.
- **Room Management**: Efficient inventory control including creating rooms, listing active availability, and toggling room status (Active/Inactive) via dynamic PATCH requests.
- **Booking Management**: Streamlined booking creation with built-in business logic validation, user-specific booking history retrieval, and cancellation functionality.
- **API Documentation**: Automated, interactive documentation generated in real-time, fully compliant with OpenAPI 3.0 standards.
- **Robust Security**: Enforces Basic Authentication with CORS preflight support to ensure secure, seamless integration with frontend clients.
- **Input Integrity**: Strict data validation at the API entry point using `jakarta.validation` to maintain system-wide data consistency.

## 🛠 Technology Stack

- **Java 17+**
- **Spring Boot 3.3.4**
- **Spring Security**
- **OpenAPI / Swagger 3.0**
- **Maven** (Build Tool)

## 📖 API Documentation

The interactive API documentation for this project is available via GitHub Pages:

**[https://devarturtrajano.github.io/UpRooms/](https://devarturtrajano.github.io/UpRooms/)**

This documentation provides comprehensive details on all available endpoints, request models, and response structures.

## 🏗 Getting Started

### Prerequisites

- **Java 17 or higher**
- **Maven** (The project includes a Maven Wrapper `mvnw` for ease of use)

### Running the Application

Use the provided Maven Wrapper to build, test, and run the project:

```bash
# Clean the project and run verification tests
.\mvnw clean verify

# Run the application
.\mvnw spring-boot:run
```

The API will typically be accessible at `http://localhost:8081`.

## ⚙️ Configuration & Environment

The project utilizes `dotenv-java` to manage sensitive configurations.

1.  Create a `data.env` file in the `src/` directory.
2.  Define your environment variables (e.g., database credentials, secrets) in this file.
3.  The application will automatically load these variables at runtime, ensuring sensitive data remains excluded from source control.

---

*UpRooms - Designed for efficiency, built for scale.*
