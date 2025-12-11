#!/usr/bin/env pwsh

# Football Competition API - Kubernetes Quick Deploy Script (PowerShell)

Write-Host ""
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "Football Competition API - Kubernetes Deployer" -ForegroundColor Cyan
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host ""

# Step 1: Check if kubectl is available
Write-Host "Step 1: Checking Kubernetes cluster..." -ForegroundColor Yellow
try {
    $null = kubectl version 2>$null
} catch {
    Write-Host "ERROR: kubectl is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install kubectl to proceed" -ForegroundColor Red
    exit 1
}

try {
    $null = kubectl cluster-info 2>$null
} catch {
    Write-Host "ERROR: Cannot connect to Kubernetes cluster" -ForegroundColor Red
    Write-Host "Make sure Docker Desktop with Kubernetes is running" -ForegroundColor Red
    exit 1
}
Write-Host "OK: Kubernetes cluster is accessible" -ForegroundColor Green

# Step 2: Build Docker image
Write-Host ""
Write-Host "Step 2: Building Docker image..." -ForegroundColor Yellow

$jarPath = "target\football-competition-api-1.0.0.jar"
if (-not (Test-Path $jarPath)) {
    Write-Host "ERROR: JAR file not found at $jarPath" -ForegroundColor Red
    Write-Host "Please run: mvn clean install -DskipTests" -ForegroundColor Red
    exit 1
}

$output = & docker build -t football-api:1.0.0 . 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Failed to build Docker image" -ForegroundColor Red
    Write-Host $output
    exit 1
}
Write-Host "OK: Docker image built successfully" -ForegroundColor Green

# Step 3: Deploy MySQL
Write-Host ""
Write-Host "Step 3: Deploying MySQL to Kubernetes..." -ForegroundColor Yellow

$output = & kubectl apply -f mysql-deployment.yaml 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Failed to deploy MySQL" -ForegroundColor Red
    Write-Host $output
    exit 1
}
Write-Host "OK: MySQL deployment created" -ForegroundColor Green

# Step 4: Wait for MySQL to be ready
Write-Host ""
Write-Host "Step 4: Waiting for MySQL to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

Write-Host "Checking MySQL pod status..." -ForegroundColor Cyan
& kubectl get pods -n football-db
Write-Host ""

# Step 5: Import schema
Write-Host "Step 5: Importing database schema..." -ForegroundColor Yellow

$mysqlPods = & kubectl get pods -n football-db -o jsonpath='{.items[0].metadata.name}' 2>$null
if (-not $mysqlPods) {
    Write-Host "WARNING: Could not find MySQL pod. Skipping schema import." -ForegroundColor Yellow
} else {
    Write-Host "MySQL Pod: $mysqlPods" -ForegroundColor Cyan
    
    if (-not (Test-Path "schema-import.sql")) {
        Write-Host "WARNING: schema-import.sql not found" -ForegroundColor Yellow
        Write-Host "Please create schema-import.sql with your database schema" -ForegroundColor Yellow
        Write-Host "And run import manually:" -ForegroundColor Yellow
        Write-Host "  kubectl cp schema-import.sql football-db/$mysqlPods`:/tmp/" -ForegroundColor Cyan
        Write-Host "  kubectl exec -it $mysqlPods -n football-db -- mysql -uroot -prootpassword football_competition < schema.sql" -ForegroundColor Cyan
    } else {
        Write-Host "Copying schema file..." -ForegroundColor Cyan
        & kubectl cp schema-import.sql football-db/$mysqlPods`:/tmp/ 2>&1
        
        Write-Host "Importing schema..." -ForegroundColor Cyan
        & kubectl exec $mysqlPods -n football-db -- mysql -uroot -prootpassword football_competition -e "SOURCE /tmp/schema-import.sql;" 2>&1
    }
}

# Step 6: Deploy Spring Boot
Write-Host ""
Write-Host "Step 6: Deploying Spring Boot application..." -ForegroundColor Yellow

$output = & kubectl apply -f spring-boot-deployment.yaml 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Failed to deploy Spring Boot app" -ForegroundColor Red
    Write-Host $output
    exit 1
}
Write-Host "OK: Spring Boot deployment created" -ForegroundColor Green

# Step 7: Wait for Spring Boot pods to be ready
Write-Host ""
Write-Host "Step 7: Waiting for Spring Boot pods to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

Write-Host "Checking Spring Boot pod status..." -ForegroundColor Cyan
& kubectl get pods -n football-app
Write-Host ""

# Summary
Write-Host ""
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host "DEPLOYMENT COMPLETE!" -ForegroundColor Green
Write-Host "===============================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Access your API:" -ForegroundColor Yellow
Write-Host ""
Write-Host "  Option 1 (Port Forwarding):" -ForegroundColor Cyan
Write-Host "  kubectl port-forward -n football-app svc/football-api-service 8080:8080" -ForegroundColor Green
Write-Host "  Then visit: http://localhost:8080/api/v1/swagger-ui.html" -ForegroundColor Green
Write-Host ""
Write-Host "  Option 2 (NodePort):" -ForegroundColor Cyan
Write-Host "  http://localhost:30080/api/v1/swagger-ui.html" -ForegroundColor Green
Write-Host ""
Write-Host "Useful commands:" -ForegroundColor Yellow
Write-Host "  View all resources: kubectl get all -n football-app" -ForegroundColor Cyan
Write-Host "  View logs: kubectl logs -f deployment/football-api -n football-app" -ForegroundColor Cyan
Write-Host "  Describe pod: kubectl describe pod -n football-app -l app=football-api" -ForegroundColor Cyan
Write-Host "  Scale deployment: kubectl scale deployment football-api --replicas=3 -n football-app" -ForegroundColor Cyan
Write-Host ""
Write-Host "===============================================" -ForegroundColor Cyan
