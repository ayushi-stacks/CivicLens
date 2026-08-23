package com.civiclens.app

object CivicPoints {
    const val ACCEPTED_REPORT = 50
    const val VERIFICATION = 20
    const val HIGH_QUALITY_REPORT_BONUS = 30
    const val RESOLUTION_CONFIRMATION = 25

    fun levelFor(points: Int): Int = when {
        points >= 2_000 -> 5
        points >= 1_000 -> 4
        points >= 500 -> 3
        points >= 250 -> 2
        else -> 1
    }
}

fun UserEntity.withPoints(points: Int): UserEntity {
    val nextPoints = (civicPoints + points).coerceAtLeast(0)
    return copy(civicPoints = nextPoints, level = CivicPoints.levelFor(nextPoints))
}