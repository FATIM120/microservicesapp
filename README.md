# Architecture Microservices pour Système de Bibliothèque

**Développé par :** Fatima Zohra KAJJOUT  
**Programme académique :** Master 2SI - INSEA Rabat  
**Période :** Janvier 2026  

## Présentation du Projet

Cette réalisation propose une implémentation sophistiquée d'une architecture microservices dédiée à la gestion d'un système de bibliothèque, exploitant les frameworks Spring Boot, les bases de données MySQL, la plateforme Apache Kafka et l'orchestration Docker.

## Vision Architecturale

```
Gateway:9999 ━━━ Eureka:8761 ━━━ Kafka:9092
     ┃              ┃              ┃
     ┗━━━━━━━━━━━━━━┳━━━━━━━━━━━━━━┛
                    ┃
    ┏━━━━━━━━━━━━━━━┳━━━━━━━━━━━━━━━┓
    ┃               ┃               ┃
User:8082      Book:8081     Emprunt:8083 ═══ Notification:8084
    ┃               ┃               ┃
MySQL_user    MySQL_book    MySQL_emprunter
```

## Écosystème Technologique

- **Spring Boot** 3.4.1 - Socle de développement microservices
- **MySQL** 8.0 - Solution de persistance (architecture distribuée sur 3 instances)
- **Apache Kafka** 7.4.0 - Plateforme de communication événementielle
- **Netflix Eureka** - Mécanisme de découverte de services
- **Spring Cloud Gateway** - Passerelle d'API unifiée
- **Docker Compose** - Orchestration d'infrastructure

## Architecture des Services

### Service Utilisateurs (Port 8082)
```bash
GET    /users              # Consultation du répertoire utilisateurs
POST   /users              # Enregistrement de nouveaux utilisateurs
```

### Service Catalogue (Port 8081)
```bash
GET    /books              # Exploration du catalogue bibliographique
POST   /books              # Enrichissement du catalogue
```

### Service Emprunts (Port 8083)
```bash
GET    /emprunts                    # Historique des transactions
POST   /emprunts/{userId}/{bookId} # Initiation d'une transaction d'emprunt
```

### Service Notifications (Port 8084)
```java
@KafkaListener(topics = "emprunt-created")
public void handleEmprunt(EmpruntEvent event) {
    // Orchestration des notifications automatisées
}
```

## Modélisation des Données

### Base db_user
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);
```

### Base db_book
```sql
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(255) NOT NULL,
    auteur VARCHAR(255)
);
```

### Base db_emprunter
```sql
CREATE TABLE emprunter (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    emprunt_date DATE NOT NULL
);
```

## Procédure d'Installation

### Configuration Préalable
```bash
java -version     # Environnement Java 17+
mvn -version      # Gestionnaire Maven 3.6+
docker --version  # Plateforme Docker 20.10+
```

### Processus de Construction
```bash
git clone https://github.com/FATIM120/microservicesapp.git
cd microservicesapp

# Phase de compilation des artefacts
./user/mvnw -f ./user/pom.xml clean package -DskipTests
./book/mvnw -f ./book/pom.xml clean package -DskipTests
./emprunter/mvnw -f ./emprunter/pom.xml clean package -DskipTests
./eurika/mvnw -f ./eurika/pom.xml clean package -DskipTests
./gateway/mvnw -f ./gateway/pom.xml clean package -DskipTests
./notification-service/mvnw -f ./notification-service/pom.xml clean package -DskipTests
```

### Initialisation de l'Écosystème
```bash
docker-compose up -d --build
docker-compose ps
```

## Protocole de Validation

### Scénario 1 : Enregistrement d'Utilisateur
```powershell
$user = @{
    name  = "Fatima Zohra KAJJOUT"
    email = "fatima@insea.ac.ma"
}

Invoke-RestMethod -Uri "http://localhost:8082/users" -Method POST -ContentType "application/json" -Body ($user | ConvertTo-Json)
```

### Scénario 2 : Catalogage d'Ouvrage
```powershell
$book = @{
    titre  = "Guide des Microservices"
    auteur = "Spécialiste"
}

Invoke-RestMethod -Uri "http://localhost:8081/books" -Method POST -ContentType "application/json" -Body ($book | ConvertTo-Json)
```

### Scénario 3 : Transaction d'Emprunt
```powershell
$emprunt = Invoke-RestMethod -Uri "http://localhost:8083/emprunts/1/1" -Method POST
Write-Output "Transaction d'emprunt initialisée: $emprunt"
```

### Scénario 4 : Validation du Flux Kafka
```bash
# Observation des événements en temps réel
docker exec -it kafka kafka-console-consumer --bootstrap-server localhost:9092 --topic emprunt-created --from-beginning

# Consultation des journaux de notification
docker logs notification-service --tail 10
```

## Diagnostic et Validation

```bash
# Endpoints de service
curl http://localhost:8082/users    # Service Utilisateurs
curl http://localhost:8081/books    # Service Catalogue  
curl http://localhost:8083/emprunts # Service Emprunts

# Infrastructure de soutien
curl http://localhost:8761          # Registre Eureka
curl http://localhost:9999          # Passerelle API
```

## Supervision Opérationnelle

```bash
# Surveillance en continu des journaux
docker logs -f user-service
docker logs -f book-service
docker logs -f emprunt-service
docker logs -f notification-service

# Évaluation de l'état des services
docker-compose ps
```

## Patterns Architecturaux Déployés

- ✅ Isolation des Données par Service (3 instances MySQL distinctes)
- ✅ Découverte de Services (Registre Eureka)
- ✅ Passerelle API (Spring Cloud Gateway)
- ✅ Architecture Événementielle (Apache Kafka)
- ✅ Disjoncteur de Protection (Spring Cloud)
- ✅ Conteneurisation (Écosystème Docker)

## Paramétrisation

```yaml
# Éléments de configuration essentiels
SPRING_DATASOURCE_URL=jdbc:mysql://db-user:3306/db_user
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://eureka-service:8761/eureka
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:29092
```

## Bénéfices Démonstrés

Cette implémentation illustre avec élégance :
- L'autonomie des microservices
- La dualité communication synchrone (REST) et asynchrone (Kafka)
- L'isolation stratégique des données
- La capacité d'extension horizontale
- La résilience architecturale

## Perspectives d'Amélioration

- [ ] Intégration Spring Security
- [ ] Métriques Prometheus
- [ ] Traçage Distribué
- [ ] Pipeline CI/CD
- [ ] Automatisation des Tests

## Coordonnées

**Fatima Zohra KAJJOUT**  
Courriel: fzkajjout@insea.ac.ma  
