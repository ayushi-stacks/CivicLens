# CivicLens Phase 2A

Native Kotlin/Jetpack Compose civic issue reporting app with local Room persistence.

## Run & Operate

- `pnpm --filter @workspace/api-server run dev` — run the API server (port 5000)
- `pnpm run typecheck` — full typecheck across all packages
- `pnpm run build` — typecheck + build all packages
- `pnpm --filter @workspace/api-spec run codegen` — regenerate API hooks and Zod schemas from the OpenAPI spec
- `pnpm --filter @workspace/db run push` — push DB schema changes (dev only)
- Required env: `DATABASE_URL` — Postgres connection string
- Android build: `cd civiclens-android && ./gradlew assembleDebug`

## Stack

- pnpm workspaces, Node.js 24, TypeScript 5.9
- API: Express 5
- DB: PostgreSQL + Drizzle ORM
- Validation: Zod (`zod/v4`), `drizzle-zod`
- API codegen: Orval (from OpenAPI spec)
- Build: esbuild (CJS bundle)
- Android: Kotlin 2.0.21, Compose Material 3, Room 2.6.1, Gradle 8.10.2

## Where things live

- `civiclens-android/app/src/main/java/com/civiclens/app/` — Compose screens, reusable CivicLens components, Room entities/DAOs, repositories, and ViewModels
- `civiclens-android/app/src/main/java/com/civiclens/app/Database.kt` — Room entities, converters, and database definition
- `civiclens-android/app/src/main/java/com/civiclens/app/SeedData.kt` — first-launch database initialization and Kolkata demo seed data

## Architecture decisions

- Phase 2A keeps the Phase 1 visual layer intact and replaces mock reads/actions at the screen boundary.
- Room is local-first and intentionally has no cloud synchronization or authentication yet.
- Demo data is inserted only when the issue table is empty, so normal launches do not duplicate records.
- Issue detail and verification routes carry the selected Room issue ID.

## Product

Users can browse civic issues around Kolkata, report a seeded pothole issue, verify an issue, view activity and profile impact, and see civic points update from local persistence.

## User preferences

- Preserve the Phase 1 Compose UI and CivicLens visual system; make the smallest UI changes needed to connect data.

## Gotchas

- Android builds require JDK 17 and an Android SDK with platform 35/build tools available at the configured `sdk.dir`.

## Pointers

- See the `pnpm-workspace` skill for workspace structure, TypeScript setup, and package details
