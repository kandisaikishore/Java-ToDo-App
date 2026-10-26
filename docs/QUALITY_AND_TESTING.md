# Development and Testing Guide

Java-ToDo-App contains a Java command-line task manager and a separate Spring Boot REST API/browser UI. The console program and API have separate runtime state; do not assume they share tasks or persistence.

## Prerequisites

- JDK 17 or newer for the API
- Maven for the Spring Boot API
- A JDK for compiling the standalone console program

## Run the console application

From the repository root:

```bash
javac ToDoApp.java
java ToDoApp
```

If the source layout or package declarations change, compile the required source files from the appropriate directory.

## Run the REST API and browser UI

```bash
cd api
mvn spring-boot:run
```

Open `http://localhost:8081/`. The API uses port 8081, with routes under `/api`. Consult the controller mappings for the exact endpoints and request/response schemas before writing client integrations.

**Persistence limitation:** the API currently keeps its data in server memory, so data resets when the process restarts. Do not use it as durable storage until a database and appropriate migration/backup strategy are configured.

## Test plan

Run API tests from the API directory:

```bash
cd api
mvn test
```

For both console and API workflows, test:
- Creating, listing, updating, completing, and deleting tasks where implemented
- Empty and whitespace-only titles
- Invalid menu choices and malformed request bodies
- Missing or unknown task IDs
- Repeated completion/deletion requests
- API error status codes and validation messages
- Restart behavior and the documented in-memory persistence boundary

Use isolated test data and avoid depending on task ordering or wall-clock timing.

## Error handling and API contracts

- Return client-error status codes for invalid requests rather than reporting success.
- Keep error response formats consistent across endpoints.
- Do not expose stack traces, local paths, or secrets in API responses.
- Document each endpoint's method, path, input, success response, and expected error responses from the actual controller implementation.

## Pull-request checklist

- [ ] Keep console and API behavior clearly distinguished.
- [ ] Add regression tests for bug fixes.
- [ ] Run `mvn test` for API changes and manually verify the console workflow for console changes.
- [ ] Document any check that could not be run.
- [ ] Do not claim durable persistence until it is implemented and tested.
