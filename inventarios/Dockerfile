FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

COPY . .

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar inventarios.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "inventarios.jar"]