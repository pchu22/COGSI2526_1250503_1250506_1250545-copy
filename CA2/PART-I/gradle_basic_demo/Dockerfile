# syntax=docker/dockerfile:1
FROM cimg/openjdk:17.0

COPY build/libs/*.jar app.jar

EXPOSE 59001

CMD ["java", "-cp", "app.jar", "basic_demo.ChatServerApp", "59001"]