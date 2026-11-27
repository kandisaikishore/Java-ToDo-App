# Java To-Do App

A small Swing desktop application for creating and deleting tasks, with UTF-8 file persistence.

## Requirements

- JDK 17 or newer

## Run locally

```bash
git clone https://github.com/kandisaikishore/Java-ToDo-App.git
cd Java-ToDo-App
javac ToDoApp.java TaskStorage.java
java ToDoApp
```

Tasks are stored in `tasks.txt` in the current working directory. The file uses UTF-8 encoding. Saving writes a temporary file and replaces the previous file only after the new content has been written successfully.

If the task file is missing, the app starts with an empty list. If it is unreadable or malformed, editing controls are disabled so the app does not overwrite the original data with an empty list. A failed save does not change the visible task list.

## Run regression tests

```bash
javac ToDoApp.java TaskStorage.java TaskStorageTest.java
java TaskStorageTest
```

The regression tests cover missing and empty files, Unicode round-trips, blank task validation, malformed UTF-8, and failed-write preservation.

## Project scope

This is a local desktop task manager. It does not currently provide cloud synchronization, multi-user accounts, or database-backed storage.
