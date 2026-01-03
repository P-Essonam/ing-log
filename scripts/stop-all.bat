@echo off
echo ========================================
echo    Arret de tous les services
echo ========================================
echo.

cd /d "%~dp0.."

echo Arret des conteneurs...
docker-compose down

echo.
echo Tous les services sont arretes.
echo.
echo Pour supprimer aussi les volumes (donnees):
echo   docker-compose down -v
echo.
pause

