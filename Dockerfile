FROM eclipse-temurin:23-jdk AS builder
WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests

FROM eclipse-temurin:23-jre
WORKDIR /app
COPY --from=builder /app/target/app.jar ./app.jar
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
