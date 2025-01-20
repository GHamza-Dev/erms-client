# Employee Resource Management System (ERMS) Client

A Java Swing-based desktop client application for managing employee resources.

This application consumes the ERMS API ([erms-api github repository](https://github.com/GHamza-Dev/erms))

## Technologies Used

### Core Technologies
- Java 17 (Eclipse Temurin JDK)
- Swing (GUI Framework)
- Maven (Build Tool)

### Libraries
- OkHttp3 - HTTP client for API communication
- Jackson - JSON serialization/deserialization
- Lombok - Reduces boilerplate code
- JSON.org - JWT token parsing

### Development Tools
- Docker - Containerization
- X11 - Display forwarding for GUI in containers

## Project Structure

```
erms-client/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── hahn/
│                   └── erms/
│                       ├── client/
│                       │   ├── api/
│                       │   ├── config/
│                       │   ├── model/
│                       │   ├── security/
│                       │   ├── service/
│                       │   └── ui/
│                       └── Main.java
├── config.properties
├── Dockerfile
├── pom.xml
└── setup.sh
```

## Features

- Authentication and Authorization (JWT-based)
- Employee Management (CRUD operations)
- Department and Job Title Management
- Audit Logging
- Role-based Access Control
- Search and Filtering Capabilities

## Getting Started

### Prerequisites

- Docker
- X11 Server (for Linux/macOS)
- Java 17 (for development)
- Maven (for development)

### Configuration

1. Update the `config.properties` file with your API settings:

```properties
api.base.url=http://localhost:8080/api/erms
auth.path=/auth/login
employees.path=/employees
```

### Running with Docker

1. Make the setup script executable:
```bash
chmod +x setup.sh
```

2. Run the setup script:
```bash
./setup.sh
```

This will:
- Enable X11 forwarding
- Remove any existing ERMS client containers
- Build the Docker image
- Start the application in a container
- Display the application logs

### Running for Development

1. Clone the repository:
```bash
git clone https://github.com/GHamza-Dev/erms-client.git
```

2. Navigate to the project directory:
```bash
cd erms-client
```

3. Build the project:
```bash
./mvnw clean package
```

4. Run the application:
```bash
java -jar target/erms-client-[version].jar
```

## Default login accounts
- Admin role:
  - Usernale: admin
  - Password: admin
- HR
  - hrmanager
  - hr
- IT Manager
  - itmanager
  - it