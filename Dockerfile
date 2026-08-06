FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY . .

RUN mvn -q -pl harvey-system/harvey-system-rest -am -Dmaven.test.skip=true package

FROM eclipse-temurin:21-jre
WORKDIR /app

ENV TZ=Asia/Shanghai

COPY --from=build /workspace/harvey-system/harvey-system-rest/target/harvey-system-rest-*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java ${JAVA_OPTS:-} -jar /app/app.jar"]
