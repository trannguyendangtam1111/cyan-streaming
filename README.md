# Cyan

Cyan is a Netflix-style movie streaming platform built with Spring Boot, PostgreSQL, React, and Vite. The current codebase keeps the existing public/auth/admin flows intact while extending the architecture with paginated browsing, cache hooks, analytics, rate limiting, route-level code splitting, and richer homepage/player interactions.

## Architecture Overview

### Backend

- Spring Boot REST API with JWT authentication
- Package layout organized around `controller`, `service`, `repository`, `model`, `dto`, `mapper`, `security`, and `config`
- PostgreSQL as the primary data store
- Spring Cache support with Redis-ready configuration
- In-memory rate limiting for login and comment posting
- Analytics event ingestion endpoint

### Frontend

- React + Vite application with TailwindCSS
- Shared API service layer under `src/services`
- Reusable hooks under `src/hooks`
- Auth state and persistence through `src/contexts/AuthContext.jsx`
- Lazy-loaded routes with `React.lazy` and `Suspense`
- Reusable Netflix-style UI primitives such as `MovieCarousel`, `HoverPreviewCard`, and `SkeletonLoader`

## Project Structure

```text
cyanStreaming/
|-- backend/
|   `-- src/main/java/com/cyan/streaming/
|       |-- config/
|       |-- controller/
|       |-- dto/
|       |-- exception/
|       |-- mapper/
|       |-- model/
|       |-- repository/
|       |-- security/
|       `-- service/
`-- frontend/
    `-- src/
        |-- components/
        |-- contexts/
        |-- hooks/
        |-- pages/
        `-- services/
```

## Core Features

- JWT login and registration
- Role-based admin movie management
- Movie search, genre filtering, and sorting
- Continue watching progress persistence
- Ratings and comments
- Recommendation rows
- Netflix-style homepage rows with horizontal carousel navigation
- Analytics tracking for movie play, hover, and search usage
- Redis-ready caching configuration for frequently read movie data

## Prerequisites

- Java 21
- Maven 3.9+
- Node.js 18+
- PostgreSQL 14+
- Redis 7+ if you want Redis-backed caching in non-local environments

## Database Setup

1. Create a PostgreSQL database named `cyan_db`.
2. Create a PostgreSQL user if needed and grant access to `cyan_db`.
3. Spring Boot will create and update the tables automatically with JPA on startup.

## Environment Variables

### Backend

```powershell
$env:CYAN_DB_URL="jdbc:postgresql://localhost:5432/cyan_db"
$env:CYAN_DB_USERNAME="postgres"
$env:CYAN_DB_PASSWORD="postgres"
$env:CYAN_JWT_SECRET="cyan-streaming-super-secret-jwt-key-please-change"
$env:CYAN_JWT_EXPIRATION="86400000"
$env:CYAN_CACHE_TYPE="simple"
$env:CYAN_REDIS_HOST="localhost"
$env:CYAN_REDIS_PORT="6379"
$env:CYAN_REDIS_PASSWORD=""
$env:CYAN_REDIS_TIMEOUT="2000ms"
```

Notes:

- Use `CYAN_CACHE_TYPE=simple` for local development if Redis is not running.
- Use `CYAN_CACHE_TYPE=redis` in deployment environments where Redis is available.

### Frontend

```powershell
$env:VITE_API_URL="http://localhost:8080/api"
```

Defaults are also documented in [.env.example](C:/Users/Admin/OneDrive%20-%20PROIZU/Desktop/cyanStreaming/.env.example) and [frontend/.env.example](C:/Users/Admin/OneDrive%20-%20PROIZU/Desktop/cyanStreaming/frontend/.env.example).

## Local Development

### Run the backend

```powershell
cd backend
mvn spring-boot:run
```

The API runs on `http://localhost:8080`.

Seed data is loaded automatically on first startup, including demo users:

- Admin: `admin` / `Admin@123`
- Viewer: `viewer` / `Viewer@123`

### Run the frontend

```powershell
cd frontend
npm install
npm run dev
```

The frontend runs on `http://localhost:5173`.

## API Summary

### Existing public/authenticated/admin flows

Public:

- `GET /api/movies`
- `GET /api/movies/{id}`
- `GET /api/movies/search?query=`
- `GET /api/movies/genre/{genre}`
- `GET /api/movies/recommend/{id}`
- `GET /api/movies/{id}/comments`
- `POST /api/auth/register`
- `POST /api/auth/login`

Authenticated:

- `POST /api/movies/{id}/rating`
- `POST /api/movies/{id}/comment`
- `GET /api/progress`
- `POST /api/progress`

Admin only:

- `POST /api/admin/movies`
- `PUT /api/admin/movies/{id}`
- `DELETE /api/admin/movies/{id}`

### New extension endpoints

- `GET /api/movies/browse?section=TRENDING&page=0&size=12`
- `POST /api/analytics/event`

## Deployment Notes

### Backend deployment

1. Provision PostgreSQL and, optionally, Redis.
2. Set the backend environment variables.
3. Build the backend with Maven.
4. Run the Spring Boot jar behind a reverse proxy or application platform.

Example:

```powershell
cd backend
mvn clean package
java -jar target/cyan-backend-0.0.1-SNAPSHOT.jar
```

### Frontend deployment

1. Set `VITE_API_URL` to the deployed backend API URL.
2. Build the frontend.
3. Serve the `frontend/dist` output from a static host or CDN.

Example:

```powershell
cd frontend
npm install
npm run build
```

## Notes For Further Hardening

- Swap the in-memory rate limiter for a distributed limiter if you scale horizontally.
- Turn on Redis caching in deployed environments to back the `movies` and `movieRecommendations` caches.
- Consider moving analytics ingestion onto an async queue if event volume grows.
