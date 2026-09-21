# Phantom Company Backend

Kotlin and Ktor API for the Phantom Company browser prototype.

## Database

PostgreSQL runs locally on `127.0.0.1:5432`. Connection credentials are stored in the Windows user environment, not in the repository.

```powershell
.\scripts\db-start.ps1
.\scripts\db-status.ps1
.\scripts\db-backup.ps1
.\scripts\db-stop.ps1
```

Flyway applies migrations from `src/main/resources/db/migration` when the API starts.

## Commands

```powershell
.\gradlew.bat test
.\gradlew.bat build
.\gradlew.bat run
```

The service listens on `http://localhost:8080`.

- `GET /api/health`
- `POST /api/guilds` with `{ "type": "training" | "raiding" | "rehab" }`
