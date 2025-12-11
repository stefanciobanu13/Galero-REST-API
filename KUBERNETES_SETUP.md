# Kubernetes MySQL Setup & Spring Boot Integration Guide

## Overview
This guide covers:
1. Setting up MySQL in Kubernetes
2. Importing the database schema
3. Configuring Spring Boot to connect to Kubernetes MySQL
4. Deploying the Spring Boot app in Kubernetes

---

## Prerequisites

- Docker Desktop with Kubernetes enabled (or Rancher)
- `kubectl` command-line tool installed
- `helm` package manager (optional but recommended)
- Project JAR built: `target/football-competition-api-1.0.0.jar`

### Verify Kubernetes is Running
```bash
kubectl cluster-info
kubectl get nodes
```

---

## Part 1: Deploy MySQL to Kubernetes

### Option A: Using Helm (Recommended - Easier)

#### Step 1: Add Bitnami Helm Repository
```bash
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo update
```

#### Step 2: Create Namespace
```bash
kubectl create namespace football-db
```

#### Step 3: Create values.yaml for MySQL
Create a file `mysql-values.yaml`:

```yaml
auth:
  rootPassword: rootpassword
  database: football_competition
  username: football_user
  password: football_password

primary:
  persistence:
    enabled: true
    size: 10Gi
    storageClass: "standard"

service:
  type: ClusterIP
  port: 3306

resources:
  limits:
    cpu: 500m
    memory: 512Mi
  requests:
    cpu: 250m
    memory: 256Mi
```

#### Step 4: Install MySQL via Helm
```bash
helm install mysql bitnami/mysql \
  -f mysql-values.yaml \
  -n football-db
```

#### Step 5: Verify MySQL is Running
```bash
kubectl get pods -n football-db
kubectl get svc -n football-db
```

You should see:
```
NAME                READY   STATUS    RESTARTS   AGE
mysql-primary-0     1/1     Running   0          2m
mysql-secondary-0   1/1     Running   0          2m
```

---

### Option B: Using Manual YAML Manifest

Create a file `mysql-deployment.yaml`:

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: football-db

---
apiVersion: v1
kind: Secret
metadata:
  name: mysql-secret
  namespace: football-db
type: Opaque
stringData:
  mysql-root-password: rootpassword
  mysql-password: football_password

---
apiVersion: v1
kind: ConfigMap
metadata:
  name: mysql-config
  namespace: football-db
data:
  my.cnf: |
    [mysqld]
    bind-address = 0.0.0.0
    port = 3306

---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql
  namespace: football-db
  labels:
    app: mysql
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - name: mysql
        image: mysql:8.0
        ports:
        - containerPort: 3306
        env:
        - name: MYSQL_ROOT_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-secret
              key: mysql-root-password
        - name: MYSQL_DATABASE
          value: football_competition
        - name: MYSQL_USER
          value: football_user
        - name: MYSQL_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-secret
              key: mysql-password
        volumeMounts:
        - name: mysql-storage
          mountPath: /var/lib/mysql
        - name: mysql-config
          mountPath: /etc/mysql
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
      volumes:
      - name: mysql-storage
        emptyDir: {}
      - name: mysql-config
        configMap:
          name: mysql-config

---
apiVersion: v1
kind: Service
metadata:
  name: mysql-service
  namespace: football-db
  labels:
    app: mysql
spec:
  selector:
    app: mysql
  ports:
  - port: 3306
    targetPort: 3306
  type: ClusterIP

---
apiVersion: v1
kind: Service
metadata:
  name: mysql-service-nodeport
  namespace: football-db
  labels:
    app: mysql
spec:
  selector:
    app: mysql
  ports:
  - port: 3306
    targetPort: 3306
    nodePort: 30306
  type: NodePort
```

Deploy it:
```bash
kubectl apply -f mysql-deployment.yaml
```

Verify:
```bash
kubectl get all -n football-db
```

---

## Part 2: Import Database Schema

### Step 1: Get MySQL Pod Name
```bash
kubectl get pods -n football-db
# Copy the pod name, e.g., mysql-0 or mysql-primary-0
```

### Step 2: Create SQL Import File
Create `schema-import.sql` with the complete schema from `db-schema.txt`

### Step 3: Import Schema into Kubernetes MySQL

#### Option A: Using kubectl exec
```bash
# Copy schema file into pod
kubectl cp schema-import.sql football-db/<POD_NAME>:/tmp/

# Connect and import
kubectl exec -it <POD_NAME> -n football-db -- mysql -uroot -prootpassword football_competition < /tmp/schema-import.sql
```

#### Option B: Using Port Forwarding
```bash
# Forward port
kubectl port-forward -n football-db svc/mysql-service 3306:3306 &

