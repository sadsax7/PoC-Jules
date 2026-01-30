# AI Agent Playbook — Cómo contribuir sin romper el proyecto

## 1) Objetivo
Permitir que un agente IA (Jules u otro) contribuya con PRs consistentes, revisables y alineados a la arquitectura.

---

## 2) Reglas de trabajo del agente
1. **PR pequeño**: 1 HU o 1 sub-tarea técnica (máx. 300–600 líneas netas si se puede).
2. **No refactor masivo** mientras se implementa feature.
3. **Respeta capas**:
   - Backend: domain/application/infrastructure
   - Frontend: atoms/molecules/organisms/templates/pages
4. **Siempre incluir tests AAA** para lo que se toca.
5. **Actualizar docs** si hay cambios de API/config.
6. **Nunca exponer secretos** (tokens, hashes, envs) en logs.

---

## 3) Checklist antes de abrir PR
- [ ] Compila
- [ ] Tests pasan
- [ ] Lint/format pasa
- [ ] OpenAPI actualizado (si aplica)
- [ ] No PII en logs
- [ ] Cambios limitados al scope

---

## 4) Plantillas de tareas “amigables para IA”
### Backend (ejemplo)
- “Crea el puerto `UserRepositoryPort` y su adapter Mongo. No toques controllers. Agrega tests AAA del adapter.”

### Frontend (ejemplo)
- “Crea atom `Button` + tests AAA. No tocar pages.”

---

## 5) Señales de alarma (PR se rechaza)
- Cambia estructura de carpetas sin justificación
- Mezcla web + domain en backend
- Lógica de negocio en atoms/molecules
- Introduce `any` en TS
- Sin tests en cambios funcionales
