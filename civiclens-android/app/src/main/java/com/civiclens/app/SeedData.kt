package com.civiclens.app

import android.content.Context
import androidx.room.Room

object CivicLensData {
    lateinit var database: CivicLensDatabase
        private set
    lateinit var users: UserRepository
        private set
    lateinit var issues: IssueRepository
        private set
    lateinit var verifications: VerificationRepository
        private set
    lateinit var resolutions: ResolutionRepository
        private set
    lateinit var activities: ActivityRepository
        private set

    fun initialize(context: Context) {
        if (::database.isInitialized) return
        database = Room.databaseBuilder(context, CivicLensDatabase::class.java, "civiclens_database").build()
        users = UserRepository(database.userDao())
        issues = IssueRepository(database.issueDao())
        verifications = VerificationRepository(database.verificationDao())
        resolutions = ResolutionRepository(database.resolutionDao())
        activities = ActivityRepository(database.activityDao())
    }

    suspend fun seedIfNeeded() {
        if (issues.count() > 0) return
        users.updateOrInsert(
            UserEntity(
                name = "Ayushi Mandal", location = "Kolkata, India", civicPoints = 1200, level = 4,
                issuesReported = 24, issuesVerified = 18, issuesResolved = 8,
            ),
        )
        val now = System.currentTimeMillis()
        val issueIds = listOf(
            issues.insert(IssueEntity(category = IssueCategory.POTHOLE, description = "Large pothole causing water logging and traffic issues.", latitude = 22.5285, longitude = 88.3662, address = "Ballygunge Circular Rd, Kolkata", confidence = 95, severity = 3, status = IssueStatus.VERIFIED, reportedBy = 1, createdAt = now - 120000, updatedAt = now - 120000, verificationCount = 12, resolutionConfidence = 76)),
            issues.insert(IssueEntity(category = IssueCategory.WASTE, description = "Overflowing waste bin needs collection.", latitude = 22.5197, longitude = 88.3632, address = "Hazra Road", confidence = 88, severity = 2, status = IssueStatus.PENDING_VERIFICATION, reportedBy = 1, createdAt = now - 172800000, updatedAt = now - 172800000, verificationCount = 3, resolutionConfidence = 42)),
            issues.insert(IssueEntity(category = IssueCategory.STREETLIGHT, description = "Streetlight is not working after sunset.", latitude = 22.5280, longitude = 88.3504, address = "Lansdowne Road", confidence = 82, severity = 2, status = IssueStatus.VERIFIED, reportedBy = 1, createdAt = now - 432000000, updatedAt = now - 432000000, verificationCount = 8, resolutionConfidence = 82)),
        )
        activities.insert(ActivityEntity(userId = 1, type = ActivityType.REPORT, title = "Pothole reported", description = "Ballygunge Circular Rd", timestamp = now - 120000, points = 50, issueId = issueIds[0]))
        activities.insert(ActivityEntity(userId = 1, type = ActivityType.RESOLUTION, title = "Streetlight repaired", description = "Hindustan Park", timestamp = now - 86400000, points = 100, issueId = issueIds[2]))
        activities.insert(ActivityEntity(userId = 1, type = ActivityType.VERIFICATION, title = "Overflowing bin", description = "Hazra Road", timestamp = now - 172800000, points = 20, issueId = issueIds[1]))
        resolutions.insert(
            ResolutionEntity(
                issueId = issueIds[2],
                municipalRemark = "Streetlight repaired and tested after sunset.",
                resolvedBy = "Kolkata Municipal Corporation",
                resolvedAt = now - 86400000,
                confidence = 91,
            ),
        )
    }

    private suspend fun UserRepository.updateOrInsert(user: UserEntity) {
        if (getUser() == null) database.userDao().insertUser(user) else update(user)
    }
}