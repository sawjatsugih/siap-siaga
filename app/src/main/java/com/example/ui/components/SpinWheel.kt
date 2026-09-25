package com.example.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DisasterType
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.cos
import kotlin.math.sin

data class WheelSector(
    val disaster: DisasterType,
    val label: String,
    val emoji: String,
    val color: Color
)

@Composable
fun SpinWheel(
    isSpinning: Boolean,
    onSpinStart: () -> Unit,
    onSpinTick: () -> Unit,
    onDisasterLanded: (DisasterType) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val random = remember { Random() }

    // 11 Complete Disasters on the Roulette Wheel
    val sectors = remember {
        listOf(
            WheelSector(DisasterType.GEMPA_BUMI, "GEMPA", "🏚️", Color(0xFFEA580C)),
            WheelSector(DisasterType.BANJIR, "BANJIR", "🌊", Color(0xFF0284C7)),
            WheelSector(DisasterType.TANAH_LONGSOR, "LONGSOR", "⛰️", Color(0xFF854D0E)),
            WheelSector(DisasterType.KEBAKARAN, "API", "🔥", Color(0xFFDC2626)),
            WheelSector(DisasterType.TSUNAMI, "TSUNAMI", "🌊⚡", Color(0xFF0D9488)),
            WheelSector(DisasterType.KEKERINGAN, "KERING", "☀️", Color(0xFFD97706)),
            WheelSector(DisasterType.ANGIN_TOPAN, "TOPAN", "🌪️", Color(0xFF4F46E5)),
            WheelSector(DisasterType.GUNUNG_BERAPI, "ERUPSI", "🌋", Color(0xFFB91C1C)),
            WheelSector(DisasterType.ABRASI, "ABRASI", "🏖️", Color(0xFF0891B2)),
            WheelSector(DisasterType.PERUBAHAN_IKLIM, "IKLIM", "🌍", Color(0xFF16A34A)),
            WheelSector(DisasterType.KONFLIK_SOSIAL, "KONFLIK", "🛡️", Color(0xFF7C3AED))
        )
    }

    val sectorCount = sectors.size
    val sectorAngle = 360f / sectorCount // ~32.727 degrees per sector

    val rotationAngle = remember { Animatable(0f) }
    var needleFlap by remember { mutableFloatStateOf(0f) }

    fun startSpin(targetIndex: Int = random.nextInt(sectorCount)) {
        if (isSpinning) return
        onSpinStart()

        coroutineScope.launch {
            val currentRot = rotationAngle.value % 360f
            val baseRotations = (5 + random.nextInt(3)) * 360f // 5 to 7 full revolutions
            val targetSectorCenter = targetIndex * sectorAngle + (sectorAngle / 2f)

            // When wheel is at rotation R, top pointer (angle 270) aligns with wheel angle
            val targetFinalAngle = (270f - targetSectorCenter + 360f) % 360f
            val totalDelta = baseRotations + (targetFinalAngle - currentRot + 360f) % 360f
            val finalTarget = rotationAngle.value + totalDelta

            var lastPassedBoundary = (rotationAngle.value / sectorAngle).toInt()

            rotationAngle.animateTo(
                targetValue = finalTarget,
                animationSpec = tween(
                    durationMillis = 3800,
                    easing = CubicBezierEasing(0.12f, 0.82f, 0.22f, 1.0f)
                )
            ) {
                val currentBoundary = (this.value / sectorAngle).toInt()
                if (currentBoundary != lastPassedBoundary) {
                    onSpinTick()
                    lastPassedBoundary = currentBoundary
                    needleFlap = if (needleFlap > 0f) -18f else 18f
                }
            }

            needleFlap = 0f
            onDisasterLanded(sectors[targetIndex].disaster)
        }
    }

    val animatedNeedleRotation by animateFloatAsState(
        targetValue = needleFlap,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "needle_bounce"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp)
            .aspectRatio(1f)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer Wheel with sectors
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .rotate(rotationAngle.value)
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2f - 14f

            // Fresh & Joyful Outer Rim: Clean crisp white with golden accent ring
            drawCircle(
                color = Color.White,
                radius = radius + 12f,
                center = center
            )
            drawCircle(
                color = Color(0xFFF1F5F9),
                radius = radius + 10f,
                center = center
            )
            drawCircle(
                color = Color(0xFFFBBF24), // Joy Sunshine Gold
                radius = radius + 5f,
                center = center,
                style = Stroke(width = 4.5f)
            )

            // Draw sectors
            sectors.forEachIndexed { i, sector ->
                val startAngle = i * sectorAngle
                drawArc(
                    color = sector.color,
                    startAngle = startAngle,
                    sweepAngle = sectorAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2f, radius * 2f)
                )

                // Divider line with clean crisp white separator
                val rad = Math.toRadians(startAngle.toDouble())
                val endLine = Offset(
                    (center.x + radius * cos(rad)).toFloat(),
                    (center.y + radius * sin(rad)).toFloat()
                )
                drawLine(
                    color = Color.White.copy(alpha = 0.85f),
                    start = center,
                    end = endLine,
                    strokeWidth = 2.5f
                )
            }

            // Draw text and emoji in native canvas
            val nativeCanvas = drawContext.canvas.nativeCanvas
            val paint = Paint().apply {
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
                typeface = Typeface.DEFAULT_BOLD
            }

            sectors.forEachIndexed { i, sector ->
                val midAngle = i * sectorAngle + sectorAngle / 2f
                val midRad = Math.toRadians(midAngle.toDouble())

                nativeCanvas.save()
                val textRadius = radius * 0.68f
                val tx = (center.x + textRadius * cos(midRad)).toFloat()
                val ty = (center.y + textRadius * sin(midRad)).toFloat()

                nativeCanvas.translate(tx, ty)
                nativeCanvas.rotate(midAngle + 90f)

                // Draw emoji
                paint.textSize = radius * 0.12f
                paint.color = android.graphics.Color.WHITE
                nativeCanvas.drawText(sector.emoji, 0f, -radius * 0.05f, paint)

                // Draw label
                paint.textSize = radius * 0.065f
                paint.color = android.graphics.Color.WHITE
                nativeCanvas.drawText(sector.label, 0f, radius * 0.06f, paint)

                nativeCanvas.restore()
            }

            // Decorative outer joyful jewel dots
            val rivetCount = 22
            for (r in 0 until rivetCount) {
                val rivetAngle = (r * (360.0 / rivetCount))
                val rRad = Math.toRadians(rivetAngle)
                val rx = (center.x + (radius + 6f) * cos(rRad)).toFloat()
                val ry = (center.y + (radius + 6f) * sin(rRad)).toFloat()
                drawCircle(
                    color = if (r % 2 == 0) Color(0xFFF59E0B) else Color(0xFF0284C7),
                    radius = 3f,
                    center = Offset(rx, ry)
                )
            }
        }

        // Center Spin Button (Static, stays upright)
        Surface(
            modifier = Modifier
                .size(80.dp)
                .shadow(14.dp, CircleShape)
                .clickable(
                    enabled = !isSpinning,
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    startSpin()
                }
                .testTag("spin_wheel_button"),
            shape = CircleShape,
            color = if (isSpinning) Color(0xFF94A3B8) else Color(0xFFEF4444)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = if (isSpinning) "..." else "SPIN!",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }

        // Top Needle / Flapper (12 o'clock pointer)
        Canvas(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(42.dp)
                .rotate(animatedNeedleRotation)
                .offset(y = (-4).dp)
        ) {
            val needlePath = Path().apply {
                moveTo(size.width / 2f, size.height) // pointing tip
                lineTo(size.width * 0.2f, size.height * 0.15f)
                lineTo(size.width * 0.8f, size.height * 0.15f)
                close()
            }
            drawPath(
                path = needlePath,
                color = Color(0xFFFBBF24) // Golden yellow
            )
            drawPath(
                path = needlePath,
                color = Color(0xFFEF4444), // Coral red stroke
                style = Stroke(width = 3f)
            )
            // Needle hinge screw
            drawCircle(
                color = Color(0xFF0F172A),
                radius = 5.5f,
                center = Offset(size.width / 2f, size.height * 0.25f)
            )
        }
    }
}
