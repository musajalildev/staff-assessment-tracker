[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/F0ieClPf)

# Staff Assessment Tracker

## Table of Contents
- [Required](#required)
- [Usage Instructions](#usage-instructions)
  - [Run](#run)
    - [Windows](#windows)
    - [Linux](#linux)
  - [System](#system)
    - [Credentials](#credentials)
  
## Required
- jdk version: 17

- Project Structure
  - Spring Boot server: server/
  - React Client: client/

## Usage Instructions
### Run
Either run it from their respective directory: 

```bash 
./server/mvnw:spring-boot:run
```

```bash
cd ./client/
npm run dev
```

Or use the run file Provided:

Parameters: 
--client, --server to run just the client or the server or both
#### Windows
```bash 
.\run.bat --client --server
```

#### Linux
```bash
./run.sh --client --server
```

### System
For the server we are using an in memory database so sample data will be created via command line runner.

We are using an in memory database where the users are created via the command line runner.

Credentials to use the system:

#### Credentials

Academic User:
- Username: john
- Password: john123
- Email: john@example.com

Teaching Support User:
- Username: mary
- Email: mary@example.com
- Password: mary123

External Examiner User:
- Username: kofi
- Email: kofi@example.com
- Password: kofi123

All operations are handled by the logging services

You can view all the exposed endpoints at the route: /swagger-ui.html

You can use actuator to check the health of the system at: /actuator

### Client
