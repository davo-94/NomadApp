FROM openjdk:17-jdk-slim

WORKDIR /app

# Copiar el JAR construido
COPY backend/build/libs/nomadapp-backend-1.0.0.jar app.jar

# Exponer puerto
EXPOSE 8080

# Variables de entorno requeridas
ENV ORACLE_HOST=""
ENV ORACLE_PORT="1521"
ENV ORACLE_SERVICE_NAME=""
ENV ORACLE_USER=""
ENV ORACLE_PASSWORD=""
ENV ORACLE_WALLET_PATH=""
ENV JWT_SECRET=""

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
