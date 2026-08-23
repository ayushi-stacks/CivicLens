package com.civiclens.app

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Long = 1,
    val name: String,
    val location: String,
    val avatarUri: String? = null,
    val civicPoints: Int,
    val level: Int,
    val issuesReported: Int,
    val issuesVerified: Int,
    val issuesResolved: Int,
)

@Entity(tableName = "issues")
data class IssueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: IssueCategory,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val imageUri: String? = null,
    val confidence: Int,
    val severity: Int,
    val status: IssueStatus,
    val reportedBy: Long,
    val createdAt: Long,
    val updatedAt: Long,
    val verificationCount: Int,
    val resolutionConfidence: Int,
    val isSynced: Boolean = false,
)

@Entity(tableName = "verifications")
data class VerificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val issueId: Long,
    val userId: Long,
    val result: VerificationResult,
    val comment: String,
    val createdAt: Long,
    val isSynced: Boolean = false,
)

@Entity(tableName = "resolutions")
data class ResolutionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val issueId: Long,
    val beforeImageUri: String? = null,
    val afterImageUri: String? = null,
    val municipalRemark: String,
    val resolvedBy: String,
    val resolvedAt: Long,
    val confidence: Int,
)

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val type: ActivityType,
    val title: String,
    val description: String,
    val timestamp: Long,
    val points: Int,
    val issueId: Long? = null,
)

class CivicLensConverters {
    @TypeConverter fun categoryToString(value: IssueCategory) = value.name
    @TypeConverter fun stringToCategory(value: String) = IssueCategory.valueOf(value)
    @TypeConverter fun statusToString(value: IssueStatus) = value.name
    @TypeConverter fun stringToStatus(value: String) = IssueStatus.valueOf(value)
    @TypeConverter fun resultToString(value: VerificationResult) = value.name
    @TypeConverter fun stringToResult(value: String) = VerificationResult.valueOf(value)
    @TypeConverter fun activityTypeToString(value: ActivityType) = value.name
    @TypeConverter fun stringToActivityType(value: String) = ActivityType.valueOf(value)
}

@Database(
    entities = [UserEntity::class, IssueEntity::class, VerificationEntity::class, ResolutionEntity::class, ActivityEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(CivicLensConverters::class)
abstract class CivicLensDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun issueDao(): IssueDao
    abstract fun verificationDao(): VerificationDao
    abstract fun resolutionDao(): ResolutionDao
    abstract fun activityDao(): ActivityDao
}