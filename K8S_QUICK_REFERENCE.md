# Kubernetes Deployment - Quick Reference Guide

## 📋 Prerequisites Checklist

- [ ] Docker Desktop or Rancher with Kubernetes enabled
- [ ] `kubectl` installed and configured
- [ ] Project built: `mvn clean install -DskipTests`
- [ ] Schema created in `schema-import.sql`

---

## 🚀 Quick Start (5 Steps)

### Step 1: Build JAR
```bash
cd d:\Repositories\Galero-backend
mvn clean install -DskipTests
```

### Step 2: Build Docker Image
```bash
docker build -t football-api:1.0.0 .
```

### Step 3: Deploy MySQL
```bash
kubectl apply -f mysql-deployment.yaml
kubectl get pods -n football-db -w
```

### Step 4: Import Schema
```bash
# Wait for MySQL pod to be ready, then:
$MYSQL_POD=$(kubectl get pods -n football-db -o jsonpath='{.items[0].metadata.name}')
kubectl cp schema-import.sql football-db/$MYSQL_POD:/tmp/
kubectl exec -it $MYSQL_POD -n football-db -- mysql -uroot -prootpassword football_competition < schema-import.sql
```

### Step 5: Deploy Spring Boot
```bash
kubectl apply -f spring-boot-deployment.yaml
kubectl get pods -n football-app -w
```

---

## 🌐 Access the Application

### Port Forwarding (Easiest)
```bash
kubectl port-forward -n football-app svc/football-api-service 8080:8080
# Visit: http://localhost:8080/api/v1/swagger-ui.html
```

### NodePort (Direct)
```bash
# Visit: http://localhost:30080/api/v1/swagger-ui.html
```

### Check Service Status
```bash
kubectl get svc -n football-app
kubectl get svc -n football-db
```

---

## 📊 Monitoring & Troubleshooting

### View Deployments
```bash
kubectl get deployments -n football-app
kubectl get deployments -n football-db
```

### View Pod Status
```bash
kubectl get pods -n football-app
kubectl get pods -n football-db
```

### Stream Logs
```bash
# Spring Boot logs
kubectl logs -f deployment/football-api -n football-app

# MySQL logs
kubectl logs -f deployment/mysql -n football-db

# Follow specific pod
kubectl logs -f <POD_NAME> -n football-app
```

### Describe Pod (Detailed Info)
```bash
kubectl describe pod -n football-app -l app=football-api
```

### Check Pod Events
```bash
kubectl get events -n football-app
kubectl get events -n football-db
```

---

## 🔧 Common Tasks

### Scale Deployment
```bash
# Scale to 3 replicas
kubectl scale deployment football-api --replicas=3 -n football-app

# View replicas
kubectl get deployment football-api -n football-app
```

### Update Image
```bash
# Rebuild image and tag it
docker build -t football-api:2.0.0 .

# Update deployment
kubectl set image deployment/football-api \
  football-api=football-api:2.0.0 -n football-app
```

### Restart Pods
```bash
kubectl rollout restart deployment/football-api -n football-app
```

### Delete Everything
```bash
# Delete app namespace (deletes everything in it)
kubectl delete namespace football-app

# Delete database namespace
kubectl delete namespace football-db

# Verify
kubectl get namespaces
```

---

## 🐛 Troubleshooting Common Issues

### Issue: Pod not starting
```bash
# Check pod status
kubectl describe pod <POD_NAME> -n football-app

# Check logs
kubectl logs <POD_NAME> -n football-app

# Check events
kubectl get events -n football-app
```

### Issue: Connection refused to MySQL
```bash
# Test connectivity
kubectl exec -it <SPRING_POD> -n football-app -- bash

# Inside pod, test:
nslookup mysql-service.football-db.svc.cluster.local
nc -zv mysql-service.football-db.svc.cluster.local 3306
```

### Issue: ImagePullBackOff error
**Solution**: Make sure you built the image with `docker build -t football-api:1.0.0 .`

Verify:
```bash
docker images | grep football-api
```

### Issue: MySQL pod stuck in pending
```bash
# Check if Docker has enough resources
docker stats

# Check node resources
kubectl top nodes
```

### Issue: Schema import failed
```bash
# Manual import steps:
MYSQL_POD=$(kubectl get pods -n football-db -o jsonpath='{.items[0].metadata.name}')

# Connect and check
kubectl exec -it $MYSQL_POD -n football-db -- mysql -uroot -prootpassword

# Inside MySQL:
USE football_competition;
SHOW TABLES;
```

