package com.example.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DisasterType
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import java.util.Random
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 * Immersive Fullscreen Disaster Screen Takeover Animation.
 * Directly animates phenomena against the front display glass:
 * - Tsunami crashing directly against the screen glass with water droplets
 * - Fire inferno licking and burning up the display glass with embers
 * - Earthquake with screen-glass shatter fissures and randomized magnitude
 * - Volcano eruption shooting molten magma bombs straight out of the screen
 * - Tornado vortex spinning debris across the front display
 * - Flood rising to submerge the screen
 * - Mudslide & boulder impacts with mud splatters
 * - Extreme drought heatwave refraction shimmer
 * - Coastal surge & abrasi cliff erosion
 * - Climate anomaly split-screen & global warning
 * - Social conflict police sirens & caution barricade
 */
@Composable
fun DisasterVisualAnimation(
    disaster: DisasterType,
    onAnimationFinished: () -> Unit,
    onStartResponse: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
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

    // Cancel active vibration when leaving the screen or dialog
    DisposableEffect(Unit) {
        onDispose {
            try {
                vibrator?.cancel()
            } catch (_: Exception) {}
        }
    }

    val random = remember { Random() }
    var shakeX by remember { mutableFloatStateOf(0f) }
    var shakeY by remember { mutableFloatStateOf(0f) }
    var rotationShake by remember { mutableFloatStateOf(0f) }
    var animationProgress by remember(disaster) { mutableFloatStateOf(1f) }
    var remainingSeconds by remember(disaster) { mutableIntStateOf(5) }

    // Synchronized Haptic Feedback loop tailored to the disaster impact against the screen
    LaunchedEffect(disaster) {
        val vib = vibrator ?: return@LaunchedEffect
        // Initial sudden shockwave impact pulse
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(VibrationEffect.createOneShot(220, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vib.vibrate(220)
            }
        } catch (_: Exception) {}

        // Continuous rhythmic impact haptics
        while (isActive) {
            try {
                when (disaster) {
                    DisasterType.GEMPA_BUMI -> {
                        // Sharp erratic tectonic shocks matching fault cracks
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val duration = (60L + random.nextInt(90))
                            val amplitude = (190 + random.nextInt(65)).coerceIn(1, 255)
                            vib.vibrate(VibrationEffect.createOneShot(duration, amplitude))
                        } else {
                            @Suppress("DEPRECATION")
                            vib.vibrate(80)
                        }
                        delay(120L + random.nextInt(100))
                    }
                    DisasterType.TSUNAMI -> {
                        // Swelling surge crescendo and crashing wave slam against screen
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vib.vibrate(
                                VibrationEffect.createWaveform(
                                    longArrayOf(0, 90, 40, 180),
                                    intArrayOf(0, 150, 0, 255),
                                    -1
                                )
                            )
                        } else {
                            @Suppress("DEPRECATION")
                            vib.vibrate(longArrayOf(0, 90, 40, 180), -1)
                        }
                        delay(380L)
                    }
                    DisasterType.GUNUNG_BERAPI -> {
                        // Molten magma bomb explosion impact against glass
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vib.vibrate(VibrationEffect.createOneShot(170L, 255))
                        } else {
                            @Suppress("DEPRECATION")
                            vib.vibrate(170L)
                        }
                        delay(270L)
                    }
                    DisasterType.KEBAKARAN -> {
                        // Crackling fire bursts licking the display
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vib.vibrate(VibrationEffect.createOneShot(55L, 190))
                        } else {
                            @Suppress("DEPRECATION")
                            vib.vibrate(55L)
                        }
                        delay(130L)
                    }
                    DisasterType.ANGIN_TOPAN -> {
                        // Swirling centrifugal vortex oscillation
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vib.vibrate(
                                VibrationEffect.createWaveform(
                                    longArrayOf(0, 50, 40, 80),
                                    intArrayOf(0, 130, 0, 220),
                                    -1
                                )
                            )
                        } else {
                            @Suppress("DEPRECATION")
                            vib.vibrate(longArrayOf(0, 50, 40, 80), -1)
                        }
                        delay(240L)
                    }
                    DisasterType.TANAH_LONGSOR -> {
                        // Heavy boulder impacts and mudslide thuds
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vib.vibrate(VibrationEffect.createOneShot(130L, 245))
                        } else {
                            @Suppress("DEPRECATION")
                            vib.vibrate(130L)
                        }
                        delay(290L)
                    }
                    DisasterType.BANJIR, DisasterType.ABRASI -> {
                        // Surging undertow and water wall collision
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vib.vibrate(
                                VibrationEffect.createWaveform(
                                    longArrayOf(0, 60, 50, 130),
                                    intArrayOf(0, 110, 0, 210),
                                    -1
                                )
                            )
                        } else {
                            @Suppress("DEPRECATION")
                            vib.vibrate(longArrayOf(0, 60, 50, 130), -1)
                        }
                        delay(330L)
                    }
                    DisasterType.KONFLIK_SOSIAL -> {
                        // Staccato emergency alert strobe pulses
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vib.vibrate(VibrationEffect.createOneShot(70L, 230))
                        } else {
                            @Suppress("DEPRECATION")
                            vib.vibrate(70L)
                        }
                        delay(170L)
                    }
                    DisasterType.KEKERINGAN, DisasterType.PERUBAHAN_IKLIM -> {
                        // Deep urgent heartbeat pulses
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vib.vibrate(
                                VibrationEffect.createWaveform(
                                    longArrayOf(0, 80, 80, 80),
                                    intArrayOf(0, 160, 0, 210),
                                    -1
                                )
                            )
                        } else {
                            @Suppress("DEPRECATION")
                            vib.vibrate(longArrayOf(0, 80, 80, 80), -1)
                        }
                        delay(490L)
                    }
                }
            } catch (_: Exception) {
                delay(200)
            }
        }
    }

    // Randomized earthquake magnitude for each earthquake occurrence
    val randomMagnitude = remember(disaster) {
        if (disaster == DisasterType.GEMPA_BUMI) DisasterType.getRandomMagnitude() else 7.2
    }
    val randomDepth = remember(disaster) {
        if (disaster == DisasterType.GEMPA_BUMI) 10 + random.nextInt(45) else 15
    }

    // Dynamic alert title
    val displayAlertTitle = remember(disaster, randomMagnitude) {
        if (disaster == DisasterType.GEMPA_BUMI) {
            "GEMPA BUMI %.1f SR! (KEDALAMAN %d KM)".format(randomMagnitude, randomDepth)
        } else {
            disaster.defaultAlertTitle
        }
    }

    // 4.5-second countdown timer for auto-transition to What To Do modal
    LaunchedEffect(disaster) {
        val durationMs = 4500L
        val startTime = System.currentTimeMillis()
        while (isActive) {
            val elapsed = System.currentTimeMillis() - startTime
            val remaining = (durationMs - elapsed).coerceAtLeast(0)
            animationProgress = remaining.toFloat() / durationMs
            remainingSeconds = ((remaining + 999) / 1000).toInt()
            if (remaining <= 0) {
                onAnimationFinished()
                break
            }
            delay(40)
        }
    }

    // Screen tremor physics
    LaunchedEffect(disaster) {
        val intensity = when (disaster) {
            DisasterType.GEMPA_BUMI -> 28f
            DisasterType.GUNUNG_BERAPI -> 22f
            DisasterType.TSUNAMI -> 18f
            DisasterType.TANAH_LONGSOR -> 20f
            DisasterType.ANGIN_TOPAN -> 16f
            DisasterType.KEBAKARAN -> 10f
            DisasterType.ABRASI -> 12f
            DisasterType.BANJIR -> 8f
            else -> 6f
        }
        while (isActive) {
            shakeX = (random.nextFloat() * 2f - 1f) * intensity
            shakeY = (random.nextFloat() * 2f - 1f) * intensity
            rotationShake = (random.nextFloat() * 2f - 1f) * (intensity * 0.08f)
            delay(28)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "disaster_fullscreen_loop")
    val pulseBeacon by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_beacon"
    )

    val timePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time_phase"
    )

    val flameRise by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(750, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "flame_rise"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("fullscreen_disaster_screen")
    ) {
        // Direct-on-Screen Dynamic Canvas
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    translationX = shakeX
                    translationY = shakeY
                    rotationZ = rotationShake
                }
        ) {
            when (disaster) {
                DisasterType.TSUNAMI -> {
                    drawDirectTsunamiWave(timePhase)
                }
                DisasterType.KEBAKARAN -> {
                    drawDirectInfernoFlames(flameRise, timePhase)
                }
                DisasterType.GEMPA_BUMI -> {
                    drawDirectEarthquakeShatter(timePhase)
                }
                DisasterType.GUNUNG_BERAPI -> {
                    drawDirectVolcanoEruption(timePhase)
                }
                DisasterType.ANGIN_TOPAN -> {
                    drawDirectTornadoVortex(timePhase)
                }
                DisasterType.BANJIR -> {
                    drawDirectFloodRising(timePhase)
                }
                DisasterType.TANAH_LONGSOR -> {
                    drawDirectMudslideImpact(timePhase)
                }
                DisasterType.KEKERINGAN -> {
                    drawDirectDroughtHeatwave(timePhase)
                }
                DisasterType.ABRASI -> {
                    drawDirectCoastalAbrasion(timePhase)
                }
                DisasterType.PERUBAHAN_IKLIM -> {
                    drawDirectClimateAnomaly(timePhase)
                }
                DisasterType.KONFLIK_SOSIAL -> {
                    drawDirectConflictStrobes(pulseBeacon, timePhase)
                }
            }
        }

        // Screen Edge Emergency Warning Glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            disaster.primaryColor.copy(alpha = 0.25f * pulseBeacon),
                            disaster.primaryColor.copy(alpha = 0.65f * pulseBeacon)
                        )
                    )
                )
        )

        // Top-Layer HUD: Emergency Alert & Countdown Bar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Emergency Header Card (constrained for tablet width)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A).copy(alpha = 0.92f)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, disaster.secondaryColor.copy(alpha = 0.8f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = disaster.primaryColor.copy(alpha = pulseBeacon),
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(text = disaster.emoji, fontSize = 20.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = displayAlertTitle,
                                    color = Color(0xFFFBBF24),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = disaster.emergencySubtitle,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    maxLines = 1
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Tutup",
                                tint = Color.LightGray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Countdown progress bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Simulasi bencana selesai dalam:",
                            color = Color(0xFFCBD5E1),
                            fontSize = 11.sp
                        )
                        Text(
                            text = "${remainingSeconds}s",
                            color = disaster.secondaryColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { animationProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = disaster.secondaryColor,
                        trackColor = Color(0xFF334155)
                    )
                }
            }

            // Bottom CTA Floating Action Bar (Responsive Tablet MaxWidth)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
            ) {
                // Primary Action Button: Visual Safety Guidelines Simulation
                Button(
                    onClick = onAnimationFinished,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("what_to_do_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = disaster.primaryColor),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LIHAT SIMULASI PANDUAN KESELAMATAN",
                        color = Color.White,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Secondary Action: Jump straight to reaction quiz
                OutlinedButton(
                    onClick = onStartResponse,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("action_respond_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color(0xFF0F172A).copy(alpha = 0.85f),
                        contentColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color.White.copy(alpha = 0.7f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = "Tanggap",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Langsung Uji Kuis Tanggap",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = disaster.soundDescription,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// ---------------------------------------------------------
// DIRECT-ON-SCREEN GRAPHIC DRAWING IMPLEMENTATIONS
// ---------------------------------------------------------

/**
 * Giant Tsunami wave crashing directly into the viewer's screen glass,
 * with foaming sea spray and water droplets running down.
 */
private fun DrawScope.drawDirectTsunamiWave(phase: Float) {
    val w = size.width
    val h = size.height

    // Deep ocean dark base
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF021B29), Color(0xFF03445C), Color(0xFF007A99))
        )
    )

    // Colossal crashing wave wall rising up from center to front
    val wavePath = Path().apply {
        moveTo(0f, h)
        lineTo(0f, h * 0.35f)
        var x = 0f
        while (x <= w) {
            val yOffset = sin((x / w * 4 * PI + phase).toDouble()).toFloat() * (h * 0.08f)
            lineTo(x, h * 0.35f + yOffset)
            x += w * 0.05f
        }
        lineTo(w, h)
        close()
    }
    drawPath(
        path = wavePath,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF00ACC1), Color(0xFF006064), Color(0xFF002F38))
        )
    )

    // Giant curling wave crest spilling forward toward the viewer
    val crestY = h * 0.38f + sin(phase.toDouble()).toFloat() * 20f
    drawCircle(
        color = Color.White.copy(alpha = 0.85f),
        radius = w * 0.55f,
        center = Offset(w * 0.5f, crestY),
        style = Stroke(width = 24f)
    )

    // Foaming ocean splash spray hitting the screen
    for (i in 0 until 40) {
        val sx = (w * ((i * 37) % 100) / 100f)
        val sy = (h * 0.25f) + ((i * 23) % 250) + sin((phase + i).toDouble()).toFloat() * 30f
        val rad = (i % 6 + 4).toFloat()
        drawCircle(
            color = Color.White.copy(alpha = 0.7f),
            radius = rad,
            center = Offset(sx, sy)
        )
    }

    // Water droplet streaks running down the front glass
    for (d in 0 until 16) {
        val dx = (w * ((d * 61) % 100) / 100f)
        val dyStart = (h * ((d * 47) % 70) / 100f)
        val dropLen = 50f + (d % 4) * 25f
        drawLine(
            color = Color(0x99B2EBF2),
            start = Offset(dx, dyStart),
            end = Offset(dx, dyStart + dropLen),
            strokeWidth = 3.5f
        )
        drawCircle(
            color = Color(0xCCB2EBF2),
            radius = 4.5f,
            center = Offset(dx, dyStart + dropLen)
        )
    }
}

