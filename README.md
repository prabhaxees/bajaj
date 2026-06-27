# BFHL REST API — Spring Boot

## Quick Start

### 1 · Set your details
Edit `src/main/resources/application.properties`:

```properties
user.id=firstname_lastname_ddmmyyyy   # e.g. john_doe_17091999
user.email=you@example.com
user.roll_number=YOUR_ROLL_NUMBER
```

### 2 · Run locally
```bash
./mvnw spring-boot:run
# API available at http://localhost:8080/bfhl
```

### 3 · Run tests
```bash
./mvnw test
```

---

## Endpoint

| Method | Path   | Status (success) |
|--------|--------|-----------------|
| POST   | /bfhl  | 200 OK           |

### Request
```json
{ "data": ["a", "1", "334", "4", "R", "$"] }
```

### Response
```json
{
  "is_success": true,
  "user_id": "john_doe_17091999",
  "email": "john@xyz.com",
  "roll_number": "ABCD123",
  "odd_numbers": ["1"],
  "even_numbers": ["334", "4"],
  "alphabets": ["A", "R"],
  "special_characters": ["$"],
  "sum": "339",
  "concat_string": "Ra"
}
```

---

## Deployment

### Option A — Railway (recommended)
1. Push this repo to GitHub.
2. Create a new Railway project → "Deploy from GitHub repo".
3. Railway auto-detects the Dockerfile and builds.
4. Set env var `PORT=8080` if not already set.

### Option B — Render
1. Push to GitHub.
2. New Web Service → connect repo → runtime: **Docker**.
3. Render reads `render.yaml` automatically.

---

## Classification Rules

| Token type        | Rule                          |
|-------------------|-------------------------------|
| Number            | matches `\d+` (digits only)   |
| Alphabet          | matches `[a-zA-Z]+`           |
| Special character | everything else               |

**concat_string algorithm**
1. Collect all individual characters from every alphabet token, in order.
2. Reverse the character list.
3. Apply alternating caps starting with UPPERCASE at index 0.

Example C trace — `["A","ABCD","DOE"]`:
- Chars in order : A A B C D D O E
- Reversed       : E O D D C B A A
- Alternating    : E o D d C b A a → `EoDdCbAa`

---

## Project Structure

```
src/
└── main/java/com/bfhl/
    ├── BfhlApplication.java
    ├── config/JacksonConfig.java        # field-visibility setup
    ├── controller/BfhlController.java   # POST /bfhl
    ├── dto/
    │   ├── BfhlRequest.java             # Request DTO
    │   └── BfhlResponse.java            # Response DTO
    ├── exception/
    │   ├── GlobalExceptionHandler.java  # @RestControllerAdvice
    │   └── ErrorResponse.java           # error envelope
    └── service/
        ├── BfhlService.java             # interface
        └── BfhlServiceImpl.java         # implementation
```
