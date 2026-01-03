@echo off
echo ========================================
echo    Demarrage Jenkins + SonarQube
echo ========================================
echo.
echo ATTENTION: Le premier demarrage peut prendre
echo plusieurs minutes pour telecharger les images.
echo.

cd /d "%~dp0.."

echo [1/3] Demarrage des services...
docker-compose up -d jenkins sonarqube sonarqube-db

echo.
echo [2/3] Attente du demarrage des services...
timeout /t 30 /nobreak

echo.
echo [3/3] Services demarres!
echo.
echo ========================================
echo    URLs d'acces:
echo ========================================
echo.
echo   Jenkins:   http://localhost:8080
echo   SonarQube: http://localhost:9000
echo.
echo ========================================
echo    Credentials par defaut:
echo ========================================
echo.
echo   Jenkins: Recuperer le mot de passe avec:
echo     docker exec jenkins-finance cat /var/jenkins_home/secrets/initialAdminPassword
echo.
echo   SonarQube: admin / admin
echo.
pause

