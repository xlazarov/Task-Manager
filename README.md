# Task-Manager

This is a Java web application built with Spring Boot and managed with Gradle. It's a simple app for managing tasks and users.

## API documentation
Swagger UI is integrated for API documentation. Access the documentation at http://localhost:8080/swagger-ui.html.

## Project structure
The application follows a three-layer architecture:
- **Data Layer**: Handles database interactions.
- **Service Layer**: Implements business logic.
- **Web Layer**: Exposes RESTful APIs.

## Features
**User Repository**: Manages user entities, each with a unique username.

**Task Repository**: Manages task entities, each with a description, due date, assigned user, and state.

**Task States**: Tasks can be in one of the following states: 
  - ☐ **Todo**: Tasks that are yet to be started.
  - 🔄 **In Progress**: Tasks that are currently being worked on.
  - ✅ **Completed**: Tasks that have been finished successfully.
  - ⏳ **Delayed**: Tasks that have missed their due dates and have been automatically marked as delayed.

**Automatic Delayed State**: Tasks automatically transition to the "Delayed" state if their due dates are missed.

**CRUD Operations**: Supports Create, Read, Update, and Delete operations for both users and tasks.

**Validation**: Validates task descriptions, due dates, and user assignments to maintain data accuracy.

## Technologies used
- **Spring Boot**: Framework used for building and configuring the application.
- **Gradle**: Build automation tool for managing dependencies and building the project.
- **Hibernate (JPA)**: Persistence framework for database operations and ORM (Object-Relational Mapping).
- **PostgreSQL**: Relational database management system used for storing application data.
- **Lombok**: Library for reducing boilerplate code in Java classes.
- **SLF4J and Logback**: Logging frameworks used for logging application events.
- **Swagger UI** (OpenAPI): Tool for documenting and visualizing RESTful APIs.
- **Spock**: Testing framework for writing unit tests, including data-driven tests.
- **Database Migration**: Database structure is managed as part of the application using **Flyway**, preferring clean SQL scripts over XML configurations.
- **Configuration Properties**: Application properties are configured using *@EnableConfigurationProperties* and are exposed via *JMX*.
