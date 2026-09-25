package com.example.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.data.DisasterDrillBank
import com.example.data.DrillOption
import com.example.data.DrillQuestion
import com.example.data.ProgressionRepository
import com.example.model.DisasterType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DisasterResponseDialog(
    disaster: DisasterType,
    onSuccess: (scoreToAdd: Int) -> Unit,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val progressionRepo = remember { ProgressionRepository(context) }
    val questions = remember(disaster) {
        DisasterDrillBank.getDrillQuestions(disaster, count = 5)
    }

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<DrillOption?>(null) }
    var hasAnswered by remember { mutableStateOf(false) }
    var remainingSeconds by remember { mutableIntStateOf(15) }
    var correctCount by remember { mutableIntStateOf(0) }
    var totalEarnedScore by remember { mutableIntStateOf(0) }
    var isQuizCompleted by remember { mutableStateOf(false) }

    fun triggerHaptic(success: Boolean) {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }
            vibrator?.let {
                if (it.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val effect = if (success) {
                            VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE)
                        } else {
                            VibrationEffect.createWaveform(longArrayOf(0, 70, 70, 100), -1)
                        }
                        it.vibrate(effect)
                    } else {
                        @Suppress("DEPRECATION")
                        it.vibrate(if (success) 80L else 180L)
                    }
                }
            }
        } catch (_: Exception) {}
    }

    // Timer per question (15 seconds)
    LaunchedEffect(currentQuestionIndex, hasAnswered, isQuizCompleted) {
        if (!hasAnswered && !isQuizCompleted) {
            remainingSeconds = 15
            while (remainingSeconds > 0 && !hasAnswered) {
                delay(1000)
                remainingSeconds--
            }
            if (remainingSeconds == 0 && !hasAnswered) {
                hasAnswered = true
                triggerHaptic(false)
            }
        }
    }

    Dialog(
        onDismissRequest = { /* Require user action */ },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .widthIn(max = 640.dp)
                    .padding(vertical = 16.dp)
                    .testTag("disaster_response_dialog"),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(2.dp, disaster.primaryColor.copy(alpha = 0.45f)),
                elevation = CardDefaults.cardElevation(18.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!isQuizCompleted && questions.isNotEmpty()) {
                        val currentQ = questions[currentQuestionIndex]

                        // Header Bar: Disaster & Question Counter (e.g. Soal 1 dari 5)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = disaster.softColor,
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = disaster.emoji, fontSize = 22.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "UJI KUIS TANGGAP: ${disaster.title.uppercase()}",
                                        color = Color(0xFF0F172A),
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = "Soal ${currentQuestionIndex + 1} dari ${questions.size}",
                                        color = disaster.primaryColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // 15s Countdown Timer Chip
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (remainingSeconds <= 5) Color(0xFFFEE2E2) else disaster.softColor,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (remainingSeconds <= 5) Color(0xFFEF4444) else disaster.primaryColor.copy(alpha = 0.5f)
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = if (remainingSeconds <= 5) Color(0xFFDC2626) else disaster.primaryColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${remainingSeconds}s",
                                        color = if (remainingSeconds <= 5) Color(0xFFDC2626) else disaster.primaryColor,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Progress bar for 5 questions
                        LinearProgressIndicator(
                            progress = { (currentQuestionIndex + 1).toFloat() / questions.size },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(5.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = disaster.primaryColor,
                            trackColor = Color(0xFFE2E8F0)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Situation Prompt Box
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = null,
                                        tint = disaster.primaryColor,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "SKENARIO DARURAT #${currentQuestionIndex + 1}",
                                        color = disaster.primaryColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = currentQ.situation,
                                    color = Color(0xFF1E293B),
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 20.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Options List
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            currentQ.options.forEach { option ->
                                val isSelected = selectedOption == option
                                val isRight = option.isCorrect

                                val backgroundColor = when {
                                    !hasAnswered && isSelected -> disaster.softColor
                                    !hasAnswered -> Color(0xFFF8FAFC)
                                    hasAnswered && isRight -> Color(0xFFDCFCE7) // Green
                                    hasAnswered && isSelected && !isRight -> Color(0xFFFEE2E2) // Red
                                    else -> Color(0xFFF8FAFC).copy(alpha = 0.6f)
                                }

                                val borderColor = when {
                                    !hasAnswered && isSelected -> disaster.primaryColor
                                    !hasAnswered -> Color(0xFFCBD5E1)
                                    hasAnswered && isRight -> Color(0xFF22C55E)
                                    hasAnswered && isSelected && !isRight -> Color(0xFFEF4444)
                                    else -> Color(0xFFE2E8F0)
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(backgroundColor)
                                        .border(
                                            width = if (isSelected || (hasAnswered && isRight)) 2.dp else 1.dp,
                                            color = borderColor,
                                            shape = RoundedCornerShape(14.dp)
                                        )
                                        .clickable(enabled = !hasAnswered) {
                                            selectedOption = option
                                            hasAnswered = true
                                            val isSuccess = option.isCorrect
                                            triggerHaptic(isSuccess)
                                            if (isSuccess) {
                                                correctCount++
                                                val score = 100 + (remainingSeconds * 10)
                                                totalEarnedScore += score
                                            }
                                        }
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = if (hasAnswered && isRight) Color(0xFF16A34A) else if (hasAnswered && isSelected && !isRight) Color(0xFFDC2626) else disaster.primaryColor.copy(alpha = 0.15f),
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = option.label,
                                                    color = if (hasAnswered && (isRight || isSelected)) Color.White else disaster.primaryColor,
                                                    fontWeight = FontWeight.Black,
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(10.dp))

                                        Text(
                                            text = option.text,
                                            color = Color(0xFF0F172A),
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected || (hasAnswered && isRight)) FontWeight.Bold else FontWeight.Normal,
                                            modifier = Modifier.weight(1f)
                                        )

                                        if (hasAnswered) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            if (isRight) {
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
                        }

                        // Explanation and Next Button when answered
                        AnimatedVisibility(
                            visible = hasAnswered,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Column(modifier = Modifier.padding(top = 14.dp)) {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selectedOption?.isCorrect == true) Color(0xFFF0FDF4) else Color(0xFFFEF2F2)
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (selectedOption?.isCorrect == true) Color(0xFF86EFAC) else Color(0xFFFCA5A5)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = if (selectedOption?.isCorrect == true) "✅ TEPAT SEKALI! (+${100 + remainingSeconds * 10} Poin)" else if (selectedOption == null) "⏰ WAKTU HABIS!" else "❌ KURANG TEPAT!",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 13.sp,
                                            color = if (selectedOption?.isCorrect == true) Color(0xFF15803D) else Color(0xFFB91C1C)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = currentQ.explanation,
                                            fontSize = 12.sp,
                                            color = Color(0xFF334155),
                                            lineHeight = 17.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = {
                                        if (currentQuestionIndex + 1 < questions.size) {
                                            currentQuestionIndex++
                                            selectedOption = null
                                            hasAnswered = false
                                        } else {
                                            isQuizCompleted = true
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .testTag("next_drill_question_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = disaster.primaryColor),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Text(
                                        text = if (currentQuestionIndex + 1 < questions.size) "Soal Berikutnya (${currentQuestionIndex + 2}/${questions.size}) ➔" else "Lihat Hasil Drill Kuis ➔",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    } else {
                        // Completed Quiz Summary Screen
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFFEF3C7),
                                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFF59E0B)),
                                modifier = Modifier.size(68.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = null,
                                        tint = Color(0xFFD97706),
                                        modifier = Modifier.size(38.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "KUIS TANGGAP SELESAI!",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A)
                            )

                            Text(
                                text = "Latihan Mitigasi: ${disaster.title}",
                                fontSize = 13.sp,
                                color = disaster.primaryColor,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Score and Correct Stats Card
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                                shape = RoundedCornerShape(18.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "JAWABAN BENAR", fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                                        Text(text = "$correctCount / ${questions.size}", fontSize = 22.sp, color = Color(0xFF16A34A), fontWeight = FontWeight.Black)
                                    }
                                    Box(modifier = Modifier.width(1.dp).height(36.dp).background(Color(0xFFCBD5E1)))
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "TOTAL SKOR", fontSize = 11.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
                                        Text(text = "+$totalEarnedScore", fontSize = 22.sp, color = Color(0xFFD97706), fontWeight = FontWeight.Black)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        progressionRepo.completeQuizDrill(disaster, correctCount)
                                    }
                                    onSuccess(totalEarnedScore)
                                    onFinish()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("finish_drill_quiz_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = disaster.primaryColor),
                                shape = RoundedCornerShape(16.dp),
                                elevation = ButtonDefaults.buttonElevation(8.dp)
                            ) {
                                Text(
                                    text = "Selesai & Simpan Skor (+${correctCount * 20} PP)",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
