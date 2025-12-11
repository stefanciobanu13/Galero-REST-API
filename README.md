# Football Competition API

A comprehensive Spring Boot REST API for managing football competition editions, players, teams, matches, goals, and attendance records.

## Features

- **Editions Management**: Create and manage football competition editions
- **Players Management**: CRUD operations for player information including grades
- **Teams Management**: Manage teams per edition
- **Team Players**: Assign and manage players within teams
- **Matches Management**: Track matches with scores and match types (group, small_final, big_final)
- **Goals Recording**: Record goals with different types (default, penalty, own_goal)
- **Attendance Tracking**: Track player attendance per edition
- **Global Exception Handling**: Centralized error handling with meaningful responses
- **Input Validation**: Bean validation annotations on all DTOs
- **API Documentation**: Swagger/OpenAPI integration
- **RESTful Design**: Standard HTTP methods and status codes

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **MySQL 8.0**
- **Maven**
- **Lombok**
- **Springdoc OpenAPI 2.2.0**

## Project Structure

```
src/main/java/com/galero/
├── controller/          # REST API endpoints
├── service/            # Business logic layer
├── repository/         # JPA repositories
├── model/              # JPA entity classes
├── dto/                # Data Transfer Objects
├── exception/          # Exception handling
├── config/             # Configuration classes
└── FootballCompetitionApplication.java
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

## Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Galero-backend
   ```

2. **Create MySQL Database**
   ```sql
   CREATE DATABASE IF NOT EXISTS football_competition;
   USE football_competition;
   ```

3. **Run the provided SQL schema**
   ```sql
   -- Run the schema from the db-schema.txt file
   ```

4. **Configure Database Connection**
   
   Update `src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/football_competition?useSSL=false&serverTimezone=UTC
       username: root
       password: your_password
   ```

5. **Build the Project**
   ```bash
   mvn clean install
   ```

6. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```

   The API will start at: `http://localhost:8080/api/v1`

## API Documentation

Once the application is running, access the Swagger UI:
- **Swagger UI**: `http://localhost:8080/api/v1/swagger-ui.html`
- **API Docs (JSON)**: `http://localhost:8080/api/v1/docs`

## API Endpoints

### Editions
- `POST /api/v1/editions` - Create a new edition
- `GET /api/v1/editions` - Get all editions
- `GET /api/v1/editions/{id}` - Get edition by ID
- `GET /api/v1/editions/number/{editionNumber}` - Get edition by number
- `PUT /api/v1/editions/{id}` - Update an edition
- `DELETE /api/v1/editions/{id}` - Delete an edition

### Players
- `POST /api/v1/players` - Create a new player
- `GET /api/v1/players` - Get all players
- `GET /api/v1/players/{id}` - Get player by ID
- `GET /api/v1/players/name?firstName=&lastName=` - Get player by name
- `PUT /api/v1/players/{id}` - Update a player
- `DELETE /api/v1/players/{id}` - Delete a player

### Teams
- `POST /api/v1/teams` - Create a new team
- `GET /api/v1/teams` - Get all teams
- `GET /api/v1/teams/{id}` - Get team by ID
- `GET /api/v1/teams/edition/{editionId}` - Get teams by edition
- `PUT /api/v1/teams/{id}` - Update a team
- `DELETE /api/v1/teams/{id}` - Delete a team

### Team Players
- `POST /api/v1/team-players` - Add player to team
- `GET /api/v1/team-players` - Get all team-player associations
- `GET /api/v1/team-players/team/{teamId}` - Get players in team
- `GET /api/v1/team-players/player/{playerId}` - Get teams for player
- `DELETE /api/v1/team-players/{teamId}/{playerId}` - Remove player from team