---

## 📈 Performance & Resources

### Check Resource Usage
```bash
# Node resources
kubectl top nodes

# Pod resources
kubectl top pods -n football-app
kubectl top pods -n football-db

# Deployment resources
kubectl describe deployment football-api -n football-app
```

### Adjust Resource Limits
Edit `spring-boot-deployment.yaml`:
```yaml
resources:
  requests:
    memory: "512Mi"
    cpu: "250m"
  limits:
    memory: "1Gi"
    cpu: "500m"
```

Then apply:
```bash
kubectl apply -f spring-boot-deployment.yaml
```

---

## 🔐 Database Access from Outside Cluster

### Via Port Forwarding
```bash
kubectl port-forward -n football-db svc/mysql-service 3306:3306

# Then connect locally:
mysql -h 127.0.0.1 -u football_user -pfootball_password football_competition
```

### Via NodePort (Already exposed on 30306)
```bash
# From your machine:
mysql -h localhost -P 30306 -u football_user -pfootball_password football_competition
```

---

## 🔑 Credentials Reference

**MySQL Credentials:**
- Root User: `root:rootpassword`
- App User: `football_user:football_password`
- Database: `football_competition`

**Service DNS Names (inside cluster):**
- MySQL: `mysql-service.football-db.svc.cluster.local:3306`
- Spring Boot: `football-api-service.football-app.svc.cluster.local:8080`

---

## 📋 Useful kubectl Commands

```bash
# Get resources
kubectl get pods -n football-app
kubectl get svc -n football-app
kubectl get deployment -n football-app
kubectl get all -n football-app

# Describe resources
kubectl describe pod <POD_NAME> -n football-app
kubectl describe svc <SERVICE_NAME> -n football-app

# Edit resources
kubectl edit deployment football-api -n football-app

# View YAML
kubectl get deployment football-api -n football-app -o yaml

# Port forward
kubectl port-forward pod/<POD_NAME> 8080:8080 -n football-app
kubectl port-forward svc/<SERVICE_NAME> 8080:8080 -n football-app

# Execute commands
kubectl exec -it <POD_NAME> -n football-app -- bash
kubectl exec <POD_NAME> -n football-app -- command

# View logs
kubectl logs -f <POD_NAME> -n football-app
kubectl logs -f deployment/football-api -n football-app

# Copy files
kubectl cp <POD_NAME>:/path/in/pod /path/on/local -n football-app
kubectl cp /path/on/local <POD_NAME>:/path/in/pod -n football-app

# Resource management
kubectl apply -f file.yaml
kubectl delete -f file.yaml
kubectl replace -f file.yaml
kubectl patch resource-type resource-name -p '{"spec":{"replicas":3}}'

# Monitoring
kubectl top nodes
kubectl top pods -n football-app
kubectl get events -n football-app
```

---

## 🎯 Automated Deployment

Use the provided deployment scripts:

**Windows (Batch):**
```bash
deploy-to-k8s.bat
```

**PowerShell:**
```bash
.\deploy-to-k8s.ps1
```

**Manual (Linux/Mac):**
```bash
chmod +x deploy-to-k8s.ps1
./deploy-to-k8s.ps1
```

---

## 📚 Additional Resources

- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Docker Hub MySQL Image](https://hub.docker.com/_/mysql)
- [Spring Boot Kubernetes](https://spring.io/guides/kubernetes/)
- [kubectl Cheat Sheet](https://kubernetes.io/docs/reference/kubectl/cheatsheet/)

---

## ✅ Verification Checklist

After deployment, verify everything is working:

```bash
# Check namespaces created
kubectl get namespaces

# Check MySQL is running
kubectl get pods -n football-db
kubectl get svc -n football-db

# Check Spring Boot is running
kubectl get pods -n football-app
kubectl get svc -n football-app

# Test connectivity from Spring Boot pod
kubectl exec -it <SPRING_POD> -n football-app -- nc -zv mysql-service.football-db.svc.cluster.local 3306

# Port forward and test API
kubectl port-forward -n football-app svc/football-api-service 8080:8080
# Visit: http://localhost:8080/api/v1/swagger-ui.html
```

---

**You're ready to run your app in Kubernetes!** 🚀
