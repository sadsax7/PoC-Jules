# Decisions and Risks

Este documento registra decisiones técnicas y riesgos del PoC para mantener alineación entre **backlog**, **código**, **tests**, **infra** y **documentación**.

---

## Alcance

- PoC Wallet (monorepo)
- Backend: Spring Boot + MongoDB (hexagonal mínimo)
- Estado actual: HU-BE-00 (health + testcontainers + docker-compose dev) en `main`
- Próximo incremento: **HU-BE-01 Registro (POST /auth/register)**

---

## Decisiones Cerradas (HU-BE-01)

### D-01 — Formato y validación de `phone`

- **Decisión:** Formato **E.164 estricto**.
- **Regla:** Debe iniciar con `+` y contener **8–15 dígitos** (solo números luego del `+`).
- **Normalización:** Eliminar espacios/guiones antes de validar; persistir el valor normalizado.
- **Motivo:** Reduce ambigüedad y facilita unicidad por teléfono.

---

### D-02 — Política mínima de `password`

- **Decisión:** Política simple para PoC.
- **Regla:** Longitud **8–64** caracteres, mínimo **1 letra** y **1 número**.
- **Seguridad:** **Nunca** guardar password en texto plano. Persistir solo `passwordHash`.

---

### D-03 — Mock KYC (determinístico)

- **Decisión:** Mock interno determinístico (no aleatorio).
- **Regla sugerida:** Si `phone` comienza con `+999` ⇒ KYC falla; de lo contrario ⇒ KYC OK.
- **Motivo:** Tests estables (sin flakiness) y casos fáciles de reproducir.

---

### D-04 — Valores por defecto al registrar usuario

- **Decisión:**
  - `kycStatus` por defecto: **PENDING** (solo si KYC OK se crea; si KYC falla no se persiste nada).
  - `mfaEnabled` por defecto: **false**.

---

### D-05 — Formato estándar de errores HTTP

- **Decisión:** Respuesta de error consistente:

```json
{
  "errorCode": "STRING_CODE",
  "message": "Human readable message",
  "traceId": "optional"
}
```

---

## Riesgos abiertos

- **SpringDoc no incorporado aún:** falta definir y exponer OpenAPI 3.0 para `POST /auth/register`.
- **BCrypt pendiente de dependencia/config:** hashing obligatorio pero aún no integrado.
- **Cobertura >= 80% sin configuración explícita:** no hay evidencia de JaCoCo/medición formal de cobertura.
- **Versiones Mongo inconsistente entre entornos:** `docker-compose` usa `mongo:7` y tests usan `mongo:6.0` en Testcontainers.
