@echo off
echo ========================================
echo    Compilation du projet Finance
echo ========================================
echo.

cd /d "%~dp0.."

echo [1/2] Construction de l'image Docker...
docker-compose build app-build

echo.
echo [2/2] Compilation terminee!
echo L'image "finance-app:latest" est prete.
echo.
pause

