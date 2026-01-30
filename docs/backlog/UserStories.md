# 📦 BACKLOG DE HISTORIAS DE USUARIO (PoC)

> Alcance PoC: Identidad & Acceso (registro, login + MFA simulado, JWT, `/users/me`, dashboard + logout).

---

# 🧠 BACKEND USER STORIES (Java/Spring Boot — Hexagonal — MongoDB)

## HU-BE-00: Configuración de Infraestructura y Conexión MongoDB
- **ID:** HU-BE-00
- **Título:** Configurar andamiaje Hexagonal y conexión a MongoDB (imperativa)
- **Estimación:** XS (5–8 hrs)
- **Dependencias:** Ninguna

### Definition of Ready (DoR)
- Repositorio Git creado y accesible por el equipo.
- Acceso a instancia MongoDB verificado (local o Atlas).
- Versión Java definida (17 LTS o 21 LTS).
- Stack definido: Spring Boot 3.x, Spring Security 6.x, Spring Data MongoDB (imperativo).

### Historia
Como **Desarrollador Backend**,
Quiero **establecer la estructura hexagonal y la conexión a MongoDB**,
Para **iniciar la implementación del módulo de usuarios sin deuda técnica inicial.**

### Criterios de Aceptación
1. **Arquitectura:** estructura creada siguiendo patrón hexagonal:
   - `domain` (entidades, value objects, puertos)
   - `application` (casos de uso)
   - `infrastructure` (adaptadores: controllers/repos/config)
2. **DB:** conexión funcional a MongoDB con `spring-boot-starter-data-mongodb`.
3. **Config:** `application.yml` usando variables de entorno (sin credenciales hardcodeadas).
4. **Health:** `GET /health` responde `200 OK` y confirma conexión (Spring Boot Actuator).

### Definition of Done (DoD)
- [ ] Formato: Google Java Format o Spotless aplicado.
- [ ] Lint: Checkstyle + PMD + SpotBugs sin errores críticos.
- [ ] CI: Pipeline mínimo ejecuta build + tests.
- [ ] Docs: README de backend + Javadoc mínimo en configuración base.
- [ ] Docker: `docker-compose` levanta API + MongoDB.
- [ ] No PII en logs.
- [ ] Límites: clases de configuración < 500 líneas.

---

## HU-BE-01: Registro de Usuarios con Persistencia NoSQL
- **ID:** HU-BE-01
- **Título:** Implementar endpoint de registro y persistencia en colección `users`
- **Estimación:** M (20 hrs)
- **Dependencias:** HU-BE-00

### Definition of Ready (DoR)
- Contrato OpenAPI 3.0 (SpringDoc) para `POST /auth/register` definido y aprobado.
- Reglas de validación definidas (teléfono, password).
- Mock KYC acordado (simulado / interno).

### Historia
Como **Arquitecto de Solución**,
Quiero **registrar usuarios guardando su información en MongoDB con validaciones**,
Para **permitir el alta segura y única por teléfono.**

### Criterios de Aceptación
1. **Modelo de datos:** colección `users` con:
   - `phone` (índice único)
   - `email` (opcional)
   - `passwordHash`
   - `kycStatus` (ej. `PENDING|APPROVED|REJECTED`)
   - `mfaEnabled` (boolean)
   - `createdAt`
2. **Seguridad:** contraseña jamás en texto plano; usar `BCryptPasswordEncoder`.
3. **Mock KYC:** se ejecuta validación interna simulada; si falla, no crea usuario.
4. **Errores:** si `phone` existe → `409 Conflict` (handler consistente).
5. **Respuesta:** `201 Created` con `userId` (sin datos sensibles).

### Definition of Done (DoD)
- [ ] Formato/Lint OK (Spotless/Checkstyle/PMD/SpotBugs).
- [ ] Pruebas unitarias (JUnit 5 + Mockito) con cobertura >= 80%.
- [ ] OpenAPI: endpoint documentado (request/response/errors).
- [ ] No PII en logs, DTOs sin exponer `passwordHash`.
- [ ] Controller/Service < 500 líneas.
- [ ] Probado con Postman/REST Client (evidencia).

---

## HU-BE-02: Autenticación, JWT y Verificación MFA
- **ID:** HU-BE-02
- **Título:** Implementar login, emisión de JWT y verificación MFA (simulada)
- **Estimación:** M (22 hrs)
- **Dependencias:** HU-BE-00, HU-BE-01

