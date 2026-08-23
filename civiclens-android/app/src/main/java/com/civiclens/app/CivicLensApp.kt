package com.civiclens.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

data class CivicThemeActions(
    val isDarkMode: Boolean,
    val toggleDarkMode: () -> Unit,
)

val LocalCivicThemeActions = compositionLocalOf { CivicThemeActions(false) {} }

@Composable
fun CivicLensApp() {
    val navController = rememberNavController()
    var darkMode by rememberSaveable { mutableStateOf(false) }
    CivicColors.useDarkMode = darkMode
    val colorScheme = if (darkMode || isSystemInDarkTheme() && darkMode) {
        darkColorScheme(
            primary = CivicColors.Cyan,
            onPrimary = CivicColors.Text,
            primaryContainer = CivicColors.CyanDeep,
            onPrimaryContainer = CivicColors.Text,
            secondary = CivicColors.Lime,
            background = CivicColors.Navy,
            onBackground = CivicColors.Text,
            surface = CivicColors.Panel,
            onSurface = CivicColors.Text,
            surfaceVariant = CivicColors.PanelSoft,
            onSurfaceVariant = CivicColors.Muted,
            outline = CivicColors.Border,
            error = CivicColors.Coral,
        )
    } else {
        lightColorScheme(
            primary = CivicColors.Cyan,
            onPrimary = CivicColors.Navy,
            primaryContainer = CivicColors.CyanDeep,
            onPrimaryContainer = CivicColors.Text,
            secondary = CivicColors.Lime,
            background = CivicColors.Navy,
            onBackground = CivicColors.Text,
            surface = CivicColors.Panel,
            onSurface = CivicColors.Text,
            surfaceVariant = CivicColors.PanelSoft,
            onSurfaceVariant = CivicColors.Muted,
            outline = CivicColors.Border,
            error = CivicColors.Coral,
        )
    }
    MaterialTheme(
        colorScheme = colorScheme,
    ) {
        androidx.compose.runtime.CompositionLocalProvider(
            LocalCivicThemeActions provides CivicThemeActions(darkMode) { darkMode = !darkMode },
        ) {
            NavHost(
                navController = navController,
                startDestination = Routes.Onboarding,
            ) {
                composable(Routes.Onboarding) { OnboardingScreen(navController) }
                composable(Routes.Home) { HomeScreen(navController) }
                composable(Routes.Report) { ReportIssueScreen(navController) }
                composable("${Routes.IssueDetails}/{issueId}") { entry ->
                    IssueDetailsScreen(navController, entry.arguments?.getString("issueId")?.toLongOrNull() ?: 1)
                }
                composable(Routes.IssueDetails) { IssueDetailsScreen(navController, 1) }
                composable(Routes.Map) { MapViewScreen(navController) }
                composable("${Routes.Verification}/{issueId}") { entry ->
                    VerificationScreen(navController, entry.arguments?.getString("issueId")?.toLongOrNull() ?: 1)
                }
                composable(Routes.Verification) { VerificationScreen(navController, 1) }
                composable("${Routes.Resolution}/{issueId}") { entry ->
                    ResolutionDetailsScreen(navController, entry.arguments?.getString("issueId")?.toLongOrNull() ?: 1)
                }
                composable(Routes.Resolution) { ResolutionDetailsScreen(navController, 1) }
                composable(Routes.Profile) { ProfileRewardsScreen(navController) }
                composable(Routes.Activity) { ActivityScreen(navController) }
            }
        }
    }
}

object Routes {
    const val Onboarding = "onboarding"
    const val Home = "home"
    const val Report = "report"
    const val IssueDetails = "issue-details"
    const val Map = "map"
    const val Verification = "verification"
    const val Resolution = "resolution"
    const val Profile = "profile"
    const val Activity = "activity"
}

object CivicColors {
    var useDarkMode by mutableStateOf(false)

    val Navy get() = if (useDarkMode) Color(0xFF081210) else Color(0xFFF4F1E9)
    val NavyRaised get() = if (useDarkMode) Color(0xFF101C19) else Color(0xFFFFFFFF)
    val Panel get() = if (useDarkMode) Color(0xFF12211E) else Color(0xFFFFFFFF)
    val PanelSoft get() = if (useDarkMode) Color(0xFF1B302B) else Color(0xFFE6EEE8)
    val PanelMuted get() = if (useDarkMode) Color(0xFF253B35) else Color(0xFFD7E3DC)
    val Cyan get() = if (useDarkMode) Color(0xFF46D6C5) else Color(0xFF147B73)
    val CyanBright get() = if (useDarkMode) Color(0xFF70EFE0) else Color(0xFF0B9B90)
    val CyanDeep get() = if (useDarkMode) Color(0xFF164B45) else Color(0xFFB9E2D8)
    val Text get() = if (useDarkMode) Color(0xFFF2FFF9) else Color(0xFF17251F)
    val Muted get() = if (useDarkMode) Color(0xFFABC2B9) else Color(0xFF65746C)
    val MutedDark get() = if (useDarkMode) Color(0xFF748A82) else Color(0xFF8B9890)
    val Border get() = if (useDarkMode) Color(0xFF284139) else Color(0xFFD1DCD5)
    val Lime get() = if (useDarkMode) Color(0xFF8FE1A8) else Color(0xFF4D9A66)
    val Green get() = if (useDarkMode) Color(0xFF68D59A) else Color(0xFF2D8A61)
    val Amber get() = if (useDarkMode) Color(0xFFFFC36C) else Color(0xFFB97924)
    val Coral get() = if (useDarkMode) Color(0xFFFF8A7D) else Color(0xFFC95C51)
    val Purple get() = if (useDarkMode) Color(0xFFB6A8FF) else Color(0xFF7067A8)
}
