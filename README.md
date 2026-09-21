# Phantom Company

A browser-playable guild-management prototype with a TypeScript/React frontend, a Kotlin/Ktor backend, and PostgreSQL persistence.

## Project layout

- `frontend/` — React game client and iteration-zero guild selection
- `backend/` — Ktor API, PostgreSQL persistence, migrations, and tests
- `sources/` — game design and planning reference material
- `TRAVEL_DEVELOPMENT.md` — local and offline development workflow
- `HANDOFF.md` — current status, known-red acceptance test, and ordered next steps

## Quick start

Start PostgreSQL and the backend from one PowerShell window:

```powershell
cd backend
.\scripts\db-start.ps1
.\gradlew.bat run
```

Start the frontend from another PowerShell window:

```powershell
cd frontend
npm run dev
```

The frontend normally runs at `http://127.0.0.1:5173`; the API health check is at `http://127.0.0.1:8080/api/health`.

See [TRAVEL_DEVELOPMENT.md](TRAVEL_DEVELOPMENT.md) for the full offline workflow and backup commands.
If you are resuming development, start with [HANDOFF.md](HANDOFF.md).

## GitHub Codespaces

From the repository page, select **Code → Codespaces → Create codespace on main**. The checked-in development container provisions Java 21, Node 24, and a persistent PostgreSQL 18 database, then installs frontend dependencies and runs the backend tests.

Inside the codespace, start the backend and frontend in separate terminals using the same commands shown above. Codespaces forwards the game and API ports automatically.

## Tests

Fast backend tests and the PostgreSQL-backed acceptance task are separate:

```powershell
cd backend
.\gradlew.bat test
.\gradlew.bat acceptanceTest
```

The acceptance task requires `PHANTOM_DB_URL`, `PHANTOM_DB_USER`, and `PHANTOM_DB_PASSWORD`. Run the browser acceptance journey from `frontend` with `npm run test:e2e`. GitHub Actions supplies an isolated PostgreSQL service and runs the complete suite on every push and pull request.