/**
 * Ultra-realistic fiery inferno simulation:
 * - Billowing turbulent dark smoke & soot rolling upward
 * - Multi-layer fluid flames: Deep crimson outer tongues, fierce cadmium orange mid-fire, white-hot core
 * - Convection-driven glowing embers (bara api) with physical drift, heat shimmering, and spark halos
 */
private fun DrawScope.drawDirectInfernoFlames(rise: Float, phase: Float) {
    val w = size.width
    val h = size.height

    // 1. Deep Charred Thermal Background with pulsating heat glow
    val heatPulse = (0.75f + 0.25f * sin((phase * 2.5f).toDouble()).toFloat())
    drawRect(Color(0xFF0C0300))
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFF991B1B).copy(alpha = 0.5f * heatPulse),
                Color(0xFF450A0A).copy(alpha = 0.35f),
                Color.Transparent
            ),
            center = Offset(w * 0.5f, h * 0.85f),
            radius = w * 0.85f
        )
    )

    // 2. Billowing Volumetric Dark Smoke Plumes (rising at top 45%)
    val smokeCount = 14
    for (s in 0 until smokeCount) {
        val sProgress = ((s * 0.07f) + (rise * 0.5f)) % 1f
        val sx = w * ((s * 41) % 100) / 100f + sin((sProgress * 2 * PI + s).toDouble()).toFloat() * 45f
        val sy = h * (0.55f - sProgress * 0.6f)
        val sRadius = 45f + sProgress * 75f
        val sAlpha = ((1f - sProgress) * 0.38f).coerceIn(0f, 0.45f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF1C0A04).copy(alpha = sAlpha), Color(0xFF0F0502).copy(alpha = sAlpha * 0.5f), Color.Transparent),
                center = Offset(sx, sy),
                radius = sRadius
            ),
            radius = sRadius,
            center = Offset(sx, sy)
        )
    }

    // 3. Layer 1: Outer Heavy Crimson Fire Wall (Broad, sweeping waves)
    val outerTongues = 12
    val crimsonPath = Path().apply {
        moveTo(0f, h)
        for (i in 0..outerTongues) {
            val cx = w * i / outerTongues
            val waveHeight = h * (0.58f + 0.22f * sin((phase * 1.8f + i * 1.3f).toDouble()).toFloat())
            val cpx = cx + cos((phase * 2f + i).toDouble()).toFloat() * 30f
            cubicTo(
                cpx - (w / outerTongues * 0.5f), h - waveHeight * 0.65f,
                cpx, h - waveHeight,
                cx, h - waveHeight * 0.85f
            )
        }
        lineTo(w, h)
        close()
    }
    drawPath(
        path = crimsonPath,
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFDC2626), Color(0xFF991B1B), Color(0xFF450A0A))
        )
    )

    // 4. Layer 2: Main Roaring Orange & Amber Flame Tongues (Dynamic licking shapes)
    val midTongues = 10
    for (t in 0 until midTongues) {
        val baseX = w * (t + 0.5f) / midTongues
        val flameHeight = h * (0.50f + 0.32f * sin((phase * 2.8f + t * 1.7f).toDouble()).toFloat())
        val tipWobble = sin((phase * 3.5f + t * 2.1f).toDouble()).toFloat() * 35f
        val midFlame = Path().apply {
            moveTo(baseX - (w / midTongues * 0.75f), h)
            cubicTo(
                baseX - (w / midTongues * 0.35f), h - flameHeight * 0.45f,
                baseX + tipWobble * 0.6f, h - flameHeight * 0.85f,
                baseX + tipWobble, h - flameHeight
            )
            cubicTo(
                baseX + tipWobble * 1.2f, h - flameHeight * 0.82f,
                baseX + (w / midTongues * 0.35f), h - flameHeight * 0.45f,
                baseX + (w / midTongues * 0.75f), h
            )
            close()
        }
        drawPath(
            path = midFlame,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFBBF24), Color(0xFFF97316), Color(0xFFEA580C), Color(0xFFB91C1C))
            )
        )
    }

    // 5. Layer 3: Ultra-Hot White & Golden Yellow Combustion Core (At the base & inner cores)
    val coreTongues = 8
    for (c in 0 until coreTongues) {
        val cx = w * (c + 0.5f) / coreTongues
        val coreHeight = h * (0.28f + 0.18f * cos((phase * 3.2f + c * 2.4f).toDouble()).toFloat())
        val coreTip = sin((phase * 4.2f + c).toDouble()).toFloat() * 20f
        val corePath = Path().apply {
            moveTo(cx - (w / coreTongues * 0.45f), h)
            cubicTo(
                cx - (w / coreTongues * 0.2f), h - coreHeight * 0.5f,
                cx + coreTip * 0.7f, h - coreHeight * 0.9f,
                cx + coreTip, h - coreHeight
            )
            cubicTo(
                cx + coreTip * 1.1f, h - coreHeight * 0.85f,
                cx + (w / coreTongues * 0.2f), h - coreHeight * 0.5f,
                cx + (w / coreTongues * 0.45f), h
            )
            close()
        }
        drawPath(
            path = corePath,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFFFFFF), Color(0xFFFEF08A), Color(0xFFFBBF24), Color(0xFFF97316))
            )
        )
    }

    // 6. Base Incandescent Furnace Baseline
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color(0xFFFDE047).copy(alpha = 0.85f), Color(0xFFFFFFFF))
        ),
        topLeft = Offset(0f, h * 0.86f),
        size = Size(w, h * 0.14f)
    )

    // 7. Realistic Ascending Embers & Glowing Cinders (Bara Api with convection drift)
    for (e in 0 until 55) {
        val speedMultiplier = 0.8f + ((e % 7) * 0.15f)
        val progress = ((e * 0.055f) + (rise * speedMultiplier)) % 1f
        val lateralDrift = sin((progress * 3 * PI + e * 1.5f).toDouble()).toFloat() * (25f + (1f - progress) * 20f)
        val ex = (w * ((e * 37) % 100) / 100f + lateralDrift).coerceIn(0f, w)
        val ey = h * (1f - progress)
        val emberSize = 2f + (1f - progress) * 5.5f
        val emberColor = when (e % 4) {
            0 -> Color(0xFFFFFBEB) // White-hot
            1 -> Color(0xFFFDE047) // Bright gold
            2 -> Color(0xFFF97316) // Blazing orange
            else -> Color(0xFFEF4444) // Ruby ember
        }

        // Ember Glow halo
        drawCircle(
            color = emberColor.copy(alpha = (1f - progress) * 0.4f),
            radius = emberSize * 2.4f,
            center = Offset(ex, ey)
        )
        // Core cinder particle
        drawCircle(
            color = emberColor.copy(alpha = (1f - progress * 0.6f)),
            radius = emberSize,
            center = Offset(ex, ey)
        )
    }
}

