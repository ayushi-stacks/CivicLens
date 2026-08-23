package com.civiclens.app

enum class IssueCategory { POTHOLE, STREETLIGHT, WASTE, WATER_LEAK, ROAD_OBSTRUCTION, FOOTPATH, SIGNAGE, OTHER }
enum class IssueStatus { PENDING_VERIFICATION, VERIFIED, IN_PROGRESS, RESOLVED }
enum class VerificationResult { STILL_EXISTS, RESOLVED, NOT_SURE }
enum class ActivityType { REPORT, VERIFICATION, RESOLUTION, REWARD }

data class CivicIssue(
    val title: String,
    val location: String,
    val distance: String,
    val status: String,
    val statusColor: androidx.compose.ui.graphics.Color,
    val confidence: String,
    val id: Long = 0,
)

fun IssueStatus.label(): String = when (this) {
    IssueStatus.PENDING_VERIFICATION -> "Pending"
    IssueStatus.VERIFIED -> "Verified"
    IssueStatus.IN_PROGRESS -> "In progress"
    IssueStatus.RESOLVED -> "Resolved"
}

fun IssueStatus.color() = when (this) {
    IssueStatus.PENDING_VERIFICATION -> CivicColors.Amber
    IssueStatus.VERIFIED -> CivicColors.Green
    IssueStatus.IN_PROGRESS -> CivicColors.CyanBright
    IssueStatus.RESOLVED -> CivicColors.Lime
}

fun IssueEntity.toCivicIssue() = CivicIssue(
    id = id,
    title = category.name.replace('_', ' ').lowercase().replaceFirstChar { it.uppercase() },
    location = address,
    distance = "Nearby",
    status = status.label(),
    statusColor = status.color(),
    confidence = "$confidence%",
)

fun ActivityEntity.toCivicIssue() = CivicIssue(
    id = issueId ?: 0,
    title = title,
    location = description,
    distance = "Recent",
    status = when (type) {
        ActivityType.REPORT -> "Reported"
        ActivityType.VERIFICATION -> "Verified"
        ActivityType.RESOLUTION -> "Resolved"
        ActivityType.REWARD -> "Reward"
    },
    statusColor = when (type) {
        ActivityType.REPORT -> CivicColors.Coral
        ActivityType.VERIFICATION -> CivicColors.CyanBright
        ActivityType.RESOLUTION -> CivicColors.Lime
        ActivityType.REWARD -> CivicColors.Amber
    },
    confidence = "+$points",
)

val mockIssues = listOf(
    CivicIssue("Pothole", "Ballygunge Circular Rd, Kolkata", "120 m away", "Verified", CivicColors.Green, "95%"),
    CivicIssue("Overflowing Bin", "Hazra Road", "250 m away", "Pending", CivicColors.Amber, "88%"),
    CivicIssue("Streetlight Outage", "Lansdowne Road", "400 m away", "Verified", CivicColors.Green, "82%"),
)

val recentActivities = listOf(
    CivicIssue("Pothole reported", "Ballygunge Circular Rd", "2 min ago", "Verified", CivicColors.Green, "95%"),
    CivicIssue("Streetlight repaired", "Hindustan Park", "Yesterday", "Verified", CivicColors.Green, "91%"),
    CivicIssue("Overflowing bin", "Hazra Road", "2 days ago", "Pending", CivicColors.Amber, "88%"),
)