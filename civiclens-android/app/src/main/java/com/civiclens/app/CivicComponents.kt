package com.civiclens.app

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun CivicBackground(content: @Composable () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = CivicColors.Navy) {
        content()
    }
}

@Composable
fun CivicLensLogo(compact: Boolean = false) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(if (compact) 30.dp else 66.dp)
                .clip(CircleShape)
                .background(CivicColors.Panel),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(if (compact) 22.dp else 46.dp)) {
                val center = Offset(size.width / 2, size.height / 2)
                drawCircle(
                    color = CivicColors.Cyan,
                    radius = size.minDimension * .36f,
                    style = Stroke(width = 5.dp.toPx()),
                )
                drawArc(
                    color = CivicColors.Text,
                    startAngle = 35f,
                    sweepAngle = 210f,
                    useCenter = false,
                    topLeft = Offset(size.width * .2f, size.height * .2f),
                    size = androidx.compose.ui.geometry.Size(size.width * .6f, size.height * .6f),
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round),
                )
                drawCircle(CivicColors.Cyan, 4.dp.toPx(), center)
            }
        }
        if (!compact) {
            Spacer(Modifier.width(10.dp))
            Text("CivicLens", color = CivicColors.Text, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ScreenScaffold(
    navController: NavHostController,
    selectedRoute: String,
    title: String? = null,
    showBack: Boolean = false,
    actions: @Composable () -> Unit = {},
    content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit,
) {
    androidx.compose.material3.Scaffold(
        containerColor = CivicColors.Navy,
        topBar = {
            if (title != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CivicColors.Navy)
                        .statusBarsPadding()
                        .padding(start = 18.dp, end = 18.dp, top = 14.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (showBack) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, "Back", tint = CivicColors.Text)
                        }
                    }
                    Text(
                        title,
                        modifier = Modifier.weight(1f),
                        color = CivicColors.Text,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    ThemeToggleButton()
                    actions()
                }
            }
        },
        bottomBar = {
            if (selectedRoute != Routes.Onboarding && selectedRoute != Routes.Report &&
                selectedRoute != Routes.Verification && selectedRoute != Routes.Resolution &&
                selectedRoute != Routes.IssueDetails
            ) {
                BottomNavBar(navController, selectedRoute)
            }
        },
        content = content,
    )
}

@Composable
private fun BottomNavBar(navController: NavHostController, selectedRoute: String) {
    val selectedIndex = when (selectedRoute) {
        Routes.Home -> 0
        Routes.Map -> 1
        Routes.Activity -> 3
        Routes.Profile -> 4
        else -> 0
    }
    Surface(
        modifier = Modifier
            .navigationBarsPadding()
            .padding(horizontal = 22.dp, vertical = 10.dp),
        color = Color.Transparent,
        shadowElevation = 0.dp,
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp),
        ) {
            val itemWidth = maxWidth / 5f
            val indicatorOffset by animateDpAsState(
                targetValue = itemWidth * selectedIndex.toFloat() + (itemWidth - 70.dp) / 2f,
                animationSpec = spring(dampingRatio = .72f, stiffness = 420f),
                label = "bottom-nav-indicator",
            )
            Box(
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(36.dp))
                    .background(CivicColors.NavPill),
            )
            Box(
                Modifier
                    .offset(x = indicatorOffset, y = 0.dp)
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(CivicColors.Navy),
            )
            Box(
                Modifier
                    .offset(x = indicatorOffset + 6.dp, y = 6.dp)
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(CivicColors.Cyan)
                    .border(6.dp, CivicColors.Navy, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                val selectedIcon = when (selectedRoute) {
                    Routes.Map -> Icons.Default.Map
                    Routes.Activity -> Icons.Default.Assignment
                    Routes.Profile -> Icons.Default.Person
                    else -> Icons.Default.Home
                }
                Icon(selectedIcon, selectedRoute, tint = CivicColors.Navy, modifier = Modifier.size(24.dp))
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                BottomNavItem("Home", Icons.Default.Home, selectedRoute == Routes.Home) {
                    navController.navigate(Routes.Home) { launchSingleTop = true }
                }
                BottomNavItem("Map", Icons.Default.Map, selectedRoute == Routes.Map) {
                    navController.navigate(Routes.Map) { launchSingleTop = true }
                }
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(CivicColors.Text.copy(alpha = if (CivicColors.useDarkMode) .92f else .96f))
                        .border(6.dp, CivicColors.NavPill, CircleShape)
                        .clickable { navController.navigate(Routes.Report) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Default.Report, "Report issue", tint = CivicColors.Navy, modifier = Modifier.size(24.dp))
                }
                BottomNavItem("Activity", Icons.Default.Assignment, selectedRoute == Routes.Activity) {
                    navController.navigate(Routes.Activity) { launchSingleTop = true }
                }
                BottomNavItem("Profile", Icons.Default.Person, selectedRoute == Routes.Profile) {
                    navController.navigate(Routes.Profile) { launchSingleTop = true }
                }
            }
        }
    }
}

