FROM openjdk:11-oracle
MAINTAINER musalasoft.com
COPY target/drones-administration-0.0.1-SNAPSHOT.jar drones-administration-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/drones-administration-0.0.1-SNAPSHOT.jar"]