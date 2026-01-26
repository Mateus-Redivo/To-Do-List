# Etapa de build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa de execução
FROM eclipse-temurin:21-jre

# Instalar curl e netcat para health checks e wait script
RUN apt-get update && apt-get install -y curl netcat-openbsd && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY scripts/wait-for-mysql.sh ./wait-for-mysql.sh
RUN sed -i 's/\r$//' wait-for-mysql.sh && chmod +x wait-for-mysql.sh

# Exponha a porta padrão do Spring Boot (ajuste se necessário)
EXPOSE 8080

# Comando para rodar a aplicação com wait script
ENTRYPOINT ["./wait-for-mysql.sh", "backend-db", "3306", "java", "-jar", "app.jar"]