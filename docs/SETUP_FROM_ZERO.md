# Setup desde cero (Windows + VS Code) — PoC Wallet AaaS

> Meta: dejar tu PC lista **desde 0** para clonar el repo, levantar Mongo, correr backend/ frontend, y empezar a delegar tareas a agentes (Jules / Devin / etc).

---

## 0) Pre-requisitos (lo mínimo)
Instala (en este orden):

1) **Git**
2) **Visual Studio Code**
3) **Docker Desktop** (para Mongo local y futuro CI parity)
4) **Java 17 o 21 (LTS)**
5) **Maven** (requerido para la PoC)
6) **Node.js LTS (>= 18, recomendado 20)**

> Si estás en Windows, considera usar **WSL2** para trabajar con herramientas CLI de agentes.

---

## 1) VS Code — Extensiones recomendadas
Instala:
- Java Extension Pack
- Spring Boot Extension Pack
- Docker
- ESLint
- Prettier
- Tailwind CSS IntelliSense
- GitLens (opcional)

(Ya dejamos sugerencias en `.vscode/extensions.json`.)

---

## 2) Clonar repo y preparar estructura
En una carpeta de trabajo:

```bash
git clone <TU_REPO_GITHUB> poc-wallet-aaas
cd poc-wallet-aaas
```

Copia el ejemplo de variables:

```bash
cp .env.example .env
```

---

## 3) Infra — MongoDB con Docker
Levanta Mongo:

```bash
docker compose -f infra/docker-compose.mongo.yml up -d
```

Verifica:

```bash
docker ps
```

---

## 4) Backend — Crear el proyecto Spring Boot (si aún no existe)
### Opción A (recomendada): Spring Initializr
Crea un proyecto con:
- Spring Web (imperativo)
- Spring Security
- Spring Data MongoDB (imperativo)
- Validation
- Actuator
- Lombok (opcional)
- SpringDoc OpenAPI (dependencia adicional si no sale)

Luego pega el contenido dentro de `backend/`.

### Opción B: Bootstrap con CLI (cuando ya tengas Maven)
Una vez tengas un `pom.xml`, usa:

```bash
cd backend
mvn -q -DskipTests package
```

---

## 5) Frontend — Crear Next.js (si aún no existe)
Dentro del repo:

```bash
cd frontend
npx create-next-app@latest . --ts --eslint --tailwind --app --src-dir --import-alias "@/*"
```

Luego crea carpetas Atomic:
```bash
mkdir -p src/components/{atoms,molecules,organisms,templates}
mkdir -p src/{hooks,lib,types}
```

---

## 6) Correr local (modo PoC)
### Backend
En una terminal:

```bash
cd backend
mvn spring-boot:run
```

### Frontend
En otra terminal:

```bash
cd frontend
npm install
npm run dev
```

---

## 7) Validación rápida (checklist)
- [ ] Mongo arriba (docker ps)
- [ ] Backend responde `/health` (Actuator)
- [ ] Swagger UI accesible (si ya lo agregaste)
- [ ] Frontend muestra Landing
- [ ] Registro/Login/MFA/Me funcionan contra backend

---

## 8) Cómo trabajar (humano + agente)
### Reglas de oro
- **Git checkpoints** antes de delegar tareas grandes.
- **PRs pequeños** (1 HU o sub-tarea).
- Exige evidencia: tests + comandos ejecutados + archivos tocados.

---

## 9) Codex CLI (si ya lo tienes) — flujo recomendado
1) Abre una terminal en la carpeta del repo.
2) Ejecuta `codex` (modo interactivo).
3) Dale una tarea **muy acotada** con checklist.

> Tip: en Windows, si notas fricción, usa WSL (mejor experiencia).

---

## 10) Sprint 0 (orden sugerido)
1) HU-BE-00 (scaffold hexagonal + mongo + health)
2) HU-FE-00 (scaffold Next + theme + atomic folders + styleguide)
3) HU-BE-01 (register)
4) HU-FE-02 (register UI)
5) HU-BE-02 (login + mfa)
6) HU-FE-03 (login + mfa)
7) HU-BE-03 + HU-FE-04 (me + dashboard + logout)
