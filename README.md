# URL Shortener API

## Overview
A simple URL shortener service built with Java 17 and Spring Boot 3.x that provides the following functionality:
- Shortening long URLs into compact, unique short URLs
- Redirecting short URLs to their original destinations
- Retrieving information about short URLs

## Architecture Highlights
- In-memory storage using ConcurrentHashMap
- Comprehensive error handling with global exception management
- Full test coverage with both unit and component tests

## Technologies Used
- Java 17
- Spring Boot 3.x
- Lombok for reduced boilerplate
- JUnit 5 and Mockito for testing
- Gradle for dependency management

## Quick Start

### Prerequisites
- Java 17 or higher  

### Getting Started

1. **Clone and navigate to the project**
   ```bash
   git clone https://github.com/NicholasJamou/linkshortener.git
   cd linkshortener
   ```

2. **Build the project**
   ```bash
   ./gradlew clean build
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080`

## API Documentation

## Testing the API

### Using cURL (postman recommended)

1. **Shorten a URL**
   ```bash
   curl -X POST http://localhost:8080/api/shorten \
     -H "Content-Type: application/json" \
     -d '{"url": "https://www.originenergy.com.au/"}'
   ```

2. **Get URL information**
   ```bash
   curl http://localhost:8080/api/info/{urlCode}
   ```

### 1. Shorten URL
**Endpoint:** `POST /api/shorten`

**Request:**
```json
{
    "url": "https://www.originenergy.com.au/electricity-gas/plans.html"
}
```

**Response:** `HTTP 201 Created`
```json
{
    "originalUrl": "https://www.originenergy.com.au/electricity-gas/plans.html",
    "shortUrl": "https://orig.in/a1B2c3",
    "urlCode": "abc123"
}
```
### 2. Get URL Information
**Endpoint:** `GET /api/info/{urlCode}`

**Example:** `GET /api/info/a1B2c3`

**Response:** `HTTP 200 OK`
```json
{
    "originalUrl": "https://www.originenergy.com.au/electricity-gas/plans.html",
    "shortUrl": "https://orig.in/a1B2c3",
    "urlCode": "abc123"
}
```


## Testing

### Run All Tests
```bash
./gradlew test
```


## Configuration

Key configuration properties in `application.properties`:
```properties
url.shortener.domain=https://orig.in/
url.shortener.code-length=6
```

## Design Decisions

### 1. Code Generation Strategy
- Uses a random alphanumeric code generator
- Automatic duplication/collison handling with retry mechanism

### 2. Data Storage
- In-memory storage using ConcurrentHashMap for thread safety
- Suitable for demonstration purposes

### 3. Error Handling
- Global exception handler for consistent error responses
- Proper HTTP status codes (400, 404, 500)
- Clean error messages without exposing internal details

### 4. Validation
- Comprehensive URL format validation
- Proper exception handling for all edge cases

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   # Add this in application.properties
   server.port=8081
   
   # Or via command line
   ./gradlew bootRun --args='--server.port=8081'
   ```

2. **Java Version Error**
   ```bash
   # Verify Java version
   java -version
   # Should show Java 17 or higher
   ```
