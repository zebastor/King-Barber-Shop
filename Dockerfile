FROM openjdk:23-jdk-slim

COPY target/AA1-EV01-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/templates /app/templates
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
