package com.example.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.DisasterQuizBank
import com.example.data.QuizOption
import com.example.data.QuizQuestion
import com.example.ui.theme.JoyPrimary
import com.example.ui.theme.JoySecondary
import com.example.ui.theme.JoyTextPrimary
import com.example.ui.theme.JoyTextSecondary
import kotlinx.coroutines.delay

enum class QuizStage {
    INTRO,
    IN_PROGRESS,
    FINISHED
}

data class UserAnswerResult(
    val question: QuizQuestion,
    val selectedOption: QuizOption?,
    val isCorrect: Boolean,
    val timeSpentSeconds: Int
)

@Composable
fun DisasterRapidQuizDialog(
    onDismiss: () -> Unit,
    onScoreEarned: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val vibrator = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vm?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                vibrator?.cancel()
            } catch (_: Exception) {}
        }
    }

    fun triggerHapticFeedback(isSuccess: Boolean) {
        try {
            val vib = vibrator ?: return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (isSuccess) {
                    vib.vibrate(VibrationEffect.createOneShot(100, 180))
                } else {
                    vib.vibrate(
                        VibrationEffect.createWaveform(
                            longArrayOf(0, 80, 60, 120),
                            intArrayOf(0, 220, 0, 240),
                            -1
                        )
                    )
                }
            } else {
                @Suppress("DEPRECATION")
                if (isSuccess) vib.vibrate(100) else vib.vibrate(longArrayOf(0, 80, 60, 120), -1)
            }
        } catch (_: Exception) {}
    }

    var stage by remember { mutableStateOf(QuizStage.INTRO) }
    var selectedQuestionCount by remember { mutableIntStateOf(5) }
    val questions = remember { mutableStateListOf<QuizQuestion>() }
    val userAnswers = remember { mutableStateListOf<UserAnswerResult>() }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<QuizOption?>(null) }
    var hasAnswered by remember { mutableStateOf(false) }
    var remainingSeconds by remember { mutableIntStateOf(15) }

    var totalScore by remember { mutableIntStateOf(0) }
    var currentCombo by remember { mutableIntStateOf(0) }
    var highestCombo by remember { mutableIntStateOf(0) }

    fun startNewQuiz(count: Int) {
        selectedQuestionCount = count
        questions.clear()
        questions.addAll(DisasterQuizBank.getQuestions(count))
        userAnswers.clear()
        currentQuestionIndex = 0
        totalScore = 0
        currentCombo = 0
        highestCombo = 0
        selectedOption = null
        hasAnswered = false
        remainingSeconds = 15
        stage = QuizStage.IN_PROGRESS
    }

    // Active question countdown timer loop
    LaunchedEffect(stage, currentQuestionIndex, hasAnswered) {
        if (stage == QuizStage.IN_PROGRESS && !hasAnswered) {
            remainingSeconds = 15
            while (remainingSeconds > 0 && !hasAnswered) {
                delay(1000)
                remainingSeconds--
                // Urgent warning tick haptic when time is almost up (<= 4s)
                if (remainingSeconds in 1..4) {
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator?.vibrate(VibrationEffect.createOneShot(40, 120))
                        }
                    } catch (_: Exception) {}
                }
            }

            // Timeout occurred
            if (remainingSeconds == 0 && !hasAnswered) {
                hasAnswered = true
                triggerHapticFeedback(isSuccess = false)
                currentCombo = 0
                val currentQ = questions[currentQuestionIndex]
                userAnswers.add(
                    UserAnswerResult(
                        question = currentQ,
                        selectedOption = null,
                        isCorrect = false,
                        timeSpentSeconds = 15
                    )
                )
            }
        }
    }

    fun handleOptionClick(option: QuizOption) {
        if (hasAnswered) return
        selectedOption = option
        hasAnswered = true
        val isCorrect = option.isCorrect
        triggerHapticFeedback(isCorrect)

        if (isCorrect) {
            currentCombo++
            if (currentCombo > highestCombo) highestCombo = currentCombo
            // Scoring: 100 base + time bonus (remainingSeconds * 10) + combo multiplier bonus
            val questionScore = (100 + remainingSeconds * 10) * currentCombo
            totalScore += questionScore
        } else {
            currentCombo = 0
        }

        val currentQ = questions[currentQuestionIndex]
        userAnswers.add(
            UserAnswerResult(
                question = currentQ,
                selectedOption = option,
                isCorrect = isCorrect,
                timeSpentSeconds = 15 - remainingSeconds
            )
        )
    }

    fun proceedToNext() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            selectedOption = null
            hasAnswered = false
            remainingSeconds = 15
        } else {
            // Quiz completed!
            stage = QuizStage.FINISHED
            onScoreEarned(totalScore)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.96f)
                    .widthIn(max = 660.dp)
                    .testTag("rapid_quiz_dialog"),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(18.dp)
            ) {
                when (stage) {
                    QuizStage.INTRO -> {
                        QuizIntroView(
                            onStart = { count -> startNewQuiz(count) },
                            onClose = onDismiss
                        )
                    }
                    QuizStage.IN_PROGRESS -> {
                        if (questions.isNotEmpty() && currentQuestionIndex < questions.size) {
                            val currentQ = questions[currentQuestionIndex]
                            QuizInProgressView(
                                question = currentQ,
                                questionIndex = currentQuestionIndex,
                                totalQuestions = questions.size,
                                remainingSeconds = remainingSeconds,
                                selectedOption = selectedOption,
                                hasAnswered = hasAnswered,
                                currentCombo = currentCombo,
                                totalScore = totalScore,
                                onOptionSelected = { handleOptionClick(it) },
                                onNextQuestion = { proceedToNext() },
                                onClose = onDismiss
                            )
                        }
                    }
                    QuizStage.FINISHED -> {
                        QuizResultView(
                            totalScore = totalScore,
                            highestCombo = highestCombo,
                            userAnswers = userAnswers,
                            onPlayAgain = { startNewQuiz(selectedQuestionCount) },
                            onClose = onDismiss
                        )
                    }
                }
            }
        }
    }
}

