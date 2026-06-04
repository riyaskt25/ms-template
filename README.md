# MS Template

Spring Boot microservice template built with Java 21, Gradle, MapStruct, Lombok, Spotless, and Checkstyle.

## Overview

This project provides a reusable backend foundation for SNB microservices with:

- Spring Boot 3.5.13
- Java 21
- Gradle
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Boot Actuator
- SpringDoc OpenAPI
- H2 database for local development
- MapStruct for type mapping
- Lombok for boilerplate reduction
- Spotless with Google Java Format
- Checkstyle for code quality validation

## Prerequisites

Before running the project, install:

- Java 21
- Git
- PowerShell 5+ on Windows, or a compatible shell on your platform
- An IDE such as IntelliJ IDEA or VS Code

Optional but recommended:

- Use the Gradle wrapper included in the project
- Enable Git hooks if you want commit-time formatting validation

## Project Structure

```text
src/main/java        Application source code
src/main/resources   Application configuration and SQL scripts
src/test/java        Test source code
config/checkstyle    Checkstyle configuration
scripts              Utility scripts
postman              Generated Postman collection
```

## Tech Stack

- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Validation
- Spring Boot Actuator
- SpringDoc OpenAPI UI
- H2 Database
- MapStruct
- Lombok
- Spotless
- Checkstyle

## Getting Started

### 1. Clone the repository

```bash
git clone <repository-url>
cd ms-template
```

### 2. Verify Java version

Make sure Java 21 is active:

```bash
java -version
```

### 3. Build the project

On Windows:

```powershell
./gradlew.bat clean build
```

On macOS/Linux:

```bash
./gradlew clean build
```

## Running the Application

Start the application with:

On Windows:

```powershell
./gradlew.bat bootRun
```

On macOS/Linux:

```bash
./gradlew bootRun
```

The application typically starts on:

```text
http://localhost:8080
```

## API Documentation

SpringDoc OpenAPI is available after the application starts.

OpenAPI endpoint:

```text
http://localhost:8080/v3/api-docs
```

YAML endpoint:

```text
http://localhost:8080/v3/api-docs.yaml
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

## Database

The project uses H2 for local development.

Useful notes:

- Schema and seed data are loaded from `src/main/resources`
- H2 is configured for development/runtime use
- SQL files may include `db.sql` and `data.sql`

If you add or change the schema, update the SQL scripts accordingly.

## Code Formatting

This project uses Spotless with Google Java Format.

Run formatting:

On Windows:

```powershell
./gradlew.bat spotlessApply
```

On macOS/Linux:

```bash
./gradlew spotlessApply
```

Check formatting without changing files:

On Windows:

```powershell
./gradlew.bat spotlessCheck
```

On macOS/Linux:

```bash
./gradlew spotlessCheck
```

## Code Quality

This project uses Checkstyle for code quality validation.

Run validation:

On Windows:

```powershell
./gradlew.bat check
```

On macOS/Linux:

```bash
./gradlew check
```

Checkstyle is configured to validate Java source code and is intended to run as part of normal development workflows.

## Commit Hook for Formatting Validation

A Git pre-commit hook can be used to block commits when formatting is not compliant.

Typical setup:

- Run `spotlessCheck` before commit
- Stop the commit if formatting fails
- Ask the developer to run `spotlessApply` and commit again

Recommended hook path:

```text
.githooks/pre-commit
```

Enable it with:

```bash
git config core.hooksPath .githooks
```

## Build Tasks

Common Gradle tasks:

```bash
./gradlew clean build
./gradlew test
./gradlew check
./gradlew bootRun
./gradlew spotlessApply
./gradlew spotlessCheck
```

On Windows, use `./gradlew.bat` instead.

## OpenAPI and Postman Generation

This project includes a task to refresh OpenAPI and regenerate the Postman collection.

Run:

On Windows:

```powershell
./gradlew.bat generatePostmanFromAnnotations
```

On macOS/Linux:

```bash
./gradlew generatePostmanFromAnnotations
```

What it does:

- Fetches OpenAPI YAML from the running application
- Regenerates the Postman collection under `postman/`

### Prerequisite

The application must be running before generating the API spec.

## Configuration Files

Important configuration files:

- `src/main/resources/application.yaml`
- `config/checkstyle/checkstyle.xml`
- `build.gradle`

## Development Notes

- Use Lombok annotations to reduce boilerplate
- Use MapStruct for mapping between entities and DTOs
- Keep generated code out of Spotless targets unless you explicitly want to validate generated sources
- Run `check` before pushing changes
- Run `spotlessApply` before committing when formatting changes are needed

## Testing

Run tests with:

```bash
./gradlew test
```

Or run the full verification flow:

```bash
./gradlew check
```

## Troubleshooting

### Java version mismatch

If Gradle fails due to Java version issues, confirm Java 21 is active.

### Formatting failures

If Spotless fails:

```bash
./gradlew spotlessApply
```

Then rerun:

```bash
./gradlew spotlessCheck
```

### Checkstyle failures

If Checkstyle fails, review the report generated by Gradle and fix the reported source issues.

### OpenAPI or Postman generation fails

Ensure the application is running locally and the endpoint is reachable at:

```text
http://localhost:8080/v3/api-docs.yaml
```

## License

Add your project license here.
