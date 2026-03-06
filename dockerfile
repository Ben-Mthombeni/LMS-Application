# Use official OpenJDK 21 runtime as a base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
# Make sure your JAR is built with JDK 21 and located in target/
COPY target/*.jar app.jar

# Expose the port Spring Boot will run on
EXPOSE 9090

# Optional: set default JVM options (adjust memory if needed)
ENV JAVA_OPTS="-Xmx512m"

# Run the Spring Boot application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]