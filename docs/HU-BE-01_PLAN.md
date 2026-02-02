# HU-BE-01 Plan (Registro)

Fecha: 2026-02-02

## 1) Requisitos exactos (fuente: backlog)

- DoR:
  - OpenAPI 3.0 (SpringDoc) para `POST /auth/register` definido y aprobado. (docs/backlog/UserStories.md:52-54)
  - Reglas de validacion definidas (telefono, password). (docs/backlog/UserStories.md:54-55)
  - Mock KYC acordado (simulado / interno). (docs/backlog/UserStories.md:55-55)
- Historia:
  - Registrar usuarios en Mongo con validaciones para alta segura y unica por telefono. (docs/backlog/UserStories.md:57-60)
- Criterios de Aceptacion:
  - Modelo `users`: phone (indice unico), email opcional, passwordHash, kycStatus, mfaEnabled, createdAt. (docs/backlog/UserStories.md:63-69)
  - Password nunca en texto plano; usar BCryptPasswordEncoder. (docs/backlog/UserStories.md:70-70)
  - Mock KYC: si falla, no crea usuario. (docs/backlog/UserStories.md:71-71)
  - Errores: si phone existe -> 409 Conflict (handler consistente). (docs/backlog/UserStories.md:72-72)
  - Respuesta: 201 Created con userId (sin datos sensibles). (docs/backlog/UserStories.md:73-73)
- DoD:
  - Lint/format OK. (docs/backlog/UserStories.md:76-76)
  - Unit tests JUnit5 + Mockito con cobertura >= 80%. (docs/backlog/UserStories.md:77-77)
  - OpenAPI documentado (request/response/errors). (docs/backlog/UserStories.md:78-78)
  - No PII en logs; DTOs sin passwordHash. (docs/backlog/UserStories.md:79-79)
  - Controller/Service < 500 lineas. (docs/backlog/UserStories.md:80-80)
  - Probado con Postman/REST Client (evidencia). (docs/backlog/UserStories.md:81-81)

## 2) Alineacion con guias

- Hexagonal estricto:
  - Domain sin framework, application orquesta, infrastructure es plugin. (docs/guidelines/BACKEND_GUIDELINES.md:13-21)
  - Estructura recomendada por capas (domain/application/infrastructure). (docs/guidelines/BACKEND_GUIDELINES.md:32-63)
  - Use case por endpoint y controllers delgados. (docs/guidelines/BACKEND_GUIDELINES.md:130-133,108-110)
  - Error model consistente y traducido a HTTP. (docs/guidelines/BACKEND_GUIDELINES.md:138-141)
- Testing AAA:
  - AAA obligatorio. (docs/guidelines/TESTING_AAA_GUIDELINES.md:10-16)
  - Piramide: unitarias primero, integracion minima, controller tests. (docs/guidelines/TESTING_AAA_GUIDELINES.md:28-31)
  - Naming recomendado: RegisterUserUseCaseTest, AuthControllerTest, MongoUserRepositoryAdapterIT. (docs/guidelines/TESTING_AAA_GUIDELINES.md:33-36)

## 3) Plan incremental en 7 pasos (micro-commits)

1) Dependencias y OpenAPI
   - Agregar SpringDoc OpenAPI (DoR). (docs/backlog/UserStories.md:52-54)
   - Agregar BCrypt (security) para hashing. (docs/backlog/UserStories.md:70-70)
2) Domain
   - Entidad User + Value Objects (PhoneNumber, Email) con invariantes.
   - Puertos out: UserRepositoryPort, PasswordHasherPort, KycServicePort.
   - Excepciones de dominio (UserAlreadyExists, InvalidPhone, InvalidPassword, KycFailed).
3) Application
   - RegisterUserUseCase + DTOs internos (command/result).
   - Orquestacion: validar -> KYC -> hash -> persistir.
4) Infrastructure (persistence + kyc + security)
   - Mongo Document + Spring Data repository.
   - Adapter Mongo implementa UserRepositoryPort.
   - MockKycServiceAdapter deterministico.
   - BCryptPasswordHasherAdapter.
5) Web
   - AuthController `POST /auth/register` + request/response DTOs.
   - ControllerAdvice para mapear errores a 400/409.
6) Tests
   - Unit tests AAA: domain + RegisterUserUseCase.
   - Controller tests (status 201/409/400).
   - Integration test con Testcontainers para Mongo adapter.
7) Docs
   - OpenAPI actualizado.
   - README/backlog si aplica (sin cambiar codigo).

## 4) Checklist de verificacion local

- Tests:
  - `mvn test` (esperado: verde; cobertura >= 80%). (docs/backlog/UserStories.md:77-77)
- Infra:
  - `docker compose -f infra/docker-compose.dev.yml --env-file .env up --build -d`
- Endpoints:
  - `curl -i -X POST http://localhost:8080/auth/register -H "Content-Type: application/json" -d '{"phone":"+5491122334455","email":"user@example.com","password":"Pass1234"}'`
    - Esperado: 201 Created con userId. (docs/backlog/UserStories.md:73-73)
  - Repetir mismo phone:
    - Esperado: 409 Conflict. (docs/backlog/UserStories.md:72-72)
  - Validacion invalida:
    - Esperado: 400 Bad Request (segun reglas definidas). (docs/backlog/UserStories.md:54-55)

## 5) Decisiones ya cerradas

- Formato phone E.164, password 8-64, mock KYC deterministico, defaults kycStatus/mfaEnabled, formato de errores.
  (docs/DECISIONS_AND_RISKS.md:18-54)
