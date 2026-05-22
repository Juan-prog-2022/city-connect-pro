FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copiar wrapper y pom primero
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .

# Dar permisos
RUN chmod +x mvnw

# Descargar dependencias
RUN ./mvnw dependency:go-offline

# Copiar código fuente
COPY src src

# Build
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java -jar target/*.jar"]