# PoC — Billetera Virtual + Agent as a Service (Inicio: Jules)

## Estado actual (implementado)
- Backend Spring Boot con Mongo imperativo y endpoint `GET /health`.
- Tests con Testcontainers (Mongo) y Docker Compose DEV.
- Frontend vacío (pendiente HU-FE-00).

## Alcance PoC (backlog / futuro)
- Auth: Registro, Login + MFA simulado, JWT, `/users/me`.
- Frontend: Landing, Registro, Login, Dashboard, Logout.
- DevEx: OpenAPI, cobertura y linting según guías.

> Fuera de alcance PoC: saldo real, P2P, pagos, recargas, notificaciones, admin.

## Cómo empezar
1. Lee: `docs/SETUP_FROM_ZERO.md`
2. Revisa guías:
   - `docs/guidelines/README_GUIDELINES_INDEX.md`
3. Variables de entorno:
   - `docs/ENVIRONMENT.md`
4. Troubleshooting WSL + Testcontainers:
   - `docs/TROUBLESHOOTING_WSL_TESTCONTAINERS.md`
5. Backlog inicial:
   - `docs/backlog/UserStories.md`
6. Plan operativo PoC (Jules + Dev):
   - `docs/POC_PLAN_JULES.md`

## Estructura
Ver `docs/PROJECT_STRUCTURE.md`.
