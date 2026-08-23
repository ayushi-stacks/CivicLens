package com.civiclens.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Switch
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun OnboardingScreen(navController: NavHostController) {
    data class OnboardingSlide(
        val eyebrow: String,
        val title: String,
        val message: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val tint: Color,
    )
    val slides = listOf(
        OnboardingSlide("Report", "Capture civic issues fast", "Use AI-assisted reports with location, category, and confidence in one clean flow.", Icons.Default.CameraAlt, CivicColors.Coral),
        OnboardingSlide("Verify", "Crowd-check what matters", "Neighbors can confirm issues, prevent duplicates, and raise confidence before action.", Icons.Default.CheckCircle, CivicColors.Green),
        OnboardingSlide("Track", "See city progress live", "Follow repairs, rewards, and local impact from the home, map, and activity tabs.", Icons.Default.Wifi, CivicColors.Cyan),
    )
    var page by remember { mutableStateOf(0) }
    val slide = slides[page]
    CivicBackground {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 26.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(32.dp))
            CivicLensLogo()
            Spacer(Modifier.weight(1f))
            CivicIllustration(slide.icon, slide.tint)
            Spacer(Modifier.height(24.dp))
            StatusChip(slide.eyebrow, slide.tint)
            Spacer(Modifier.height(12.dp))
            Text(slide.title, color = CivicColors.Text, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, lineHeight = 28.sp)
            Spacer(Modifier.height(10.dp))
            Text(slide.message, color = CivicColors.Muted, fontSize = 13.sp, textAlign = TextAlign.Center, lineHeight = 20.sp)
            Spacer(Modifier.height(28.dp))
            PrimaryButton(if (page == slides.lastIndex) "Get Started" else "Next", Modifier.fillMaxWidth()) {
                if (page == slides.lastIndex) navController.navigate(Routes.Home) else page += 1
            }
            Spacer(Modifier.height(10.dp))
            SecondaryButton(if (page == slides.lastIndex) "Explore Demo" else "Skip intro", Modifier.fillMaxWidth()) {
                navController.navigate(Routes.Home)
            }
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                slides.indices.forEach { index ->
                    Box(
                        Modifier
                            .size(if (index == page) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (index == page) CivicColors.Cyan else CivicColors.MutedDark)
                            .clickable { page = index },
                    )
                }
            }
            Spacer(Modifier.height(18.dp))
        }
    }
}