/**
 * Screen-glass crack fissures spreading violently across the display,
 * with flying fractured glass shards and rubble.
 */
private fun DrawScope.drawDirectEarthquakeShatter(phase: Float) {
    val w = size.width
    val h = size.height

    // Deep tectonic earth fissure background
    drawRect(Color(0xFF1E1714))

    // Jagged earth fault crack down the center
    val faultPath = Path().apply {
        moveTo(w * 0.5f, 0f)
        lineTo(w * 0.48f, h * 0.2f)
        lineTo(w * 0.54f, h * 0.4f)
        lineTo(w * 0.46f, h * 0.65f)
        lineTo(w * 0.52f, h * 0.85f)
        lineTo(w * 0.5f, h)
    }
    drawPath(
        path = faultPath,
        color = Color(0xFFFF6F00),
        style = Stroke(width = 8f)
    )

    // Glass shatter spider-web branching across the entire front glass
    val impactCenter = Offset(w * 0.5f, h * 0.45f)
    val crackLines = 10
    for (c in 0 until crackLines) {
        val angle = (c * (360.0 / crackLines) + sin(phase.toDouble()) * 5f)
        val rad = Math.toRadians(angle)
        val p1 = Offset(
            (impactCenter.x + w * 0.2f * cos(rad)).toFloat(),
            (impactCenter.y + w * 0.2f * sin(rad)).toFloat()
        )
        val p2 = Offset(
            (impactCenter.x + w * 0.45f * cos(rad + 0.1)).toFloat(),
            (impactCenter.y + w * 0.45f * sin(rad + 0.1)).toFloat()
        )
        drawLine(
            color = Color.White.copy(alpha = 0.85f),
            start = impactCenter,
            end = p1,
            strokeWidth = 3f
        )
        drawLine(
            color = Color.White.copy(alpha = 0.65f),
            start = p1,
            end = p2,
            strokeWidth = 2f
        )
    }

    // Fractured concentric glass rings
    for (r in 1..4) {
        drawCircle(
            color = Color.White.copy(alpha = 0.4f),
            radius = (r * 60f),
            center = impactCenter,
            style = Stroke(width = 1.5f)
        )
    }

    // Flying debris chunks
    for (i in 0 until 18) {
        val dx = (impactCenter.x + cos(i.toDouble()) * (i * 22f)).toFloat()
        val dy = (impactCenter.y + sin(i.toDouble()) * (i * 28f)).toFloat()
        drawCircle(
            color = Color(0xFFFFB300),
            radius = 6f,
            center = Offset(dx, dy)
        )
    }
}

