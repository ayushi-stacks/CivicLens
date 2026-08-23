# CivicLens Android — Phase 2A

Native Android foundation for CivicLens, built with Kotlin and Jetpack Compose.

Phase 2A adds local-first persistence beneath the existing Phase 1 Compose UI:

- Room database: `civiclens_database`
- MVVM ViewModels exposing StateFlow
- Repository and DAO layers for users, issues, verifications, resolutions, and activity
- First-launch seed data using the existing Kolkata demo locations
- Database-backed Home, Profile, Map, Activity, Issue Details, Report, and Verification flows

## Requirements

- Android Studio Ladybug or newer
- JDK 17 or 21 (Kotlin 2.0.21 / AGP 8.7.3 do not support JDK 25+). `gradle/gradle-daemon-jvm.properties` pins the Gradle daemon to a JDK 21 installed on the machine, so `JAVA_HOME` may point at a newer JDK.
- Android SDK Platform 35

## Build

From this directory:

```bash
./gradlew assembleDebug
```

The generated APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

The app intentionally does not include CameraX, ML Kit, GPS, Maps SDK, authentication, or cloud sync. Those remain future-phase boundaries.