# Dockerfile multi-stage pour l'application Finance
# Stage 1: Build avec Maven
FROM maven:3.8-openjdk-11 AS builder

WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml .

# Télécharger les dépendances (mise en cache Docker)
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Compiler et packager l'application
RUN mvn clean package -DskipTests

# Stage 2: Image de production légère
FROM openjdk:11-jre-slim

WORKDIR /app

# Copier le JAR depuis le stage de build
COPY --from=builder /app/target/spaghetti-finance-1.0.0.jar ./app.jar

# Exposer le port (si nécessaire pour des extensions futures)
EXPOSE 8081

# Commande par défaut
CMD ["java", "-jar", "app.jar"]

