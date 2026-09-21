# Phantom Company: travel development

The project is prepared to run locally without an internet connection. Open a fresh PowerShell window after the setup restart so it receives the saved Java, Node, Gradle, Git, and PostgreSQL settings.

## Start the database

```powershell
cd <path-to-phantom-company>\backend
.\scripts\db-start.ps1
```

PostgreSQL is also registered as the per-user `PhantomCompanyPostgres` startup task, so it normally starts when you sign in. The command above is safe to run anyway. The database listens only on this computer at `127.0.0.1:5432`; its files live in `D:\PhantomCompany\postgres-data`.

## Start the backend

In the same PowerShell window:

```powershell
.\gradlew.bat run
```

The API is available at `http://127.0.0.1:8080/api/health`. Database migrations run automatically when the API starts.

## Start the browser game

Open a second PowerShell window:

```powershell
cd <path-to-phantom-company>\frontend
npm run dev
```

Open the address printed by Vite, normally `http://127.0.0.1:5173`.

## Before closing the laptop

Stop the frontend and backend with `Ctrl+C`. Then make a database backup and stop PostgreSQL:

```powershell
cd <path-to-phantom-company>\backend
.\scripts\db-backup.ps1
.\scripts\db-stop.ps1
```

Backups are stored in `D:\PhantomCompany\backups`. Copy that folder to another device or cloud storage before travel if you want protection from laptop loss.

## Offline checks

The required packages and Gradle distribution have already been cached. To confirm the backend without network access:

```powershell
.\gradlew.bat --offline test
```

The application password is stored in your Windows user environment as `PHANTOM_DB_PASSWORD`; it is not committed to the project.
