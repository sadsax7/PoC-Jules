# Backend Guidelines — Java/Spring Boot (Hexagonal Architecture)

## 1) Objetivo
Estas guías definen **cómo evolucionar** el backend de la PoC hacia un producto mantenible, usando:
- **Arquitectura Hexagonal (Ports & Adapters)**
- **DDD ligero** (dominio claro, sin sobre-ingeniería)
- **Testing con patrón AAA (Arrange–Act–Assert)**

Están escritas para que sean fáciles de seguir por un **equipo humano** y por un **agente de IA** (PRs pequeños, reglas explícitas, checklists).

---

## 2) Principios no negociables
1. **Dominio independiente del framework**
   - `domain` no importa Spring, Mongo, Web, ni librerías de infraestructura.
2. **Casos de uso orquestan, dominio decide**
   - `application` coordina dependencias.
   - `domain` contiene reglas de negocio (invariantes).
3. **Infraestructura es “plug-in”**
   - Web/Mongo/Security son adaptadores reemplazables.
4. **Cambios pequeños y trazables**
   - 1 historia = 1 PR.
   - PRs que mezclen “refactor masivo + feature” se rechazan.
5. **Contrato primero**
   - Cambios API ⇒ actualizar OpenAPI + tests contractuales.

---

## 3) Estructura recomendada
> Puedes implementarlo como **módulos Maven** o como paquetes dentro de un módulo. Para PoC, paquetes es suficiente.

```
backend/
  src/main/java/com/sfka/wallet/
    domain/
      model/              # Entidades / Value Objects
      service/            # Servicios de dominio (puro)
      port/
        in/               # Use-case interfaces (opcional)
        out/              # Puertos de salida (repos, gateways)
      exception/
    application/
      usecase/            # Implementaciones de casos de uso
      dto/                # DTOs internos de app (si aplica)
      mapper/             # Mapeos app<->domain (si aplica)
    infrastructure/
      web/
        controller/
        request/
        response/
        mapper/           # Mapeos web<->app
        error/            # ExceptionHandlers
      persistence/
        mongo/
          document/
          repository/     # Spring Data
          adapter/        # Implementa puertos out
          mapper/
      security/
        jwt/
      config/
  src/test/java/...       # Espejo de estructura (tests AAA)
```

---

## 4) Reglas por capa

### 4.1 `domain/` (núcleo)
**Contiene:**
- Entidades (`User`, etc.), Value Objects (`PhoneNumber`, `Email`)
- Reglas e invariantes (ej.: “teléfono único” se valida vía caso de uso + repositorio)
- Puertos de salida (`UserRepositoryPort`, `KycServicePort`)
- Excepciones de dominio (`InvalidPhoneException`, `UserAlreadyExistsException`)

**Prohibido:**
- `@Component`, `@Document`, `@RestController`
- `MongoTemplate`, `ReactiveMongoRepository`, `ObjectId` (si se puede evitar)
- `ResponseEntity`, `HttpStatus`

**Buenas prácticas:**
- Constructores/fábricas que garanticen invariantes
- Value Objects inmutables
- Métodos que expresen intención (no “setters” indiscriminados)

---

### 4.2 `application/` (casos de uso)
**Contiene:**
- Implementación de casos de uso: `RegisterUserUseCase`, `LoginUseCase`, `GetMeUseCase`
- Orquestación: validaciones, llamadas a puertos, transacciones lógicas
- Políticas de seguridad “de negocio” (ej. “si mfaEnabled entonces…”) — no detalles de JWT

**Buenas prácticas:**
- Un caso de uso = una clase (pequeña y enfocada)
- Inputs/Outputs claros (DTOs o records internos)
- No filtrar detalles de infraestructura (evitar `HttpServletRequest`, etc.)

---

### 4.3 `infrastructure/` (adaptadores)
**Contiene:**
- Web Controllers (Spring MVC)
- Persistencia Mongo (documents, repositorios Spring Data, mappers)
- Seguridad (JWT filters, config)
- Configuración (Beans, properties, Actuator)

**Buenas prácticas:**
- Controllers *delgados*: validan request, llaman caso de uso, mapean response.
- Persistencia aislada: `MongoUserRepositoryAdapter implements UserRepositoryPort`
- Manejo de errores consistente (`@ControllerAdvice`)

---

## 5) Patrones recomendados (para evolucionar sin romper la arquitectura)

### 5.1 Ports & Adapters (obligatorio)
- **Port OUT**: interfaz en `domain/port/out/`
- **Adapter**: implementación en `infrastructure/.../adapter/`

Ejemplo conceptual:
```java
// domain/port/out/UserRepositoryPort.java
public interface UserRepositoryPort {
  Optional<User> findByPhone(PhoneNumber phone);
  User save(User user);
}
```

### 5.2 Use Case per Endpoint (simple y claro)
- 1 endpoint principal ↔ 1 caso de uso.
- Facilita a un agente IA crear PRs pequeños y testeables.

### 5.3 Mapper pattern (evitar fugas de modelos)
- `Document <-> Domain`, `Request <-> Input`, `Output <-> Response`
- Los mappers viven en infraestructura (web/persistence).

### 5.4 Result / Error Model (errores predecibles)
- Dominio lanza excepciones controladas.
- Web layer traduce a HTTP (409, 400, 401).

### 5.5 Configuración “12-factor”
- Secrets por variables de entorno.
- `application.yml` sin credenciales.

---

## 6) Contratos y API Evolution
1. Toda API expuesta debe estar en **OpenAPI** (SpringDoc).
2. Cambios incompatibles ⇒ versión (`/v1`, `/v2`) o estrategia acordada.
3. Responses consistentes:
   - `errorCode`, `message`, `traceId` (si hay observabilidad)

---

## 7) Seguridad mínima (PoC pro)
- Password hash: **BCrypt**
- JWT firmado, expiración corta (15–30m)
- `tempToken` de MFA con expiración menor (ej. 5m)
- No loggear tokens ni `passwordHash`
- `GET /users/me` requiere Bearer token

---

## 8) Observabilidad mínima
- `traceId` por request (filtro)
- logs estructurados (key=value)
- Actuator: health/info/metrics (según política)

---

## 9) Evolución del proyecto (ruta recomendada)
### Fase 0 — PoC (actual)
- Auth (register/login/mfa/me)
- Mongo + Docker Compose
- CI básico + pruebas unitarias

### Fase 1 — MVP técnico
- Integración tests (Testcontainers Mongo)
- Contratos API + validaciones más robustas
- Hardening de seguridad (rate limit, CSRF si aplica)

### Fase 2 — Producto
- Modularización por bounded context (Identity, Wallet, Transactions)
- Eventos internos (sin microservicios aún) si se requiere
- Observabilidad completa + auditoría

> Regla: **primero modular monolith**, microservicios solo si hay drivers claros (escala organizacional, despliegue independiente, etc.).

---

## 10) Checklist de PR (humano + agente IA)
- [ ] Cambios en una sola intención (feature o refactor pequeño)
- [ ] OpenAPI actualizado (si aplica)
- [ ] Tests AAA agregados/actualizados
- [ ] No PII/tokens en logs
- [ ] Lint/format pasa
- [ ] README/Docs actualizados si toca configuración
