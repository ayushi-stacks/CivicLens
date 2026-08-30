package com.civiclens.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
    val systemDarkMode = isSystemInDarkTheme()
    var darkMode by rememberSaveable { mutableStateOf(systemDarkMode) }
    var showBrandLaunch by remember { mutableStateOf(true) }
    CivicColors.useDarkMode = darkMode
    val colorScheme = if (darkMode) {
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
            if (showBrandLaunch) {
                BrandedLaunchScreen { showBrandLaunch = false }
            } else {
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

    val Navy get() = if (useDarkMode) Color(0xFF061010) else Color(0xFFFBF8EF)
    val NavyRaised get() = if (useDarkMode) Color(0xFF0D1A19) else Color(0xFFFFFCF5)
    val Panel get() = if (useDarkMode) Color(0xFF10201E) else Color(0xFFFFFDF8)
    val PanelSoft get() = if (useDarkMode) Color(0xFF172B28) else Color(0xFFF1F2E8)
    val PanelMuted get() = if (useDarkMode) Color(0xFF223A35) else Color(0xFFE2EADD)
    val NavPill get() = if (useDarkMode) Color(0xFF0E2422) else Color(0xFFDDEBE5)
    val NavIcon get() = if (useDarkMode) Color(0xFF718986) else Color(0xFF70807B)
    val Cyan get() = if (useDarkMode) Color(0xFF1CE5D0) else Color(0xFF128E78)
    val CyanBright get() = if (useDarkMode) Color(0xFF6AFBE8) else Color(0xFF0A9E87)
    val CyanDeep get() = if (useDarkMode) Color(0xFF123F3B) else Color(0xFFD6EFE5)
    val Text get() = if (useDarkMode) Color(0xFFF7FFFB) else Color(0xFF10231F)
    val Muted get() = if (useDarkMode) Color(0xFFA9BDB7) else Color(0xFF66766F)
    val MutedDark get() = if (useDarkMode) Color(0xFF6C817B) else Color(0xFF9AA5A0)
    val Border get() = if (useDarkMode) Color(0xFF253D39) else Color(0xFFE2E7DF)
    val Lime get() = if (useDarkMode) Color(0xFF83E6A6) else Color(0xFF4C9B67)
    val Green get() = if (useDarkMode) Color(0xFF54DA92) else Color(0xFF2B8A61)
    val Amber get() = if (useDarkMode) Color(0xFFFFBF58) else Color(0xFFD08A24)
    val Coral get() = if (useDarkMode) Color(0xFFFF7D72) else Color(0xFFD95A4E)
    val Purple get() = if (useDarkMode) Color(0xFFAFA0FF) else Color(0xFF766BB4)
}
