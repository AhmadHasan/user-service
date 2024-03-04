# Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the executable JAR file and any other necessary files into the container
COPY build/libs/*SNAPSHOT.jar app.jar

# Expose the port that your Spring Boot application will listen on
EXPOSE 8080

# Define the command to run your application when the container starts
CMD ["java", "-jar", "app.jar"]