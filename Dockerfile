# 1. Schimbăm baza către una stabilă și optimizată pentru ARM (Pi)
FROM eclipse-temurin:17-jre-jammy

# 2. Instalăm curl pentru ca Healthcheck-ul să funcționeze
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 3. Copiem JAR-ul (Asigură-te că numele fișierului este exact acesta în /target)
COPY target/football-competition-api-1.0.0.jar app.jar

# 4. Expunem portul tău specific
EXPOSE 4444

# 5. Health check corectat
# Am schimbat start-period la 30s (Spring Boot pe Pi poate porni mai greu)
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:4444/api/v1/swagger-ui.html || exit 1

# 6. Run application cu profil de producție
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]