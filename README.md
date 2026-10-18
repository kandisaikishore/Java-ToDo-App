# Java To-Do App

[![Java](https://img.shields.io/badge/Java-CLI-orange?logo=openjdk)](https://www.java.com/) [![Source](https://img.shields.io/badge/Source-GitHub-181717?logo=github)](https://github.com/kandisaikishore/Java-ToDo-App)

A compact Java command-line task manager built around `ToDoApp.java`. It provides a starting point for learning how to structure task workflows, handle console input, and extend a small Java application.

## What this project demonstrates

- Menu-driven task interaction in the terminal
- Java methods and control flow
- Basic task data handling
- Opportunities to add priorities, due dates, filtering, and persistence

The exact operations and data-storage behaviour depend on the current source implementation.

## Technology

- Java / JDK
- Command-line interface

## Run locally

Install a JDK, then run:

```bash
git clone https://github.com/kandisaikishore/Java-ToDo-App.git
cd Java-ToDo-App
javac ToDoApp.java
java ToDoApp
```

If the source uses packages or additional Java files, compile from the appropriate directory and include the related classes.

## Suggested validation

Try creating multiple tasks, handling invalid menu choices, and checking how tasks behave when the application is restarted. Add tests for empty inputs, duplicate tasks, and any update/delete operations.

## Roadmap

- Task completion status, priority, and due dates
- Search, sorting, and filtering
- File or database persistence
- Automated unit tests and stronger input validation

## Contributing

Open an issue with reproducible steps or send a focused pull request with validation notes.

## License

No license file is currently declared. Add a `LICENSE` file if you intend to define reuse terms.


## REST API and browser UI

The browser interface now calls a Java Spring Boot REST API. Install JDK 17+ and Maven, then start the API from the repository root:

```bash
cd api
mvn spring-boot:run
```

Open the UI at `http://localhost:8081/`. The API uses port 8081. API endpoints are under `/api`. Current API data is held in server memory and resets when the process restarts; configure a database before production use. The API module is separate from the original console/Swing classes, so it does not share their in-process state.
