# Stage 2: Create the final image
# Use a lightweight JRE base image (like Temurin-21-jre-alpine for Java 21)
FROM eclipse-temurin:21-jre-alpine

# Set the working directory for the runtime
WORKDIR /usr/app

# Copy the executable JAR from the 'build' stage
# Adjust the path '/app/target/*.jar' and the final name 'app.jar' to match your build output
COPY --from=build /app/target/*.jar /usr/app/app.jar

# Your Javalin app usually listens on a specific port (e.g., 7000)
# EXPOSE the port your Javalin application starts on
EXPOSE 7000

# Define the command to run your application when the container starts
# Replace 'app.jar' with the name of your executable JAR if it's different
ENTRYPOINT ["java", "-jar", "app.jar"]