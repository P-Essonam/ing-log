@echo off
chcp 65001 >nul
title Finance App - Demarrage Complet

echo.
echo ╔══════════════════════════════════════════════════════════════╗
echo ║                                                              ║
echo ║        PROJET FINANCE APP - DEMARRAGE COMPLET                ║
echo ║                                                              ║
echo ║   Refactoring + Design Patterns + CI/CD                      ║
echo ║                                                              ║
echo ╚══════════════════════════════════════════════════════════════╝
echo.

:: Verifier que Docker est installe et fonctionne
echo [1/6] Verification de Docker...
docker --version >nul 2>&1
if errorlevel 1 (
    echo.
    echo ❌ ERREUR: Docker n'est pas installe ou n'est pas demarre!
    echo.
    echo Veuillez:
    echo   1. Installer Docker Desktop: https://www.docker.com/products/docker-desktop
    echo   2. Demarrer Docker Desktop
    echo   3. Relancer ce script
    echo.
    pause
    exit /b 1
)
echo    ✓ Docker est disponible

:: Verifier que Docker est en cours d'execution
docker info >nul 2>&1
if errorlevel 1 (
    echo.
    echo ❌ ERREUR: Docker n'est pas en cours d'execution!
    echo.
    echo Veuillez demarrer Docker Desktop et relancer ce script.
    echo.
    pause
    exit /b 1
)
echo    ✓ Docker est en cours d'execution

:: Se placer dans le bon repertoire
cd /d "%~dp0"
echo.
echo [2/6] Repertoire: %CD%

:: Compiler et tester le projet
echo.
echo [3/6] Compilation et tests du projet Java...
echo    (Cela peut prendre quelques minutes la premiere fois)
echo.
docker-compose run --rm maven mvn clean test -q

if errorlevel 1 (
    echo.
    echo ❌ ERREUR lors de la compilation/tests!
    pause
    exit /b 1
)
echo.
echo    ✓ Compilation et tests reussis!

:: Generer le rapport de couverture
echo.
echo [4/6] Generation du rapport de couverture JaCoCo...
docker-compose run --rm maven mvn verify -DskipTests -q
echo    ✓ Rapport de couverture genere dans: target/site/jacoco/index.html

:: Demarrer Jenkins et SonarQube
echo.
echo [5/6] Demarrage de Jenkins et SonarQube...
echo    (Le premier demarrage peut prendre 5-10 minutes)
echo.
docker-compose up -d jenkins sonarqube sonarqube-db

:: Attendre que les services demarrent
echo.
echo [6/6] Attente du demarrage des services...
echo    Veuillez patienter...

:: Attendre 30 secondes
timeout /t 30 /nobreak >nul

echo.
echo ╔══════════════════════════════════════════════════════════════╗
echo ║                                                              ║
echo ║                    DEMARRAGE TERMINE!                        ║
echo ║                                                              ║
echo ╚══════════════════════════════════════════════════════════════╝
echo.
echo ┌────────────────────────────────────────────────────────────┐
echo │  ACCES AUX SERVICES                                        │
echo ├────────────────────────────────────────────────────────────┤
echo │                                                            │
echo │  Jenkins:    http://localhost:8080                         │
echo │              (mot de passe ci-dessous)                     │
echo │                                                            │
echo │  SonarQube:  http://localhost:9000                         │
echo │              Login: admin / admin                          │
echo │                                                            │
echo │  Couverture: target\site\jacoco\index.html                 │
echo │                                                            │
echo └────────────────────────────────────────────────────────────┘
echo.

:: Recuperer le mot de passe Jenkins
echo ┌────────────────────────────────────────────────────────────┐
echo │  MOT DE PASSE JENKINS (initial)                            │
echo ├────────────────────────────────────────────────────────────┤
docker exec jenkins-finance cat /var/jenkins_home/secrets/initialAdminPassword 2>nul
if errorlevel 1 (
    echo │  En attente... Reessayez dans 1-2 minutes avec:         │
    echo │  docker exec jenkins-finance cat /var/jenkins_home/secrets/initialAdminPassword
)
echo └────────────────────────────────────────────────────────────┘
echo.

:: Ouvrir les pages web
echo Ouverture des pages dans le navigateur...
start http://localhost:8080
start http://localhost:9000

echo.
echo ┌────────────────────────────────────────────────────────────┐
echo │  COMMANDES UTILES                                          │
echo ├────────────────────────────────────────────────────────────┤
echo │                                                            │
echo │  Arreter les services:                                     │
echo │    docker-compose down                                     │
echo │                                                            │
echo │  Relancer les tests:                                       │
echo │    docker-compose run --rm maven mvn test                  │
echo │                                                            │
echo │  Voir les logs Jenkins:                                    │
echo │    docker logs jenkins-finance                             │
echo │                                                            │
echo └────────────────────────────────────────────────────────────┘
echo.
pause

