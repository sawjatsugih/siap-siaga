package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DisasterType
import kotlinx.coroutines.isActive
import java.util.Random
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Dedicated frame-based animation composable for each disaster type.
 * Advances animation frames continuously at 60 FPS using withFrameNanos.
 */
@Composable
fun DisasterFrameAnimation(
    disaster: DisasterType,
    modifier: Modifier = Modifier
) {
    var currentFrame by remember(disaster) { mutableLongStateOf(0L) }
    val random = remember { Random() }

    // Drive 60 FPS frame updates synchronized with the display frame clock
    LaunchedEffect(disaster) {
        currentFrame = 0L
        while (isActive) {
            withFrameNanos {
                currentFrame++
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("frame_animation_${disaster.id}")
    ) {
        // Frame-based Canvas rendering tailored to the active disaster
        when (disaster) {
            DisasterType.GEMPA_BUMI -> EarthquakeFrameCanvas(currentFrame)
            DisasterType.BANJIR -> FloodFrameCanvas(currentFrame)
            DisasterType.TANAH_LONGSOR -> LandslideFrameCanvas(currentFrame)
            DisasterType.KEBAKARAN -> FireFrameCanvas(currentFrame)
            else -> EarthquakeFrameCanvas(currentFrame)
        }

        // Frame status HUD
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 80.dp, start = 16.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color.Black.copy(alpha = 0.7f),
            border = androidx.compose.foundation.BorderStroke(1.dp, disaster.secondaryColor.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.Red,
                    modifier = Modifier.size(8.dp)
                ) {}
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "FRAME ${currentFrame % 9999} | 60 FPS",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

/**
 * Frame-by-frame Earthquake Simulation:
 * - Dynamic tectonic crack propagation across frames
 * - Expanding seismic shockwave circles from the fault line
 * - Shaking ground strata & multi-body tumbling debris
 */
@Composable
private fun EarthquakeFrameCanvas(frame: Long) {
    val random = remember { Random() }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Dark volcanic earth background
        drawRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFF1F0906), Color(0xFF100301), Color(0xFF280702))
            )
        )

        // Frame-dependent ground jitter
        val jitterX = (sin(frame * 0.9) * 14.0 + cos(frame * 1.7) * 8.0).toFloat()
        val jitterY = (cos(frame * 1.1) * 12.0 + sin(frame * 2.3) * 6.0).toFloat()

        // Expanding seismic shockwaves (rings emanating from center)
        val waveCycle = (frame % 75) / 75f
        val shockRadius = waveCycle * (w * 0.75f)
        val shockAlpha = (1f - waveCycle).coerceIn(0f, 1f)
        drawCircle(
            color = Color(0xFFFF5722).copy(alpha = shockAlpha * 0.6f),
            radius = shockRadius,
            center = Offset(w * 0.5f + jitterX, h * 0.55f + jitterY),
            style = Stroke(width = 8f * (1f - waveCycle))
        )
        val waveCycle2 = ((frame + 37) % 75) / 75f
        drawCircle(
            color = Color(0xFFFFEB3B).copy(alpha = (1f - waveCycle2) * 0.5f),
            radius = waveCycle2 * (w * 0.65f),
            center = Offset(w * 0.5f + jitterX, h * 0.55f + jitterY),
            style = Stroke(width = 5f)
        )

        // Dynamic tectonic fissure path generated per frame
        val crack = Path().apply {
            moveTo(w * 0.12f + jitterX, h * 0.92f + jitterY)
            lineTo(w * 0.28f + jitterX, h * 0.78f + jitterY)
            lineTo(w * 0.22f + jitterX, h * 0.66f + jitterY)
            lineTo(w * 0.45f + jitterX, h * 0.54f + jitterY)
            lineTo(w * 0.38f + jitterX, h * 0.42f + jitterY)
            lineTo(w * 0.62f + jitterX, h * 0.30f + jitterY)
            lineTo(w * 0.82f + jitterX, h * 0.16f + jitterY)
        }
        val subCrack = Path().apply {
            moveTo(w * 0.45f + jitterX, h * 0.54f + jitterY)
            lineTo(w * 0.65f + jitterX, h * 0.62f + jitterY)
            lineTo(w * 0.75f + jitterX, h * 0.58f + jitterY)
            lineTo(w * 0.88f + jitterX, h * 0.72f + jitterY)
        }

        // Deep glowing magma fracture
        drawPath(
            path = crack,
            color = Color(0xFFD84315),
            style = Stroke(width = 16f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        drawPath(
            path = crack,
            color = Color(0xFFFF9800),
            style = Stroke(width = 8f, cap = StrokeCap.Round)
        )
        drawPath(
            path = crack,
            color = Color(0xFFFFF59D),
            style = Stroke(width = 3f)
        )
        drawPath(
            path = subCrack,
            color = Color(0xFFFF7043),
            style = Stroke(width = 10f, cap = StrokeCap.Round)
        )
        drawPath(
            path = subCrack,
            color = Color(0xFFFFE082),
            style = Stroke(width = 4f)
        )

        // Tumbling structural debris falling down across frames
        val debrisCount = 30
        for (i in 0 until debrisCount) {
            val speed = 3.5f + (i % 6) * 1.2f
            val startX = (i * 37f) % w
            val curY = ((frame * speed + i * 85) % (h * 1.1f)).toFloat()
            val curX = startX + sin((frame + i * 15) * 0.08) * 20.0
            val rotAngle = (frame * 4f + i * 25) % 360f

            drawCircle(
                color = if (i % 3 == 0) Color(0xFF8D6E63) else Color(0xFF5D4037),
                radius = 8f + (i % 5) * 3f,
                center = Offset(curX.toFloat(), curY)
            )
        }
    }
}

/**
 * Frame-by-frame Flood Simulation:
 * - Mathematical sine-wave water surface updated per frame
 * - Raindrop trajectories with wind shear angle
 * - Water surge vortices & lightning flash frames
 */
@Composable
private fun FloodFrameCanvas(frame: Long) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Stormy deep oceanic gradient
        drawRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFF02131F), Color(0xFF072740), Color(0xFF033256))
            )
        )

        // Periodic lightning flash on specific frame intervals (e.g. 5 frames every 80 frames)
        val flashInterval = frame % 85
        if (flashInterval in 1..4) {
            drawRect(
                color = Color(0x77E1F5FE)
            )
        }

        // Angled rain streaks updated every frame
        val rainCount = 45
        for (i in 0 until rainCount) {
            val speed = 8f + (i % 5) * 3f
            val rx = (i * 27f + (frame * 4f)) % (w + 100f) - 50f
            val ry = ((frame * speed + i * 65f) % (h + 100f)) - 50f
            val len = 25f + (i % 4) * 8f
            drawLine(
                color = Color(0x88B3E5FC),
                start = Offset(rx, ry),
                end = Offset(rx - 8f, ry + len),
                strokeWidth = 2.5f
            )
        }

        // Rising water surge level with multi-frequency sine wave crests
        val baseWaterY = h * 0.50f + sin(frame * 0.03) * 15f
        val phase = frame * 0.06

        // Deep background surge wave
        val wave1 = Path().apply {
            moveTo(0f, h)
            lineTo(0f, (baseWaterY + 30f).toFloat())
            var x = 0f
            while (x <= w) {
                val y = baseWaterY + 30f + sin(x * 0.015 + phase) * 24.0 + cos(x * 0.008 - phase) * 12.0
                lineTo(x, y.toFloat())
                x += 18f
            }
            lineTo(w, h)
            close()
        }
        drawPath(
            path = wave1,
            brush = Brush.verticalGradient(
                listOf(Color(0xFF01579B), Color(0xFF002F6C)),
                startY = baseWaterY.toFloat(),
                endY = h
            )
        )

        // Foreground turbulent surge wave with foam
        val wave2 = Path().apply {
            moveTo(0f, h)
            lineTo(0f, baseWaterY.toFloat())
            var x = 0f
            while (x <= w) {
                val y = baseWaterY + sin(x * 0.02 - phase * 1.3) * 20.0 + cos(x * 0.035 + phase) * 10.0
                lineTo(x, y.toFloat())
                x += 14f
            }
            lineTo(w, h)
            close()
        }
        drawPath(
            path = wave2,
            brush = Brush.verticalGradient(
                listOf(Color(0xFF0288D1), Color(0xFF01579B)),
                startY = baseWaterY.toFloat(),
                endY = h
            )
        )

        // Frothing wave crest line
        drawPath(
            path = wave2,
            color = Color(0xCCE1F5FE),
            style = Stroke(width = 5f, cap = StrokeCap.Round)
        )

        // Floating hazard warning barrel/buoy bobbing in the water
        val buoyX = w * 0.5f + sin(frame * 0.04) * 30.0
        val buoyY = baseWaterY + sin(buoyX * 0.02 - phase * 1.3) * 20.0 + 8.0
        drawCircle(
            color = Color(0xFFFF3D00),
            radius = 16f,
            center = Offset(buoyX.toFloat(), buoyY.toFloat())
        )
        drawCircle(
            color = Color(0xFFFFFFFF),
            radius = 10f,
            center = Offset(buoyX.toFloat(), buoyY.toFloat())
        )
    }
}

