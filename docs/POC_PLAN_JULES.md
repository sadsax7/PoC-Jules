# PoC — Billetera Virtual + Agent as a Service (Inicio: Jules)

> **Objetivo PoC (alcance reducido por plan gratuito de Jules):** Validar viabilidad de un **agente IA** apoyando a Dev/QA en un **slice mínimo** de producto (Identidad & Acceso) con calidad profesional (CI, pruebas, documentación y seguridad básica).
>
> **Qué SÍ incluye esta PoC:** Setup FE/BE, Registro, Login + MFA simulado, JWT, endpoint `/users/me`, Dashboard + Logout, documentación OpenAPI y pipeline CI.
>
> **Qué NO incluye (fuera de alcance PoC):** saldo real, P2P, pagos, recargas, notificaciones, panel admin, analítica, antifraude completo, KYC real, integración con terceros.

---

## Convenciones Scrum (buenas prácticas)
- **Historias tipo INVEST:** pequeñas, valiosas, estimables, testeables.
- **Trazabilidad:** cada HU indica qué capability habilita (Auth, MFA, Perfil, UI).
- **Definition of Ready (DoR):** evita trabajo a ciegas.
- **Criterios de Aceptación:** claros, verificables.
- **Definition of Done (DoD):** evidencia técnica (CI verde, pruebas, docs).
- **1 HU = 1 PR** (ideal). Si es muy grande, se parte en subtareas técnicas, pero manteniendo una entrega demostrable.

---

# 📌 Distribución recomendada (Jules vs Dev)

## Reglas de operación (para que el plan gratuito rinda)
- Jules trabaja por PR pequeño: 1 HU o parte atómica por PR.
- Dev define decisiones y revisa: arquitectura, seguridad, contratos, merges.
- Prompts claros y acotados: “Haz exactamente esto, no más”.

## Asignación por Historia (resumen)
### Backend
- **HU-BE-00 → Jules (principal)**
  - Estructura hexagonal, docker-compose, actuator `/health`, config env.
- **HU-BE-01 → Dev (principal) + Jules**
  - Jules: DTOs, repositorios, handlers, tests base.
  - Dev: reglas de negocio, seguridad password, validación final.
- **HU-BE-02 → Dev (principal) + Jules**
  - Jules: scaffolding endpoints + tests básicos.
  - Dev: diseño flujo MFA + claims + expiración + security filter chain.
- **HU-BE-03 → Jules + Dev**
  - Jules: endpoint `/users/me`, DTO seguro, tests.
  - Dev: hardening de seguridad y revisión de logs.

### Frontend
- **HU-FE-00 → Jules (principal)**
  - Setup Next.js + Tailwind + atomic folders + styleguide page.
- **HU-FE-01 → Jules**
  - Landing UI + responsive + componentes atomizados.
- **HU-FE-02 → Dev FE (principal) + Jules**
  - Jules apoya con componentes reutilizables + tests base.
- **HU-FE-03 → Dev FE (principal) + Jules**
  - Jules: UI OTP + estados.
  - Dev: integración auth real + manejo de tokens y errores.
- **HU-FE-04 → Jules + Dev FE**
  - Jules: layout dashboard + logout UI + protección base.
  - Dev: robustecer protección de rutas + expiración + pruebas.

---

# 🧾 Prompts base para Jules

## Backend (HU-BE-00)
- “Crea un proyecto Spring Boot 3.x con Java 17, estructura hexagonal (domain/application/infrastructure), Actuator habilitado y endpoint `/health`. Agrega docker-compose con MongoDB y configura `application.yml` usando variables de entorno. Incluye un README mínimo y un GitHub Action que ejecute `mvn test`. PR solo con esto.”

## Frontend (HU-FE-00)
- “Crea Next.js + TS strict + Tailwind. Configura tokens `primary/bg-dark/text-light`, fuente Inter global, carpetas atomic design y una página `/styleguide` que muestre botones e inputs. Incluye Prettier y ESLint strict. PR solo con esto.”

---

# ✅ Checkpoint de validación PoC (demo final)
- Demo 1: Landing → Registro → `201 Created`
- Demo 2: Login → `status: "MFA_REQUIRED"` → OTP → `accessToken`
- Demo 3: Dashboard protegido consume `/users/me`
- Demo 4: Logout + expiración (simulada) → redirect a home
- Evidencia: CI verde + Swagger + pruebas mínimas

---

# 📎 Nota de coherencia
- Login con MFA devuelve `200 OK` + `status: "MFA_REQUIRED"`.
- MongoDB imperativo (no reactivo) para PoC.
- Cobertura objetivo de BE: 80%.
- Build oficial: Maven.
