# Phantom Company Development Handoff

This is the restart point for the first playable vertical slice. The iteration-zero foundation is in place on both Windows and GitHub Codespaces.

## Current state

- The React/TypeScript client lets a player choose a guild, sign its charter, and see its starting headquarters.
- The Kotlin/Ktor API creates the same guild model and persists it to PostgreSQL.
- PostgreSQL is provisioned locally and in Codespaces.
- Backend unit tests, the real PostgreSQL acceptance test, frontend linting, and both production builds pass.
- GitHub Actions runs the complete stack on pushes and pull requests.
- One browser acceptance test is intentionally red: the selected guild disappears on page reload because the frontend still uses local React state instead of the API.

The deliberate failing test is `frontend/e2e/guild-creation.spec.ts`. It is the executable specification for the next slice, not an unrelated regression.

## Next vertical slice: connect the two halves

Work in this order so that each change remains small and testable:

1. Add `findById` and delete/reset operations to the backend guild repository and service.
2. Add `GET /api/guilds/{id}` and `DELETE /api/guilds/{id}` routes with explicit not-found behavior.
3. Add backend route tests and PostgreSQL acceptance coverage for create, reload, and reset.
4. Configure the Vite development server to proxy `/api` to Ktor. This keeps browser calls same-origin locally and in Codespaces.
5. Replace the frontend's local guild creation with `POST /api/guilds`, then retain only the returned guild ID in browser storage.
6. On startup, load that guild with `GET /api/guilds/{id}` and show useful loading and error states.
7. Make **Reset Campaign** call the delete route and clear the stored guild ID.
8. Run the full suite. The existing Playwright reload test should turn green without weakening its assertion.

No new database migration should be necessary for this slice; the initial schema already stores the guild, components, roster, and traits.

## Definition of done

A player can select a guild, sign the charter, reload the page, and return to the same headquarters backed by PostgreSQL. Resetting the campaign removes that guild and returns the player to guild selection. All local and hosted acceptance checks are green.

## Useful commands

On Windows, start the database once:

```powershell
cd backend
.\scripts\db-start.ps1
```

Run the backend checks:

```powershell
cd backend
.\gradlew.bat test acceptanceTest build
```

Run the frontend checks from another terminal:

```powershell
cd frontend
npm run lint
npm run build
npm run test:e2e
```

In Codespaces, the database and required environment variables are supplied by the development container:

```bash
./backend/gradlew --project-dir backend test acceptanceTest build
npm --prefix frontend run lint
npm --prefix frontend run build
npm --prefix frontend run test:e2e
```

Until the vertical slice above is complete, `test:e2e` is expected to fail only at the post-reload headquarters assertion. The current hosted result can be found on the repository's [Acceptance workflow page](https://github.com/natebuwalda/phantom-company/actions/workflows/acceptance.yml).

## Orientation

- `frontend/src/App.tsx` — current UI state and the seam to replace with API calls
- `frontend/src/game.ts` — current frontend guild-generation rules
- `frontend/e2e/guild-creation.spec.ts` — end-to-end definition of the connected slice
- `backend/src/main/kotlin/com/phantomcompany/guild/` — API models, routes, and service
- `backend/src/main/kotlin/com/phantomcompany/persistence/PostgresGuildRepository.kt` — persistence implementation
- `backend/src/main/resources/db/migration/` — Flyway-managed schema
- `backend/src/acceptanceTest/` — tests that use real PostgreSQL
- `.github/workflows/acceptance.yml` — hosted full-stack verification
- `TRAVEL_DEVELOPMENT.md` — offline setup, database operations, and backups

## Deliberately deferred

Keep the next change focused on the persistence seam. AWS deployment, authentication, multiplayer, tactical-grid combat, additional classes, and a general-purpose effects engine can wait until the create/reload/reset journey is green. After that, return to the MVF plan and build the next thin, playable loop rather than expanding infrastructure first.

The project references under `sources/` are synchronized planning material and should remain read-only.
