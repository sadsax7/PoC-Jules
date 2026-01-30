# Testing Guidelines — Patrón AAA (Arrange–Act–Assert) para Backend y Frontend

## 1) Objetivo
Estándar único de pruebas para que:
- un equipo humano mantenga consistencia
- un agente IA pueda generar tests predecibles y revisables

---

## 2) Patrón AAA (obligatorio)
Cada test debe tener secciones claras:

1. **Arrange**: preparar datos, mocks, estado inicial  
2. **Act**: ejecutar la acción bajo prueba  
3. **Assert**: validar resultados (y efectos secundarios)

Ejemplo (pseudo):
```txt
Arrange: crear usuario, mock repo
Act: ejecutar RegisterUserUseCase
Assert: userId existe, repo.save llamado, errores correctos
```

---

## 3) Backend (JUnit 5 + Mockito)

### 3.1 Tipos de pruebas (pirámide)
- **Unitarias (prioridad):** dominio + casos de uso  
- **Integración (mínimas):** repos Mongo + security filter chain (si aplica)  
- **Contract/API:** tests de controller (MockMvc/WebTestClient) para endpoints críticos

### 3.2 Ubicación y naming
- `RegisterUserUseCaseTest`
- `AuthControllerTest`
- `MongoUserRepositoryAdapterIT` (Integration Test)

### 3.3 Reglas de mocking
- Mockear **puertos out** en tests de application
- No mockear lo que estás probando
- Evitar mocks “inútiles”: sólo lo necesario

### 3.4 Checklist AAA Backend
- [ ] Arrange explícito (builders/fixtures)
- [ ] Act = 1 acción principal
- [ ] Assert = resultado + efectos (llamadas, estado)
- [ ] Tests deterministas (sin sleeps)
- [ ] Casos: happy path + edge + error

---

## 4) Frontend (Jest + React Testing Library)

### 4.1 Tipos de pruebas
- **Unit UI:** atoms/molecules (render y props)
- **Component tests:** organisms (flujos: login, MFA)
- **Hooks tests:** `useLogin`, `useRegister` (mock fetch/client)
- (Opcional) **E2E**: Playwright (si el alcance crece)

### 4.2 AAA con RTL
Ejemplo conceptual:
- Arrange: render componente con props + mock API
- Act: usuario escribe y hace click (userEvent)
- Assert: se ve error / se llama API / se navega

### 4.3 Checklist AAA Frontend
- [ ] Arrange: render + mocks claros
- [ ] Act: interacciones reales (userEvent)
- [ ] Assert: UI visible + llamadas + estado
- [ ] No snapshots “ciegos” (usar assertions significativas)

---

## 5) Datos de prueba (Fixtures)
- Centralizar fixtures en:
  - backend: `src/test/java/.../fixtures/`
  - frontend: `src/__tests__/fixtures/`
- Usar builders para entidades:
  - `UserBuilder.withPhone(...).withMfaEnabled(true).build()`

---

## 6) Qué debe generar un agente IA cuando crea tests
Por cada feature:
1. 1–3 tests unitarios de caso de uso (happy + 1 error)
2. 1 test de controller/endpoint (status codes)
3. En FE: 1 test de organism (flujo principal) + 1 test de error

---

## 7) Definition of Done (DoD) de testing (PoC)
- Unit tests cubren la lógica clave
- Al menos 1 test de integración “smoke” por capa crítica
- CI corre tests en cada PR
- Cobertura objetivo PoC: **≥ 70–80%** (subir a 85% en hardening)
