# Drones Administration Service

Service was deployed on Render
https://drones-administration-service.onrender.com/

<a href="https://drones-administration-service.onrender.com/">
  <picture>
    <img src="screenshots/capture1.png" height="180" width="300" alt="Spring"/>
  </picture>
</a>

<a href="https://drones-administration-service.onrender.com/">
  <picture>
    <img src="screenshots/capture2.png" height="180" width="300" alt="Spring"/>
  </picture>
</a>


## Table of Contents

- [About](#About)
- [Service Architecture](#service-architecture)
- [Technologies Used](#technologies-used)
- [Build And Run Instructions](#build-and-run-instructions)


## About

Drones Administration service allows:
- Registering a drone
- Loading a drone with medication items
- Get drone data to check loaded medication items and drone battery level
- Get list of drones available for loading

## Service Architecture

The service architecture was built following [Robert C. Martin's Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

The clean architecture compiles many code designs and principles, like SOLID, stable abstractions, and others. But, the core idea is to divide the system into levels based on the business value. Hence, the highest level has business rules, with each lower one getting closer to the I/O devices.

The architecture produce systems that are:

- Independent of Frameworks.
- Testable. The business rules can be tested without the UI, Database, Web Server, or any other external element.
- Independent of UI. The UI can change easily, without changing the rest of the system. A Web UI could be replaced with a console UI, for example, without changing the business rules.
- Independent of Database. We can swap out PostgreSQL Database for MongoDB or something else. so business rules are not bound to the database.

<a href="https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html">
  <picture>
    <img src="https://blog.cleancoder.com/uncle-bob/images/2012-08-13-the-clean-architecture/CleanArchitecture.jpg" height="500" width="700" alt="Spring"/>
  </picture>
</a>

## Technologies Used

- Java
- Maven
- Spring Boot, Spring MVC, Spring Data JPA
- Hibernate ORM
- PostgreSQL Database
- Swagger, OpenAPI Generator
- JUnit 5, Mockito
- Docker and docker compose

## Build And Run Instructions

**Install Required Tools:**

1. Install Java OpenJDK 11 using [Software Development Kit Manager](https://sdkman.io/) or from preferred source
```
curl -s "https://get.sdkman.io" | bash
sdk install java 11.0.2-open
```
2. Install maven using APT or manually from [Apache Maven Website](https://maven.apache.org/install.html)
```
sudo apt update
sudo apt install maven
```

3. Install PostgreSQL Database configured for the service by running docker compose command against the compose.yaml file in the project root directory

```
 docker compose up -d postgres
```

**Build And Run Service Steps:**

1. Clone the GitLab repository into local machine downloads directory for example

```
git clone https://oauth:glpat-2r4Q5zpjfpstAZCfPrFy@gitlab.com/musala_soft/DEV_DRONES-034a7f37-af29-9591-126f-a7a402526b71.git
```

2. Step into the service maven project root directory where pom.xml, Dockerfile and compose.yaml files exist:
```
cd Downloads/DEV_DRONES-034a7f37-af29-9591-126f-a7a402526b71/drones-administration/
```

3. Build and release service JAR file using maven commands either with running tests or without tests

```
mvn clean package
mvn clean package -DskipTests
```

4. Run docker compose command against the compose.yaml file in the project root directory

```
docker compose up --build
docker compose up --build -d
```
5. Navigate to the service [Home Page](http://localhost:8080/) into preferred browser that should open the swagger OpenAPI documentation for the endpoints implemented in the service hosted as homepage

```
http://localhost:8080
http://localhost:8080/swagger-ui/index.html
```
