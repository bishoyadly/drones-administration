#
# Build stage
#
FROM maven:3.6.3-jdk-11 AS build
COPY . .
RUN mvn clean package -Pprod -DskipTests

#
# Package stage
#
FROM openjdk:11-oracle
MAINTAINER musalasoft.com
COPY --from=build target/drones-administration-0.0.1-SNAPSHOT.jar drones-administration-0.0.1-SNAPSHOT.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","/drones-administration-0.0.1-SNAPSHOT.jar"]