# Frontend Guidelines — Next.js (Atomic Design) + Testing AAA

## 1) Objetivo
Guías para evolucionar el frontend de la PoC con:
- **Next.js + TypeScript strict**
- **Atomic Design** (Atoms/Molecules/Organisms/Templates/Pages)
- **Testing AAA (Arrange–Act–Assert)**

Diseñadas para que las ejecute un equipo humano o un agente IA con cambios pequeños y repetibles.

---

## 2) Principios no negociables
1. **Componentes pequeños, composición grande**
   - Atoms no conocen negocio; Organisms sí.
2. **Diseño consistente**
   - Tokens en Tailwind (colors, spacing, typography).
3. **Lógica fuera del JSX**
   - Hooks para side-effects y llamadas a API.
4. **1 feature = 1 PR**
   - Evitar PRs que “reformen todo”.

---

## 3) Estructura Atomic Design recomendada
```
frontend/
  src/
    app/                     # Next.js App Router (routes)
      (public)/
      (private)/
    components/
      atoms/
      molecules/
      organisms/
      templates/
    hooks/
      useAuth/
      useRegister/
    lib/
      api/                   # cliente fetch/axios, interceptores
      auth/                  # helpers token/session
      config/                # env + constants
    styles/
    types/
  src/__tests__/             # tests AAA (o junto al archivo)
```

---

## 4) Reglas por nivel (Atomic Design)

### 4.1 Atoms
- UI pura: Button, Input, Text, Spinner
- Props minimalistas y tipadas
- Sin llamadas a API
- Sin lógica de negocio

### 4.2 Molecules
- Combinan atoms: Input + Label + Error
- Manejan estados locales simples (ej. focus)

### 4.3 Organisms
- Unidades funcionales: LoginForm, RegisterWizard, MFACodeInput
- Pueden usar hooks (`useLogin`, `useRegister`)
- No deben contener rutas ni layout global

### 4.4 Templates
- Layouts de pantalla: AuthLayout, DashboardLayout
- Componen organisms y definen estructura

### 4.5 Pages/Routes
- Mínima lógica: elegir template, validar sesión, navegar
- `app/(private)/dashboard/page.tsx` debe proteger ruta

---

## 5) Estado y Data Fetching
- Preferir **server components** solo para contenido público/estático.
- Para auth + dashboards: hooks cliente (`useEffect`) o server actions si está definido.
- Centralizar llamadas en `lib/api/`:
  - `apiClient.ts` (fetch wrapper)
  - manejo consistente de errores
  - inyectar token desde `lib/auth/`

---

## 6) Auth y sesiones (PoC)
### Recomendado (si el backend lo soporta)
- Tokens en **cookie httpOnly** (más seguro) + refresco si se implementa.

### Alternativa PoC (si no hay cookies httpOnly)
- Token en memory + fallback en storage
- **Nunca** imprimir token en consola
- Logout borra todo y redirige a Landing

---

## 7) Patrones recomendados
### 7.1 Container/Presenter (simple)
- Organism actúa como “container” (usa hooks)
- Atoms/Molecules son “presenters”

### 7.2 Hook per Feature
- `useRegister`, `useLogin`, `useMfaVerify`, `useMe`
- Facilita a IA: “crea hook + tests + integra en organism”

### 7.3 DTO mapping
- `types/` define DTOs de request/response
- No usar `any`

---

## 8) Accesibilidad y UX
- Inputs con `label` asociado
- Estados: loading, error, success
- Manejo de foco en OTP/MFA

---

## 9) Evolución del proyecto (ruta recomendada)
### Fase 0 — PoC
- Landing
- Registro
- Login + MFA
- Dashboard + Logout

### Fase 1 — UI Hardening
- Librería de componentes estable
- Pruebas más completas (RTL)
- Storybook (opcional)

### Fase 2 — Producto
- Layouts y navegación por roles
- Telemetría UX
- E2E (Playwright) si el alcance crece

---

## 10) Checklist de PR (humano + agente IA)
- [ ] Respeta Atomic Design (no mezclar niveles)
- [ ] TS strict sin `any`
- [ ] Hook para lógica y API
- [ ] Tests AAA agregados/actualizados
- [ ] Responsive verificado
- [ ] No secretos/tokens en logs/console
