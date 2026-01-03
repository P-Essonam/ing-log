@echo off
echo ========================================
echo    Execution des tests unitaires
echo ========================================
echo.

cd /d "%~dp0.."

echo [1/3] Preparation de l'environnement...
if not exist "target" mkdir target
if not exist "test-reports" mkdir test-reports

echo.
echo [2/3] Execution des tests avec Maven...
docker-compose run --rm app-test

echo.
echo [3/3] Tests termines!
echo Les rapports sont disponibles dans:
echo   - target/surefire-reports/ (rapports JUnit)
echo   - target/site/jacoco/ (couverture de code)
echo.
pause

