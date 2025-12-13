FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /root/.m2 /root/.m2
COPY --from=builder /app/pom.xml .
RUN apk add --no-cache bash
COPY mvnw .
COPY .mvn .mvn
RUN chmod +x mvnw

EXPOSE 8080
CMD ["./mvnw", "spring-boot:run", "-Dspring-boot.run.jvmArguments=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"]