/**
 * Volcanic eruption shooting molten magma bombs straight out of the screen,
 * with dense billowing ash plumes and fiery glow.
 */
private fun DrawScope.drawDirectVolcanoEruption(phase: Float) {
    val w = size.width
    val h = size.height

    // Volcanic ash sky
    drawRect(Color(0xFF26110D))

    // Volcano cone at the bottom
    val volcanoPath = Path().apply {
        moveTo(0f, h)
        lineTo(w * 0.25f, h * 0.65f)
        lineTo(w * 0.75f, h * 0.65f)
        lineTo(w, h)
        close()
    }
    drawPath(volcanoPath, color = Color(0xFF3E2723))

    // Magma caldera rim
    drawOval(
        color = Color(0xFFFF3D00),
        topLeft = Offset(w * 0.25f, h * 0.62f),
        size = Size(w * 0.5f, 35f)
    )

    // Billowing ash clouds billowing forward
    for (b in 0 until 12) {
        val bx = w * 0.5f + cos((phase + b).toDouble()).toFloat() * (w * 0.3f)
        val by = h * 0.45f - (b * 22f)
        val cloudRadius = 60f + b * 10f
        drawCircle(
            color = Color(0xFF5D4037).copy(alpha = 0.55f),
            radius = cloudRadius,
            center = Offset(bx, by)
        )
    }

    // Molten magma bombs shooting toward the viewer's face
    for (m in 0 until 14) {
        val angle = Math.toRadians((m * 25.0) - 150.0)
        val dist = (h * 0.45f) * ((sin((phase + m).toDouble()) + 1.2f) / 2.2f)
        val mx = (w * 0.5f + dist * cos(angle)).toFloat()
        val my = (h * 0.62f + dist * sin(angle)).toFloat()
        drawCircle(
            color = Color(0xFFFFEA00),
            radius = 12f,
            center = Offset(mx, my)
        )
        drawCircle(
            color = Color(0xFFFF3D00),
            radius = 18f,
            center = Offset(mx, my),
            style = Stroke(width = 4f)
        )
    }
}

