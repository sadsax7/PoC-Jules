# Documentación de cambios realizados por Jules

## HU-FE-01 — Landing base con Atomic Design (Hero + CTAs)
- Se creó el átomo `Button` en `frontend/src/components/atoms/Button.tsx`.
- Se creó el molecule `HeroCtas` en `frontend/src/components/molecules/HeroCtas.tsx`.
- Se creó el organism `HeroSection` en `frontend/src/components/organisms/HeroSection.tsx`.
- Se actualizó `frontend/src/app/page.tsx` para ensamblar los componentes nuevos.
- Se verificó el comportamiento responsivo y el uso de tokens institucionales.

## HU-FE-02 — Registro (UI)
1. **Actualización del átomo Button**: se extendió `frontend/src/components/atoms/Button.tsx` para soportar navegación con `Link` y acciones de formulario con `button`, incluyendo `type`, `disabled` y `onClick`.
2. **Nuevo átomo Input**: se creó `frontend/src/components/atoms/Input.tsx` con tokens del diseño y accesibilidad (relación `id/htmlFor`).
3. **RegisterForm (molecule)**: se creó `frontend/src/components/molecules/RegisterForm.tsx` con manejo de estado, validaciones básicas y feedback visual.
4. **RegisterSection (organism)**: se creó `frontend/src/components/organisms/RegisterSection.tsx` con layout centrado tipo card.
5. **Página de registro**: se añadió `frontend/src/app/register/page.tsx` como entry point de la ruta.

## HU-FE-03 — Login + MFA (UI simulada)
- Se implementó la UI de login y MFA siguiendo Atomic Design y restricciones del proyecto.
- Se reutilizaron átomos existentes (`Button`, `Input`, `Logo`).
- Se respetaron los tokens de Tailwind v4 (primary, bg-dark, text-light).
- MFA se simula sin llamadas a API (flujo local en UI).
- `OtpInput` accesible con 6 dígitos y layout responsivo en tema oscuro.

## Verificación realizada
- `npm run lint`
- `npm run build`
- Validación manual visual con capturas de Playwright.
- Confirmación de uso de tokens institucionales.
- Confirmación de responsive básico.
