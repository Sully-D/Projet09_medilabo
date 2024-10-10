# Étape 1 : Utiliser une image Java officielle comme base
FROM openjdk:21-jdk-slim

# Étape 2 : Ajouter un répertoire pour l'application
WORKDIR /app

# Étape 3 : Copier le fichier JAR généré dans le conteneur
COPY target/note-0.0.1-SNAPSHOT.jar /app/note-0.0.1-SNAPSHOT.jar

# Étape 4 : Exposer le port sur lequel ton service écoute
EXPOSE 8083

# Étape 5 : Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "/app/note-0.0.1-SNAPSHOT.jar"]
