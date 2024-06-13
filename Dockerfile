# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the application JAR file to the container
COPY target/blooddonation-security-3.2.jar app.jar

# Copy the .env file to the container (assuming it's in the same directory as Dockerfile)
#COPY .env .env

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
