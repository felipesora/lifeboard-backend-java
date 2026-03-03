# ====== STAGE 1: Build ======
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copia apenas arquivos necessários primeiro (melhora cache)
COPY pom.xml .
COPY src ./src

# Gera o jar
RUN mvn clean package -DskipTests


# ====== STAGE 2: Runtime ======
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia o jar gerado do stage anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta do Spring
EXPOSE 8080

# Executa aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]