# Use official OpenJDK image as base
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy Gradle wrapper files
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./

# Make gradlew executable
RUN chmod +x ./gradlew

# Download dependencies
RUN ./gradlew dependencies

# Copy source code
COPY src ./src

# Build the application
RUN ./gradlew build -x test

# Expose the port the app runs on
EXPOSE 8070

# Command to run the application
ENTRYPOINT ["java", "-jar", "build/libs/weatherDemo-0.0.1-SNAPSHOT.jar"] 