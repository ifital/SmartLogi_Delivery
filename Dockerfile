# Étape 1 : build avec Maven
FROM maven:3.9.3-eclipse-temurin-20 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Étape 2 : image finale JRE
FROM eclipse-temurin:20-jre-alpine
WORKDIR /app
COPY --from=build /app/target/sdms-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
