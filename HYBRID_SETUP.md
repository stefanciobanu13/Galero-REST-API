# Hybrid Setup: Kubernetes Database + Local Backend

Run the MySQL database in your local Kubernetes cluster while running the Spring Boot backend directly on your machine.

---

## 📋 Prerequisites

- Docker Desktop or Rancher with Kubernetes enabled (`kubectl` working)
- Java 17 installed
- Maven 3.9+ installed
- Project built: `mvn clean install -DskipTests`
- `schema-import.sql` file in project root

---

## 🚀 Quick Setup (3 Commands)

### 1. Deploy MySQL to Kubernetes
```bash
kubectl apply -f mysql-deployment.yaml
```

Wait for pod to be ready:
```bash
kubectl get pods -n football-db -w
# Press Ctrl+C when you see: mysql-xxx... 1/1 Running
```

### 2. Import Database Schema
```bash
# Get the MySQL pod name
$MYSQL_POD=$(kubectl get pods -n football-db -o jsonpath='{.items[0].metadata.name}')

# Copy schema file to pod
kubectl cp schema-import.sql football-db/$MYSQL_POD:/tmp/

# Import schema
kubectl exec -it $MYSQL_POD -n football-db -- mysql -uroot -prootpassword football_competition < /tmp/schema-import.sql
```

### 3. Run Spring Boot Locally
```bash
cd d:\Repositories\Galero-backend
mvn spring-boot:run
```

Wait for message: **"Started FootballCompetitionApplication in X seconds"**

---

## ✅ Verify It Works

**Test the API:**
```bash
# Open in browser:
http://localhost:8080/api/v1/swagger-ui.html

# Or use curl:
curl http://localhost:8080/api/v1/editions
```

**Test database connection:**
```bash
# From your machine, connect directly to Kubernetes MySQL:
mysql -h localhost -P 30306 -u football_user -pfootball_password football_competition

# Inside MySQL, list tables:
SHOW TABLES;
```

---

## 🔌 Connection Details

**Spring Boot Backend:**
- URL: http://localhost:8080
- API Context: `/api/v1`
- Swagger UI: http://localhost:8080/api/v1/swagger-ui.html

**MySQL Database:**
- Host: `localhost` (from your machine)
- Port: `30306` (Kubernetes NodePort)
- Database: `football_competition`
- User: `football_user`
- Password: `football_password`

**application.yml already configured with:**
```yaml
datasource:
  url: jdbc:mysql://localhost:30306/football_competition?useSSL=false&serverTimezone=UTC
  username: football_user
  password: football_password
```

---

## 📊 Monitoring

### Check MySQL Status
```bash
# View pod status
kubectl get pods -n football-db

# View pod logs
kubectl logs -f deployment/mysql -n football-db

# Connect to MySQL and verify
mysql -h localhost -P 30306 -u football_user -pfootball_password football_competition
```

### Stop MySQL (Keep Kubernetes Running)
```bash
kubectl scale deployment mysql --replicas=0 -n football-db
```

### Restart MySQL
```bash
kubectl scale deployment mysql --replicas=1 -n football-db
```

### Stop Spring Boot Backend
```bash
# In the terminal where it's running: Press Ctrl+C
```

---

## 🔧 Troubleshooting

### Spring Boot can't connect to MySQL

**Error:** `CommunicationsException: Communications link failure`

**Solutions:**

1. **Verify MySQL pod is running:**
   ```bash
   kubectl get pods -n football-db
   # Should show: STATUS: Running, READY: 1/1
   ```

2. **Test connection from your machine:**
   ```bash
   mysql -h localhost -P 30306 -u football_user -pfootball_password football_competition -e "SELECT 1;"
   ```

3. **Check MySQL logs:**
   ```bash
   kubectl logs -f deployment/mysql -n football-db
   ```

4. **Verify NodePort service is exposed:**
   ```bash
   kubectl get svc -n football-db
   # Should show: mysql-service with port 3306:30306/TCP
   ```

### Schema tables not found

```bash
# Check if schema was imported:
$MYSQL_POD=$(kubectl get pods -n football-db -o jsonpath='{.items[0].metadata.name}')
kubectl exec -it $MYSQL_POD -n football-db -- mysql -uroot -prootpassword football_competition -e "SHOW TABLES;"

# If empty, re-import:
kubectl cp schema-import.sql football-db/$MYSQL_POD:/tmp/
kubectl exec -it $MYSQL_POD -n football-db -- mysql -uroot -prootpassword football_competition < /tmp/schema-import.sql
```

### MySQL pod stuck in pending

```bash
# Check pod events
kubectl describe pod -n football-db -l app=mysql

# Check if Docker has resources
docker stats

# If needed, delete and recreate:
kubectl delete deployment mysql -n football-db
kubectl apply -f mysql-deployment.yaml
```

---

## 🔄 Development Workflow

### When developing locally:

1. **Keep MySQL running in Kubernetes:**
   ```bash
   # In one terminal, monitor:
   kubectl get pods -n football-db -w
   ```

2. **Work on backend locally:**
   ```bash
   # In another terminal:
   cd d:\Repositories\Galero-backend
   mvn spring-boot:run
   ```

3. **Test API:**
   - Swagger UI: http://localhost:8080/api/v1/swagger-ui.html
   - Direct calls: `curl http://localhost:8080/api/v1/...`

4. **Make changes to code:**
   - Edit Java files
   - Spring Boot will auto-reload (if devtools enabled)
   - Or restart with Ctrl+C and `mvn spring-boot:run` again

5. **Access database:**
   ```bash
   mysql -h localhost -P 30306 -u football_user -pfootball_password football_competition
   ```

---

## 📦 Database Credentials

**For application (in `application.yml`):**
```yaml
username: football_user
password: football_password
```

**For direct database access:**
- Root user: `root:rootpassword`
- App user: `football_user:football_password`
- Database: `football_competition`

---

## 🧹 Cleanup

### Stop MySQL (keep Kubernetes cluster)
```bash
kubectl delete namespace football-db
```

### Stop Kubernetes completely
```bash
# In Docker Desktop: Kubernetes → Disable (or reset)
```

### Start Fresh
```bash
# Reapply everything:
kubectl apply -f mysql-deployment.yaml
# (And re-import schema)
```

---

## 📚 Useful Commands

```bash
# Get MySQL pod name
kubectl get pods -n football-db -o jsonpath='{.items[0].metadata.name}'

# Tail MySQL logs
kubectl logs -f deployment/mysql -n football-db

# Execute command in MySQL pod
kubectl exec -it <POD_NAME> -n football-db -- mysql -uroot -prootpassword

# Check service endpoints
kubectl get endpoints mysql-service -n football-db

# Check all MySQL-related resources
kubectl get all -n football-db

# Get detailed pod info
kubectl describe pod <POD_NAME> -n football-db

# Port forward (alternative to NodePort)
kubectl port-forward -n football-db svc/mysql-service 3306:3306
```

---

## ⚡ Performance Tips

- **Local development:** Spring Boot on your machine is faster for active coding
- **Database in K8s:** Keeps database management and networking separate
- **Testing:** Use Swagger UI for quick API testing without curl
- **Debugging:** Spring Boot IDE integrations work normally when running locally

---

**You're ready to go!** Start with Step 1 above. 🚀
