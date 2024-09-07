# Use the official Maven image to build the project
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use a minimal image for running the application
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/verve-service-0.0.1-SNAPSHOT.jar /app/verve-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/app/verve-service-0.0.1-SNAPSHOT.jar"]
