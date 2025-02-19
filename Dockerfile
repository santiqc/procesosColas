# Etapa de construcción
FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17
WORKDIR /app

ENV TZ=America/Bogota
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY --from=builder /app/target/colas-0.0.1-SNAPSHOT.jar .
EXPOSE 8080

CMD ["java", "-Duser.timezone=America/Bogota", "-jar", "colas-0.0.1-SNAPSHOT.jar"]
