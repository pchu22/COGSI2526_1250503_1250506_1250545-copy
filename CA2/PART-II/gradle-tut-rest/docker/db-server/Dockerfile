# syntax=docker/dockerfile:1
FROM openjdk:21-ea-21-jdk-slim

COPY docker/db-server/h2-2.2.224.jar h2.jar

RUN mkdir -p /h2database/payroll/data

COPY ../../CA2-Part2/PayrollDB.mv.db /h2database/payroll/data

VOLUME /h2database/payroll/data

EXPOSE 9092

CMD ["java", "-cp", "h2.jar", "org.h2.tools.Server", "-tcp", "-tcpAllowOthers", "-tcpPort", "9092", "-baseDir", "/h2database/payroll/data"]
