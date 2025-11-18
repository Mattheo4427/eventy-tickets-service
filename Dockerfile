# =========================
# 1️⃣ Build Stage
# =========================
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Optimisation du cache : on télécharge les dépendances avant le code source
COPY pom.xml .
RUN mvn dependency:go-offline

# Compilation du code
COPY src ./src
RUN mvn clean package -DskipTests

# =========================
# 2️⃣ Runtime Stage
# =========================
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copie du JAR généré (le nom dépend de votre pom.xml, ici on utilise un wildcard)
COPY --from=builder /app/target/tickets-service-*.jar app.jar

# Port standard pour ce service (on a dit 8082 dans le plan)
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]