FROM eclipse-temurin:23-jdk-alpine AS build

# Copiar los archivos fuente
COPY . .

# Ejecutar la construcción de la aplicación (con Maven)
#RUN mvn clean package -DskipTests

# Crear el contenedor final
FROM openjdk:23-jdk-slim

# Copiar el archivo JAR generado
COPY --from=build /target/AA1-EV01-0.0.1-SNAPSHOT.jar app.jar

# Copiar los templates directamente al directorio de clases
COPY --from=build /src/main/resources/templates /target/classes/templates
#
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
