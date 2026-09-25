package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [DisasterBadgeEntity::class, UserProgressionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun progressionDao(): ProgressionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tanggap_bencana.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Initialize default user progression and default 11 disaster badges
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    seedInitialBadgesAndProfile(database.progressionDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedInitialBadgesAndProfile(dao: ProgressionDao) {
            val existingProfile = dao.getUserProgressionOnce()
            if (existingProfile == null) {
                dao.upsertProgression(
                    UserProgressionEntity(
                        id = 1,
                        preparednessPoints = 0,
                        level = 1,
                        levelTitle = "Relawan Pemula",
                        totalManualsCompleted = 0,
                        totalQuizzesCompleted = 0
                    )
                )
            }

            val defaultBadges = listOf(
                DisasterBadgeEntity(
                    disasterId = "GEMPA_BUMI",
                    badgeTitle = "Pelindung Lindu",
                    badgeSubtitle = "Pakar Drop, Cover, Hold On & Jalur Aman",
                    badgeIconTag = "EARTHQUAKE"
                ),
                DisasterBadgeEntity(
                    disasterId = "TSUNAMI",
                    badgeTitle = "Penakluk Gelombang",
                    badgeSubtitle = "Pakar Evakuasi Cepat 20/20/20 & Perbukitan",
                    badgeIconTag = "TSUNAMI"
                ),
                DisasterBadgeEntity(
                    disasterId = "KEBAKARAN",
                    badgeTitle = "Penjinak Kobaran",
                    badgeSubtitle = "Pakar Merangkak Asap & Teknik Semprot PASS",
                    badgeIconTag = "FIRE"
                ),
                DisasterBadgeEntity(
                    disasterId = "BANJIR",
                    badgeTitle = "Pengawal Arus",
                    badgeSubtitle = "Pakar Putus Utilitas & Evakuasi Vertikal",
                    badgeIconTag = "FLOOD"
                ),
                DisasterBadgeEntity(
                    disasterId = "GUNUNG_BERAPI",
                    badgeTitle = "Penyintas Kaldera",
                    badgeSubtitle = "Pakar Masker N95, Goggle & Radius Bahaya",
                    badgeIconTag = "VOLCANO"
                ),
                DisasterBadgeEntity(
                    disasterId = "TANAH_LONGSOR",
                    badgeTitle = "Penjaga Lereng",
                    badgeSubtitle = "Pakar Jalur Tegak Lurus & Deteksi Retakan",
                    badgeIconTag = "LANDSLIDE"
                ),
                DisasterBadgeEntity(
                    disasterId = "ANGIN_TOPAN",
                    badgeTitle = "Benteng Siklon",
                    badgeSubtitle = "Pakar Ruang Tanpa Jendela & Pelindung Kasur",
                    badgeIconTag = "TORNADO"
                ),
                DisasterBadgeEntity(
                    disasterId = "KEKERINGAN",
                    badgeTitle = "Pelindung Mata Air",
                    badgeSubtitle = "Pakar Panen Air Hujan & Konservasi Tirta",
                    badgeIconTag = "DROUGHT"
                ),
                DisasterBadgeEntity(
                    disasterId = "ABRASI",
                    badgeTitle = "Benteng Mangrove",
                    badgeSubtitle = "Pakar Sabuk Hijau Pesisir & Penahan Gelombang",
                    badgeIconTag = "ABRASION"
                ),
                DisasterBadgeEntity(
                    disasterId = "PERUBAHAN_IKLIM",
                    badgeTitle = "Penjaga Bumi",
                    badgeSubtitle = "Pakar Reduksi Emisi & Aksi Hijau Berkelanjutan",
                    badgeIconTag = "CLIMATE"
                ),
                DisasterBadgeEntity(
                    disasterId = "KONFLIK_SOSIAL",
                    badgeTitle = "Duta Harmoni",
                    badgeSubtitle = "Pakar De-eskalasi, Filter Hoaks & Zona Damai",
                    badgeIconTag = "CONFLICT"
                )
            )

            for (badge in defaultBadges) {
                if (dao.getBadge(badge.disasterId) == null) {
                    dao.updateBadge(badge)
                }
            }
        }
    }
}
