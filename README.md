# Phantom Company

A browser-playable guild-management prototype with a TypeScript/React frontend, a Kotlin/Ktor backend, and PostgreSQL persistence.

## Project layout

- `frontend/` — React game client and iteration-zero guild selection
- `backend/` — Ktor API, PostgreSQL persistence, migrations, and tests
- `sources/` — game design and planning reference material
- `TRAVEL_DEVELOPMENT.md` — local and offline development workflow

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
