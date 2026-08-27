# CivicLens

CivicLens is a local-first civic issue reporting app for Kolkata, built with a native Android experience and a supporting TypeScript workspace. It helps residents see nearby infrastructure issues, report new problems with AI-assisted review, verify community reports, and track civic impact through points, activity, achievements, and resolution progress.

## Highlights

- Native Jetpack Compose Android app with a redesigned premium civic-tech interface
- Light and dark modes with distinct visual systems
- Sculpted icon-only bottom navigation with a moving selected-state indicator
- Guided reporting flow: Capture, Review, Location, Submit
- AI-style detection review for issue reports
- Map-first nearby issue exploration with search and filters
- Local Room persistence for users, issues, verifications, resolutions, and activity
- Civic points, achievements, leaderboard, and personal impact tracking
- TypeScript workspace with API specification, generated clients, Zod schemas, and server package

## Product Flow

1. Onboarding introduces reporting, verification, and tracking.
2. Home summarizes the city health index, nearby needs, recent civic activity, and user impact.
3. Map lets users search and filter nearby issues around Kolkata.
4. Report guides users through capture, AI detection review, location confirmation, optional details, and submission.
5. Verification records citizen confirmations and updates issue confidence/status.
6. Profile presents a civic identity: level, points, achievements, impact metrics, and leaderboard.
7. Activity shows a timeline of reports, verifications, resolutions, and rewards.

## Tech Stack

- Android: Kotlin, Jetpack Compose, Material 3, Navigation Compose
- Persistence: Room
- Backend workspace: Node.js, TypeScript, Express
- API contracts: OpenAPI, Orval-generated React client, Zod schemas
- Package manager: pnpm workspaces
- Build tooling: Gradle, TypeScript, esbuild

## Repository Structure

```text
civiclens-android/
  app/src/main/java/com/civiclens/app/
    CivicLensApp.kt        # App theme, routes, navigation host
    CivicComponents.kt     # Reusable Compose UI primitives and bottom nav
    Screens.kt             # Onboarding, Home, Map, Report, Details, Profile, Activity
    Database.kt            # Room database configuration
    Models.kt              # UI/domain model helpers
    ViewModels.kt          # Screen state and local-first actions
    SeedData.kt            # First-run Kolkata demo data

artifacts/api-server/      # Express API server artifact
artifacts/mockup-sandbox/  # React/Vite mockup sandbox
lib/api-spec/              # OpenAPI spec and codegen config
lib/api-client-react/      # Generated API client hooks
lib/api-zod/               # Generated Zod API schemas
lib/db/                    # Shared database package
scripts/                   # Workspace scripts
```

## Getting Started

### Prerequisites

- JDK 17
- Android SDK with platform 35 and build tools installed
- Android Studio or a configured Android SDK path
- pnpm, if working with the TypeScript workspace

### Android App

```bash
cd civiclens-android
./gradlew assembleDebug
```

If building on Windows, use:

```powershell
cd civiclens-android
.\gradlew.bat assembleDebug
```

Make sure `civiclens-android/local.properties` points to your local Android SDK:

```properties
sdk.dir=C\:\\Users\\your-name\\AppData\\Local\\Android\\Sdk
```

### TypeScript Workspace

```bash
pnpm install
pnpm run typecheck
pnpm run build
```

Useful workspace commands:

```bash
pnpm --filter @workspace/api-server run dev
pnpm --filter @workspace/api-spec run codegen
pnpm --filter @workspace/db run push
```

## Local Data

The Android app seeds demo data on first launch when the issue table is empty. This includes a Kolkata user profile, nearby civic issues, recent activity, and a sample resolution. Subsequent launches reuse the local Room database instead of duplicating records.

## Current Release

Latest release: [CivicLens UI Redesign v0.2.0](https://github.com/ayushi-stacks/CivicLens/releases/tag/v0.2.0)

This release focuses on the mobile UI redesign: a new visual system, dark mode, sculpted navigation, guided reporting, map-first exploration, and redesigned Home/Profile/Activity experiences.

## Notes

- The app is local-first in this phase. Cloud sync and authentication are intentionally not required for the core mobile demo.
- Android builds should use JDK 17. Newer Java runtimes may fail with this Gradle/Kotlin configuration.
- The Compose UI uses actual local data and ViewModel actions rather than static screen mockups.
