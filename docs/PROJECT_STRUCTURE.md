# Estructura del proyecto (Monorepo PoC)

```
poc-wallet-aaas/
  backend/                 # Spring Boot (Hexagonal)
  frontend/                # Next.js (Atomic Design)
  infra/                   # docker-compose, scripts infra
  docs/
    SETUP_FROM_ZERO.md     # Setup desde cero (VS Code)
    PROJECT_STRUCTURE.md   # Este archivo
    POC_PLAN_JULES.md      # Plan operativo PoC (Jules + Dev)
    guidelines/            # Guías de arquitectura + testing AAA + playbook agente
    backlog/               # HU/US y alcance
    requirements/          # PDFs, estimación, insumos
    prompts/               # Prompts para Codex CLI y Jules
  scripts/                 # automatización local (opcional)
  .github/                 # CI + templates
  .vscode/                 # settings/tasks/extensions
  .env.example
  README.md
```

## Reglas rápidas
- 1 HU = 1 PR (ideal).
- Mantén el alcance PoC: Identidad & Acceso.
- Respeta guías:
  - Backend Hexagonal: `docs/guidelines/BACKEND_GUIDELINES.md`
  - Frontend Atomic: `docs/guidelines/FRONTEND_GUIDELINES.md`
  - Testing AAA: `docs/guidelines/TESTING_AAA_GUIDELINES.md`
