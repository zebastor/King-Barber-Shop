FROM amazoncorretto:22-alpine-jdk

COPY target/AA1-EV01-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]