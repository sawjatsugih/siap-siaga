package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.audio.DisasterSoundEngine
import com.example.data.AppSettingsManager
import com.example.data.ProgressionRepository
import com.example.data.local.DisasterBadgeEntity
import com.example.data.local.UserProgressionEntity
import com.example.model.DisasterType
import com.example.ui.components.DisasterCardCarousel
import com.example.ui.components.DisasterRapidQuizDialog
import com.example.ui.components.DisasterResponseDialog
import com.example.ui.components.DisasterVisualAnimation
import com.example.ui.components.MitigationGuideDialog
import com.example.ui.components.ProgressionBadgesModal
import com.example.ui.components.SettingsDialog
import com.example.ui.components.WhatToDoModal
import com.example.ui.theme.JoyBackground
import com.example.ui.theme.JoyPrimary
import com.example.ui.theme.JoySurface
import com.example.ui.theme.JoyTextPrimary
import com.example.ui.theme.JoyTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisasterGameScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val settingsManager = remember { AppSettingsManager(context) }
    val soundEngine = remember {
        DisasterSoundEngine(context).apply {
            isMuted = !settingsManager.isSoundEffectsEnabled
            isMaxLoudness = !settingsManager.isComfortVolume
            isHapticsEnabled = settingsManager.isHapticsEnabled
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            soundEngine.stopAudio()
        }
    }

    var isRandomizing by remember { mutableStateOf(false) }
    var activeDisaster by remember { mutableStateOf<DisasterType?>(null) }
    var showWhatToDoModal by remember { mutableStateOf(false) }
    var showQuizDialog by remember { mutableStateOf(false) }
    var showRapidQuizDialog by remember { mutableStateOf(false) }
    var showGuideDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    var score by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }

    val progressionRepo = remember { ProgressionRepository(context) }
    LaunchedEffect(Unit) {
        progressionRepo.ensureInitialized()
    }
    val userProgression by progressionRepo.userProgression.collectAsStateWithLifecycle(initialValue = null)
    val allBadges by progressionRepo.allBadges.collectAsStateWithLifecycle(initialValue = emptyList())
    var showProgressionBadgesModal by remember { mutableStateOf(false) }

    fun triggerDisaster(disaster: DisasterType) {
        activeDisaster = disaster
        showWhatToDoModal = false
        showQuizDialog = false
        soundEngine.startDisasterAudio(disaster)
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .testTag("disaster_game_screen"),
        containerColor = JoyBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = JoyPrimary,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Tanggap Bencana",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = JoyTextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = JoySurface
                ),
                actions = {
                    // Badges gallery modal button
                    IconButton(
                        onClick = { showProgressionBadgesModal = true },
                        modifier = Modifier.testTag("badges_action_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MilitaryTech,
                            contentDescription = "Galeri Lencana Bencana",
                            tint = Color(0xFFD97706)
                        )
                    }

                    // Timed rapid quiz challenge button
                    IconButton(
                        onClick = { showRapidQuizDialog = true },
                        modifier = Modifier.testTag("rapid_quiz_action_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Mode Kuis Beruntun",
                            tint = Color(0xFFD97706)
                        )
                    }

                    // Settings menu button for sound effect comfort toggle & settings
                    IconButton(
                        onClick = { showSettingsDialog = true },
                        modifier = Modifier.testTag("settings_action_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Menu Pengaturan Suara & Kenyamanan",
                            tint = if (settingsManager.isSoundEffectsEnabled) JoyPrimary else Color(0xFF94A3B8)
                        )
                    }

                    // Mitigation guide info button
                    IconButton(
                        onClick = { showGuideDialog = true },
                        modifier = Modifier.testTag("guide_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = "Panduan Mitigasi",
                            tint = JoyPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val isTablet = maxWidth >= 720.dp

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = if (isTablet) 32.dp else 16.dp, vertical = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = if (isTablet) 720.dp else 520.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Top Status Badges (Score, Streak & Audio mode pill)
                            StatusRow(
                                score = score,
                                streak = streak,
                                isSoundMuted = soundEngine.isMuted,
                                onOpenSettings = { showSettingsDialog = true }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Badges Collection Banner (Without Level/Tingkat)
                            PreparednessProgressionCard(
                                userProgression = userProgression,
                                badges = allBadges,
                                onClick = { showProgressionBadgesModal = true }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Header Text
                            Text(
                                text = "SIMULASI 11 BENCANA ALAM",
                                color = JoyTextPrimary,
                                fontSize = if (isTablet) 23.sp else 19.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Geser kartu untuk melihat preview, atau tekan 'Acak Bencana' untuk mengundi!",
                                color = JoyTextSecondary,
                                fontSize = 12.5.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                            )

                            // Interactive Card Carousel replacing the Spin Wheel
                            DisasterCardCarousel(
                                isRandomizing = isRandomizing,
                                onRandomizeStart = {
                                    isRandomizing = true
                                    activeDisaster = null
                                },
                                onCardTick = {
                                    soundEngine.playTick()
                                },
                                onDisasterSelected = { selectedDisaster ->
                                    isRandomizing = false
                                    triggerDisaster(selectedDisaster)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Dedicated Timed Rapid Quiz Mode Banner Card
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("rapid_quiz_banner_card"),
                                shape = RoundedCornerShape(22.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFFDE68A)),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Surface(
                                                shape = CircleShape,
                                                color = Color(0xFFFEF3C7),
                                                modifier = Modifier.size(38.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Icon(
                                                        imageVector = Icons.Default.EmojiEvents,
                                                        contentDescription = null,
                                                        tint = Color(0xFFD97706),
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    text = "MODE TANTANGAN KUIS",
                                                    fontWeight = FontWeight.Black,
                                                    fontSize = 14.sp,
                                                    color = Color(0xFFB45309),
                                                    letterSpacing = 0.5.sp
                                                )
                                                Text(
                                                    text = "Jawab Soal Beruntun dengan Batas Waktu",
                                                    fontSize = 11.5.sp,
                                                    color = JoyTextSecondary
                                                )
                                            }
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFFFEF2F2),
                                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Timer,
                                                    contentDescription = null,
                                                    tint = Color(0xFFDC2626),
                                                    modifier = Modifier.size(13.dp)
                                                )
                                                Spacer(modifier = Modifier.width(3.dp))
                                                Text(
                                                    text = "15s / Soal",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFDC2626)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    Text(
                                        text = "Tantang refleksmu menjawab pertanyaan pilihan ganda mitigasi 11 bencana secara beruntun. Dapatkan bonus skor kombo untuk setiap jawaban cepat & tepat!",
                                        fontSize = 12.sp,
                                        color = Color(0xFF475569),
                                        lineHeight = 17.sp
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Button(
                                        onClick = { showRapidQuizDialog = true },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .testTag("open_rapid_quiz_button"),
                                        shape = RoundedCornerShape(14.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                        elevation = ButtonDefaults.buttonElevation(2.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.PlayArrow,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Mulai Tantangan Kuis Beruntun",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.5.sp
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }

            // Fullscreen Disaster Visual Takeover & Loud Audio Animation Overlay
            activeDisaster?.let { disaster ->
                if (!showWhatToDoModal && !showQuizDialog) {
                    DisasterVisualAnimation(
                        disaster = disaster,
                        onAnimationFinished = {
                            soundEngine.stopAudio()
                            showWhatToDoModal = true
                        },
                        onStartResponse = {
                            soundEngine.stopAudio()
                            showQuizDialog = true
                        },
                        onDismiss = {
                            soundEngine.stopAudio()
                            activeDisaster = null
                        }
                    )
                }
            }

            // Educational 'What to do' modal (explaining 3 key safety steps with AI-generated text)
            if (showWhatToDoModal && activeDisaster != null) {
                WhatToDoModal(
                    disaster = activeDisaster!!,
                    onStartDrillQuiz = {
                        showWhatToDoModal = false
                        showQuizDialog = true
                    },
                    onDismiss = {
                        showWhatToDoModal = false
                        activeDisaster = null
                    }
                )
            }

            // Interactive Single Disaster Mitigation Response Dialog (Quiz Drill)
            if (showQuizDialog && activeDisaster != null) {
                DisasterResponseDialog(
                    disaster = activeDisaster!!,
                    onSuccess = { addedScore ->
                        score += addedScore
                        streak++
                    },
                    onFinish = {
                        showQuizDialog = false
                        activeDisaster = null
                    }
                )
            }

            // Timed Rapid Sequential Quiz Challenge Dialog
            if (showRapidQuizDialog) {
                DisasterRapidQuizDialog(
                    onDismiss = { showRapidQuizDialog = false },
                    onScoreEarned = { earnedScore ->
                        score += earnedScore
                        streak++
                    }
                )
            }

            // Disaster Mitigation Guidelines Modal Dialog
            if (showGuideDialog) {
                MitigationGuideDialog(
                    onDismiss = { showGuideDialog = false }
                )
            }

            // Progression Badges Modal Dialog
            if (showProgressionBadgesModal) {
                ProgressionBadgesModal(
                    userProgression = userProgression,
                    badges = allBadges,
                    onDismiss = { showProgressionBadgesModal = false }
                )
            }

            // Comfort & Disaster Sound Settings Dialog
            if (showSettingsDialog) {
                SettingsDialog(
                    soundEngine = soundEngine,
                    settingsManager = settingsManager,
                    onDismiss = { showSettingsDialog = false }
                )
            }
        }
    }
}

@Composable
private fun PreparednessProgressionCard(
    userProgression: UserProgressionEntity?,
    badges: List<DisasterBadgeEntity>,
    onClick: () -> Unit
) {
    val points = userProgression?.preparednessPoints ?: 0
    val unlockedCount = badges.count { it.isUnlocked }
    val progressFraction = (unlockedCount.toFloat() / 11f).coerceIn(0f, 1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .testTag("preparedness_progression_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF1E293B)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFBBF24).copy(alpha = 0.2f),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = null,
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "KOLEKSI LENCANA SIAGA",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "$points PP • $unlockedCount dari 11 Terbuka",
                            color = Color(0xFFFBBF24),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF1E293B)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color(0xFF38BDF8),
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$unlockedCount/11 Lencana",
                            color = Color(0xFFF1F5F9),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color(0xFFFBBF24),
                trackColor = Color(0xFF334155)
            )
        }
    }
}

@Composable
private fun StatusRow(
    score: Int,
    streak: Int,
    isSoundMuted: Boolean,
    onOpenSettings: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Score Badge
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFFFEF3C7),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⭐ Skor: $score",
                    color = Color(0xFFB45309),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                if (streak > 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFF97316),
                        modifier = Modifier.padding(1.dp)
                    ) {
                        Text(
                            text = "x$streak",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Sound Settings Pill Button
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = if (isSoundMuted) Color(0xFFF1F5F9) else Color(0xFFE0F2FE),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSoundMuted) Color(0xFFCBD5E1) else Color(0xFFBAE6FD)
            ),
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .clickable(onClick = onOpenSettings)
                .testTag("status_sound_settings_pill")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (isSoundMuted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = null,
                    tint = if (isSoundMuted) Color(0xFF64748B) else Color(0xFF0369A1),
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isSoundMuted) "Suara: Bisu" else "Suara: Aktif",
                    color = if (isSoundMuted) Color(0xFF64748B) else Color(0xFF0369A1),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