/**
 * Frame-by-frame Landslide Simulation:
 * - Steep mountain cliff cross-section
 * - Physics-driven boulders tumbling, bouncing, and accelerating down
 * - Billowing dust cloud particles expanding per frame
 */
@Composable
private fun LandslideFrameCanvas(frame: Long) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Dusty ochre earth background
        drawRect(
            brush = Brush.verticalGradient(
                listOf(Color(0xFF261812), Color(0xFF382319), Color(0xFF1D120D))
            )
        )

        // Steep terrain incline
        val slope = Path().apply {
            moveTo(0f, h * 0.12f)
            lineTo(w * 0.35f, h * 0.35f)
            lineTo(w * 0.70f, h * 0.65f)
            lineTo(w, h * 0.88f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        drawPath(
            path = slope,
            brush = Brush.linearGradient(
                listOf(Color(0xFF4E342E), Color(0xFF3E2723)),
                start = Offset(0f, 0f),
                end = Offset(w, h)
            )
        )

        // Billowing dust clouds animated along the slope
        for (i in 0..7) {
            val dustPhase = (frame * 0.05 + i * 1.2) % (2 * PI)
            val dX = w * (0.15f + i * 0.11f) + sin(dustPhase) * 15f
            val dY = h * (0.22f + i * 0.09f)
            val dRadius = 55f + sin(dustPhase * 1.5) * 20f
            drawCircle(
                color = Color(0x33A1887F),
                radius = dRadius.toFloat(),
                center = Offset(dX.toFloat(), dY.toFloat())
            )
        }

        // Boulders tumbling down the mountain slope frame by frame
        val boulderCount = 20
        for (i in 0 until boulderCount) {
            val speed = 2.8f + (i % 5) * 1.1f
            val progress = ((frame * speed + i * 120) % 1000) / 1000f

            // Bouncing trajectory calculation along the slope
            val bx = progress * (w * 1.1f) - 50f
            val slopeY = h * 0.12f + progress * (h * 0.76f)
            // Periodic bouncing arcs off the ground
            val bounceArc = sin(progress * 16.0 * PI) * 22.0
            val by = slopeY - (bounceArc.coerceAtLeast(0.0)).toFloat()
            val radius = 14f + (i % 6) * 6f

            if (bx in -60f..w + 60f && by in 0f..h + 60f) {
                // Boulder shadow
                drawCircle(
                    color = Color(0xFF1B110D),
                    radius = radius,
                    center = Offset(bx, by)
                )
                // Boulder body
                drawCircle(
                    color = if (i % 2 == 0) Color(0xFF6D4C41) else Color(0xFF8D6E63),
                    radius = radius * 0.85f,
                    center = Offset(bx - radius * 0.15f, by - radius * 0.15f)
                )
                // Highlight glint
                drawCircle(
                    color = Color(0xFFBCAAA4),
                    radius = radius * 0.35f,
                    center = Offset(bx - radius * 0.35f, by - radius * 0.35f)
                )
            }
        }
    }
}

