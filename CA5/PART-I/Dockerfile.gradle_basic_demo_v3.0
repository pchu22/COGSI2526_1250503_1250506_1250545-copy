# syntax=docker/dockerfile:1
FROM gradle:8.9-jdk17-focal AS builder

WORKDIR /home/gradle/gradle-tut-rest

COPY gradle gradle
COPY gradlew gradlew
COPY build.gradle build.gradle
COPY settings.gradle settings.gradle

COPY src src

RUN ./gradlew build --no-daemon

FROM openjdk:17.0.2-jdk-slim AS runtime

COPY --from=builder /home/gradle/gradle-tut-rest/build/libs/*.jar app.jar

EXPOSE 59001

CMD ["java", "-cp", "app.jar", "basic_demo.ChatServerApp", "59001"]