# Then connect locally and import
mysql -h 127.0.0.1 -u root -prootpassword football_competition < schema-import.sql

# Stop port forwarding
fg
# Press Ctrl+C
```

#### Option C: Direct SQL Command
```bash
kubectl exec -it <POD_NAME> -n football-db -- mysql -uroot -prootpassword -e "
CREATE DATABASE IF NOT EXISTS football_competition;
USE football_competition;
-- Paste all CREATE TABLE statements here
"
```

### Step 4: Verify Schema Import
```bash
kubectl exec -it <POD_NAME> -n football-db -- mysql -uroot -prootpassword football_competition -e "SHOW TABLES;"
```

You should see all 8 tables:
- editions
- players
- teams
- team_players
- matches
- goals
- attendance

---

## Part 3: Create Spring Boot Deployment

### Step 1: Build Docker Image for Spring Boot

Create `Dockerfile` in project root:

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy JAR file
COPY target/football-competition-api-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Step 2: Build Docker Image

```bash
# Navigate to project directory
cd d:\Repositories\Galero-backend

# Build the image
docker build -t football-api:1.0.0 .

# Verify image
docker images | grep football-api
```

### Step 3: Load Image to Kubernetes (if using local K8s)

For Docker Desktop/Rancher:
```bash
# The image should be automatically available
# Verify by running a test container
docker run -d --name test-api football-api:1.0.0
docker stop test-api
docker rm test-api
```

---

## Part 4: Deploy Spring Boot to Kubernetes

Create `spring-boot-deployment.yaml`:

```yaml
apiVersion: v1
kind: Namespace
metadata:
  name: football-app

---
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
  namespace: football-app
data:
  application.yml: |
    spring:
      application:
        name: football-competition-api
      jpa:
        hibernate:
          ddl-auto: validate
        show-sql: false
        properties:
          hibernate:
            dialect: org.hibernate.dialect.MySQLDialect
            format_sql: true
      datasource:
        url: jdbc:mysql://mysql-service.football-db.svc.cluster.local:3306/football_competition?useSSL=false&serverTimezone=UTC
        username: football_user
        password: football_password
        driver-class-name: com.mysql.cj.jdbc.Driver

    server:
      port: 8080
      servlet:
        context-path: /api/v1

    logging:
      level:
        root: INFO
        com.galero: DEBUG
        org.hibernate.SQL: DEBUG

    springdoc:
      api-docs:
        path: /docs
      swagger-ui:
        path: /swagger-ui.html
        enabled: true

---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: football-api
  namespace: football-app
  labels:
    app: football-api
spec:
  replicas: 2
  selector:
    matchLabels:
      app: football-api
  template:
    metadata:
      labels:
        app: football-api
    spec:
      containers:
      - name: football-api
        image: football-api:1.0.0
        imagePullPolicy: Never  # Use local image
        ports:
        - containerPort: 8080
          name: http
        env:
        - name: SPRING_CONFIG_LOCATION
          value: file:/etc/config/application.yml
        volumeMounts:
        - name: app-config
          mountPath: /etc/config
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /api/v1/swagger-ui.html
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /api/v1/swagger-ui.html
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 5
      volumes:
      - name: app-config
        configMap:
          name: app-config

---
apiVersion: v1
kind: Service
metadata:
  name: football-api-service
  namespace: football-app
  labels:
    app: football-api
spec:
  selector:
    app: football-api
  ports:
  - port: 8080
    targetPort: 8080
    protocol: TCP
  type: LoadBalancer

---
apiVersion: v1
kind: Service
metadata:
  name: football-api-service-nodeport
  namespace: football-app
  labels:
    app: football-api
spec:
  selector:
    app: football-api
  ports:
  - port: 8080
    targetPort: 8080
    nodePort: 30080
  type: NodePort
```

### Step 5: Deploy to Kubernetes

```bash
# Apply deployment
kubectl apply -f spring-boot-deployment.yaml

# Verify deployment
kubectl get all -n football-app

# Check pod status
kubectl get pods -n football-app -w

# View pod logs
kubectl logs -f deployment/football-api -n football-app

# Describe pod for details
kubectl describe pod -n football-app -l app=football-api
```

---

## Part 5: Access the Application

### Option A: Using Port Forwarding
```bash
# Forward port from service
kubectl port-forward -n football-app svc/football-api-service 8080:8080

# Access at: http://localhost:8080/api/v1/swagger-ui.html
```

### Option B: Using NodePort
```bash
# Get local IP
kubectl cluster-info

