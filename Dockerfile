FROM amazoncorretto:23-alpine-jdk

COPY target/AA1-EV01-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 16058
ENTRYPOINT ["java", "-jar", "/app.jar"]