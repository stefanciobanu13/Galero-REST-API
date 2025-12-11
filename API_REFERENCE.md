# API Quick Reference

## Base URL
```
http://localhost:8080/api/v1
```

## Response Format
All responses follow this pattern:
```json
{
  "id": 1,
  "field": "value",
  ...
}
```

## Error Response Format
```json
{
  "timestamp": "2025-01-15T10:30:00",
  "status": 400,
  "error": "ERROR_TYPE",
  "message": "Detailed error message",
  "path": "/api/v1/endpoint"
}
```

## Common Status Codes
- 200: Success
- 201: Created
- 204: No Content (successful delete)
- 400: Bad Request (validation error)
- 404: Not Found
- 500: Internal Server Error

---

## CRUD Operations Pattern

### Create
```bash
POST /resource
Content-Type: application/json

{
  "field": "value"
}
```
Returns: 201 Created with resource data

### Read Single
```bash
GET /resource/{id}
```
Returns: 200 OK with resource data

### Read Multiple
```bash
GET /resource
```
Returns: 200 OK with array of resources

### Update
```bash
PUT /resource/{id}
Content-Type: application/json

{
  "field": "updated_value"
}
```
Returns: 200 OK with updated resource

### Delete
```bash
DELETE /resource/{id}
```
Returns: 204 No Content

---

## Editions API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /editions | Create edition |
| GET | /editions | List all editions |
| GET | /editions/{id} | Get by ID |
| GET | /editions/number/{editionNumber} | Get by number |
| PUT | /editions/{id} | Update edition |
| DELETE | /editions/{id} | Delete edition |

**Sample Create Request:**
```json
{
  "editionNumber": 1,
  "date": "2025-01-15"
}
```

---

## Players API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /players | Create player |
| GET | /players | List all players |
| GET | /players/{id} | Get by ID |
| GET | /players/name | Get by first/last name |
| PUT | /players/{id} | Update player |
| DELETE | /players/{id} | Delete player |

**Sample Create Request:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "grade": 8.5
}
```

**Query Parameters for Name Search:**
- `firstName`: First name (required)
- `lastName`: Last name (required)

---

## Teams API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /teams | Create team |
| GET | /teams | List all teams |
| GET | /teams/{id} | Get by ID |
| GET | /teams/edition/{editionId} | Get by edition |
| PUT | /teams/{id} | Update team |
| DELETE | /teams/{id} | Delete team |

**Sample Create Request:**
```json
{
  "editionId": 1,
  "teamColor": "Red"
}
```

---

## Team Players API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /team-players | Add player to team |
| GET | /team-players | List all |
| GET | /team-players/team/{teamId} | Players in team |
| GET | /team-players/player/{playerId} | Teams for player |
| DELETE | /team-players/{teamId}/{playerId} | Remove player |

**Sample Create Request:**
```json
{
  "teamId": 1,
  "playerId": 1
}
```

---

## Matches API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /matches | Create match |
| GET | /matches | List all matches |
| GET | /matches/{id} | Get by ID |
| GET | /matches/edition/{editionId} | Get by edition |
| GET | /matches/team/{teamId} | Get by team |
| GET | /matches/type/{matchType} | Get by type |
| PUT | /matches/{id} | Update match |
| DELETE | /matches/{id} | Delete match |

**Sample Create Request:**
```json
{
  "editionId": 1,
  "matchType": "group",
  "stage": "Round 1",
  "team1Id": 1,
  "team2Id": 2,
  "team1Score": 3,
  "team2Score": 2
}
```

**Match Types:**
- `group`: Group stage match
- `small_final`: Third place match
- `big_final`: Final match

---

## Goals API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /goals | Record goal |
| GET | /goals | List all goals |
| GET | /goals/{id} | Get by ID |
| GET | /goals/match/{matchId} | Goals in match |
| GET | /goals/team/{teamId} | Goals by team |
| GET | /goals/player/{playerId} | Goals by player |
| GET | /goals/type/{goalType} | Goals by type |
| PUT | /goals/{id} | Update goal |
| DELETE | /goals/{id} | Delete goal |

**Sample Create Request:**
```json
{
  "goalType": "default",
  "matchId": 1,
  "teamId": 1,
  "playerId": 1
}
```

**Goal Types:**
- `default`: Regular goal
- `penalty`: Penalty goal
- `own_goal`: Own goal

---

## Attendance API

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /attendance | Record attendance |
| GET | /attendance | List all records |
| GET | /attendance/{id} | Get by ID |
| GET | /attendance/player/{playerId} | Attendance by player |
| GET | /attendance/edition/{editionId} | Attendance by edition |
| GET | /attendance/date/{date} | Attendance by date |
| GET | /attendance/player/{playerId}/edition/{editionId} | Player attendance in edition |
| PUT | /attendance/{id} | Update record |
| DELETE | /attendance/{id} | Delete record |

**Sample Create Request:**
```json
{
  "date": "2025-01-15",
  "playerId": 1,
  "playerFirstName": "John",
  "playerLastName": "Doe",
  "editionId": 1
}
```

**Date Format:** YYYY-MM-DD

---

## Validation Rules

### Player
- First name: Required, not blank
- Last name: Required, not blank
- Grade: Required, number

### Team
- Edition ID: Required, must exist
- Team color: Required, not blank

### Match
- Edition ID: Required, must exist
- Team 1 ID: Required, must exist
- Team 2 ID: Required, must exist
- Match type: Required (group, small_final, big_final)

### Goal
- Match ID: Required, must exist
- Team ID: Required, must exist
- Player ID: Required, must exist
- Goal type: Required (default, penalty, own_goal)

### Attendance
- Date: Required
- Player ID: Required, must exist
- Edition ID: Required, must exist
- Player first/last name: Required, not blank

---

## Using with Postman

1. Import the Swagger URL: `http://localhost:8080/api/v1/docs`
2. Set base URL as environment variable: `{{base_url}} = http://localhost:8080/api/v1`
3. Use in requests: `{{base_url}}/players`

---

## Using with cURL

**Example:**
```bash
# Create a player
curl -X POST http://localhost:8080/api/v1/players \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","grade":8.5}'

# Get all players
curl http://localhost:8080/api/v1/players

# Get specific player
curl http://localhost:8080/api/v1/players/1

# Update player
curl -X PUT http://localhost:8080/api/v1/players/1 \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Jane","lastName":"Doe","grade":9.0}'

# Delete player
curl -X DELETE http://localhost:8080/api/v1/players/1
```

---

## Troubleshooting

### Port Already in Use
```bash
# Change port in application.yml
server:
  port: 8081
```

### Database Connection Error
- Verify MySQL is running
- Check credentials in application.yml
- Ensure database exists
- Verify JDBC URL format

### Validation Errors
- Check required fields are provided
- Verify data types (strings should not be numbers)
- Ensure IDs reference existing resources

---

## Development Tips

1. **Swagger UI**: Use `/swagger-ui.html` for interactive testing
2. **Database**: Check data directly with MySQL CLI
3. **Logs**: Monitor `DEBUG` level logs in console
4. **Postman**: Save requests in collections for repeated testing
5. **cURL**: Use `| jq` for pretty JSON output (if jq installed)

---

**Last Updated**: December 6, 2025
