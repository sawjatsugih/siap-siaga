package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressionDao {

    @Query("SELECT * FROM disaster_badges")
    fun getAllBadges(): Flow<List<DisasterBadgeEntity>>

    @Query("SELECT * FROM disaster_badges WHERE disasterId = :disasterId LIMIT 1")
    suspend fun getBadge(disasterId: String): DisasterBadgeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBadges(badges: List<DisasterBadgeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateBadge(badge: DisasterBadgeEntity)

    @Query("SELECT * FROM user_progression WHERE id = 1 LIMIT 1")
    fun getUserProgression(): Flow<UserProgressionEntity?>

    @Query("SELECT * FROM user_progression WHERE id = 1 LIMIT 1")
    suspend fun getUserProgressionOnce(): UserProgressionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgression(progression: UserProgressionEntity)

    @Query("SELECT COUNT(*) FROM disaster_badges WHERE isUnlocked = 1")
    fun getUnlockedBadgesCount(): Flow<Int>
}