### Definition of Ready (DoR)
- Librería JWT definida (ej. `io.jsonwebtoken:jjwt`).
- Expiración y claims definidos.
- Flujo MFA acordado (código simulado).

### Historia
Como **Sistema de Seguridad**,
Quiero **validar credenciales y manejar el flujo Login + MFA**,
Para **emitir tokens de acceso seguros.**

### Criterios de Aceptación
**A) Login — `POST /auth/login`**
1. Valida `phone` + `password` (BCrypt).
2. Si `mfaEnabled=true`:
   - Responde `200 OK` con `{ status: "MFA_REQUIRED", tempToken }`.
   - `tempToken` es corto (ej. 5 min) y de alcance limitado.
3. Si `mfaEnabled=false`:
   - Responde `200 OK` con `{ accessToken }`.

**B) MFA Verify — `POST /auth/mfa/verify`**
1. Recibe `tempToken` + `code`.
2. Valida `code` simulado `"123456"`.
3. Si OK → devuelve `{ accessToken }` final.

**C) JWT**
- Claims mínimos: `sub` (userId), `exp` (15–30 min), `role` (ej. `USER`).

### Definition of Done (DoD)
- [ ] Lint + análisis estático OK.
- [ ] Pruebas unitarias >= 80% (incluye token inválido/expirado).
- [ ] Docs: OpenAPI + nota de claims y errores.
- [ ] Flujo probado: Login → (MFA) → Token final.

---

## HU-BE-03: Endpoint de Información de Usuario (Me)
- **ID:** HU-BE-03
- **Título:** Exponer perfil del usuario autenticado (`/users/me`)
- **Estimación:** S (12 hrs)
- **Dependencias:** HU-BE-02

### Definition of Ready (DoR)
- DTO de respuesta definido (sin secretos).
- Filter Chain de Spring Security configurado para JWT.

### Historia
Como **Frontend**,
Quiero **obtener los datos del usuario actual desde un endpoint protegido**,
Para **mostrar su información en el Dashboard.**

### Criterios de Aceptación
1. `GET /users/me` requiere `Authorization: Bearer <token>`:
   - sin token/ inválido → `401 Unauthorized`
2. Extrae `sub` del JWT, consulta usuario en Mongo.
3. Respuesta devuelve solo `name` (si existe), `phone`, `kycStatus`.
4. No expone `passwordHash` ni secretos MFA.

### Definition of Done (DoD)
- [ ] Lint + análisis estático OK.
- [ ] Tests unitarios >= 80% (opcional: integración con Testcontainers).
- [ ] DTOs documentados (Javadoc) + OpenAPI.
- [ ] Validación manual: no secrets en respuesta/logs.

---

# 🎨 FRONTEND USER STORIES (Next.js — Atomic Design — Inter/Naranja)

## HU-FE-00: Setup de Proyecto, Tema Institucional y Configuración Base
- **ID:** HU-FE-00
- **Título:** Configurar Next.js, Tailwind y Design System Institucional
- **Estimación:** S (10 hrs)
- **Dependencias:** Ninguna

### DoR
- Node.js listo.
- Paleta definida (Naranja/Negro/Blanco).
- Fuente Inter disponible.

### Historia
Como **Diseñador UI/UX**,
Quiero **configurar tipografía y paleta institucional**,
Para **asegurar consistencia visual desde el primer componente.**

### Criterios de Aceptación
1. Fuente global: **Inter**.
2. Tokens Tailwind: `primary (#FF6B00)`, `bg-dark (#000)`, `text-light (#FFF)`.
3. Dark mode por “inversión total”.
4. Estructura atomic: `atoms/ molecules/ organisms/ templates/`.

### DoD
- [ ] Prettier configurado.
- [ ] ESLint strict pasando.
- [ ] TypeScript strict (sin `any`).
- [ ] Styleguide page funcional.
- [ ] README FE + TSDoc en config clave.
- [ ] Configs < 500 líneas.

---

## HU-FE-01: Landing Page (Home)
- **ID:** HU-FE-01
- **Título:** Implementar Landing con opciones de acceso
- **Estimación:** S (14 hrs)
- **Dependencias:** HU-FE-00

### DoR
- Wireframe aprobado.
- Logo disponible (SVG/PNG).