/**
 * Frame-by-frame Fire Simulation:
 * - Dynamic fiery tongues computed via sine wave combinations
 * - Glowing embers and rising sparks with random flutter
 * - Heat distortion pulse and inferno glow
 */
@Composable
private fun FireFrameCanvas(frame: Long) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Inferno ambient heat background
        val heatPulse = (sin(frame * 0.15) * 0.2 + 0.8).toFloat()
        drawRect(
            brush = Brush.verticalGradient(
                listOf(
                    Color(0xFF1E0101),
                    Color(0xFF3E0502),
                    Color(0xFF660901).copy(alpha = heatPulse)
                )
            )
        )

        // Frame-driven rising sparks & embers
        val emberCount = 50
        for (i in 0 until emberCount) {
            val speed = 3.5f + (i % 6) * 1.4f
            val curY = h - ((frame * speed + i * 75) % (h * 1.15f)).toFloat()
            val sway = sin(frame * 0.08 + i * 2.0) * (18.0 + (i % 4) * 8.0)
            val curX = (i * 31f % w) + sway.toFloat()
            val emberAlpha = (curY / h).coerceIn(0.15f, 1f)

            drawCircle(
                color = if (i % 3 == 0) Color(0xFFFFEB3B).copy(alpha = emberAlpha) else Color(0xFFFF5722).copy(alpha = emberAlpha),
                radius = 3f + (i % 4) * 2f,
                center = Offset(curX, curY)
            )
        }

        // Layer 1: Outer deep red roaring flames
        val baseFlameY = h * 0.56f
        val flame1 = Path().apply {
            moveTo(0f, h)
            lineTo(0f, baseFlameY)
            var x = 0f
            while (x <= w) {
                val y = baseFlameY + sin(x * 0.035 + frame * 0.14) * 45.0 + cos(x * 0.018 - frame * 0.10) * 28.0
                lineTo(x, y.toFloat())
                x += 16f
            }
            lineTo(w, h)
            close()
        }
        drawPath(
            path = flame1,
            brush = Brush.verticalGradient(
                listOf(Color(0xFFFF3D00), Color(0xFFD50000), Color(0xFF7F0000)),
                startY = baseFlameY - 40f,
                endY = h
            )
        )

        // Layer 2: Inner yellow and orange core flames
        val flame2 = Path().apply {
            val innerBase = baseFlameY + 50f
            moveTo(0f, h)
            lineTo(0f, innerBase)
            var x = 0f
            while (x <= w) {
                val y = innerBase + sin(x * 0.05 - frame * 0.18) * 35.0 + cos(x * 0.025 + frame * 0.12) * 18.0
                lineTo(x, y.toFloat())
                x += 12f
            }
            lineTo(w, h)
            close()
        }
        drawPath(
            path = flame2,
            brush = Brush.verticalGradient(
                listOf(Color(0xFFFFF59D), Color(0xFFFFD54F), Color(0xFFFF6F00)),
                startY = baseFlameY,
                endY = h
            )
        )
    }
}
