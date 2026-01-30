# Prompts profesionales para Jules (PoC)

> Úsalo como “pair developer” orientado a PRs pequeños.

## Plantilla base de tarea
**Contexto:** Estamos en una PoC, alcance limitado (Identidad & Acceso).  
**Guías:** backend hexagonal + frontend atomic + testing AAA.  
**No negociables:** no PII en logs, PR pequeño, tests AAA.

### Prompt
- Toma la HU: <ID>
- Implementa solo lo necesario para cumplir criterios de aceptación.
- Respeta la estructura:
  - backend: domain/application/infrastructure
  - frontend: atoms/molecules/organisms/templates/pages
- Agrega pruebas AAA.
- Ejecuta linters/tests y pega output.
- Devuelve: lista de archivos tocados + resumen de decisiones.

## Ejemplos rápidos
### Jules — HU-BE-01 Register
“Implementa /auth/register en Spring Boot siguiendo hexagonal, con DTOs, validación, BCrypt, índice único phone, manejo 409, tests AAA para el caso de uso.”

### Jules — HU-FE-03 MFA flow
“Implementa flujo login + pantalla MFA (OTP 6 dígitos). Si API retorna MFA_REQUIRED, muestra OTP y llama /auth/mfa/verify. Tests AAA del organism.”
