# 🚀 Guide de Démarrage Complet

## Pour les personnes qui ne connaissent pas le projet

Ce guide vous permettra de lancer le projet complet en quelques étapes simples.

---

## ⚠️ Prérequis

### 1. Installer Docker Desktop

1. Téléchargez Docker Desktop : https://www.docker.com/products/docker-desktop
2. Installez-le (suivez les instructions)
3. **Redémarrez votre PC** après l'installation
4. Lancez Docker Desktop (icône sur le bureau ou menu démarrer)
5. Attendez que Docker soit prêt (l'icône devient verte)

### 2. Vérifier que Docker fonctionne

Ouvrez PowerShell ou CMD et tapez :
```
docker --version
```

Si vous voyez un numéro de version, c'est bon !

---

## 🎯 Démarrage en UN CLIC

### Option 1 : Script automatique (RECOMMANDÉ)

1. Ouvrez le dossier du projet : `C:\Users\Hassiatou\Desktop\ing-logi`
2. **Double-cliquez sur** `DEMARRAGE_RAPIDE.bat`
3. Attendez que tout se lance (5-10 minutes la première fois)
4. Les pages web s'ouvriront automatiquement !

---

## 🔧 Démarrage Manuel (si le script ne marche pas)

### Étape 1 : Ouvrir PowerShell

1. Appuyez sur `Windows + X`
2. Cliquez sur "Windows PowerShell"

### Étape 2 : Aller dans le dossier du projet

```powershell
cd C:\Users\Hassiatou\Desktop\ing-logi
```

### Étape 3 : Compiler et tester le projet

```powershell
docker-compose run --rm maven mvn clean test
```

**Attendez** que ça finisse (3-5 minutes la première fois).
Vous devez voir `BUILD SUCCESS` à la fin.

### Étape 4 : Générer le rapport de couverture

```powershell
docker-compose run --rm maven mvn verify -DskipTests
```

### Étape 5 : Démarrer Jenkins et SonarQube

```powershell
docker-compose up -d jenkins sonarqube sonarqube-db
```

### Étape 6 : Attendre le démarrage

Attendez 2-3 minutes, puis ouvrez dans votre navigateur :
- **Jenkins** : http://localhost:8080
- **SonarQube** : http://localhost:9000

---

## 🔑 Mots de passe

### Jenkins (première connexion)

Pour récupérer le mot de passe initial :

```powershell
docker exec jenkins-finance cat /var/jenkins_home/secrets/initialAdminPassword
```

Copiez le mot de passe affiché et collez-le sur la page Jenkins.

### SonarQube

- **Login** : `admin`
- **Mot de passe** : `admin`

(Vous devrez le changer à la première connexion)

---

## ❓ Problèmes courants

### "Docker n'est pas reconnu"

→ Docker Desktop n'est pas installé ou pas démarré.
→ Solution : Installez Docker Desktop et lancez-le.

### "Cannot connect to the Docker daemon"

→ Docker Desktop n'est pas en cours d'exécution.
→ Solution : Lancez Docker Desktop depuis le menu démarrer.

### "Port 8080 already in use"

→ Un autre programme utilise le port 8080.
→ Solution : Fermez l'autre programme ou changez le port dans `docker-compose.yml`.

### "La page ne charge pas"

→ Les services ne sont pas encore prêts.
→ Solution : Attendez 2-3 minutes et réessayez.

---

## 🛑 Arrêter les services

Pour arrêter tout :

```powershell
cd C:\Users\Hassiatou\Desktop\ing-logi
docker-compose down
```

Pour arrêter ET supprimer les données :

```powershell
docker-compose down -v
```

---

## 📁 Structure du Projet

```
ing-logi/
├── DEMARRAGE_RAPIDE.bat    ← Double-cliquez ici pour tout démarrer !
├── src/                    ← Code source Java
├── target/                 ← Fichiers compilés et rapports
│   └── site/jacoco/        ← Rapport de couverture (ouvrir index.html)
├── docker-compose.yml      ← Configuration Docker
├── pom.xml                 ← Configuration Maven
├── Jenkinsfile             ← Pipeline CI/CD
└── README.md               ← Documentation complète
```

---

## 📊 Ce que fait le projet

1. **Application bancaire refactorisée** avec 4 Design Patterns :
   - Strategy (transactions)
   - Factory (création d'objets)
   - Observer (notifications)
   - Singleton (configuration)

2. **163 tests unitaires** avec couverture > 80%

3. **Pipeline CI/CD** avec Jenkins

4. **Analyse de qualité** avec SonarQube

---

## 🆘 Besoin d'aide ?

Si vous avez des problèmes :
1. Vérifiez que Docker Desktop est lancé (icône verte dans la barre des tâches)
2. Redémarrez Docker Desktop
3. Réessayez les commandes

Bonne utilisation ! 🎉

