package com.civiclens.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val user: UserEntity? = null,
    val issues: List<IssueEntity> = emptyList(),
    val activities: List<ActivityEntity> = emptyList(),
)

private const val CURRENT_USER_ID = 1L

class HomeViewModel : ViewModel() {
    val uiState: StateFlow<HomeUiState> = CivicLensData.users.observeUser().map { user ->
        HomeUiState(user = user)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())
    val issues = CivicLensData.issues.observeIssues().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    val activities = CivicLensData.activities.observe(1).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

class ProfileViewModel : ViewModel() {
    val user = CivicLensData.users.observeUser().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}

class ActivityViewModel : ViewModel() {
    val activities = CivicLensData.activities.observe(1).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

class MapViewModel : ViewModel() {
    val issues = CivicLensData.issues.observeIssues().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

class IssueDetailsViewModel(private val issueId: Long) : ViewModel() {
    val issue = CivicLensData.issues.observeIssue(issueId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}

class ReportViewModel : ViewModel() {
    fun submit(
        onPossibleDuplicate: (Long) -> Unit,
        onSubmitted: (Long) -> Unit,
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val draft = IssueEntity(
                category = IssueCategory.POTHOLE,
                description = "Large pothole causing water logging and traffic issues.",
                latitude = 22.5285, longitude = 88.3662,
                address = "Ballygunge Circular Rd, Kolkata",
                confidence = 95, severity = 3, status = IssueStatus.PENDING_VERIFICATION,
                reportedBy = CURRENT_USER_ID, createdAt = now, updatedAt = now,
                verificationCount = 0, resolutionConfidence = 0, isSynced = false,
            )
            CivicLensData.issues.findNearby(draft, 50.0)?.let {
                onPossibleDuplicate(it.id)
                return@launch
            }
            createIssue(draft, onSubmitted)
        }
    }

    fun submitAnyway(onSubmitted: (Long) -> Unit) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            createIssue(
                IssueEntity(
                    category = IssueCategory.POTHOLE,
                    description = "Large pothole causing water logging and traffic issues.",
                    latitude = 22.5285, longitude = 88.3662,
                    address = "Ballygunge Circular Rd, Kolkata",
                    confidence = 95, severity = 3, status = IssueStatus.PENDING_VERIFICATION,
                    reportedBy = CURRENT_USER_ID, createdAt = now, updatedAt = now,
                    verificationCount = 0, resolutionConfidence = 0, isSynced = false,
                ),
                onSubmitted,
            )
        }
    }

