# Staff Assessment Tracker

A full-stack staff assessment tracking application built with **Spring Boot** and **React**.

The project uses a Spring Boot backend to expose REST API endpoints and a React frontend for the user interface. It was developed collaboratively as part of a team software engineering project at the **University of Sheffield**.

---

## Overview

The Staff Assessment Tracker provides a web-based system supporting different types of users involved in the assessment process.

The system includes three user roles:

* **Academic User**
* **Teaching Support User**
* **External Examiner**

The application is split into two primary components:

```text
React Frontend
      │
      │ HTTP / REST
      ▼
Spring Boot Backend
      │
      ▼
In-Memory Database
```

The separation between the client and server allows the frontend and backend to be developed and run independently.

---

## Key Features

* Full-stack client-server architecture
* REST API built with Spring Boot
* React-based frontend
* Multiple user roles
* Development sample data
* Application logging
* Interactive API documentation with Swagger
* Application health monitoring with Spring Boot Actuator
* Cross-platform development scripts
* Separate frontend and backend development environments

---

## Technologies

### Backend

* Java 17
* Spring Boot
* Maven
* REST APIs
* Spring Boot Actuator

### Frontend

* React
* JavaScript
* Node.js
* npm

### Development & Tooling

* Git
* GitHub
* Swagger / OpenAPI
* Maven
* Cross-platform shell and batch scripts

---

## Project Structure

```text
staff-assessment-tracker/
│
├── client/                 # React frontend
│
├── server/                 # Spring Boot backend
│
├── run.bat                 # Windows startup script
├── run.sh                  # Linux startup script
│
├── .gitignore
├── .gitattributes
└── README.md
```

The `client` and `server` components can be run independently during development.

---

## Backend

The backend is implemented using **Spring Boot** and exposes the application's functionality through REST endpoints.

API documentation is available through Swagger when the server is running:

```text
/swagger-ui.html
```

Spring Boot Actuator is also available for checking application health:

```text
/actuator
```

The development environment uses an **in-memory database**, with sample users and application data created when the application starts.

---

## Frontend

The client is implemented using **React** and communicates with the Spring Boot backend through its exposed API endpoints.

The frontend and backend are kept as separate components within the repository:

```text
client/
server/
```

This separation allows each part of the application to be developed and run independently.

---

## Running Locally

### Prerequisites

Before running the application, install:

* **JDK 17**
* **Node.js**
* **npm**

---

### Start the Backend

From the project root:

```bash
./server/mvnw spring-boot:run
```

---

### Start the Frontend

Move into the client directory:

```bash
cd client
```

Then start the development server:

```bash
npm run dev
```

---

## Cross-Platform Run Scripts

The repository also includes scripts for starting the application components.

### Windows

```powershell
.\run.bat --client --server
```

### Linux

```bash
./run.sh --client --server
```

The `--client` and `--server` parameters can be used to start the individual components when required.

---

## API Documentation

When the backend is running, the exposed endpoints can be explored through Swagger:

```text
/swagger-ui.html
```

Swagger provides an interactive interface for viewing and testing the application's API endpoints.

---

## Application Monitoring

Spring Boot Actuator is used to expose application health information.

The health endpoint is available at:

```text
/actuator
```

This provides a simple way to verify that the backend application is running correctly.

---

## Development Data

The development version of the application uses an **in-memory database**.

Sample users and data are generated automatically for development and demonstration purposes, allowing the application t
