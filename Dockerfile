# Stage 1: Build the application
FROM maven:3.9.5-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /app

# Copy the project files (pom.xml, source code, etc.)
COPY . /app

# Package the application. The 'app.jar' is a common convention for the final artifact name.
# Replace 'mvn clean package' with your build command (e.g., 'gradlew clean shadowJar' for Gradle)
# Ensure your build process creates a single executable JAR (a "fat" or "shaded" JAR).
RUN mvn clean package -DskipTests