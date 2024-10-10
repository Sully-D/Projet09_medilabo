# Projet Microservices MediLabo

## Description
Ce projet est composé de plusieurs microservices construits avec Spring Boot, Spring Cloud, et Docker. Les microservices interagissent via Eureka pour la découverte des services et sont gérer via Spring Cloud Gateway.

### Microservices :
1. **Backend** : Gère les informations des patients et interagit avec MySQL.
2. **Frontend** : Interface utilisateur pour gérer les patients, les notes médicales et les niveaux de risque.
3. **Gateway** : Un Spring Cloud Gateway qui route les requêtes vers les services appropriés.
4. **Note** : Gère les notes médicales et les stocke dans MongoDB.
5. **Risklevel** : Calcule le niveau de risque des patients en fonction des données médicales.
6. **Eureka** : Le serveur de découverte des services qui enregistre tous les microservices.

## Prérequis
- Docker
- Docker Compose
- JDK 21
- Maven

## Mise en place

### Étape 1 : Compiler les services
Naviguez dans chaque répertoire de microservice et exécutez la commande suivante :
```bash
mvn clean package
```

### Étape 2 : Construire les images Docker
Pour chaque service, construisez l'image Docker avec la commande :
```bash
docker build -t medilabo/<nom-du-service> .
```
Remplacez `<nom-du-service>` par le nom approprié du service (par exemple `backend`, `frontend`, `gateway`, etc.).

### Étape 3 : Configuration Docker Compose
Utilisez Docker Compose pour configurer et exécuter tous les services. Assurez-vous que le fichier `docker-compose.yml` est correctement configuré pour inclure les services nécessaires et les paramètres réseau.

### Étape 4 : Lancer les services
Une fois les images créées, exécutez la commande suivante :
```bash
docker-compose up
```

## Configuration
### Serveur Eureka
Le serveur de découverte des services Eureka fonctionne sur le port `8010`. Tous les services s'enregistrent auprès d'Eureka.

### Gateway
L'API Gateway fonctionne sur le port `8080` et route les requêtes vers les microservices suivants :
- `/api/patients/**` -> Backend
- `/api/notes/**` -> Note
- `/api/risklevels/**` -> Risklevel

### Backend
- Fonctionne sur le port `8081`
- Utilise MySQL pour la persistance des données (`jdbc:mysql://192.168.1.108:3306/medilabo`)

### Frontend
- Fonctionne sur le port `8082`
- Fournit une interface utilisateur pour la gestion des patients et des notes médicales

### Note
- Fonctionne sur le port `8083`
- Stocke les notes dans MongoDB (`mongodb://192.168.1.108:27017/medicalnotesdb`)

### Risklevel
- Fonctionne sur le port `8084`
- Effectue l'analyse du niveau de risque des patients en fonction des données médicales
