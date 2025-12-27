# Architecture Microservices avec Spring Cloud

Ce projet implémente une architecture microservices complète utilisant Spring Cloud, Eureka, Spring Cloud Gateway, et OpenFeign pour la communication inter-services.

## 📋 Table des matières

- [Vue d'ensemble](#vue-densemble)
- [Architecture](#architecture)
- [Services](#services)
- [Technologies utilisées](#technologies-utilisées)
- [Prérequis](#prérequis)
- [Installation et démarrage](#installation-et-démarrage)
- [Endpoints](#endpoints)
- [Captures d'écran](#captures-décran)
- [Tests](#tests)

## 🎯 Vue d'ensemble

Ce projet démontre une architecture microservices complète avec :
- **Service Discovery** : Eureka Server pour l'enregistrement et la découverte des services
- **API Gateway** : Spring Cloud Gateway comme point d'entrée unique
- **Communication inter-services** : OpenFeign pour les appels REST entre microservices
- **Base de données** : H2 en mémoire pour chaque service

### Diagramme d'architecture

```
┌─────────────┐
│   Client    │
│ (Browser)   │
└──────┬──────┘
       │
       ▼
┌─────────────────────────────────┐
│      Spring Cloud Gateway       │
│         (Port 8888)             │
└──────┬──────────────────┬───────┘
       │                  │
       ▼                  ▼
┌─────────────┐    ┌──────────────┐
│   Eureka    │    │  Load        │
│   Server    │    │  Balancer    │
│ (Port 8761) │    └──────┬───────┘
└──────┬──────┘           │
       │                  │
       │         ┌────────┴────────┐
       │         │                 │
       ▼         ▼                 ▼
┌─────────────┐  ┌──────────────┐  ┌──────────────┐
│SERVICE-     │  │SERVICE-     │  │SERVICE-     │
│CLIENT       │  │VOITURE       │  │VOITURE      │
│(Port 8088)  │  │(Port 8089)   │  │(via Feign)  │
└─────────────┘  └──────┬───────┘  └─────────────┘
                        │
                        ▼
                 ┌──────────────┐
                 │SERVICE-      │
                 │CLIENT        │
                 │(via Feign)   │
                 └──────────────┘
```

## 🏗️ Architecture

### Composants principaux

1. **Eureka Server** : Registre de services pour la découverte dynamique
2. **SERVICE-CLIENT** : Microservice de gestion des clients
3. **SERVICE-VOITURE** : Microservice de gestion des voitures (appelle SERVICE-CLIENT via Feign)
4. **Gateway** : Point d'entrée unique avec routage vers les services

### Flux de requête

1. Le client envoie une requête à la Gateway
2. La Gateway consulte Eureka pour découvrir les instances de services
3. Le Load Balancer sélectionne une instance
4. La requête est routée vers le service approprié
5. Si nécessaire, le service appelle un autre service via OpenFeign

## 🔧 Services

### 1. Eureka Server
- **Port** : 8761
- **Rôle** : Service Discovery
- **URL** : http://localhost:8761

### 2. SERVICE-CLIENT
- **Port** : 8088
- **Rôle** : Gestion des clients (CRUD)
- **Base de données** : H2 (en mémoire)
- **Entités** : Client (id, nom, age)

### 3. SERVICE-VOITURE
- **Port** : 8089
- **Rôle** : Gestion des voitures (CRUD)
- **Base de données** : H2 (en mémoire)
- **Entités** : Voiture (id, marque, matricule, model, id_client)
- **Communication** : Appelle SERVICE-CLIENT via OpenFeign

### 4. Gateway
- **Port** : 8888
- **Rôle** : Point d'entrée unique, routage et load balancing
- **Routage** : Statique et dynamique via Eureka

## 🛠️ Technologies utilisées

- **Java** : 17+
- **Spring Boot** : 3.2.0
- **Spring Cloud** : 2023.0.0
- **Eureka** : Service Discovery
- **Spring Cloud Gateway** : API Gateway
- **OpenFeign** : Client REST déclaratif
- **Spring Data JPA** : Accès aux données
- **H2 Database** : Base de données en mémoire
- **Lombok** : Réduction du code boilerplate
- **Maven** : Gestion des dépendances

## 📦 Prérequis

- JDK 17 ou supérieur
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)
- Navigateur web pour les tests

## 🚀 Installation et démarrage

### 1. Cloner le projet

```bash
git clone https://github.com/HaytamNajam26/TP19.git
cd TP19
```

### 2. Démarrer les services

**Ordre de démarrage recommandé :**

1. **Eureka Server**
```bash
cd eureka-server
mvn spring-boot:run
```

2. **SERVICE-CLIENT**
```bash
cd service-client
mvn spring-boot:run
```

3. **SERVICE-VOITURE**
```bash
cd service-voiture
mvn spring-boot:run
```

4. **Gateway**
```bash
cd gateway
mvn spring-boot:run
```

### 3. Vérifier le démarrage

Attendre 30-60 secondes que tous les services démarrent et s'enregistrent auprès d'Eureka.

## 📡 Endpoints

### Eureka Dashboard
- **URL** : http://localhost:8761
- **Description** : Interface web pour visualiser les services enregistrés

### SERVICE-CLIENT (Direct)
- `GET http://localhost:8088/clients` - Liste tous les clients
- `GET http://localhost:8088/client/{id}` - Récupère un client par ID

### SERVICE-VOITURE (Direct)
- `GET http://localhost:8089/voitures` - Liste toutes les voitures avec données client
- `GET http://localhost:8089/voitures/{id}` - Récupère une voiture par ID
- `GET http://localhost:8089/voitures/client/{id}` - Liste les voitures d'un client
- `POST http://localhost:8089/voitures/{clientId}` - Crée une nouvelle voiture
- `PUT http://localhost:8089/voitures/{id}` - Met à jour une voiture

### Gateway (Routage via Eureka)
- `GET http://localhost:8888/clients` - Route vers SERVICE-CLIENT
- `GET http://localhost:8888/client/{id}` - Route vers SERVICE-CLIENT
- `GET http://localhost:8888/voitures` - Route vers SERVICE-VOITURE
- `GET http://localhost:8888/voitures/{id}` - Route vers SERVICE-VOITURE
- `GET http://localhost:8888/voitures/client/{id}` - Route vers SERVICE-VOITURE

### Gateway (Routage dynamique - optionnel)
Pour activer le routage dynamique, modifier `GatewayApplication.java` :
- `GET http://localhost:8888/SERVICE-CLIENT/clients`
- `GET http://localhost:8888/SERVICE-VOITURE/voitures`

## 📸 Captures d'écran
<img width="2507" height="1552" alt="Screenshot 2025-12-27 191722" src="https://github.com/user-attachments/assets/189be54b-bf33-4450-9c94-8c652bb0215a" />
<img width="2559" height="1139" alt="image" src="https://github.com/user-attachments/assets/69131bc6-76fe-4509-a45d-cb1eaab64e53" />
<img width="2559" height="642" alt="image" src="https://github.com/user-attachments/assets/8511c375-c44a-42ae-83d1-938d888d894d" />




## 🧪 Tests

### Test manuel avec cURL

```bash
# Test SERVICE-CLIENT
curl http://localhost:8088/clients
curl http://localhost:8088/client/1

# Test SERVICE-VOITURE
curl http://localhost:8089/voitures
curl http://localhost:8089/voitures/1

# Test Gateway
curl http://localhost:8888/clients
curl http://localhost:8888/voitures
```


## 📁 Structure du projet

```
TP19/
├── eureka-server/          # Serveur Eureka
│   ├── src/
│   └── pom.xml
├── service-client/         # Microservice Client
│   ├── src/
│   └── pom.xml
├── service-voiture/        # Microservice Voiture
│   ├── src/
│   └── pom.xml
├── gateway/                # API Gateway
│   ├── src/
│   └── pom.xml
└── README.md
```

## 🔍 Fonctionnalités clés

- ✅ Service Discovery avec Eureka
- ✅ API Gateway avec routage dynamique
- ✅ Communication inter-services via OpenFeign
- ✅ Load Balancing automatique
- ✅ Base de données isolée par service (H2)
- ✅ Gestion d'erreurs avec ResponseEntity
- ✅ Enrichissement de données (voitures avec clients)

## 🐛 Dépannage

### Problème : Service ne démarre pas
- Vérifier que le port n'est pas déjà utilisé
- Vérifier que Java 17+ est installé
- Vérifier les logs pour les erreurs

### Problème : Service non visible dans Eureka
- Attendre 30-60 secondes après le démarrage
- Vérifier que `spring.cloud.discovery.enabled=true`
- Vérifier l'URL d'Eureka dans `application.properties`

### Problème : Gateway ne route pas
- Vérifier que les services sont enregistrés dans Eureka
- Vérifier la configuration des routes dans `GatewayApplication.java`
- Redémarrer la Gateway après modification des routes

## 📚 Ressources

- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Eureka Documentation](https://github.com/Netflix/eureka)
- [Spring Cloud Gateway](https://spring.io/projects/spring-cloud-gateway)
- [OpenFeign](https://spring.io/projects/spring-cloud-openfeign)