# Access at: http://<LOCAL_IP>:30080/api/v1/swagger-ui.html
# Or: http://localhost:30080/api/v1/swagger-ui.html
```

### Option C: Using Ingress (Advanced)
Create `ingress.yaml`:

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: football-api-ingress
  namespace: football-app
spec:
  rules:
  - host: football-api.local
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: football-api-service
            port:
              number: 8080
```

Apply and access:
```bash
kubectl apply -f ingress.yaml
# Add to /etc/hosts: 127.0.0.1 football-api.local
# Visit: http://football-api.local/api/v1/swagger-ui.html
```

---

## Part 6: Add Sample Data (Optional)

### Method 1: Using kubectl exec
```bash
# Get MySQL pod
MYSQL_POD=$(kubectl get pods -n football-db -o jsonpath='{.items[0].metadata.name}')

# Port forward
kubectl port-forward -n football-db $MYSQL_POD 3306:3306 &

# Run sample data script
mysql -h 127.0.0.1 -u football_user -pfootball_password football_competition < SAMPLE_DATA.sql

# Stop port forwarding
kill %1
```

### Method 2: Using ConfigMap and Init Container
Add to `spring-boot-deployment.yaml`:

```yaml
initContainers:
- name: wait-for-db
  image: busybox:1.28
  command: ['sh', '-c', 'until nc -z mysql-service.football-db.svc.cluster.local 3306; do echo waiting for MySQL; sleep 2; done']
```

---

## Troubleshooting

### Check MySQL Pod Logs
```bash
kubectl logs -n football-db <MYSQL_POD_NAME>
```

### Check Spring Boot Pod Logs
```bash
kubectl logs -n football-app -l app=football-api
```

### Access MySQL from Spring Boot Pod
```bash
# Exec into Spring Boot pod
kubectl exec -it <SPRING_POD_NAME> -n football-app -- bash

# Test MySQL connection (if curl available)
curl mysql-service.football-db.svc.cluster.local:3306
```

### Verify Network Connectivity
```bash
# Check DNS resolution
kubectl exec -it <POD_NAME> -n football-app -- nslookup mysql-service.football-db.svc.cluster.local

# Test connectivity
kubectl exec -it <POD_NAME> -n football-app -- nc -zv mysql-service.football-db.svc.cluster.local 3306
```

### Check Resource Usage
```bash
kubectl top nodes
kubectl top pods -n football-app
kubectl top pods -n football-db
```

---

## Monitoring & Logging

### Stream Logs
```bash
# Real-time logs
kubectl logs -f deployment/football-api -n football-app

# Last 100 lines
kubectl logs --tail=100 deployment/football-api -n football-app
```

### Access Kubernetes Dashboard
```bash
# Install dashboard (if not already)
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.7.0/aio/deploy/recommended.yaml

# Create admin user
kubectl apply -f - <<EOF
apiVersion: v1
kind: ServiceAccount
metadata:
  name: admin-user
  namespace: kubernetes-dashboard
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin-user
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: admin-user
  namespace: kubernetes-dashboard
EOF

# Get token
kubectl -n kubernetes-dashboard create token admin-user

# Port forward
kubectl proxy

# Access: http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/
```

---

## Quick Reference Commands

```bash
# View all resources
kubectl get all -n football-db
kubectl get all -n football-app

# Scale deployment
kubectl scale deployment football-api --replicas=3 -n football-app

# Update deployment
kubectl set image deployment/football-api \
  football-api=football-api:2.0.0 -n football-app

# Delete deployment
kubectl delete deployment football-api -n football-app
kubectl delete namespace football-app

# Get detailed status
kubectl describe pod -n football-app -l app=football-api

# Execute command in pod
kubectl exec -it <POD_NAME> -n football-app -- bash

# Copy files
kubectl cp <POD_NAME>:/app/logs ./logs -n football-app
```

---

## Summary of Kubernetes DNS Names

Use these DNS names from within Kubernetes:

- **MySQL Service**: `mysql-service.football-db.svc.cluster.local:3306`
- **MySQL Root**: `root:rootpassword`
- **MySQL User**: `football_user:football_password`
- **Database**: `football_competition`

These are automatically resolved within the cluster.

---

## Next Steps

1. **Deploy MySQL** using Helm or manual YAML
2. **Import Database Schema** into MySQL
3. **Build Docker Image** of Spring Boot app
4. **Deploy Spring Boot** to Kubernetes
5. **Access Swagger UI** and test endpoints
6. **(Optional) Add Ingress** for better access
7. **(Optional) Add Monitoring** with Prometheus

---

**You now have a complete containerized solution running in Kubernetes!** 🚀