### Historia
Como **Usuario Visitante**,
Quiero **ver una bienvenida con acceso a registro/login**,
Para **iniciar mi interacción con la billetera.**

### Criterios de Aceptación
1. Hero simple: título, subtítulo, logo.
2. CTA: “Ingresar” (outline naranja) y “Registrarse” (solid naranja).
3. Responsive: columna móvil, centrado desktop.

### DoD
- [ ] Prettier + ESLint.
- [ ] TS strict.
- [ ] Tests (Jest/RTL) recomendados >= 70% (snapshots + render).
- [ ] Componentes atomizados, archivos < 500 líneas.
- [ ] Responsive verificado.

---

## HU-FE-02: Formulario de Registro Guiado
- **ID:** HU-FE-02
- **Título:** Registro guiado y envío a `POST /auth/register`
- **Estimación:** M (24 hrs)
- **Dependencias:** HU-FE-00, HU-BE-01

### DoR
- Contrato API conocido.
- Átomos Input/Button definidos.

### Historia
Como **Usuario Nuevo**,
Quiero **completar mi registro en una interfaz guiada**,
Para **crear mi cuenta exitosamente.**

### Criterios de Aceptación
1. Formulario por pasos o secciones claras.
2. Feedback visual: loading naranja.
3. Integración real: éxito/error desde backend.
4. Validaciones en cliente (mínimas) + manejo de 409.

### DoD
- [ ] Prettier + ESLint + TS strict.
- [ ] Tests recomendados >= 70% (validaciones + submit).
- [ ] Hook `useRegister` separa lógica.
- [ ] Docs TSDoc en hooks/components.
- [ ] Integración validada end-to-end.

---

## HU-FE-03: Login y Verificación MFA
- **ID:** HU-FE-03
- **Título:** Login + reto MFA (OTP simulado)
- **Estimación:** M (24 hrs)
- **Dependencias:** HU-FE-00, HU-BE-02

### DoR
- Contrato de login claro (token vs `MFA_REQUIRED`).
- Diseño OTP 6 dígitos definido.

### Historia
Como **Usuario Registrado**,
Quiero **ingresar credenciales y, si aplica, OTP**,
Para **acceder a mi cuenta.**

### Criterios de Aceptación
1. Paso 1: formulario phone/password.
2. Si backend responde `status: "MFA_REQUIRED"`, muestra OTP.
3. Paso 2: OTP 6 dígitos → `POST /auth/mfa/verify`.
4. Si obtiene `accessToken`, guarda sesión.

### DoD
- [ ] Lint + TS strict.
- [ ] Tests recomendados >= 70% (OK/fail/MFA/fail MFA).
- [ ] Estados de auth documentados (TSDoc).
- [ ] Almacenamiento token definido.

> Nota de seguridad (PoC): preferible cookie httpOnly. Si no, usar storage con cuidado y evitar exponer token en logs.

---

## HU-FE-04: Dashboard de Usuario y Logout
- **ID:** HU-FE-04
- **Título:** Dashboard privado + Logout
- **Estimación:** S (16 hrs)
- **Dependencias:** HU-FE-03, HU-BE-03

### DoR
- Token storage implementado.
- `/users/me` disponible.

### Historia
Como **Usuario Autenticado**,
Quiero **ver mi bienvenida y poder cerrar sesión**,
Para **confirmar acceso correcto y terminar sesión seguro.**

### Criterios de Aceptación
1. Ruta protegida: sin token → redirect.
2. Consume `/users/me` y muestra datos (name/phone/kycStatus).
3. Logout limpia sesión y redirige a Landing.
4. Si token expira → logout automático.

### DoD
- [ ] Lint + TS strict.
- [ ] Tests recomendados >= 70% (protección + logout).
- [ ] Docs TSDoc, componentes < 500 líneas.
- [ ] Flujo completo validado.

---

# 🔧 Stack Tecnológico (Backend)
- **Core:** Java 17/21, Spring Boot 3.x, Spring Security 6.x
- **DB:** MongoDB (imperativo)
- **JWT:** `jjwt`
- **Calidad:** Spotless/Google Java Format, Checkstyle, PMD, SpotBugs
- **Testing:** JUnit 5, Mockito, AssertJ, (opcional) Testcontainers
- **Docs:** SpringDoc OpenAPI (Swagger UI), Javadoc
- **Build:** Maven + JaCoCo
