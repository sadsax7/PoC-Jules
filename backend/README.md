# Backend (Spring Boot)

## Requisitos
- Java 17
- Maven 3.9+
- Docker (solo para Testcontainers y `docker-compose`)

## Variables de entorno
- `MONGO_URI`: URI de MongoDB (ej. `mongodb://localhost:27017/poc_wallet?serverSelectionTimeoutMS=2000&connectTimeoutMS=2000`)

Puedes usar el archivo `.env` en la raíz del repo como referencia.

## Cómo correr localmente
```bash
cd backend
export MONGO_URI=mongodb://localhost:27017/poc_wallet?serverSelectionTimeoutMS=2000&connectTimeoutMS=2000
mvn spring-boot:run
```

## Tests
```bash
cd backend
mvn test
```
`mvn test` usa Testcontainers para levantar MongoDB, por lo que Docker debe estar activo.

## Docker Compose (dev)
```bash
docker compose -f infra/docker-compose.dev.yml --env-file .env up --build
```

## Endpoints
- `GET /health` -> `200 {"status":"UP"}` o `503 {"status":"DOWN"}`

Los timeouts en `MONGO_URI` ayudan a responder rápidamente cuando Mongo está caído.

## Estado actual
- Implementado: `GET /health`.
- Pendiente (backlog): Auth/JWT/`/users/me` según `docs/backlog/UserStories.md`.

## Troubleshooting (WSL + Testcontainers)
- Ver `docs/TROUBLESHOOTING_WSL_TESTCONTAINERS.md`.
