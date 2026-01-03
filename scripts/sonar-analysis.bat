@echo off
echo ========================================
echo    Analyse SonarQube
echo ========================================
echo.
echo Assurez-vous que SonarQube est demarre:
echo   http://localhost:9000
echo.

cd /d "%~dp0.."

set /p SONAR_TOKEN="Entrez votre token SonarQube: "

echo.
echo Lancement de l'analyse...
docker-compose run --rm maven mvn clean verify sonar:sonar -Dsonar.projectKey=finance-refactoring -Dsonar.host.url=http://sonarqube:9000 -Dsonar.login=%SONAR_TOKEN%

echo.
echo Analyse terminee!
echo Voir les resultats sur: http://localhost:9000/dashboard?id=finance-refactoring
echo.
pause

