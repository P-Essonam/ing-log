@echo off
echo ========================================
echo    Console Maven Interactive
echo ========================================
echo.
echo Vous pouvez executer toutes les commandes Maven:
echo   - mvn clean compile
echo   - mvn test
echo   - mvn package
echo   - mvn verify
echo   - mvn sonar:sonar
echo.
echo Tapez 'exit' pour quitter.
echo ========================================
echo.

cd /d "%~dp0.."

docker-compose run --rm maven bash

