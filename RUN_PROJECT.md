# 🚀 How to Run the Project

## ⚠️ Prerequisite: Install Maven

Maven is required to build and run this project. If you don't have it installed:

👉 **See [MAVEN_INSTALLATION.md](MAVEN_INSTALLATION.md)** for step-by-step installation guide

### Quick Check
Run this to see if Maven is installed:
```bash
mvn --version
```

If you get "command not found", follow the Maven installation guide above.

---

## Step 1: Database Setup

### Create Database
```sql
CREATE DATABASE football_competition;
USE football_competition;
```

### Import Schema
Run the provided schema from `db-schema.txt`:
```sql
-- Copy and paste the entire schema from db-schema.txt into MySQL
```

### (Optional) Add Sample Data
```sql
-- Run SAMPLE_DATA.sql for test data
SOURCE SAMPLE_DATA.sql;
```

---

## Step 2: Configure Database Connection

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/football_competition?useSSL=false&serverTimezone=UTC
    username: root
    password: your_password
```

Replace `your_password` with your MySQL root password.

---

## Step 3: Build the Project

```bash
cd D:\Repositories\Galero-backend

# Clean build
mvn clean install -DskipTests
```

Wait for the build to complete. You should see:
```
BUILD SUCCESS
```

---

## Step 4: Run the Application

### Option A: Run with Maven (Recommended)
```bash
mvn spring-boot:run
```

You should see:
```
Started FootballCompetitionApplication in X seconds
```

### Option B: Run the JAR File
```bash
java -jar target/football-competition-api-1.0.0.jar
```

---

## Step 5: Access the API

Once running, you can access:

**Swagger UI** (Interactive API Documentation)
```
http://localhost:8080/api/v1/swagger-ui.html
```

**API Base URL**
```
http://localhost:8080/api/v1
```

**Swagger JSON**
```
http://localhost:8080/api/v1/docs
```

---

## Example: Test an Endpoint with cURL

```bash
# Get all editions
curl http://localhost:8080/api/v1/editions

# Create a new edition
curl -X POST http://localhost:8080/api/v1/editions \
  -H "Content-Type: application/json" \
  -d '{"editionNumber":1,"date":"2025-01-15"}'
```

---

## Stopping the Server

Press `Ctrl + C` in the terminal where the server is running.

---

## Troubleshooting

### Error: "Could not connect to database"
- Ensure MySQL is running
- Check credentials in `application.yml`
- Verify database exists: `SHOW DATABASES;`

### Error: "Could not find or load main class"
- Run `mvn clean install` first
- Ensure Java 17+ is installed: `java -version`

### Error: "Port 8080 already in use"
- Change port in `application.yml`:
  ```yaml
  server:
    port: 8081
  ```

### Build Takes Long Time
- First build downloads ~500MB of dependencies
- Subsequent builds are faster
- Can run `mvn clean install -DskipTests` to skip tests

---

## Development Tips

1. **Swagger UI** is your best friend - test endpoints interactively
2. **Check logs** in the console for error messages
3. **Database queries** - Monitor with: `SELECT * FROM <table_name>;`
4. **Keep terminal open** - You can see real-time logs as you use the API

---

## Next Steps

1. ✅ Install Maven (if not already done)
2. ✅ Setup database
3. ✅ Build project
4. ✅ Run application
5. 🎉 Start testing with Swagger UI!

For more details, see:
- [README.md](README.md) - Complete API documentation
- [API_REFERENCE.md](API_REFERENCE.md) - Endpoint reference
- [SETUP.md](SETUP.md) - Installation guide

---

**Need help?** Check [MAVEN_INSTALLATION.md](MAVEN_INSTALLATION.md) first!
