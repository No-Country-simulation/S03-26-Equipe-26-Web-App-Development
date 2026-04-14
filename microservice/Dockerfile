# Java 21 base image
FROM eclipse-temurin:21-jdk

# Diretório da aplicação
WORKDIR /app

# Copia o jar gerado pelo Maven
COPY target/*.jar app.jar

# Porta do microservice
EXPOSE 8090

# Executa a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]