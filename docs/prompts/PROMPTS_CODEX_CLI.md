# Prompts profesionales para Codex CLI (PoC)

> Codex CLI puede inspeccionar el repo, editar archivos y correr comandos localmente. Úsalo con tareas pequeñas y checklist.

## Reglas de prompt (siempre)
Incluye:
1) **Objetivo** (1 frase)
2) **Scope** (archivos/carpetas permitidas)
3) **No tocar** (lo prohibido)
4) **Criterios de éxito** (tests/commands)
5) **Salida esperada** (PR-ready)

---

## Prompt A — Bootstrap Backend (HU-BE-00, Mongo imperativo, Maven)
**Objetivo:** Implementar HU-BE-00 (scaffold hexagonal + Mongo imperativo + /health).
**Scope permitido:** `backend/`, `infra/`, `docs/` (solo si actualiza).
**Prohibido:** tocar `frontend/` o cambiar estructura global del repo.

**Pasos:**
1) Lee `docs/guidelines/BACKEND_GUIDELINES.md`.
2) Crea proyecto Spring Boot 3.x con **Maven** en `backend/`.
3) Estructura hexagonal: `domain/`, `application/`, `infrastructure/`.
4) Mongo **imperativo**: `spring-boot-starter-data-mongodb`.
5) Actuator habilitado; `/health` debe responder `200` cuando Mongo esté arriba (Actuator o controller explícito).
6) Configura `application.yml` con variables de entorno (usa `.env.example`).
7) Agrega tests AAA mínimos del health (si usas Mongo real, Testcontainers recomendado).

**Éxito:**
- `mvn test` pasa
- `/health` responde 200 con Mongo arriba

**Entrega:**
- lista de archivos tocados
- comandos ejecutados + output resumido

---

## Prompt B — Bootstrap Frontend (HU-FE-00, Next + Atomic + Styleguide)
**Objetivo:** Implementar HU-FE-00 (Next base + tokens + atomic folders + styleguide).
**Scope permitido:** `frontend/`, `docs/` (solo si actualiza).
**Prohibido:** tocar `backend/` o `infra/`.

**Pasos:**
1) Lee `docs/guidelines/FRONTEND_GUIDELINES.md`.
2) Crea Next.js App Router con TS strict y Tailwind en `frontend/`.
3) Configura Inter (next/font) + tokens Tailwind: `primary (#FF6B00)`, `bg-dark (#000)`, `text-light (#FFF)`.
4) Crea carpetas atomic: `atoms/`, `molecules/`, `organisms/`, `templates/`.
5) Crea `/styleguide` mostrando Button/Input/Spinner (atoms).
6) Agrega tests AAA mínimos de render (Jest/RTL si no existe).

**Éxito:**
- `npm run lint` pasa
- `npm test` (si existe) pasa

**Entrega:**
- lista de archivos tocados
- comandos ejecutados

---

## Prompt C — Revisión de PR como Code Reviewer
Actúa como reviewer estricto de PoC profesional.
Revisa el diff del PR y marca hallazgos como: `BLOCKER`, `HIGH`, `MEDIUM`, `NICE-TO-HAVE`.

**Criterios:**
- Backend respeta hexagonal (domain sin Spring)
- Mongo imperativo (no reactivo)
- Login con MFA devuelve `200` + `status: "MFA_REQUIRED"`
- Tests AAA presentes (cobertura objetivo 80%)
- No PII/tokens en logs
- CI y comandos de verificación documentados