/**
 * Massive 3D spinning tornado funnel vortex twisting across the front display.
 */
private fun DrawScope.drawDirectTornadoVortex(phase: Float) {
    val w = size.width
    val h = size.height

    // Stormy dark sky
    drawRect(Color(0xFF0F172A))

    // Spiral vortex rings expanding from bottom to top
    val rings = 18
    for (r in 0 until rings) {
        val progress = r.toFloat() / rings
        val ry = h * (0.85f - progress * 0.7f)
        val rx = w * 0.5f + sin((phase * 2f + r).toDouble()).toFloat() * (w * 0.12f)
        val rw = (w * 0.15f) + progress * (w * 0.65f)
        val rh = 16f + progress * 24f

        drawOval(
            color = Color(0xFF94A3B8).copy(alpha = 0.4f + progress * 0.35f),
            topLeft = Offset(rx - rw / 2f, ry - rh / 2f),
            size = Size(rw, rh),
            style = Stroke(width = 4f)
        )
    }

    // Whirling debris particles spiraling around the vortex
    for (d in 0 until 35) {
        val angle = (d * 35.0 + Math.toDegrees(phase.toDouble() * 3.0))
        val rad = Math.toRadians(angle)
        val radiusDist = (w * 0.35f) * sin(d.toDouble()).toFloat()
        val px = (w * 0.5f + radiusDist * cos(rad)).toFloat()
        val py = h * 0.5f + (sin((phase + d).toDouble()).toFloat() * (h * 0.3f))

        drawRect(
            color = if (d % 2 == 0) Color(0xFFFBBF24) else Color(0xFFE2E8F0),
            topLeft = Offset(px, py),
            size = Size(8f, 5f)
        )
    }
}

