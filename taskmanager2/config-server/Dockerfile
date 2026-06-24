# ── Etapa 1: compilar ──────────────────────────────────────────────
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace

# Copia el wrapper de Maven primero (cache de dependencias)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Descarga dependencias (cacheadas si pom.xml no cambia)
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B -DskipTests

# Copia el código fuente y compila
COPY src/ src/
RUN ./mvnw package -B -DskipTests

# ── Etapa 2: imagen final (solo JRE, mucho más liviana) ────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Crea usuario sin privilegios
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia el jar compilado
COPY --from=build /workspace/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
