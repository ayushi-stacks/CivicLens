package com.civiclens.app

import kotlinx.coroutines.flow.Flow

class UserRepository(private val dao: UserDao) {
    fun observeUser() = dao.observeUser()
    suspend fun getUser() = dao.getUser()
    suspend fun update(user: UserEntity) = dao.updateUser(user)
}

class IssueRepository(private val dao: IssueDao) {
    fun observeIssues() = dao.observeIssues()
    fun observeIssue(id: Long) = dao.observeIssueById(id)
    suspend fun insert(issue: IssueEntity) = dao.insertIssue(issue)
    suspend fun update(issue: IssueEntity) = dao.updateIssue(issue)
    suspend fun count() = dao.count()
}

class VerificationRepository(private val dao: VerificationDao) {
    suspend fun insert(value: VerificationEntity) = dao.insertVerification(value)
    suspend fun getForIssue(issueId: Long) = dao.getVerificationsForIssueOnce(issueId)
    suspend fun findForUser(issueId: Long, userId: Long) = dao.findForUser(issueId, userId)
}

class ResolutionRepository(private val dao: ResolutionDao) {
    suspend fun insert(value: ResolutionEntity) = dao.insertResolution(value)
}

class ActivityRepository(private val dao: ActivityDao) {
    fun observe(userId: Long): Flow<List<ActivityEntity>> = dao.observeActivities(userId)
    suspend fun insert(value: ActivityEntity) = dao.insertActivity(value)
}