### Matches
- `POST /api/v1/matches` - Create a new match
- `GET /api/v1/matches` - Get all matches
- `GET /api/v1/matches/{id}` - Get match by ID
- `GET /api/v1/matches/edition/{editionId}` - Get matches by edition
- `GET /api/v1/matches/team/{teamId}` - Get matches by team
- `GET /api/v1/matches/type/{matchType}` - Get matches by type
- `PUT /api/v1/matches/{id}` - Update a match
- `DELETE /api/v1/matches/{id}` - Delete a match

### Goals
- `POST /api/v1/goals` - Record a goal
- `GET /api/v1/goals` - Get all goals
- `GET /api/v1/goals/{id}` - Get goal by ID
- `GET /api/v1/goals/match/{matchId}` - Get goals by match
- `GET /api/v1/goals/team/{teamId}` - Get goals by team
- `GET /api/v1/goals/player/{playerId}` - Get goals by player
- `GET /api/v1/goals/type/{goalType}` - Get goals by type
- `PUT /api/v1/goals/{id}` - Update a goal
- `DELETE /api/v1/goals/{id}` - Delete a goal

### Attendance
- `POST /api/v1/attendance` - Record attendance
- `GET /api/v1/attendance` - Get all attendance records
- `GET /api/v1/attendance/{id}` - Get attendance by ID
- `GET /api/v1/attendance/player/{playerId}` - Get attendance by player
- `GET /api/v1/attendance/edition/{editionId}` - Get attendance by edition
- `GET /api/v1/attendance/date/{date}` - Get attendance by date (YYYY-MM-DD)
- `GET /api/v1/attendance/player/{playerId}/edition/{editionId}` - Get player attendance in edition
- `PUT /api/v1/attendance/{id}` - Update attendance record
- `DELETE /api/v1/attendance/{id}` - Delete attendance record

## Example Requests

### Create an Edition
```bash
curl -X POST http://localhost:8080/api/v1/editions \
  -H "Content-Type: application/json" \
  -d '{
    "editionNumber": 1,
    "date": "2025-01-15"
  }'
```

### Create a Player
```bash
curl -X POST http://localhost:8080/api/v1/players \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "grade": 8.5
  }'
```

### Create a Team
```bash
curl -X POST http://localhost:8080/api/v1/teams \
  -H "Content-Type: application/json" \
  -d '{
    "editionId": 1,
    "teamColor": "Red"
  }'
```

### Create a Match
```bash
curl -X POST http://localhost:8080/api/v1/matches \
  -H "Content-Type: application/json" \
  -d '{
    "editionId": 1,
    "matchType": "group",
    "stage": "Round 1",
    "team1Id": 1,
    "team2Id": 2,
    "team1Score": 3,
    "team2Score": 2
  }'
```

### Record a Goal
```bash
curl -X POST http://localhost:8080/api/v1/goals \
  -H "Content-Type: application/json" \
  -d '{
    "goalType": "default",
    "matchId": 1,
    "teamId": 1,
    "playerId": 1
  }'
```

## Error Handling

The API returns standardized error responses with appropriate HTTP status codes:

- **400 Bad Request**: Validation errors
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server errors

Example error response:
```json
{
  "timestamp": "2025-01-15T10:30:00",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Player not found with ID: 999",
  "path": "/api/v1/players/999"
}
```

## Development

### Building the Project
```bash
mvn clean install
```

### Running Tests
```bash
mvn test
```

### Running the Application
```bash
mvn spring-boot:run
```

## Configuration

The application configuration can be modified in `src/main/resources/application.yml`:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate  # Options: validate, update, create, create-drop
    show-sql: false

server:
  port: 8080
  servlet:
    context-path: /api/v1

logging:
  level:
    root: INFO
    com.galero: DEBUG
```

## Future Enhancements

- Spring Security with JWT authentication
- Rate limiting and throttling
- Caching layer (Redis)
- Advanced filtering and search
- Pagination and sorting
- Audit logging
- Unit and integration tests
- Docker containerization

## License

This project is licensed under the MIT License.

## Support

For issues or questions, please contact support@galero.com