@Composable
private fun BottomNavItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, selected: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(if (selected) .6f else 1f, label = "bottom-nav-scale")
    Box(
        modifier = Modifier
            .width(56.dp)
            .height(54.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            icon,
            label,
            tint = if (selected) Color.Transparent else CivicColors.NavIcon,
            modifier = Modifier.size(23.dp).scale(scale),
        )
    }
}

@Composable
fun ThemeToggleButton() {
    val theme = LocalCivicThemeActions.current
    IconButton(onClick = theme.toggleDarkMode) {
        Icon(
            if (theme.isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
            if (theme.isDarkMode) "Switch to light mode" else "Switch to dark mode",
            tint = CivicColors.Text,
        )
    }
}

@Composable
fun PrimaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = CivicColors.Cyan, contentColor = CivicColors.Navy),
    ) {
        Text(text, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun SecondaryButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(13.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = CivicColors.Text),
        border = androidx.compose.foundation.BorderStroke(1.dp, CivicColors.Border),
    ) {
        Text(text, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}

@Composable
fun GlassCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(CivicColors.Panel)
            .border(1.dp, CivicColors.Border.copy(alpha = .7f), RoundedCornerShape(18.dp))
            .padding(17.dp),
        content = content,
    )
}

@Composable
fun CivicHeroSurface(
    modifier: Modifier = Modifier,
    accent: Color = CivicColors.Cyan,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(26.dp))
            .background(CivicColors.Panel)
            .border(1.dp, CivicColors.Border.copy(alpha = .45f), RoundedCornerShape(26.dp))
            .drawBehind {
                drawCircle(
                    color = accent.copy(alpha = if (CivicColors.useDarkMode) .18f else .12f),
                    radius = size.maxDimension * .55f,
                    center = Offset(size.width * .92f, size.height * .04f),
                )
                drawCircle(
                    color = CivicColors.Lime.copy(alpha = if (CivicColors.useDarkMode) .1f else .13f),
                    radius = size.maxDimension * .42f,
                    center = Offset(size.width * .15f, size.height * 1.05f),
                )
            }
            .padding(18.dp),
        content = content,
    )
}

@Composable
fun EditorialHeader(
    eyebrow: String,
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    action: @Composable () -> Unit = {},
) {
    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Column(Modifier.weight(1f)) {
            Text(eyebrow, color = CivicColors.CyanBright, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(title, color = CivicColors.Text, fontSize = 28.sp, fontWeight = FontWeight.Bold, lineHeight = 32.sp)
            if (subtitle != null) {
                Spacer(Modifier.height(6.dp))
                Text(subtitle, color = CivicColors.Muted, fontSize = 12.sp, lineHeight = 18.sp)
            }
        }
        action()
    }
}

@Composable
fun OpenMetric(value: String, label: String, tint: Color, modifier: Modifier = Modifier) {
    Column(modifier.padding(vertical = 4.dp)) {
        Text(value, color = tint, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, color = CivicColors.Muted, fontSize = 10.sp, lineHeight = 13.sp)
    }
}

@Composable
fun CivicDivider(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxWidth().height(1.dp).background(CivicColors.Border.copy(alpha = .55f)))
}

@Composable
fun IssueLine(issue: CivicIssue, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(issue.statusColor),
        )
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(issue.title, color = CivicColors.Text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(2.dp))
            Text(issue.location, color = CivicColors.Muted, fontSize = 10.sp, maxLines = 1)
        }
        StatusChip(issue.status, issue.statusColor)
    }
}

@Composable
fun TimelineEntry(
    title: String,
    subtitle: String,
    points: String,
    tint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 13.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(13.dp).clip(CircleShape).background(tint))
            Box(Modifier.width(1.dp).height(44.dp).background(tint.copy(alpha = .28f)))
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = CivicColors.Text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Text(subtitle, color = CivicColors.Muted, fontSize = 10.sp, lineHeight = 15.sp)
        }
        Text(points, color = tint, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SectionHeader(title: String, action: String? = null, onAction: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(title, color = CivicColors.Text, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        if (action != null) {
            Text(
                action,
                color = CivicColors.Cyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(onClick = onAction),
            )
        }
    }
}

@Composable
fun StatusChip(text: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = .16f))
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(text.uppercase(), color = color, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = .3.sp)
    }
}

