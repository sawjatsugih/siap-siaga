package com.example.data

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.local.DisasterBadgeEntity
import com.example.data.local.ProgressionDao
import com.example.data.local.UserProgressionEntity
import com.example.model.DisasterType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

data class ProgressionReward(
    val pointsAwarded: Int,
    val isFirstTimeUnlock: Boolean,
    val badgeTitle: String,
    val oldLevel: Int,
    val newLevel: Int,
    val isLevelUp: Boolean,
    val newLevelTitle: String,
    val currentPoints: Int,
    val pointsForNextLevel: Int
)

data class LevelInfo(
    val level: Int,
    val title: String,
    val minPoints: Int,
    val nextLevelPoints: Int,
    val badgeColorHex: Long
)

class ProgressionRepository(
    private val dao: ProgressionDao
) {

    constructor(context: Context) : this(AppDatabase.getInstance(context).progressionDao())

    val allBadges: Flow<List<DisasterBadgeEntity>> = dao.getAllBadges()
    val userProgression: Flow<UserProgressionEntity?> = dao.getUserProgression()
    val unlockedCount: Flow<Int> = dao.getUnlockedBadgesCount()

    suspend fun ensureInitialized() = withContext(Dispatchers.IO) {
        AppDatabase.seedInitialBadgesAndProfile(dao)
    }

    suspend fun completeSafetyManual(disaster: DisasterType): ProgressionReward = withContext(Dispatchers.IO) {
        ensureInitialized()

        val disasterKey = disaster.name
        val currentBadge = dao.getBadge(disasterKey)
        val currentProfile = dao.getUserProgressionOnce() ?: UserProgressionEntity()

        val isFirstTime = currentBadge == null || !currentBadge.isUnlocked
        val pointsToAdd = if (isFirstTime) 150 else 30

        val oldLevel = currentProfile.level
        val newTotalPoints = currentProfile.preparednessPoints + pointsToAdd
        val (newLevel, newTitle) = calculateLevel(newTotalPoints)
        val isLevelUp = newLevel > oldLevel

        // Update badge
        val updatedBadge = (currentBadge ?: DisasterBadgeEntity(
            disasterId = disasterKey,
            badgeTitle = getBadgeTitleForDisaster(disaster),
            badgeSubtitle = getBadgeSubtitleForDisaster(disaster),
            badgeIconTag = disasterKey
        )).copy(
            isUnlocked = true,
            unlockedTimestamp = if (isFirstTime) System.currentTimeMillis() else currentBadge?.unlockedTimestamp ?: System.currentTimeMillis(),
            manualCompletions = (currentBadge?.manualCompletions ?: 0) + 1
        )
        dao.updateBadge(updatedBadge)

        // Update user profile
        val updatedProfile = currentProfile.copy(
            preparednessPoints = newTotalPoints,
            level = newLevel,
            levelTitle = newTitle,
            totalManualsCompleted = currentProfile.totalManualsCompleted + 1,
            lastUpdated = System.currentTimeMillis()
        )
        dao.upsertProgression(updatedProfile)

        val nextLevelPoints = getNextLevelThreshold(newLevel)

        ProgressionReward(
            pointsAwarded = pointsToAdd,
            isFirstTimeUnlock = isFirstTime,
            badgeTitle = updatedBadge.badgeTitle,
            oldLevel = oldLevel,
            newLevel = newLevel,
            isLevelUp = isLevelUp,
            newLevelTitle = newTitle,
            currentPoints = newTotalPoints,
            pointsForNextLevel = nextLevelPoints
        )
    }

    suspend fun completeQuizDrill(disaster: DisasterType, correctCount: Int): ProgressionReward = withContext(Dispatchers.IO) {
        ensureInitialized()

        val disasterKey = disaster.name
        val currentBadge = dao.getBadge(disasterKey)
        val currentProfile = dao.getUserProgressionOnce() ?: UserProgressionEntity()

        val pointsToAdd = correctCount * 20

        val oldLevel = currentProfile.level
        val newTotalPoints = currentProfile.preparednessPoints + pointsToAdd
        val (newLevel, newTitle) = calculateLevel(newTotalPoints)
        val isLevelUp = newLevel > oldLevel

        // Update badge highest quiz score if higher
        if (currentBadge != null) {
            val newHighest = maxOf(currentBadge.highestQuizScore, correctCount)
            dao.updateBadge(currentBadge.copy(highestQuizScore = newHighest))
        }

        // Update user profile
        val updatedProfile = currentProfile.copy(
            preparednessPoints = newTotalPoints,
            level = newLevel,
            levelTitle = newTitle,
            totalQuizzesCompleted = currentProfile.totalQuizzesCompleted + 1,
            lastUpdated = System.currentTimeMillis()
        )
        dao.upsertProgression(updatedProfile)

        val nextLevelPoints = getNextLevelThreshold(newLevel)

        ProgressionReward(
            pointsAwarded = pointsToAdd,
            isFirstTimeUnlock = false,
            badgeTitle = currentBadge?.badgeTitle ?: getBadgeTitleForDisaster(disaster),
            oldLevel = oldLevel,
            newLevel = newLevel,
            isLevelUp = isLevelUp,
            newLevelTitle = newTitle,
            currentPoints = newTotalPoints,
            pointsForNextLevel = nextLevelPoints
        )
    }

    companion object {
        fun calculateLevel(points: Int): Pair<Int, String> {
            return when {
                points >= 1800 -> 5 to "Master Tanggap Bencana"
                points >= 1000 -> 4 to "Komandan Mitigasi"
                points >= 500  -> 3 to "Penyelamat Tanggap"
                points >= 200  -> 2 to "Kader Siaga"
                else           -> 1 to "Relawan Pemula"
            }
        }

        fun getLevelInfo(level: Int): LevelInfo {
            return when (level) {
                1 -> LevelInfo(1, "Relawan Pemula", 0, 200, 0xFF38BDF8)
                2 -> LevelInfo(2, "Kader Siaga", 200, 500, 0xFF34D399)
                3 -> LevelInfo(3, "Penyelamat Tanggap", 500, 1000, 0xFFF59E0B)
                4 -> LevelInfo(4, "Komandan Mitigasi", 1000, 1800, 0xFF8B5CF6)
                else -> LevelInfo(5, "Master Tanggap Bencana", 1800, 2500, 0xFFEF4444)
            }
        }

        fun getNextLevelThreshold(level: Int): Int {
            return when (level) {
                1 -> 200
                2 -> 500
                3 -> 1000
                4 -> 1800
                else -> 2500
            }
        }

        fun getBadgeTitleForDisaster(disaster: DisasterType): String {
            return when (disaster) {
                DisasterType.GEMPA_BUMI -> "Pelindung Lindu"
                DisasterType.TSUNAMI -> "Penakluk Gelombang"
                DisasterType.KEBAKARAN -> "Penjinak Kobaran"
                DisasterType.BANJIR -> "Pengawal Arus"
                DisasterType.GUNUNG_BERAPI -> "Penyintas Kaldera"
                DisasterType.TANAH_LONGSOR -> "Penjaga Lereng"
                DisasterType.ANGIN_TOPAN -> "Benteng Siklon"
                DisasterType.KEKERINGAN -> "Pelindung Mata Air"
                DisasterType.ABRASI -> "Benteng Mangrove"
                DisasterType.PERUBAHAN_IKLIM -> "Penjaga Bumi"
                DisasterType.KONFLIK_SOSIAL -> "Duta Harmoni"
            }
        }

        fun getBadgeSubtitleForDisaster(disaster: DisasterType): String {
            return when (disaster) {
                DisasterType.GEMPA_BUMI -> "Pakar Drop, Cover, Hold On & Jalur Aman"
                DisasterType.TSUNAMI -> "Pakar Evakuasi Cepat 20/20/20 & Perbukitan"
                DisasterType.KEBAKARAN -> "Pakar Merangkak Asap & Teknik Semprot PASS"
                DisasterType.BANJIR -> "Pakar Putus Utilitas & Evakuasi Vertikal"
                DisasterType.GUNUNG_BERAPI -> "Pakar Masker N95, Goggle & Radius Bahaya"
                DisasterType.TANAH_LONGSOR -> "Pakar Jalur Tegak Lurus & Deteksi Retakan"
                DisasterType.ANGIN_TOPAN -> "Pakar Ruang Tanpa Jendela & Pelindung Kasur"
                DisasterType.KEKERINGAN -> "Pakar Panen Air Hujan & Konservasi Tirta"
                DisasterType.ABRASI -> "Pakar Sabuk Hijau Pesisir & Penahan Gelombang"
                DisasterType.PERUBAHAN_IKLIM -> "Pakar Reduksi Emisi & Aksi Hijau Berkelanjutan"
                DisasterType.KONFLIK_SOSIAL -> "Pakar De-eskalasi, Filter Hoaks & Zona Damai"
            }
        }
    }
}