    private suspend fun createIssue(issue: IssueEntity, onSubmitted: (Long) -> Unit) {
        val id = CivicLensData.issues.insert(issue)
        val qualityBonus = if (issue.confidence >= 90) CivicPoints.HIGH_QUALITY_REPORT_BONUS else 0
        CivicLensData.users.getUser()?.let {
            CivicLensData.users.update(
                it.withPoints(CivicPoints.ACCEPTED_REPORT + qualityBonus)
                    .copy(issuesReported = it.issuesReported + 1),
            )
        }
        CivicLensData.activities.insert(
            ActivityEntity(
                userId = CURRENT_USER_ID, type = ActivityType.REPORT, title = "Pothole reported",
                description = "Ballygunge Circular Rd", timestamp = issue.createdAt,
                points = CivicPoints.ACCEPTED_REPORT, issueId = id,
            ),
        )
        if (qualityBonus > 0) {
            CivicLensData.activities.insert(
                ActivityEntity(
                    userId = CURRENT_USER_ID, type = ActivityType.REWARD,
                    title = "High-quality report bonus",
                    description = "Clear evidence helped classify the issue",
                    timestamp = issue.createdAt, points = qualityBonus, issueId = id,
                ),
            )
        }
        onSubmitted(id)
    }
}

class VerificationViewModel(private val issueId: Long) : ViewModel() {
    fun submit(
        result: VerificationResult,
        comment: String,
        onDuplicate: () -> Unit,
        onSubmitted: () -> Unit,
    ) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            if (CivicLensData.verifications.findForUser(issueId, CURRENT_USER_ID) != null) {
                onDuplicate()
                return@launch
            }
            CivicLensData.verifications.insert(
                VerificationEntity(
                    issueId = issueId, userId = CURRENT_USER_ID, result = result,
                    comment = comment, createdAt = now,
                ),
            )
            CivicLensData.database.issueDao().getIssueById(issueId)?.let { issue ->
                val verifications = CivicLensData.verifications.getForIssue(issueId)
                val valid = verifications.filter { it.result != VerificationResult.NOT_SURE }
                val stillExists = valid.count { it.result == VerificationResult.STILL_EXISTS }
                val confidence = if (valid.isEmpty()) 0 else stillExists * 100 / valid.size
                val nextStatus = when {
                    result == VerificationResult.RESOLVED -> IssueStatus.RESOLVED
                    valid.size >= 3 -> IssueStatus.VERIFIED
                    else -> IssueStatus.PENDING_VERIFICATION
                }
                CivicLensData.issues.update(
                    issue.copy(
                        status = nextStatus,
                        verificationCount = valid.size,
                        resolutionConfidence = confidence,
                        updatedAt = now,
                    ),
                )
                if (result == VerificationResult.RESOLVED) {
                    CivicLensData.resolutions.insert(
                        ResolutionEntity(
                            issueId = issueId,
                            municipalRemark = "Citizen confirmed the issue has been resolved.",
                            resolvedBy = "CivicLens community",
                            resolvedAt = now,
                            confidence = 100 - confidence,
                        ),
                    )
                }
            }
            CivicLensData.users.getUser()?.let {
                CivicLensData.users.update(it.withPoints(CivicPoints.VERIFICATION).copy(issuesVerified = it.issuesVerified + 1))
            }
            CivicLensData.activities.insert(
                ActivityEntity(
                    userId = CURRENT_USER_ID, type = if (result == VerificationResult.RESOLVED) ActivityType.RESOLUTION else ActivityType.VERIFICATION,
                    title = if (result == VerificationResult.RESOLVED) "Repair confirmed" else "Issue verified",
                    description = comment.ifBlank { "Citizen verification submitted" }, timestamp = now,
                    points = CivicPoints.VERIFICATION, issueId = issueId,
                ),
            )
            if (result == VerificationResult.RESOLVED) {
                CivicLensData.users.getUser()?.let {
                    CivicLensData.users.update(
                        it.withPoints(CivicPoints.RESOLUTION_CONFIRMATION)
                            .copy(issuesResolved = it.issuesResolved + 1),
                    )
                }
                CivicLensData.activities.insert(
                    ActivityEntity(
                        userId = CURRENT_USER_ID, type = ActivityType.REWARD,
                        title = "Resolution confirmation reward",
                        description = "You helped close the civic loop",
                        timestamp = now, points = CivicPoints.RESOLUTION_CONFIRMATION, issueId = issueId,
                    ),
                )
            }
            onSubmitted()
        }
    }
}

class ResolutionViewModel(private val issueId: Long) : ViewModel() {
    val issue = CivicLensData.issues.observeIssue(issueId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
    val resolution = CivicLensData.database.resolutionDao().getResolutionForIssue(issueId).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)
}

private suspend fun IssueRepository.findNearby(draft: IssueEntity, radiusMeters: Double): IssueEntity? {
    return observeIssuesOnce().firstOrNull { existing ->
        existing.category == draft.category &&
            distanceMeters(existing.latitude, existing.longitude, draft.latitude, draft.longitude) <= radiusMeters
    }
}

private suspend fun IssueRepository.observeIssuesOnce(): List<IssueEntity> = CivicLensData.database.issueDao().getIssues()

private fun distanceMeters(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val latDistance = (lat2 - lat1) * 111_320.0
    val lonDistance = (lon2 - lon1) * 111_320.0 * kotlin.math.cos(Math.toRadians((lat1 + lat2) / 2.0))
    return kotlin.math.sqrt(latDistance * latDistance + lonDistance * lonDistance)
}

class ViewModelFactory<T : ViewModel>(private val creator: () -> T) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM = creator() as VM
}