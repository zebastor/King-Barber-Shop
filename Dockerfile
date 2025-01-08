FROM eclipse-temurin:23-jdk-alpine AS build

#COPY target/AA1-EV01-0.0.1-SNAPSHOT.jar app.jar
#COPY src/main/resources/templates /app/templates
COPY . .

#ENTRYPOINT ["java", "-jar", "app.jar"]
RUN mvn clean package -DskiTests
FROM openjdk:23-jdk-slim
COPY --from=build /target/AA1-EV01-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
