@echo off
echo ========================================
echo    Build Complet avec Tests
echo ========================================
echo.

cd /d "%~dp0.."

echo [1/4] Nettoyage...
if exist "target" rmdir /s /q target
mkdir target

echo.
echo [2/4] Compilation...
docker-compose run --rm maven mvn clean compile

echo.
echo [3/4] Execution des tests...
docker-compose run --rm maven mvn test

echo.
echo [4/4] Generation du rapport de couverture...
docker-compose run --rm maven mvn verify -DskipTests

echo.
echo ========================================
echo    BUILD TERMINE!
echo ========================================
echo.
echo Rapports disponibles:
echo   - target/surefire-reports/ (tests)
echo   - target/site/jacoco/index.html (couverture)
echo.
pause