@Composable
private fun CivicIllustration(centerIcon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color) {
    Box(Modifier.fillMaxWidth().height(190.dp)) {
        Box(Modifier.align(Alignment.Center).size(180.dp).clip(CircleShape).background(CivicColors.Panel.copy(alpha = .7f)))
        Icon(centerIcon, null, Modifier.align(Alignment.Center).size(105.dp), tint = tint.copy(alpha = .8f))
        Icon(Icons.Default.Groups, null, Modifier.align(Alignment.TopStart).padding(start = 48.dp, top = 30.dp).size(30.dp), tint = CivicColors.Purple)
        Icon(Icons.Default.Place, null, Modifier.align(Alignment.TopCenter).size(30.dp), tint = CivicColors.Lime)
        Icon(Icons.Default.Lightbulb, null, Modifier.align(Alignment.BottomStart).padding(start = 45.dp).size(28.dp), tint = CivicColors.Amber)
        Icon(Icons.Default.SmartToy, null, Modifier.align(Alignment.BottomEnd).padding(end = 45.dp).size(28.dp), tint = CivicColors.CyanBright)
    }
}

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = viewModel()) {
    val user by viewModel.uiState.collectAsState()
    val issues by viewModel.issues.collectAsState()
    val activities by viewModel.activities.collectAsState()
    var showNotifications by remember { mutableStateOf(false) }
    if (showNotifications) {
        AlertDialog(
            onDismissRequest = { showNotifications = false },
            containerColor = CivicColors.Panel,
            title = { Text("Notifications", color = CivicColors.Text, fontWeight = FontWeight.SemiBold) },
            text = {
                Column {
                    Text("3 nearby issues need verification.", color = CivicColors.Muted)
                    Spacer(Modifier.height(8.dp))
                    Text("Streetlight repair was confirmed yesterday.", color = CivicColors.Muted)
                }
            },
            confirmButton = { TextButton(onClick = { showNotifications = false }) { Text("Done", color = CivicColors.Cyan) } },
        )
    }
    ScreenScaffold(navController, Routes.Home) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp)) {
            Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("CivicLens", color = CivicColors.CyanBright, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    Text("Hello, ${user.user?.name?.substringBefore(" ") ?: "Ayushi"}", color = CivicColors.Text, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Text("Let's make our city better together.", color = CivicColors.Muted, fontSize = 11.sp)
                }
                ThemeToggleButton()
                IconButton(onClick = { showNotifications = true }) { Icon(Icons.Default.NotificationsNone, "Notifications", tint = CivicColors.Text) }
                Avatar(Modifier.size(34.dp))
            }
            Spacer(Modifier.height(18.dp))
            HealthCard(issues)
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatTile(issues.size.toString(), "Issues Near You", CivicColors.Coral, Modifier.weight(1f))
                StatTile(issues.count { it.status == IssueStatus.PENDING_VERIFICATION }.toString(), "Pending Verifications", CivicColors.CyanBright, Modifier.weight(1f))
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatTile((user.user?.civicPoints ?: 0).toString(), "Civic Points", CivicColors.Amber, Modifier.weight(1f))
                StatTile((user.user?.issuesResolved ?: 0).toString(), "Issues Resolved", CivicColors.Lime, Modifier.weight(1f))
            }
            Spacer(Modifier.height(22.dp))
            SectionHeader("Recent Activity", "View All") { navController.navigate(Routes.Activity) }
            Spacer(Modifier.height(10.dp))
            activities.take(2).forEach { activity ->
                IssueRow(activity.toCivicIssue()) { navController.navigate("${Routes.IssueDetails}/${activity.issueId ?: 1}") }
                Spacer(Modifier.height(9.dp))
            }
            Spacer(Modifier.height(14.dp))
            SectionHeader("Nearby Issues", "Open map") { navController.navigate(Routes.Map) }
            Spacer(Modifier.height(10.dp))
            issues.take(2).forEach { issue ->
                IssueRow(issue.toCivicIssue()) { navController.navigate("${Routes.IssueDetails}/${issue.id}") }
                Spacer(Modifier.height(9.dp))
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun HealthCard(issues: List<IssueEntity>) {
    val score = if (issues.isEmpty()) 72 else issues.map { issue ->
        when (issue.status) {
            IssueStatus.PENDING_VERIFICATION -> 60
            IssueStatus.VERIFIED -> 75
            IssueStatus.IN_PROGRESS -> 88
            IssueStatus.RESOLVED -> 100
        }
    }.average().roundToInt()
    val healthLabel = when {
        score >= 85 -> "Excellent"
        score >= 65 -> "Good"
        else -> "Needs attention"
    }
    GlassCard(
        Modifier
            .fillMaxWidth()
            .background(CivicColors.Lime.copy(alpha = .12f), RoundedCornerShape(18.dp)),
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            Column {
                Text("CITY HEALTH INDEX", color = CivicColors.Text, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                Spacer(Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                Text(score.toString(), color = CivicColors.Text, fontSize = 38.sp, fontWeight = FontWeight.Bold)
                Text(" / 100", color = CivicColors.Muted, fontSize = 12.sp, modifier = Modifier.padding(bottom = 7.dp))
                }
                Text(healthLabel, color = CivicColors.Cyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
            CanvasSparkline()
        }
    }
}

@Composable
private fun CanvasSparkline() {
    androidx.compose.foundation.Canvas(Modifier.size(80.dp, 44.dp).padding(top = 10.dp)) {
        val points = listOf(0f to .8f, .2f to .55f, .4f to .7f, .62f to .22f, .82f to .45f, 1f to .1f)
        points.zipWithNext().forEach { (a, b) ->
            drawLine(CivicColors.Green, androidx.compose.ui.geometry.Offset(size.width * a.first, size.height * a.second), androidx.compose.ui.geometry.Offset(size.width * b.first, size.height * b.second), 3f, androidx.compose.ui.graphics.StrokeCap.Round)
        }
    }
}

@Composable
fun ReportIssueScreen(navController: NavHostController, viewModel: ReportViewModel = viewModel()) {
    var possibleDuplicateId by remember { mutableStateOf<Long?>(null) }
    var scanIndex by remember { mutableStateOf(0) }
    val scanOptions = listOf(
        Triple("Pothole", "Confidence: 95%", "Ballygunge Circular Rd, Kolkata"),
        Triple("Overflowing waste", "Confidence: 88%", "Hazra Road, Kolkata"),
        Triple("Streetlight outage", "Confidence: 82%", "Lansdowne Road, Kolkata"),
    )
    val scan = scanOptions[scanIndex]
    if (possibleDuplicateId != null) {
        AlertDialog(
            onDismissRequest = { possibleDuplicateId = null },
            containerColor = CivicColors.Panel,
            title = { Text("Possible existing issue nearby", color = CivicColors.Text, fontWeight = FontWeight.SemiBold) },
            text = { Text("A similar pothole was already reported within 50 meters.", color = CivicColors.Muted) },
            confirmButton = {
                TextButton(onClick = {
                    val id = possibleDuplicateId ?: return@TextButton
                    possibleDuplicateId = null
                    navController.navigate("${Routes.IssueDetails}/$id")
                }) { Text("View Existing Issue", color = CivicColors.Cyan) }
            },
            dismissButton = {
                TextButton(onClick = {
                    possibleDuplicateId = null
                    viewModel.submitAnyway { id -> navController.navigate("${Routes.IssueDetails}/$id") }
                }) { Text("Submit Anyway", color = CivicColors.Text) }
            },
        )
    }
    ScreenScaffold(navController, Routes.Report, "Report New Issue", true, { IconButton(onClick = { scanIndex = (scanIndex + 1) % scanOptions.size }) { Icon(Icons.Default.Bolt, "AI detection", tint = CivicColors.Cyan) } }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp)) {
            FakePhotoCard(Modifier.fillMaxWidth().height(255.dp), label = "DEMO PREVIEW - AI READY")
            Spacer(Modifier.height(13.dp))
            GlassCard(Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(34.dp).clip(RoundedCornerShape(10.dp)).background(CivicColors.Cyan.copy(alpha = .2f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.SmartToy, null, tint = CivicColors.Cyan, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(11.dp))
                    Column(Modifier.weight(1f)) {
                        Text("AI Detection Result", color = CivicColors.Muted, fontSize = 10.sp)
                        Text(scan.first, color = CivicColors.Text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        Text(scan.second, color = CivicColors.Muted, fontSize = 10.sp)
                    }
                    Icon(Icons.Default.CheckCircle, null, tint = CivicColors.Green)
                }
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, null, tint = CivicColors.Text, modifier = Modifier.size(19.dp))
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("Location", color = CivicColors.Muted, fontSize = 10.sp)
                        Text(scan.third, color = CivicColors.Text, fontSize = 12.sp)
                    }
                }
            }
            Spacer(Modifier.height(18.dp))
            PrimaryButton("Confirm & Submit", Modifier.fillMaxWidth()) {
                viewModel.submit(
                    onPossibleDuplicate = { possibleDuplicateId = it },
                    onSubmitted = { id -> navController.navigate("${Routes.IssueDetails}/$id") },
                )
            }
            Spacer(Modifier.height(10.dp))
            SecondaryButton("Retake Photo", Modifier.fillMaxWidth()) { scanIndex = (scanIndex + 1) % scanOptions.size }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun IssueDetailsScreen(navController: NavHostController, issueId: Long, viewModel: IssueDetailsViewModel = viewModel(factory = ViewModelFactory { IssueDetailsViewModel(issueId) })) {
    val issue by viewModel.issue.collectAsState()
    var showShare by remember { mutableStateOf(false) }
    if (showShare) {
        AlertDialog(
            onDismissRequest = { showShare = false },
            containerColor = CivicColors.Panel,
            title = { Text("Share issue", color = CivicColors.Text, fontWeight = FontWeight.SemiBold) },
            text = { Text("CivicLens issue #CL-$issueId is ready to share with neighbors or municipal teams.", color = CivicColors.Muted) },
            confirmButton = { TextButton(onClick = { showShare = false }) { Text("Copy Link", color = CivicColors.Cyan) } },
            dismissButton = { TextButton(onClick = { showShare = false }) { Text("Close", color = CivicColors.Text) } },
        )
    }
    ScreenScaffold(navController, Routes.IssueDetails, "Issue Details", true, { IconButton(onClick = { showShare = true }) { Icon(Icons.Default.Share, "Share", tint = CivicColors.Text) } }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                StatusChip(issue?.status?.label() ?: "Pending", issue?.status?.color() ?: CivicColors.Amber)
                Spacer(Modifier.width(9.dp))
                Text("#CL-${issue?.id ?: issueId}", color = CivicColors.MutedDark, fontSize = 10.sp)
            }
            Spacer(Modifier.height(12.dp))
            Text("Reported on", color = CivicColors.Muted, fontSize = 10.sp)
            Text(issue?.createdAt?.let(::formatCivicDate) ?: "Loading issue…", color = CivicColors.Text, fontSize = 12.sp)
            Spacer(Modifier.height(15.dp))
            Text("Location", color = CivicColors.Muted, fontSize = 10.sp)
            Text(issue?.address ?: "Ballygunge Circular Rd, Kolkata", color = CivicColors.Text, fontSize = 12.sp)
            Spacer(Modifier.height(10.dp))
            MapPreview(Modifier.fillMaxWidth().height(145.dp))
            Spacer(Modifier.height(17.dp))
            Text("Description", color = CivicColors.Muted, fontSize = 10.sp)
            Text(issue?.description ?: "Large pothole causing water logging and traffic issues.", color = CivicColors.Text, fontSize = 12.sp, lineHeight = 18.sp)
            Spacer(Modifier.height(22.dp))
            SectionHeader("Verification Progress")
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Confidence Score", color = CivicColors.Muted, fontSize = 10.sp)
                 Text("${issue?.resolutionConfidence ?: 76}%", color = CivicColors.Text, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(7.dp))
             ProgressBar((issue?.resolutionConfidence ?: 76) / 100f, Modifier.fillMaxWidth())
             Text("${issue?.verificationCount ?: 12} citizens verified this issue", color = CivicColors.MutedDark, fontSize = 10.sp, modifier = Modifier.padding(top = 7.dp))
            Spacer(Modifier.height(22.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SecondaryButton("Share", Modifier.weight(1f)) { showShare = true }
                 PrimaryButton("Verify", Modifier.weight(1f)) { navController.navigate("${Routes.Verification}/$issueId") }
            }
            Spacer(Modifier.height(22.dp))
        }
    }
}

@Composable
fun MapViewScreen(navController: NavHostController, viewModel: MapViewModel = viewModel()) {
    val issues by viewModel.issues.collectAsState()
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf("All") }
    var showFilters by remember { mutableStateOf(false) }
    val visibleIssues = issues.filter { issue ->
        val matchesQuery = query.isBlank() || issue.address.contains(query, ignoreCase = true) || issue.category.name.contains(query, ignoreCase = true)
        val matchesFilter = filter == "All" || issue.status.label() == filter
        matchesQuery && matchesFilter
    }
    if (showFilters) {
        AlertDialog(
            onDismissRequest = { showFilters = false },
            containerColor = CivicColors.Panel,
            title = { Text("Map filters", color = CivicColors.Text, fontWeight = FontWeight.SemiBold) },
            text = {
                Column {
                    listOf("All", "Pending", "Verified", "In progress", "Resolved").forEach { option ->
                        VerificationChoice(option, CivicColors.Cyan, filter == option) { filter = option }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showFilters = false }) { Text("Apply", color = CivicColors.Cyan) } },
            dismissButton = { TextButton(onClick = { filter = "All"; showFilters = false }) { Text("Reset", color = CivicColors.Text) } },
        )
    }
    ScreenScaffold(navController, Routes.Map) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(horizontal = 14.dp)) {
            Row(Modifier.fillMaxWidth().padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("CivicLens", color = CivicColors.CyanBright, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                ThemeToggleButton()
                IconButton(onClick = { showFilters = true }) { Icon(Icons.Default.FilterList, "Filter", tint = CivicColors.Text) }
            }
            BasicTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(CivicColors.Panel).border(1.dp, CivicColors.Border, RoundedCornerShape(12.dp)).padding(13.dp),
                textStyle = androidx.compose.ui.text.TextStyle(color = CivicColors.Muted, fontSize = 12.sp),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Search, null, tint = CivicColors.MutedDark, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Box(Modifier.weight(1f)) {
                            if (query.isBlank()) Text("Search location or issue type", color = CivicColors.MutedDark, fontSize = 12.sp)
                            innerTextField()
                        }
                    }
                },
            )
            Spacer(Modifier.height(12.dp))
            MapPreview(Modifier.fillMaxWidth().height(270.dp))
            Spacer(Modifier.height(18.dp))
            SectionHeader("Nearby Issues", filter) { showFilters = true }
            Spacer(Modifier.height(10.dp))
             visibleIssues.forEach { issue ->
                 IssueRow(issue.toCivicIssue()) { navController.navigate("${Routes.IssueDetails}/${issue.id}") }
                Spacer(Modifier.height(9.dp))
            }
             if (visibleIssues.isEmpty()) {
                 CivicEmptyState(
                     title = "No nearby issues yet",
                     message = "Try a different search or report an infrastructure problem to start improving your neighborhood.",
                     actionLabel = "Report an issue",
                     onAction = { navController.navigate(Routes.Report) },
                 )
             }
        }
    }
}

