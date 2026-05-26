# ESTÁGIO 1: BUILD — compila o projeto
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# copia pom.xml primeiro (aproveitamento de cache Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# copia o código fonte
COPY src ./src

# compila e gera o JAR (pula os testes)
RUN mvn package -DskipTests

# ─────────────────────────────────────────────
# ESTÁGIO 2: RUNTIME — imagem final menor
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# copia APENAS o JAR do estágio de build
COPY --from=build /app/target/*.jar app.jar

# expõe a porta (informativo)
EXPOSE 8080

# comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]