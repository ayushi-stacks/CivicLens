package com.civiclens.app

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users LIMIT 1") fun observeUser(): Flow<UserEntity?>
    @Query("SELECT * FROM users LIMIT 1") suspend fun getUser(): UserEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insertUser(user: UserEntity)
    @Update suspend fun updateUser(user: UserEntity)
}

@Dao
interface IssueDao {
    @Query("SELECT * FROM issues ORDER BY createdAt DESC") fun observeIssues(): Flow<List<IssueEntity>>
    @Query("SELECT * FROM issues ORDER BY createdAt DESC") suspend fun getIssues(): List<IssueEntity>
    @Query("SELECT * FROM issues WHERE id = :id") fun observeIssueById(id: Long): Flow<IssueEntity?>
    @Query("SELECT * FROM issues WHERE id = :id") suspend fun getIssueById(id: Long): IssueEntity?
    @Insert suspend fun insertIssue(issue: IssueEntity): Long
    @Update suspend fun updateIssue(issue: IssueEntity)
    @Query("SELECT COUNT(*) FROM issues") suspend fun count(): Int
}

@Dao
interface VerificationDao {
    @Query("SELECT * FROM verifications WHERE issueId = :issueId ORDER BY createdAt DESC")
    fun getVerificationsForIssue(issueId: Long): Flow<List<VerificationEntity>>
    @Query("SELECT * FROM verifications WHERE issueId = :issueId ORDER BY createdAt ASC")
    suspend fun getVerificationsForIssueOnce(issueId: Long): List<VerificationEntity>
    @Query("SELECT * FROM verifications WHERE issueId = :issueId AND userId = :userId LIMIT 1")
    suspend fun findForUser(issueId: Long, userId: Long): VerificationEntity?
    @Insert suspend fun insertVerification(verification: VerificationEntity)
}

@Dao
interface ResolutionDao {
    @Query("SELECT * FROM resolutions WHERE issueId = :issueId LIMIT 1")
    fun getResolutionForIssue(issueId: Long): Flow<ResolutionEntity?>
    @Insert suspend fun insertResolution(resolution: ResolutionEntity)
}

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activities WHERE userId = :userId ORDER BY timestamp DESC")
    fun observeActivities(userId: Long): Flow<List<ActivityEntity>>
    @Insert suspend fun insertActivity(activity: ActivityEntity)
}