/**
 * Flood water rushing from bottom to submerge the entire screen.
 */
private fun DrawScope.drawDirectFloodRising(phase: Float) {
    val w = size.width
    val h = size.height

    // Murky flood water base
    drawRect(Color(0xFF0C243B))

    // Wave 1
    val wave1 = Path().apply {
        moveTo(0f, h)
        lineTo(0f, h * 0.4f)
        var x = 0f
        while (x <= w) {
            val y = h * 0.4f + sin((x / w * 3 * PI + phase).toDouble()).toFloat() * 25f
            lineTo(x, y)
            x += w * 0.05f
        }
        lineTo(w, h)
        close()
    }
    drawPath(wave1, color = Color(0xFF0288D1).copy(alpha = 0.85f))

    // Wave 2 foaming surface
    val wave2 = Path().apply {
        moveTo(0f, h)
        lineTo(0f, h * 0.45f)
        var x = 0f
        while (x <= w) {
            val y = h * 0.45f + cos((x / w * 4 * PI + phase * 1.5f).toDouble()).toFloat() * 18f
            lineTo(x, y)
            x += w * 0.05f
        }
        lineTo(w, h)
        close()
    }
    drawPath(wave2, color = Color(0xFF4FC3F7).copy(alpha = 0.75f))

    // Floating emergency debris on water
    for (i in 0 until 12) {
        val fx = (w * ((i * 43) % 100) / 100f)
        val fy = h * 0.48f + sin((phase + i).toDouble()).toFloat() * 12f
        drawCircle(
            color = Color(0xFFFFD54F),
            radius = 8f,
            center = Offset(fx, fy)
        )
    }
}

