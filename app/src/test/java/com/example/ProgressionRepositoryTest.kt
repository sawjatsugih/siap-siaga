package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.ProgressionRepository
import com.example.data.local.AppDatabase
import com.example.model.DisasterType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ProgressionRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: ProgressionRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = ProgressionRepository(database.progressionDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInitialProgressionSetup() = runBlocking {
        repository.ensureInitialized()
        val userProgression = repository.userProgression.first()
        assertNotNull(userProgression)
        assertEquals(1, userProgression?.level)
        assertEquals(0, userProgression?.preparednessPoints)

        val badges = repository.allBadges.first()
        assertEquals(11, badges.size)
        assertTrue(badges.all { !it.isUnlocked })
    }

    @Test
    fun testCompleteSafetyManualAwardsBadgeAndPoints() = runBlocking {
        repository.ensureInitialized()
        val reward = repository.completeSafetyManual(DisasterType.GEMPA_BUMI)

        assertTrue(reward.isFirstTimeUnlock)
        assertEquals(150, reward.pointsAwarded)
        assertTrue(reward.badgeTitle.isNotEmpty())

        val userProgression = repository.userProgression.first()
        assertEquals(150, userProgression?.preparednessPoints)

        val badges = repository.allBadges.first()
        val badge = badges.firstOrNull { it.disasterId == DisasterType.GEMPA_BUMI.name }
        assertNotNull(badge)
        assertTrue(badge?.isUnlocked == true)

        // Completing it again awards refresher points but does not re-unlock
        val secondReward = repository.completeSafetyManual(DisasterType.GEMPA_BUMI)
        assertFalse(secondReward.isFirstTimeUnlock)
        assertEquals(30, secondReward.pointsAwarded)
    }

    @Test
    fun testLevelProgressionThresholds() = runBlocking {
        repository.ensureInitialized()

        // Complete 2 manuals: 150 + 150 = 300 PP -> should reach Level 2 (200 PP threshold)
        repository.completeSafetyManual(DisasterType.GEMPA_BUMI)
        val reward2 = repository.completeSafetyManual(DisasterType.BANJIR)

        assertTrue(reward2.isLevelUp)
        assertEquals(2, reward2.newLevel)

        val userProgression = repository.userProgression.first()
        assertEquals(2, userProgression?.level)
        assertEquals(300, userProgression?.preparednessPoints)
    }
}