/**
 * Screen 1: Quiz Mode Introduction & Question Count Selection
 */
@Composable
private fun QuizIntroView(
    onStart: (Int) -> Unit,
    onClose: () -> Unit
) {
    var selectedCount by remember { mutableIntStateOf(5) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFFEF3C7),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Mode Kuis Tanggap",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = JoyTextPrimary
                    )
                    Text(
                        text = "Tantangan Mitigasi Beruntun",
                        fontSize = 12.sp,
                        color = JoyPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Tutup",
                    tint = Color(0xFF64748B)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Banner Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBAE6FD))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🎯 ATURAN TANTANGAN:",
                    color = Color(0xFF0369A1),
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "• Jawab pertanyaan pilihan ganda mitigasi bencana secara beruntun.\n• Setiap soal memiliki batas waktu 15 detik.\n• Jawab cepat & tepat untuk melipatgandakan poin Kombo (x2, x3, x4)!\n• Jika waktu habis atau salah, kombo akan ter-reset.",
                    color = Color(0xFF0F172A),
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "PILIH JUMLAH SOAL:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = JoyTextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (selectedCount == 5) Color(0xFFE0F2FE) else Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(
                    2.dp,
                    if (selectedCount == 5) JoyPrimary else Color(0xFFE2E8F0)
                ),
                modifier = Modifier
                    .weight(1f)
                    .clickable { selectedCount = 5 }
                    .testTag("quiz_count_5")
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "⚡ 5 Soal", fontWeight = FontWeight.Black, fontSize = 15.sp, color = JoyTextPrimary)
                    Text(text = "Mode Kilat (~2 Menit)", fontSize = 11.sp, color = JoyTextSecondary)
                }
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = if (selectedCount == 10) Color(0xFFE0F2FE) else Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(
                    2.dp,
                    if (selectedCount == 10) JoyPrimary else Color(0xFFE2E8F0)
                ),
                modifier = Modifier
                    .weight(1f)
                    .clickable { selectedCount = 10 }
                    .testTag("quiz_count_10")
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🏆 10 Soal", fontWeight = FontWeight.Black, fontSize = 15.sp, color = JoyTextPrimary)
                    Text(text = "Tantangan Penuh (~4 Menit)", fontSize = 11.sp, color = JoyTextSecondary)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onStart(selectedCount) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("start_quiz_button"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = JoyPrimary)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Mulai Kuis Beruntun",
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp
                )
            }
        }
    }
}

