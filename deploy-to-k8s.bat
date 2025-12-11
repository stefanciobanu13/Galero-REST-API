@echo off
REM Football Competition API - Kubernetes Quick Deploy Script
REM This script automates the deployment process

setlocal enabledelayedexpansion

echo.
echo ===============================================
echo Football Competition API - Kubernetes Deployer
echo ===============================================
echo.

REM Check if kubectl is available
kubectl version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: kubectl is not installed or not in PATH
    echo Please install kubectl to proceed
    exit /b 1
)

echo Step 1: Checking Kubernetes cluster...
kubectl cluster-info >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Cannot connect to Kubernetes cluster
    echo Make sure Docker Desktop with Kubernetes is running
    exit /b 1
)
echo OK: Kubernetes cluster is accessible

echo.
echo Step 2: Building Docker image...
if not exist "target\football-competition-api-1.0.0.jar" (
    echo ERROR: JAR file not found
    echo Please run: mvn clean install -DskipTests
    exit /b 1
)

docker build -t football-api:1.0.0 .
if %errorlevel% neq 0 (
    echo ERROR: Failed to build Docker image
    exit /b 1
)
echo OK: Docker image built successfully

echo.
echo Step 3: Deploying MySQL to Kubernetes...
kubectl apply -f mysql-deployment.yaml
if %errorlevel% neq 0 (
    echo ERROR: Failed to deploy MySQL
    exit /b 1
)
echo OK: MySQL deployment created

echo.
echo Step 4: Waiting for MySQL to be ready...
timeout /t 15 /nobreak
echo Checking MySQL pod status...
kubectl get pods -n football-db
echo.

echo Step 5: Importing database schema...
REM Get the MySQL pod name
for /f "tokens=1" %%A in ('kubectl get pods -n football-db -o jsonpath={.items[0].metadata.name}') do set MYSQL_POD=%%A

echo MySQL Pod: !MYSQL_POD!

REM Create the schema SQL file if it doesn't exist
if not exist "schema-import.sql" (
    echo.
    echo WARNING: schema-import.sql not found
    echo Please create schema-import.sql with your database schema
    echo and run import manually:
    echo.
    echo   kubectl cp schema-import.sql football-db/!MYSQL_POD!:/tmp/
    echo   kubectl exec -it !MYSQL_POD! -n football-db -- mysql -uroot -prootpassword football_competition ^< /tmp/schema-import.sql
    echo.
) else (
    echo Copying schema file...
    kubectl cp schema-import.sql football-db/!MYSQL_POD!:/tmp/
    
    echo Importing schema...
    kubectl exec -it !MYSQL_POD! -n football-db -- mysql -uroot -prootpassword football_competition ^< schema-import.sql
)

echo.
echo Step 6: Deploying Spring Boot application...
kubectl apply -f spring-boot-deployment.yaml
if %errorlevel% neq 0 (
    echo ERROR: Failed to deploy Spring Boot app
    exit /b 1
)
echo OK: Spring Boot deployment created

echo.
echo Step 7: Waiting for Spring Boot pods to be ready...
timeout /t 15 /nobreak
echo Checking Spring Boot pod status...
kubectl get pods -n football-app
echo.

echo.
echo ===============================================
echo DEPLOYMENT COMPLETE!
echo ===============================================
echo.
echo Access your API:
echo.
echo   Option 1 (Port Forwarding):
echo   kubectl port-forward -n football-app svc/football-api-service 8080:8080
echo   Then visit: http://localhost:8080/api/v1/swagger-ui.html
echo.
echo   Option 2 (NodePort):
echo   http://localhost:30080/api/v1/swagger-ui.html
echo.
echo Useful commands:
echo   View all resources: kubectl get all -n football-app
echo   View logs: kubectl logs -f deployment/football-api -n football-app
echo   Describe pod: kubectl describe pod -n football-app -l app=football-api
echo   Scale deployment: kubectl scale deployment football-api --replicas=3 -n football-app
echo.
echo ===============================================
