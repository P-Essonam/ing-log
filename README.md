# Finance App Refactoring

## Projet d'Intégration Continue - Refactoring et Pipeline DevOps

Application bancaire refactorisée utilisant les design patterns **Strategy**, **Factory**, **Observer** et **Singleton**, avec un pipeline CI/CD complet (Jenkins + SonarQube).

---

## Table des matières

- [Contexte](#contexte)
- [Architecture](#architecture)
- [Design Patterns Implémentés](#design-patterns-implémentés)
- [Structure du Projet](#structure-du-projet)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Exécution](#exécution)
- [Tests](#tests)
- [Pipeline CI/CD](#pipeline-cicd)
- [SonarQube](#sonarqube)
- [Auteurs](#auteurs)

---

## Contexte

Ce projet transforme une application Java "spaghetti" (monolithique) en une architecture modulaire et maintenable, en appliquant :

1. **Refactoring** avec 3 design patterns fondamentaux
2. **Tests unitaires** avec couverture > 80%
3. **Pipeline CI/CD** avec Jenkins
4. **Analyse de qualité** avec SonarQube

---

## Architecture

### Diagramme de classes simplifié

```
┌─────────────────────────────────────────────────────────────────┐
│                         MainApp                                  │
│                            │                                     │
│                    ┌───────┴───────┐                            │
│                    │               │                             │
│              BankingService   ConfigurationManager               │
│                    │              (Singleton)                    │
│         ┌─────────┼─────────┐                                   │
│         │         │         │                                    │
│  UserFactory  AccountFactory  TransactionService                │
│  (Factory)    (Factory)           │                             │
│                            ┌──────┼──────┐                      │
│                            │      │      │                       │
│                     DepositStrategy │ TransferStrategy          │
│                     WithdrawStrategy                            │
│                        (Strategy)                               │
│                            │                                     │
│                   ┌────────┴────────┐                           │
│                   │                 │                            │
│              AuditLogger    NotificationService                 │
│                     (Observer)                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Flux de données

```
Utilisateur → MainApp → BankingService → TransactionService
                                              │
                              ┌───────────────┼───────────────┐
                              │               │               │
                        Strategy        Observer Pattern
                        Pattern         ┌─────┴─────┐
                    ┌─────┴─────┐       │           │
                    │     │     │   AuditLogger  Notification
              Deposit Withdraw Transfer            Service
```

---

## Design Patterns Implémentés

### 1. Pattern Strategy (Transactions)

**Problème résolu**: Éviter les conditionnelles multiples pour différents types de transactions.

**Implémentation**:
- `TransactionStrategy` - Interface commune
- `DepositStrategy` - Logique de dépôt
- `WithdrawStrategy` - Logique de retrait avec vérification du solde
- `TransferStrategy` - Logique de transfert entre comptes

### 2. Pattern Factory (Création d'objets)

**Problème résolu**: Centraliser et valider la création d'utilisateurs et de comptes.

**Implémentation**:
- `UserFactory` - Création d'utilisateurs avec validation
- `AccountFactory` - Création de comptes avec dépôt initial

### 3. Pattern Observer (Notifications)

**Problème résolu**: Découpler la logique métier des systèmes de notification et d'audit.

**Implémentation**:
- `TransactionObserver` - Interface observer
- `AuditLogger` - Journalisation des transactions
- `NotificationService` - Notifications utilisateur

### 4. Pattern Singleton (Bonus - Configuration)

**Problème résolu**: Garantir une instance unique du gestionnaire de configuration.

**Implémentation**:
- `ConfigurationManager` - Gestionnaire de configuration thread-safe

---

## Structure du Projet

```
ing-logi/
├── src/
│   ├── main/java/com/university/finance/
│   │   ├── config/
│   │   │   └── ConfigurationManager.java      # Singleton
│   │   ├── model/
│   │   │   ├── Account.java
│   │   │   ├── Transaction.java
│   │   │   └── User.java
│   │   ├── pattern/
│   │   │   ├── factory/
│   │   │   │   ├── AccountFactory.java
│   │   │   │   └── UserFactory.java
│   │   │   ├── observer/
│   │   │   │   ├── AuditLogger.java
│   │   │   │   ├── NotificationService.java
│   │   │   │   └── TransactionObserver.java
│   │   │   └── strategy/
│   │   │       ├── DepositStrategy.java
│   │   │       ├── TransactionStrategy.java
│   │   │       ├── TransferStrategy.java
│   │   │       └── WithdrawStrategy.java
│   │   ├── service/
│   │   │   ├── BankingService.java
│   │   │   └── TransactionService.java
│   │   └── MainApp.java
│   └── test/java/com/university/finance/
│       ├── config/
│       ├── model/
│       ├── pattern/
│       └── service/
├── docker-compose.yml          # Jenkins + SonarQube
├── Jenkinsfile                 # Pipeline CI/CD
├── pom.xml                     # Configuration Maven
├── sonar-project.properties    # Configuration SonarQube
└── README.md
```

---

## Prérequis

- **Docker Desktop** (seul prérequis!)
- **Git** (optionnel, pour cloner le projet)

> **Note**: Vous n'avez PAS besoin d'installer Java ni Maven sur votre PC. Tout s'exécute dans Docker!

---

## Installation et Utilisation avec Docker

### Scripts disponibles (dossier `scripts/`)

| Script | Description |
|--------|-------------|
| `full-build.bat` | Compile + Tests + Couverture |
| `test.bat` | Exécute les tests unitaires |
| `build.bat` | Compile l'application |
| `run-maven.bat` | Console Maven interactive |
| `start-cicd.bat` | Démarre Jenkins + SonarQube |
| `stop-all.bat` | Arrête tous les services |
| `sonar-analysis.bat` | Lance l'analyse SonarQube |

### 1. Build complet avec tests

Double-cliquez sur `scripts\full-build.bat` ou exécutez :

```powershell
cd C:\Users\Hassiatou\Desktop\ing-logi
docker-compose run --rm maven mvn clean verify
```

### 2. Exécuter uniquement les tests

```powershell
docker-compose run --rm maven mvn test
```

### 3. Compiler le projet

```powershell
docker-compose run --rm maven mvn clean compile
```

### 4. Console Maven interactive

```powershell
docker-compose run --rm maven bash
# Puis dans le conteneur:
mvn clean compile
mvn test
mvn package
```

### 5. Générer le rapport de couverture

```powershell
docker-compose run --rm maven mvn verify
# Le rapport est disponible dans target/site/jacoco/index.html
```

---

## Exécution de l'application

### Option 1: Via Docker

```powershell
# Construire l'image
docker-compose build app-build

# Exécuter (mode interactif pour l'application console)
docker run -it finance-app:latest
```

### Option 2: Via Maven dans Docker

```powershell
docker-compose run --rm maven mvn exec:java -Dexec.mainClass="com.university.finance.MainApp"
```

### Menu de l'application

```
╔══════════════════════════════════════════╗
║    Finance Refactoring App               ║
║           Version 1.0.0                  ║
╚══════════════════════════════════════════╝

=== Menu de Connexion ===
1. Se connecter
2. Créer un compte
0. Quitter
```

---

## Tests

### Exécuter tous les tests

```bash
mvn test
```

### Tests avec couverture JaCoCo

```bash
mvn verify
```

### Liste des classes de test (20+ tests)

| Classe de Test | Nombre de Tests |
|----------------|-----------------|
| `UserTest` | 11 |
| `AccountTest` | 15 |
| `TransactionTest` | 10 |
| `DepositStrategyTest` | 10 |
| `WithdrawStrategyTest` | 11 |
| `TransferStrategyTest` | 16 |
| `UserFactoryTest` | 14 |
| `AccountFactoryTest` | 12 |
| `AuditLoggerTest` | 8 |
| `NotificationServiceTest` | 9 |
| `TransactionServiceTest` | 14 |
| `BankingServiceTest` | 18 |
| `ConfigurationManagerTest` | 12 |

**Total : 150+ tests unitaires**

---

## Pipeline CI/CD

### Démarrer l'infrastructure Docker

Double-cliquez sur `scripts\start-cicd.bat` ou exécutez :

```powershell
docker-compose up -d jenkins sonarqube sonarqube-db
```

> **Note**: Le premier démarrage peut prendre 5-10 minutes pour télécharger les images Docker.

### Services disponibles

| Service | URL | Credentials |
|---------|-----|-------------|
| Jenkins | http://localhost:8080 | admin / (voir logs) |
| SonarQube | http://localhost:9000 | admin / admin |

### Configurer Jenkins

1. Accéder à http://localhost:8080
2. Récupérer le mot de passe initial :
   ```powershell
   docker exec jenkins-finance cat /var/jenkins_home/secrets/initialAdminPassword
   ```
3. Installer les plugins recommandés + JaCoCo + SonarQube Scanner
4. Configurer Maven et JDK dans "Global Tool Configuration"
5. Ajouter le serveur SonarQube dans "Configure System"
6. Créer un nouveau job Pipeline pointant vers le Jenkinsfile

### Arrêter les services

```powershell
docker-compose down
# Ou pour supprimer aussi les données:
docker-compose down -v
```

### Stages du Pipeline

```
┌──────────┐   ┌───────┐   ┌─────────────┐   ┌──────────────┐
│ Checkout │ → │ Build │ → │ Unit Tests  │ → │ Code Quality │
└──────────┘   └───────┘   └─────────────┘   └──────────────┘
                                                    │
                           ┌─────────┐   ┌─────────────────┐
                           │ Package │ ← │ Quality Gate    │
                           └─────────┘   └─────────────────┘
```

---

## SonarQube

### Première connexion

1. Accéder à http://localhost:9000
2. Se connecter avec admin/admin
3. Changer le mot de passe
4. Créer un token d'accès (Mon Compte > Sécurité > Générer Token)

### Lancer l'analyse manuellement

Double-cliquez sur `scripts\sonar-analysis.bat` ou exécutez :

```powershell
docker-compose run --rm maven mvn clean verify sonar:sonar `
  -Dsonar.projectKey=finance-refactoring `
  -Dsonar.host.url=http://sonarqube:9000 `
  -Dsonar.login=VOTRE_TOKEN
```

### Métriques surveillées

- **Bugs** : 0 (objectif)
- **Vulnerabilities** : 0 (objectif)
- **Code Smells** : Minimiser
- **Coverage** : > 80%
- **Duplications** : < 3%

---

## Anti-patterns corrigés

| Anti-pattern Original | Solution Appliquée |
|-----------------------|-------------------|
| Classe monolithique (150+ lignes) | Séparation en packages (model, service, pattern) |
| Variables statiques globales | Injection de dépendances via constructeurs |
| Duplication de code (journalisation) | Pattern Observer centralisé |
| Violation SRP | Chaque classe a une responsabilité unique |
| Logique métier mélangée avec UI | Séparation Service/Présentation |

---

## Violations SOLID corrigées

| Principe | Violation Originale | Correction |
|----------|---------------------|------------|
| **S**ingle Responsibility | Une classe pour tout | Classes séparées par responsabilité |
| **O**pen/Closed | Code rigide | Stratégies extensibles |
| **L**iskov Substitution | N/A | Interfaces respectées |
| **I**nterface Segregation | N/A | Interfaces spécialisées |
| **D**ependency Inversion | Dépendances concrètes | Injection via interfaces |

---