/**
 * Screen 2: In-Progress Quiz Question with Countdown Timer
 */
@Composable
private fun QuizInProgressView(
    question: QuizQuestion,
    questionIndex: Int,
    totalQuestions: Int,
    remainingSeconds: Int,
    selectedOption: QuizOption?,
    hasAnswered: Boolean,
    currentCombo: Int,
    totalScore: Int,
    onOptionSelected: (QuizOption) -> Unit,
    onNextQuestion: () -> Unit,
    onClose: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Info Bar (Question number, Score, Close)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Disaster Pill & Step
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = question.disaster.softColor,
                    border = androidx.compose.foundation.BorderStroke(1.dp, question.disaster.primaryColor.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = question.disaster.emoji, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = question.disaster.title,
                            color = question.disaster.primaryColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Soal ${questionIndex + 1}/$totalQuestions",
                    color = JoyTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Score & Combo
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFFEF3C7)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⭐ $totalScore",
                            color = Color(0xFFB45309),
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                        if (currentCombo > 1) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "🔥x$currentCombo",
                                color = Color(0xFFDC2626),
                                fontWeight = FontWeight.Black,
                                fontSize = 11.5.sp
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(onClick = onClose, modifier = Modifier.size(32.dp)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Batal", tint = Color(0xFF94A3B8))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Countdown Timer & Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sisa Waktu:",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = if (remainingSeconds <= 4) Color(0xFFDC2626) else JoyTextSecondary
            )
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (remainingSeconds <= 4) Color(0xFFFEF2F2) else Color(0xFFF1F5F9),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (remainingSeconds <= 4) Color(0xFFF87171) else Color(0xFFCBD5E1)
                )
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = if (remainingSeconds <= 4) Color(0xFFDC2626) else Color(0xFF0284C7),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${remainingSeconds}s",
                        fontWeight = FontWeight.Black,
                        color = if (remainingSeconds <= 4) Color(0xFFDC2626) else Color(0xFF0F172A),
                        fontSize = 12.5.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
            progress = { remainingSeconds / 15f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = when {
                remainingSeconds <= 4 -> Color(0xFFDC2626)
                remainingSeconds <= 8 -> Color(0xFFF59E0B)
                else -> Color(0xFF0284C7)
            },
            trackColor = Color(0xFFE2E8F0)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Question Situation Box
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "SKENARIO KEJADIAN:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF64748B),
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = question.situation,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF0F172A),
                    lineHeight = 21.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Options List
        question.options.forEach { option ->
            val isSelected = selectedOption == option
            val isCorrect = option.isCorrect

            val bgColor = when {
                hasAnswered && isCorrect -> Color(0xFFF0FDF4)
                hasAnswered && isSelected && !isCorrect -> Color(0xFFFEF2F2)
                else -> Color.White
            }

            val borderColor = when {
                hasAnswered && isCorrect -> Color(0xFF22C55E)
                hasAnswered && isSelected && !isCorrect -> Color(0xFFEF4444)
                else -> Color(0xFFCBD5E1)
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = bgColor,
                border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable(enabled = !hasAnswered) {
                        onOptionSelected(option)
                    }
                    .testTag("quiz_choice_${option.label}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (hasAnswered && isCorrect) Color(0xFF22C55E) else Color(0xFFE2E8F0),
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = option.label,
                                fontWeight = FontWeight.Bold,
                                color = if (hasAnswered && isCorrect) Color.White else Color(0xFF0F172A),
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = option.text,
                        color = Color(0xFF0F172A),
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f),
                        lineHeight = 17.sp
                    )

                    if (hasAnswered) {
                        if (isCorrect) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Benar",
                                tint = Color(0xFF16A34A),
                                modifier = Modifier.size(20.dp)
                            )
                        } else if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Cancel,
                                contentDescription = "Salah",
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        // Explanation & Next Button
        AnimatedVisibility(
            visible = hasAnswered,
            enter = fadeIn() + slideInVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                val isSuccess = selectedOption?.isCorrect == true
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSuccess) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = if (isSuccess) "🎉 JAWABAN ANDA TEPAT!" else if (remainingSeconds == 0) "⏱️ WAKTU HABIS!" else "❌ TINDAKAN KURANG TEPAT!",
                            color = if (isSuccess) Color(0xFF15803D) else Color(0xFFB91C1C),
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = question.explanation,
                            color = Color(0xFF0F172A),
                            fontSize = 11.5.sp,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onNextQuestion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("quiz_next_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = JoyPrimary)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (questionIndex < totalQuestions - 1) "Soal Berikutnya ➔" else "Lihat Hasil Skor 🏆",
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Screen 3: Final Quiz Results & Mitigation Mastery Badge
 */
@Composable
private fun QuizResultView(
    totalScore: Int,
    highestCombo: Int,
    userAnswers: List<UserAnswerResult>,
    onPlayAgain: () -> Unit,
    onClose: () -> Unit
) {
    val total = userAnswers.size
    val correctCount = userAnswers.count { it.isCorrect }
    val percentage = if (total > 0) (correctCount * 100) / total else 0

    val (badgeTitle, badgeColor) = when {
        percentage >= 100 -> "🏆 Pakar Mitigasi Tanggap BNPB" to Color(0xFF059669)
        percentage >= 80 -> "🥇 Komandan Siaga Bencana" to Color(0xFF0284C7)
        percentage >= 60 -> "🥈 Relawan Siaga Aktif" to Color(0xFFD97706)
        else -> "🥉 Calon Taruna Siaga" to Color(0xFF64748B)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFFEF3C7),
            modifier = Modifier.size(60.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = "🎉", fontSize = 32.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "TANTANGAN SELESAI!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = JoyTextPrimary
        )

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = badgeColor.copy(alpha = 0.12f),
            modifier = Modifier.padding(top = 6.dp, bottom = 14.dp)
        ) {
            Text(
                text = badgeTitle,
                color = badgeColor,
                fontWeight = FontWeight.Black,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        // Score Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "TOTAL SKOR", fontSize = 11.sp, color = JoyTextSecondary, fontWeight = FontWeight.Bold)
                    Text(text = "$totalScore", fontSize = 22.sp, fontWeight = FontWeight.Black, color = Color(0xFFB45309))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "AKURASI", fontSize = 11.sp, color = JoyTextSecondary, fontWeight = FontWeight.Bold)
                    Text(text = "$correctCount/$total ($percentage%)", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF0284C7))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "KOMBO TERTINGGI", fontSize = 11.sp, color = JoyTextSecondary, fontWeight = FontWeight.Bold)
                    Text(text = "🔥 x$highestCombo", fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFFDC2626))
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Brief Review of Answers
        Text(
            text = "RINGKASAN JAWABAN:",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = JoyTextSecondary,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            userAnswers.forEachIndexed { idx, item ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (item.isCorrect) Color(0xFFF0FDF4) else Color(0xFFFEF2F2),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (item.isCorrect) Color(0xFFBBF7D0) else Color(0xFFFECACA)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${item.question.disaster.emoji} Soal ${idx + 1}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = JoyTextPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (item.isCorrect) "Benar (${item.timeSpentSeconds}s)" else "Salah / Waktu Habis",
                            color = if (item.isCorrect) Color(0xFF16A34A) else Color(0xFFDC2626),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onPlayAgain,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("quiz_play_again_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = JoyPrimary)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Main Lagi", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Button(
                onClick = onClose,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("quiz_close_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9))
            ) {
                Text(text = "Kembali", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = JoyTextPrimary)
            }
        }
    }
}
