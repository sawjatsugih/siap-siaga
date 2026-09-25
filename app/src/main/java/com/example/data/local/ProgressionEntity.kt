package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a Disaster Preparedness Badge earned by completing
 * safety manual sequences or mastering mitigation drills.
 */
@Entity(tableName = "disaster_badges")
data class DisasterBadgeEntity(
    @PrimaryKey
    val disasterId: String,          // Matches DisasterType.name (e.g. "GEMPA_BUMI")
    val badgeTitle: String,          // e.g. "Pelindung Lindu"
    val badgeSubtitle: String,       // e.g. "Pakar Drop, Cover, Hold On"
    val isUnlocked: Boolean = false,
    val unlockedTimestamp: Long = 0L,
    val manualCompletions: Int = 0,
    val highestQuizScore: Int = 0,
    val badgeIconTag: String = "SHIELD"
)

/**
 * Entity storing user's overall preparedness level and points.
 */
@Entity(tableName = "user_progression")
data class UserProgressionEntity(
    @PrimaryKey
    val id: Int = 1,
    val preparednessPoints: Int = 0,
    val level: Int = 1,
    val levelTitle: String = "Relawan Pemula",
    val totalManualsCompleted: Int = 0,
    val totalQuizzesCompleted: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)
