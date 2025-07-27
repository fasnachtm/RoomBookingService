# Use a base image with Java 21, e.g., Eclipse Temurin or Amazon Corretto
#FROM eclipse-temurin:21-jdk-jammy
FROM amazoncorretto:21

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from your build output to the container
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose the port your Java application listens on (e.g., 8080 for Spring Boot)
EXPOSE 8080

# Define the command to run your Java application
ENTRYPOINT ["java", "-jar", "app.jar"]