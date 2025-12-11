@echo off
REM This script helps you build and run the Football Competition API

setlocal enabledelayedexpansion

echo.
echo ===============================================
echo Football Competition API - Setup Helper
echo ===============================================
echo.

REM Check Java installation
echo Checking Java installation...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    exit /b 1
)
echo OK: Java found

echo.
echo To build and run this project, you need Maven installed.
echo.
echo Option 1: Install Maven
echo   - Download from: https://maven.apache.org/download.cgi
echo   - Add to PATH environment variable
echo   - Run: mvn clean install
echo   - Then: mvn spring-boot:run
echo.
echo Option 2: Use an IDE (Recommended)
echo   - Open this project in IntelliJ IDEA or VS Code
echo   - Maven plugins will handle the build automatically
echo   - Run from IDE
echo.
echo Option 3: Use Java directly (if JAR is built)
echo   - Build: mvn clean package
echo   - Run: java -jar target\football-competition-api-1.0.0.jar
echo.
echo ===============================================
