# Variables de entorno (Source of Truth)

## Principios
- `.env.example` es la **plantilla** versionada.
- `.env` es **local** y no se commitea (ver `.gitignore`).
- Docker Compose DEV carga `.env` con `--env-file .env`.

## Variables definidas en `.env.example`

| Variable | Dónde se define | Dónde se usa hoy | Estado | Valor esperado (ejemplo) |
| --- | --- | --- | --- | --- |
| `MONGO_URI` | `.env.example` | `backend/src/main/resources/application.yml` (`spring.data.mongodb.uri`) y `infra/docker-compose.dev.yml` | USADA HOY | `mongodb://localhost:27017/poc_wallet?serverSelectionTimeoutMS=2000&connectTimeoutMS=2000` |
| `JWT_SECRET` | `.env.example` | No se usa en código actual | PLACEHOLDER FUTURO | `CHANGE_ME_DEV_ONLY` |
| `JWT_EXPIRES_MIN` | `.env.example` | No se usa en código actual | PLACEHOLDER FUTURO | `20` |
| `MFA_TEMP_EXPIRES_MIN` | `.env.example` | No se usa en código actual | PLACEHOLDER FUTURO | `5` |
| `NEXT_PUBLIC_API_BASE_URL` | `.env.example` | Frontend aún vacío | PLACEHOLDER FUTURO | `http://localhost:8080` |

## Variables definidas solo en Docker Compose

| Variable | Dónde se define | Dónde se usa | Estado |
| --- | --- | --- | --- |
| `MONGO_INITDB_DATABASE` | `infra/docker-compose.dev.yml` y `infra/docker-compose.mongo.yml` | Contenedor Mongo | USADA HOY |

## Recomendación de uso
1) Copia plantilla a `.env`.
2) Ajusta valores locales si cambias puertos o nombres.
3) Mantén `.env.example` como referencia compartida.