/**
 * Mudslide avalanche crashing directly down with mud splatters on the screen.
 */
private fun DrawScope.drawDirectMudslideImpact(phase: Float) {
    val w = size.width
    val h = size.height

    drawRect(Color(0xFF271A12))

    // Mud cascade flowing down
    val mudPath = Path().apply {
        moveTo(0f, h)
        lineTo(0f, h * 0.3f)
        lineTo(w * 0.35f, h * 0.5f + sin(phase.toDouble()).toFloat() * 20f)
        lineTo(w * 0.7f, h * 0.35f)
        lineTo(w, h * 0.45f)
        lineTo(w, h)
        close()
    }
    drawPath(mudPath, color = Color(0xFF5D4037))

    // Heavy boulders tumbling down
    for (b in 0 until 8) {
        val bx = w * ((b * 33) % 90 + 5) / 100f
        val by = h * 0.4f + (b * 45f)
        drawCircle(
            color = Color(0xFF3E2723),
            radius = 26f,
            center = Offset(bx, by)
        )
    }

    // Mud splatters directly on the screen glass
    for (s in 0 until 15) {
        val sx = w * ((s * 51) % 95 + 2) / 100f
        val sy = h * ((s * 37) % 80 + 10) / 100f
        drawCircle(
            color = Color(0xFF795548).copy(alpha = 0.85f),
            radius = (10 + (s % 5) * 6).toFloat(),
            center = Offset(sx, sy)
        )
    }
}

/**
 * Blinding heatwave refraction distortion and arid cracked soil.
 */
private fun DrawScope.drawDirectDroughtHeatwave(phase: Float) {
    val w = size.width
    val h = size.height

    // Scorching amber sky
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFFF8F00), Color(0xFFFFB300), Color(0xFFFFE082))
        )
    )

    // Giant blazing sun in the center
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.White, Color(0xFFFFF176), Color(0xFFFFB300), Color.Transparent)
        ),
        radius = w * 0.45f,
        center = Offset(w * 0.5f, h * 0.35f)
    )

    // Shimmering horizontal heat waves
    for (wave in 0 until 10) {
        val wy = h * 0.45f + (wave * 35f)
        val path = Path().apply {
            moveTo(0f, wy)
            var x = 0f
            while (x <= w) {
                val offset = sin((x / w * 6 * PI + phase * 2f + wave).toDouble()).toFloat() * 10f
                lineTo(x, wy + offset)
                x += 20f
            }
        }
        drawPath(
            path = path,
            color = Color.White.copy(alpha = 0.35f),
            style = Stroke(width = 3f)
        )
    }

    // Parched cracked earth at the bottom
    drawRect(
        color = Color(0xFF8D6E63),
        topLeft = Offset(0f, h * 0.7f),
        size = Size(w, h * 0.3f)
    )
    for (c in 0 until 12) {
        val cx = w * (c + 0.5f) / 12f
        drawLine(
            color = Color(0xFF3E2723),
            start = Offset(cx, h * 0.7f),
            end = Offset(cx + sin(c.toDouble()).toFloat() * 25f, h),
            strokeWidth = 3f
        )
    }
}

