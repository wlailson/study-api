# ==========================================
# 1. Stage: Build
# ==========================================
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

# Copia primeiro o pom para aproveitar o cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Agora copia o código
COPY src ./src

# Gera o JAR
RUN mvn clean package -DskipTests


# ==========================================
# 2. Stage: Runtime
# ==========================================
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Copia somente o JAR gerado no stage anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]