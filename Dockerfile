# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Uncomment it when local working
#COPY target/blooddonation-security-3.2.jar app.jar
#COPY .env .env

# Uncomment it when deployment
COPY blooddonation-security-3.2.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
