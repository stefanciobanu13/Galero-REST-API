FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy JAR file from target directory
COPY target/football-competition-api-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/v1/swagger-ui.html || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
