# 🎫 Eventy Tickets Service

Le **Tickets Service** gère le cœur du système de billetterie de la plateforme Eventy. Il est responsable de l'inventaire des billets, de leur génération, de la validation de leur disponibilité et de leur attribution lors des achats.

## 🚀 Fonctionnalités

* **Gestion des Billets** : Création de billets associés à un événement (prix, section, rang, siège).
* **Inventaire** : Suivi en temps réel des billets disponibles, vendus ou réservés.
* **Sécurité** : Génération et validation de QR Codes et codes-barres uniques.
* **Intégration** :
    * **Synchrone (Feign)** : Vérifie l'existence et le statut des événements via `eventy-events-service`.
    * **Asynchrone (Kafka)** : Écoute les événements `PaymentValidatedEvent` pour finaliser une vente.

## 🛠️ Stack Technique

* **Langage** : Java 21
* **Framework** : Spring Boot 3.5.x
* **Base de données** : PostgreSQL 15
* **Communication** : Spring Cloud OpenFeign, Spring Kafka
* **Découverte** : Netflix Eureka Client
* **Outils** : Lombok, Maven, Docker

## ⚙️ Installation et Démarrage

### Prérequis
* JDK 21 installé
* Docker et Docker Compose (pour Kafka, Zookeeper et Postgres)
* Maven

### Démarrage en local (avec Docker Compose)

Ce service dépend de l'infrastructure globale (Eureka, Kafka).

# Depuis la racine du projet backend global
docker-compose up -d --build eventy-tickets-service
Le service sera accessible sur le port **8084**.

### Démarrage autonome (Développement)

1.  Assurez-vous que les services dépendants (Eureka, Postgres, Kafka) sont accessibles.
    
2.  Configurez les variables d'environnement.
    
3.  Lancez l'application :
    
./mvnw spring-boot:run   

🔧 Configuration
----------------

Les variables d'environnement principales (définies dans docker-compose.yml) :

📚 API Reference
----------------

### Billets (/tickets)

*   POST /tickets : Mettre un billet en vente (Nécessite Auth).
    
*   GET /tickets/event/{eventId} : Lister les billets disponibles pour un événement.
    
*   GET /tickets/{id} : Détail d'un billet.
    
*   POST /tickets/{id}/buy : Acheter un billet (change le statut).
    

🔄 Architecture Événementielle (Kafka)
--------------------------------------

### Consommateur (Consumer)

*   **Topic :** ticket-sold
    
*   **Groupe :** tickets-service-group
    
*   **Action :** Met à jour le statut du billet à SOLD lorsqu'une transaction est validée par le service Transactions.
    

© 2025 Eventy Project
