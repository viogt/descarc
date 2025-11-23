# Use a JDK image to build
FROM eclipse-temurin:17 AS build

WORKDIR /app

# Copy Maven files and resolve dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# Use a lighter JRE image for runtime
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/your-app.jar ./your-app.jar

# Expose port (make sure your Javalin app binds to $PORT or 0.0.0.0)
EXPOSE 8080

# Command to run your app
CMD ["java", "-jar", "your-app.jar"]
