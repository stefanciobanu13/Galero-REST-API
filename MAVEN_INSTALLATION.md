# Maven Installation Guide for Windows

## Why Maven is Needed
Maven is required to build the Spring Boot application from source code. It handles:
- Downloading dependencies from Maven Central Repository
- Compiling Java source code
- Running tests
- Packaging the application

## Quick Installation - 3 Steps

### Step 1: Download Maven
Visit: https://maven.apache.org/download.cgi

Download: **apache-maven-3.8.1-bin.zip** (or latest version)

### Step 2: Extract the Archive
- Extract to a location like: `C:\tools\apache-maven-3.8.1`
- Remember this path for Step 3

### Step 3: Add Maven to PATH
1. Press `Win + X` → Select "System"
2. Click "Advanced system settings"
3. Click "Environment Variables..."
4. Under "System variables", click "New..."
5. Variable name: `MAVEN_HOME`
6. Variable value: `C:\tools\apache-maven-3.8.1` (adjust if different)
7. Click OK
8. Find "Path" in System variables, click "Edit..."
9. Click "New" and add: `%MAVEN_HOME%\bin`
10. Click OK to save

### Step 4: Verify Installation
Open Command Prompt and run:
```bash
mvn --version
```

You should see Maven version information.

---

## Building and Running After Maven Installation

From the project directory:

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

Or:

```bash
# Build as JAR
mvn clean package

# Run the JAR
java -jar target\football-competition-api-1.0.0.jar
```

---

## Alternative: Use an IDE

If Maven installation seems complex, use an IDE instead:

### IntelliJ IDEA
1. Open the project folder in IntelliJ IDEA
2. Maven plugins will auto-configure
3. Right-click `pom.xml` → Run Maven → clean install
4. Run → Run... → Select FootballCompetitionApplication

### VS Code
1. Install "Extension Pack for Java" by Microsoft
2. Open the project folder in VS Code
3. Maven should auto-configure
4. Press Ctrl+Shift+P → Tasks: Run Task → Maven build
5. Use Run/Debug to execute

### Eclipse
1. File → Open Projects from File System
2. Select the project folder
3. Eclipse will auto-configure Maven
4. Right-click project → Run As → Maven build (Goals: clean install)

---

## If You Still Have Issues

### Issue 1: "mvn: command not found"
**Solution**: Maven is not in PATH. Restart your terminal/command prompt after adding to PATH.

### Issue 2: Connection timeouts when downloading dependencies
**Solution**: Check your internet connection and firewall. If behind corporate proxy, configure Maven settings.

### Issue 3: Java version mismatch
**Solution**: Run `java -version` and ensure you have Java 17 or higher.

---

## Database Setup (Do This First)

Before building, ensure your database is ready:

```sql
CREATE DATABASE football_competition;
USE football_competition;

-- Run the SQL schema from db-schema.txt file
-- Or optionally run SAMPLE_DATA.sql for test data
```

Then update `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/football_competition
    username: root
    password: your_password
```

---

## Next Steps After Build

1. **Build completes successfully** → You'll see "BUILD SUCCESS"
2. **Run `mvn spring-boot:run`** → Server starts on port 8080
3. **Visit API Documentation** → http://localhost:8080/api/v1/swagger-ui.html
4. **Test endpoints** → Use Swagger UI to test the API

---

That's it! Maven will handle everything else.
