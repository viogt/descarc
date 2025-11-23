# Stage 1: Build the application
# Use an appropriate Java/Maven version for your project
FROM maven:3.9.5-eclipse-temurin-21 AS builder

# Set the working directory
WORKDIR /app

# Copy all project files
COPY . /app

# Package the application (ensure it creates a single executable JAR)
RUN mvn clean package -DskipTests

# --- (This line is illustrative, do not include it in the file) ---

# Stage 2: Create the final image
# Use a lightweight JRE base image
FROM eclipse-temurin:21-jre-alpine

# Set the working directory for the runtime
WORKDIR /usr/app

# Copy the executable JAR from the 'builder' stage
# Adjust the path '/app/target/*.jar' to match your build output
COPY --from=builder /app/target/*.jar /usr/app/app.jar

# Informational port exposure (Javalin must read Render's PORT environment variable)
EXPOSE 7000

# Define the command to run your application
ENTRYPOINT ["java", "-jar", "app.jar"]