@Composable
fun VerificationScreen(navController: NavHostController, issueId: Long, viewModel: VerificationViewModel = viewModel(factory = ViewModelFactory { VerificationViewModel(issueId) })) {
    var selected by remember { mutableStateOf("Yes, it's there") }
    var comment by remember { mutableStateOf("") }
    var duplicateVerification by remember { mutableStateOf(false) }
    if (duplicateVerification) {
        AlertDialog(
            onDismissRequest = { duplicateVerification = false },
            containerColor = CivicColors.Panel,
            title = { Text("Verification already recorded", color = CivicColors.Text, fontWeight = FontWeight.SemiBold) },
            text = { Text("You have already verified this issue. Its confidence score is already using your response.", color = CivicColors.Muted) },
            confirmButton = { TextButton(onClick = { duplicateVerification = false }) { Text("Done", color = CivicColors.Cyan) } },
        )
    }
    ScreenScaffold(navController, Routes.Verification, "Verify Issue", true) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp)) {
            Text("Does this issue still exist?", color = CivicColors.Text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Text("Pothole at Ballygunge Circular Rd", color = CivicColors.Muted, fontSize = 10.sp, modifier = Modifier.padding(top = 5.dp))
            Spacer(Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FakePhotoCard(Modifier.weight(1f).height(158.dp), "Before")
                FakePhotoCard(Modifier.weight(1f).height(158.dp), "Now")
            }
            Spacer(Modifier.height(18.dp))
            Text("Is the issue still visible?", color = CivicColors.Muted, fontSize = 11.sp)
            Spacer(Modifier.height(9.dp))
            VerificationChoice("Yes, it's there", CivicColors.Coral, selected == "Yes, it's there") { selected = "Yes, it's there" }
            Spacer(Modifier.height(8.dp))
            VerificationChoice("No, it's resolved", CivicColors.Green, selected == "No, it's resolved") { selected = "No, it's resolved" }
            Spacer(Modifier.height(8.dp))
            VerificationChoice("Not Sure", CivicColors.Muted, selected == "Not Sure") { selected = "Not Sure" }
            Spacer(Modifier.height(20.dp))
            Text("Add a comment (optional)", color = CivicColors.Muted, fontSize = 11.sp)
            Spacer(Modifier.height(7.dp))
            TextField(
                value = comment,
                onValueChange = { comment = it },
                placeholder = { Text("Write your comment...", color = CivicColors.MutedDark, fontSize = 12.sp) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                colors = TextFieldDefaults.colors(focusedContainerColor = CivicColors.Panel, unfocusedContainerColor = CivicColors.Panel, focusedIndicatorColor = CivicColors.Cyan, unfocusedIndicatorColor = CivicColors.Border),
            )
            Spacer(Modifier.height(18.dp))
            PrimaryButton("Submit Verification", Modifier.fillMaxWidth()) {
                val result = when (selected) {
                    "Yes, it's there" -> VerificationResult.STILL_EXISTS
                    "No, it's resolved" -> VerificationResult.RESOLVED
                    else -> VerificationResult.NOT_SURE
                }
                viewModel.submit(
                    result = result,
                    comment = comment,
                    onDuplicate = { duplicateVerification = true },
                    onSubmitted = { navController.navigate("${Routes.Resolution}/$issueId") },
                )
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun VerificationChoice(label: String, color: Color, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(if (selected) color.copy(alpha = .18f) else CivicColors.Panel).border(1.dp, if (selected) color else CivicColors.Border, RoundedCornerShape(10.dp)).clickable(onClick = onClick).padding(13.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(15.dp).clip(CircleShape).background(if (selected) color else Color.Transparent).border(1.dp, if (selected) color else CivicColors.MutedDark, CircleShape))
        Spacer(Modifier.width(10.dp))
        Text(label, color = if (selected) CivicColors.Text else CivicColors.Muted, fontSize = 12.sp, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
    }
}

@Composable
fun ResolutionDetailsScreen(navController: NavHostController, issueId: Long, viewModel: ResolutionViewModel = viewModel(factory = ViewModelFactory { ResolutionViewModel(issueId) })) {
    val issue by viewModel.issue.collectAsState()
    val resolution by viewModel.resolution.collectAsState()
    var showShare by remember { mutableStateOf(false) }
    if (showShare) {
        AlertDialog(
            onDismissRequest = { showShare = false },
            containerColor = CivicColors.Panel,
            title = { Text("Share resolution", color = CivicColors.Text, fontWeight = FontWeight.SemiBold) },
            text = { Text("Resolution proof for issue #CL-$issueId is ready to share with the community.", color = CivicColors.Muted) },
            confirmButton = { TextButton(onClick = { showShare = false }) { Text("Copy Link", color = CivicColors.Cyan) } },
            dismissButton = { TextButton(onClick = { showShare = false }) { Text("Close", color = CivicColors.Text) } },
        )
    }
    ScreenScaffold(navController, Routes.Resolution, "Resolution Details", true, { IconButton(onClick = { showShare = true }) { Icon(Icons.Default.Share, "Share", tint = CivicColors.Text) } }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp)) {
            StatusChip(issue?.status?.label() ?: "Resolved", issue?.status?.color() ?: CivicColors.Green)
            Spacer(Modifier.height(11.dp))
            Text(issue?.category?.name?.replace('_', ' ') ?: "Pothole", color = CivicColors.Text, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            Text(issue?.address ?: "Ballygunge Circular Rd", color = CivicColors.Muted, fontSize = 11.sp)
            Text(resolution?.resolvedAt?.let(::formatCivicDate)?.let { "Resolved on $it" } ?: "Resolution pending", color = CivicColors.MutedDark, fontSize = 10.sp)
            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FakePhotoCard(Modifier.weight(1f).height(205.dp), "Before")
                FakePhotoCard(Modifier.weight(1f).height(205.dp), "After")
            }
            Spacer(Modifier.height(18.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Verified by Citizens", color = CivicColors.Muted, fontSize = 10.sp)
                    Row(Modifier.padding(top = 7.dp)) {
                        repeat(4) { Avatar(Modifier.size(25.dp), if (it == 0) "AM" else "C${it}") ; Spacer(Modifier.width(3.dp)) }
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("${resolution?.confidence ?: (100 - (issue?.resolutionConfidence ?: 8))}%", color = CivicColors.Text, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                    Text("Confidence Score", color = CivicColors.Muted, fontSize = 10.sp)
                }
            }
            Spacer(Modifier.height(10.dp))
            ProgressBar((resolution?.confidence ?: (100 - (issue?.resolutionConfidence ?: 8))) / 100f, Modifier.fillMaxWidth())
            Spacer(Modifier.height(20.dp))
            Text("Municipal Remark", color = CivicColors.Muted, fontSize = 10.sp)
            Text(resolution?.municipalRemark ?: "Resolution details will appear after a repair is confirmed.", color = CivicColors.Text, fontSize = 12.sp, lineHeight = 18.sp, modifier = Modifier.padding(top = 5.dp))
            Spacer(Modifier.height(20.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.WorkspacePremium, null, tint = CivicColors.Cyan, modifier = Modifier.size(28.dp))
                Spacer(Modifier.width(9.dp))
                Column {
                    Text("Resolved By", color = CivicColors.Muted, fontSize = 10.sp)
                    Text(resolution?.resolvedBy ?: "CivicLens community", color = CivicColors.Text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.height(24.dp))
            PrimaryButton("Back to Home", Modifier.fillMaxWidth()) { navController.navigate(Routes.Home) { popUpTo(Routes.Home) } }
            Spacer(Modifier.height(18.dp))
        }
    }
}

@Composable
fun ProfileRewardsScreen(navController: NavHostController, viewModel: ProfileViewModel = viewModel()) {
    val user by viewModel.user.collectAsState()
    var showSettings by remember { mutableStateOf(false) }
    val theme = LocalCivicThemeActions.current
    if (showSettings) {
        AlertDialog(
            onDismissRequest = { showSettings = false },
            containerColor = CivicColors.Panel,
            title = { Text("Settings", color = CivicColors.Text, fontWeight = FontWeight.SemiBold) },
            text = {
                Column {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text("Dark mode", color = CivicColors.Text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Text("Use the deeper CivicLens interface.", color = CivicColors.Muted, fontSize = 10.sp)
                        }
                        Switch(checked = theme.isDarkMode, onCheckedChange = { theme.toggleDarkMode() })
                    }
                    Spacer(Modifier.height(14.dp))
                    Text("Notifications, rewards, and city alerts are enabled for this demo profile.", color = CivicColors.Muted, fontSize = 11.sp, lineHeight = 16.sp)
                }
            },
            confirmButton = { TextButton(onClick = { showSettings = false }) { Text("Done", color = CivicColors.Cyan) } },
        )
    }
    ScreenScaffold(navController, Routes.Profile) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp)) {
            Row(Modifier.fillMaxWidth().padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("My Profile", color = CivicColors.Text, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                ThemeToggleButton()
                IconButton(onClick = { showSettings = true }) { Icon(Icons.Default.Settings, "Settings", tint = CivicColors.Text) }
            }
            Row(Modifier.padding(top = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                Avatar(Modifier.size(58.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(user?.name ?: "Ayushi Mandal", color = CivicColors.Text, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                    Text(user?.location ?: "Kolkata, India", color = CivicColors.Muted, fontSize = 11.sp)
                    Text("Level ${user?.level ?: 4} · Civic Contributor", color = CivicColors.CyanBright, fontSize = 10.sp)
                }
            }
            Spacer(Modifier.height(18.dp))
            PointsCard(user)
            Spacer(Modifier.height(18.dp))
            SectionHeader("Your Impact")
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                 StatTile((user?.issuesReported ?: 24).toString(), "Issues Reported", CivicColors.Coral, Modifier.weight(1f))
                 StatTile((user?.issuesVerified ?: 18).toString(), "Verified", CivicColors.CyanBright, Modifier.weight(1f))
                 StatTile((user?.issuesResolved ?: 8).toString(), "Resolved", CivicColors.Lime, Modifier.weight(1f))
            }
            Spacer(Modifier.height(20.dp))
            SectionHeader("Badges", "View All") {}
            Spacer(Modifier.height(11.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                val badges: List<Pair<androidx.compose.ui.graphics.vector.ImageVector, Color>> = listOf(
                    Icons.Default.Shield to CivicColors.Cyan,
                    Icons.Default.EmojiEvents to CivicColors.Amber,
                    Icons.Default.WorkspacePremium to CivicColors.Purple,
                    Icons.Default.CheckCircle to CivicColors.Green,
                )
                badges.forEach { (icon, tint) ->
                    Box(Modifier.size(53.dp).clip(CircleShape).background(tint.copy(alpha = .13f)).border(1.dp, tint.copy(alpha = .7f), CircleShape), contentAlignment = Alignment.Center) { Icon(icon, null, tint = tint, modifier = Modifier.size(28.dp)) }
                }
            }
            Spacer(Modifier.height(22.dp))
            LeaderboardCard()
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
private fun PointsCard(user: UserEntity?) {
    GlassCard(Modifier.fillMaxWidth().background(CivicColors.Purple.copy(alpha = .22f), RoundedCornerShape(18.dp))) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Civic Points", color = CivicColors.Muted, fontSize = 11.sp)
                Text((user?.civicPoints ?: 0).toString(), color = CivicColors.Text, fontSize = 29.sp, fontWeight = FontWeight.Bold)
                Text("Level ${user?.level ?: 1} · Keep improving Kolkata", color = CivicColors.CyanBright, fontSize = 10.sp)
            }
            Icon(Icons.Default.EmojiEvents, null, tint = CivicColors.Amber, modifier = Modifier.size(58.dp))
        }
    }
}

@Composable
private fun LeaderboardCard() {
    GlassCard(Modifier.fillMaxWidth()) {
        SectionHeader("Leaderboard", "Top Contributors This Month") {}
        Spacer(Modifier.height(13.dp))
        listOf("Rahul" to "2450", "Priya" to "1980", "Ayushi (You)" to "1200").forEachIndexed { index, item ->
            Row(Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("${index + 1}", color = CivicColors.MutedDark, fontSize = 12.sp, modifier = Modifier.width(20.dp))
                Avatar(Modifier.size(26.dp), item.first.take(2).uppercase())
                Spacer(Modifier.width(9.dp))
                Text(item.first, color = CivicColors.Text, fontSize = 12.sp, modifier = Modifier.weight(1f))
                Text(item.second, color = if (index == 2) CivicColors.CyanBright else CivicColors.Muted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ActivityScreen(navController: NavHostController, viewModel: ActivityViewModel = viewModel()) {
    val activities by viewModel.activities.collectAsState()
    val weekStart = System.currentTimeMillis() - 7L * 24L * 60L * 60L * 1000L
    val weekActivities = activities.filter { it.timestamp >= weekStart }
    val weeklyPoints = weekActivities.sumOf { it.points }
    val weeklyPlaces = weekActivities.mapNotNull { it.issueId }.distinct().size
    ScreenScaffold(navController, Routes.Activity, "Activity") { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 18.dp)) {
            Text("Your civic journey", color = CivicColors.Muted, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
            Spacer(Modifier.height(16.dp))
            GlassCard(Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(38.dp).clip(RoundedCornerShape(12.dp)).background(CivicColors.Cyan.copy(alpha = .15f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Wifi, null, tint = CivicColors.Cyan)
                    }
                    Spacer(Modifier.width(11.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Weekly civic impact", color = CivicColors.Text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Text("You helped improve $weeklyPlaces places this week.", color = CivicColors.Muted, fontSize = 10.sp)
                    }
                    Text("+$weeklyPoints", color = CivicColors.Lime, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(21.dp))
            SectionHeader("Recent Activity")
            Spacer(Modifier.height(10.dp))
             activities.take(3).forEach { activity ->
                 IssueRow(activity.toCivicIssue()) { navController.navigate("${Routes.IssueDetails}/${activity.issueId ?: 1}") }
                Spacer(Modifier.height(9.dp))
            }
            Spacer(Modifier.height(12.dp))
            SectionHeader("Earlier")
            Spacer(Modifier.height(10.dp))
             activities.drop(3).forEach { activity ->
                GlassCard(Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null, tint = CivicColors.Green, modifier = Modifier.size(22.dp))
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                             Text(activity.title, color = CivicColors.Text, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                             Text("${activity.description} · ${formatCivicDate(activity.timestamp)}", color = CivicColors.Muted, fontSize = 10.sp)
                        }
                         Text("+${activity.points}", color = CivicColors.Lime, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(9.dp))
            }
             if (activities.isEmpty()) {
                 CivicEmptyState("Your civic journey starts here", "Report or verify an issue to see your impact appear in this timeline.")
             }
            Spacer(Modifier.height(20.dp))
        }
    }
}

private fun formatCivicDate(timestamp: Long): String =
    SimpleDateFormat("dd MMM yyyy, h:mm a", Locale.getDefault()).format(Date(timestamp))