/**
 * Coastal waves pounding and breaking coastal cliffs (Abrasi).
 */
private fun DrawScope.drawDirectCoastalAbrasion(phase: Float) {
    val w = size.width
    val h = size.height

    drawRect(Color(0xFF00363A))

    // Wave impact wall
    val wavePath = Path().apply {
        moveTo(0f, h)
        lineTo(0f, h * 0.5f)
        var x = 0f
        while (x <= w * 0.7f) {
            val y = h * 0.5f + sin((x / w * 4 * PI + phase).toDouble()).toFloat() * 30f
            lineTo(x, y)
            x += w * 0.05f
        }
        lineTo(w * 0.7f, h)
        close()
    }
    drawPath(wavePath, color = Color(0xFF00838F))

    // Collapsing cliff on right side
    val cliffPath = Path().apply {
        moveTo(w * 0.65f, 0f)
        lineTo(w, 0f)
        lineTo(w, h)
        lineTo(w * 0.55f, h)
        lineTo(w * 0.65f, h * 0.5f)
        close()
    }
    drawPath(cliffPath, color = Color(0xFF5D4037))

    // Salt spray splashes
    for (s in 0 until 20) {
        val sx = w * 0.6f + sin((phase + s).toDouble()).toFloat() * 50f
        val sy = h * 0.5f - (s * 15f)
        drawCircle(
            color = Color.White.copy(alpha = 0.75f),
            radius = 5f,
            center = Offset(sx, sy)
        )
    }
}

/**
 * Split-screen global climate anomaly: Arctic melting vs desert drought.
 */
private fun DrawScope.drawDirectClimateAnomaly(phase: Float) {
    val w = size.width
    val h = size.height

    // Left half: Arctic ice melting into rising water
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFE0F7FA), Color(0xFF006064))
        ),
        topLeft = Offset(0f, 0f),
        size = Size(w * 0.5f, h)
    )

    // Right half: Scorching scorched earth
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFFF5722), Color(0xFF3E2723))
        ),
        topLeft = Offset(w * 0.5f, 0f),
        size = Size(w * 0.5f, h)
    )

    // Center divider with temperature warning needle
    drawLine(
        color = Color(0xFFFBBF24),
        start = Offset(w * 0.5f, 0f),
        end = Offset(w * 0.5f, h),
        strokeWidth = 5f
    )

    // Global thermometer bar
    val tempY = h * 0.5f + sin(phase.toDouble()).toFloat() * 40f
    drawCircle(
        color = Color(0xFFD50000),
        radius = 18f,
        center = Offset(w * 0.5f, tempY)
    )
}

/**
 * Emergency police strobe beacons and caution barricade (Konflik Sosial).
 */
private fun DrawScope.drawDirectConflictStrobes(pulse: Float, phase: Float) {
    val w = size.width
    val h = size.height

    drawRect(Color(0xFF0F172A))

    // Alternating blue & red emergency flashing beacon halos
    val isRed = (phase % (2 * PI)) < PI
    drawRect(
        brush = Brush.radialGradient(
            colors = if (isRed) {
                listOf(Color(0xFFDC2626).copy(alpha = 0.6f * pulse), Color.Transparent)
            } else {
                listOf(Color(0xFF2563EB).copy(alpha = 0.6f * pulse), Color.Transparent)
            },
            radius = w * 0.8f,
            center = Offset(if (isRed) w * 0.25f else w * 0.75f, h * 0.45f)
        )
    )

    // Diagonal caution tape stripes across screen
    val tapeY = h * 0.48f
    drawRect(
        color = Color(0xFFFBBF24),
        topLeft = Offset(0f, tapeY - 25f),
        size = Size(w, 50f)
    )
    val stripeCount = 20
    for (s in 0 until stripeCount) {
        val sx = s * (w / stripeCount)
        drawLine(
            color = Color.Black,
            start = Offset(sx, tapeY - 25f),
            end = Offset(sx + 20f, tapeY + 25f),
            strokeWidth = 8f
        )
    }
}