@Composable
fun FakePhotoCard(modifier: Modifier = Modifier, label: String? = null, tall: Boolean = false) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(13.dp))
            .background(CivicColors.PanelMuted),
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawRect(Color(0xFF4C5553))
            for (i in 0..9) {
                drawLine(
                    color = Color(0xFF67716C).copy(alpha = .45f),
                    start = Offset(0f, size.height * i / 10f),
                    end = Offset(size.width, size.height * (i + 1) / 11f),
                    strokeWidth = 2f,
                )
            }
            drawOval(
                color = Color(0xFF1C2C2F),
                topLeft = Offset(size.width * .2f, size.height * .34f),
                size = androidx.compose.ui.geometry.Size(size.width * .62f, size.height * .3f),
            )
            drawOval(
                color = Color(0xFF0B1A20),
                topLeft = Offset(size.width * .29f, size.height * .39f),
                size = androidx.compose.ui.geometry.Size(size.width * .44f, size.height * .2f),
            )
        }
        if (label != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(CivicColors.Navy.copy(alpha = .85f))
                    .padding(horizontal = 7.dp, vertical = 4.dp),
            ) {
                Text(label, color = CivicColors.Text, fontSize = 10.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun MapPreview(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(Color(0xFF173C43)),
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawRect(Color(0xFF173C43))
            for (i in 1..5) {
                drawLine(Color(0xFF35605D), Offset(size.width * i / 6, 0f), Offset(size.width * (i - 1) / 5, size.height), 3f)
                drawLine(Color(0xFF35605D), Offset(0f, size.height * i / 6), Offset(size.width, size.height * (i - 1) / 5), 2f)
            }
            drawLine(Color(0xFF6D9691), Offset(0f, size.height * .68f), Offset(size.width, size.height * .32f), 10f)
            drawLine(Color(0xFF294D58), Offset(size.width * .18f, 0f), Offset(size.width * .82f, size.height), 8f)
        }
        MapPin(Modifier.align(Alignment.Center), CivicColors.Coral)
        MapPin(Modifier.align(Alignment.TopStart), CivicColors.Purple)
        MapPin(Modifier.align(Alignment.BottomEnd), CivicColors.Green)
        Text(
            "KOLKATA",
            modifier = Modifier.align(Alignment.Center).padding(top = 58.dp),
            color = CivicColors.Text.copy(alpha = .82f),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
        )
    }
}

@Composable
private fun MapPin(modifier: Modifier, color: Color) {
    Box(
        modifier = modifier
            .padding(24.dp)
            .size(25.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = .22f))
            .border(1.dp, color, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(color))
    }
}

@Composable
fun IssueRow(issue: CivicIssue, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 11.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(issue.statusColor.copy(alpha = .14f))
                    .border(1.dp, issue.statusColor.copy(alpha = .45f), CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(Icons.Default.Place, "Issue location", tint = issue.statusColor, modifier = Modifier.size(19.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(issue.title, color = CivicColors.Text, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(issue.location, color = CivicColors.Muted, fontSize = 10.sp, maxLines = 1)
                Text(issue.distance, color = CivicColors.MutedDark, fontSize = 10.sp)
            }
            StatusChip(issue.status, issue.statusColor)
        }
        Spacer(Modifier.height(11.dp))
        Box(Modifier.fillMaxWidth().height(1.dp).background(CivicColors.Border.copy(alpha = .55f)))
    }
}

@Composable
fun StatTile(value: String, label: String, tint: Color, modifier: Modifier = Modifier) {
    Column(modifier.padding(vertical = 5.dp)) {
        Text(value, color = tint, fontSize = 21.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(label, color = CivicColors.Muted, fontSize = 10.sp, lineHeight = 13.sp)
    }
}

@Composable
fun Avatar(modifier: Modifier = Modifier, initials: String = "AM") {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(CivicColors.CyanDeep)
            .border(1.dp, CivicColors.CyanBright.copy(alpha = .6f), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Text(initials, color = CivicColors.Text, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ProgressBar(progress: Float, modifier: Modifier = Modifier, color: Color = CivicColors.Green) {
    Box(
        modifier = modifier
            .height(7.dp)
            .clip(CircleShape)
            .background(CivicColors.Navy),
    ) {
        Box(
            Modifier
                .fillMaxWidth(progress)
                .fillMaxSize()
                .clip(CircleShape)
                .background(color),
        )
    }
}

@Composable
fun CivicEmptyState(title: String, message: String, actionLabel: String? = null, onAction: () -> Unit = {}) {
    GlassCard(Modifier.fillMaxWidth()) {
        Text(title, color = CivicColors.Text, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(5.dp))
        Text(message, color = CivicColors.Muted, fontSize = 11.sp, lineHeight = 16.sp)
        if (actionLabel != null) {
            Spacer(Modifier.height(12.dp))
            Text(actionLabel, color = CivicColors.Cyan, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable(onClick = onAction))
        }
    }
}
