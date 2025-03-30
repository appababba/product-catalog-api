# Simple Dockerfile - assumes JAR is already built locally (e.g., using 'mvn package')

# Use a Java 21 JRE base image (Alpine version for smaller size)
FROM eclipse-temurin:21-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Define an argument for the JAR file path (finds the JAR in target/)
ARG JAR_FILE=target/product-catalog-api-*.jar

# Copy the built JAR file from the build context (your machine) into the container
# Renames it to app.jar for consistency
COPY ${JAR_FILE} app.jar

# Inform Docker that the container listens on port 8080 at runtime
# (The application inside needs to actually listen on this port)
EXPOSE 8080

# Specify the command to execute when the container starts
# Runs the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]