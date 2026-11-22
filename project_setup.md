# Project Setup Log

Conversational AI Chat App. Follows `Conversational AI Chat App - Complete Project Document` (phases in section 10).

## Layout

```
chat-app/
├── Backend/              Spring Boot API (Maven, Java 25)
├── frontend/             React + Vite (JavaScript)
├── docker-compose.yml    PostgreSQL 18
├── .env                  local secrets (git-ignored)
├── .env.example          template for .env
└── project_setup.md      this file
```

## Prerequisites (Phase 0)

| Tool | Version found |
|---|---|
| JDK | 25 |
| Node | 24 LTS |
| Docker | 29.x (Docker Desktop must be running) |

- Git repository initialised at the repo root.
- Root `.gitignore`: `.env`, `target/`, `node_modules/`, `dist/`, `.idea/`.

## Phase 1: Database

- `docker-compose.yml` runs `postgres:18`, container `chatapp-db`, port 5432, named volume `chatapp-pgdata`.
- Credentials come from `.env` (`DB_NAME`, `DB_USER`, `DB_PASSWORD`); `DB_PASSWORD` is required.

```powershell
docker compose up -d db
```

Check: both tables and both indexes exist (Flyway creates them on first backend start).

## Phase 2: Backend skeleton

- Generated with Spring Initializr. Spring Boot **4.1.2** (the doc says 3.x; kept 4.x).
- Dependencies: Web MVC, Data JPA, Validation, Actuator, Flyway (+ PostgreSQL module), PostgreSQL driver, Lombok, Testcontainers.
- Added `com.anthropic:anthropic-java:3.12.0`.
- `src/main/resources/application.yml` (replaced `application.properties`): datasource from env vars, `ddl-auto: validate`, `open-in-view: false`, virtual threads on, Anthropic model `claude-sonnet-5-5`, chat limits (8000 chars, 24000 token budget), CORS origin `http://localhost:5173`.
- `db/migration/V1__init.sql`: `chat_sessions`, `chat_messages`, and the two indexes (section 8).
- Package `com.chatapp.backend.session`: `ChatSession`, `ChatMessage`, `ChatSessionRepository`, `ChatMessageRepository`.

Status: compiles (`./mvnw -DskipTests compile`). Not yet run against a live database.

## Environment variables (`.env`)

```
DB_NAME=chatapp
DB_USER=chatapp
DB_PASSWORD=<your password>
DB_URL=jdbc:postgresql://localhost:5432/chatapp
ANTHROPIC_API_KEY=<your key>
```

Never commit `.env`.

## Frontend skeleton (Phase 5 groundwork, built early; running on mock data)

- Vite + React (JavaScript, `.jsx`), Tailwind v4 (`@tailwindcss/vite`), React Router, TanStack Query, react-markdown, Vitest + Testing Library.
- `vite.config.js`: Tailwind plugin, `/api` proxy to `http://localhost:8080`, Vitest (jsdom, `src/test/setup.js`).
- `npm run dev` (http://localhost:5173), `npm run build`, `npm run lint`, `npm test`.
- Routes: `/` (NewChatPage), `/chat/:sessionId` (ChatPage); unknown paths redirect to `/`.
- Built: `AppLayout` (sidebar, mobile drawer), `Sidebar`, `Composer`, `MessageList`, `MessageBubble`, `EmptyState`, both pages.
- Empty stubs: `api/*`, `hooks/*`, `CodeBlock`, `ErrorBanner`, `ConfirmDialog`, `Skeletons`.
- Data comes from `src/lib/mockData.js` until the backend API exists.

## Progress vs plan

| Phase | Status |
|---|---|
| 0 Prerequisites | Done |
| 1 Database | Files written; needs Docker running to verify |
| 2 Backend skeleton | Written and compiling; needs DB to verify Flyway and `validate` |
| 3 Identity and session CRUD | Next |
| 4 Claude streaming | Pending |
| 5 Frontend skeleton | Layout done on mock data; REST wiring pending |
| 6-9 | Pending |

## Next steps

1. Start Docker Desktop, create `.env`, run `docker compose up -d db`.
2. Start the backend; confirm Flyway applies V1 and `validate` passes.
3. Phase 3: `UserIdentityFilter`, `SessionService`, `SessionController`, `GlobalExceptionHandler`, Testcontainers tests (including foreign-owner 404).
