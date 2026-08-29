# AGENTS.md — O.M.C.E

## Structure — code is under `projetos/`, not repo root

```
projetos/back-end/        Java 23 / Spring Boot 3.4.3 / Maven — API at :8080
projetos/front-end-react/ React + Vite + pnpm — dev :5173, prod nginx :80
projetos/mobile/          Expo SDK 52 / React Native 0.76 / expo-router / pnpm
projetos/docker/          dev MySQL only
projetos/testesrobot/     Robot Framework — LEGACY, do not touch unless asked
projetos/docs/guidelines/ source of truth for architecture and change rules
documentacao/             spec PDFs + SQL models — reference only
```

Skills: `.opencode/skills/spring-boot-best-practices/SKILL.md` (back-end) and `.opencode/skills/react-ux-ui-best-practices/SKILL.md` (front-end) — auto-loaded, describe project-specific patterns. Root `README.md` is minimal; trust `projetos/docs/guidelines/*.md` + executable configs over prose.

## Stack quirks

- **Back-end package:** `OMCE.OMCE` (`projetos/back-end/src/main/java/OMCE/OMCE/`). Controllers in `controller/`, business in `*/service/` or `User/Service/`, data in `repository/`, I/O in `dto/` per domain. Flyway migrations in `src/main/resources/db/migration/` (`V1__`..`V17__` exists — never edit applied migrations, add next `V18__` incrementally).
- **Front-end-react:** entry `src/main.jsx` → `src/app/App.jsx` → `src/routes/AppRoutes.jsx`. All API calls must go through `src/api.js:request()` (handles `Authorization: Bearer <jwt>` from `localStorage` + JSON error extraction). Do not create parallel fetch clients.
- **Mobile:** expo-router file-based routing in `app/`. Alias `@/*` → `<rootDir>/*` (`tsconfig.json:paths`, `jest.config.js:moduleNameMapper`). Central client `src/lib/api.ts:request()` + `src/context/AppContext.tsx`. Storage: `expo-secure-store` for session (`omce_session`), `AsyncStorage` for cart.
- **DB:** MySQL 8, three compose files (see below). JPA `ddl-auto=none` (prod) / `update` (test profile only).

## Commands

All paths relative to repo root.

```bash
# Full stack (DB + back-end + front-end)
docker compose -f projetos/docker-compose.yml up --build

# Dev DB only (port 3306, db `omce`)
docker compose -f projetos/docker/docker-compose.yaml up -d

# Back-end — requires Java 23
cd projetos/back-end
./mvnw test                          # unit + integration (needs test DB)
docker compose up -d mysql_db        # test DB on 3307 (omce_test, root/senha_root) — from this dir, uses compose.yaml
./mvnw -DskipTests package           # build jar
./mvnw spring-boot:run               # run without Docker (needs MySQL on 3306)

# Front-end React — requires Node 22 + pnpm (uses frozen lockfile in Docker)
cd projetos/front-end-react
pnpm install
pnpm dev                             # Vite :5173
pnpm build                           # production check (no test suite)
pnpm preview

# Mobile — pnpm, Expo
cd projetos/mobile
pnpm install
pnpm start                           # Expo dev server
pnpm android / pnpm ios / pnpm web
pnpm typecheck                       # tsc --noEmit
pnpm test                            # jest --runInBand, tests in __tests__/**/*.test.{ts,tsx}
```

Single-package verification: run only the relevant command above. No root-level test/lint runner.

## Environment

| Var | Where | Default |
|-----|-------|---------|
| `VITE_API_URL` | `front-end-react` | `http://localhost:8080` (`src/api.js:1`) |
| `EXPO_PUBLIC_API_URL` | `mobile` | `http://10.0.2.2:8080` on Android, `http://localhost:8080` elsewhere (`src/lib/api.ts:4`) — copy `.env.example` → `.env` |
| `SPRING_DATASOURCE_*` | `back-end` | `application.properties:2-4` = `localhost:3306/omce` (usuario/senha); test profile `application-test.yml:3` = `127.0.0.1:3307/omce_test` (root/senha_root) |

Mobile physical device: set `EXPO_PUBLIC_API_URL` to host LAN IP (e.g. `http://192.168.1.10:8080`), not localhost.

## Gotchas

- **Working directory matters:** Maven wrapper is `projetos/back-end/mvnw` (not `mvn`). Compose files are not interchangeable — `projetos/docker-compose.yml` (full stack, ports 3306/8080/5173), `projetos/docker/docker-compose.yaml` (dev DB only, 3306), `projetos/back-end/compose.yaml` (test DB, 3307). Integration tests use `replace: none` (`application-test.yml:15`) so they hit real MySQL, not H2.
- **Credentials are committed** in `application.properties` and compose files (e.g. `senha`/`senha_root`, Gmail app password). Treat as existing exposure — do not copy/repeat in new code, logs, or docs; use env vars for new secrets.
- **Legacy scope:** `testesrobot/` (and the deleted `front-end/` static site) are out of scope per `docs/guidelines`. Never modify or run Robot tests unless the task explicitly says so.
- **Style:** preserve Portuguese naming (DTOs, messages like `mensagem`) and existing `controller → service → repository` separation. Keep HTTP status/auth/error formats compatible unless breaking change is requested.

## Validation (from `projetos/docs/guidelines/VALIDACAO_E_SEGURANCA.md`)

| Change | Minimum check |
|--------|---------------|
| Back-end | `cd projetos/back-end && ./mvnw test` (start test DB first: `docker compose up -d mysql_db`) |
| Front-end React | `cd projetos/front-end-react && pnpm build` |
| Mobile | `pnpm typecheck && pnpm test` |
| Dockerfiles/compose | `docker build` / `docker compose config` |

Robot tests are not part of the supported flow.

## Before changing code

Read in order: `projetos/docs/guidelines/CONTEXTO_DO_PROJETO.md` → `REGRAS_DE_ALTERACAO.md` → `VALIDACAO_E_SEGURANCA.md`. For API changes, inspect controller + service + DTOs + validations + tests + consumers (`front-end-react/src/api.js` and `mobile/src/lib/api.ts`) before touching the contract.
