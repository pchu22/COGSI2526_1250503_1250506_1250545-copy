# syntax=docker/dockerfile:1
FROM cimg/openjdk:21.0.9

COPY build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]