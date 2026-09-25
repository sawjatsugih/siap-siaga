package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.model.DisasterType
import com.example.ui.theme.JoyPrimary
import com.example.ui.theme.JoySecondary
import com.example.ui.theme.JoyTextMuted
import com.example.ui.theme.JoyTextPrimary
import com.example.ui.theme.JoyTextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Random
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

/**
 * Interactive Card Carousel replacing the Spin Wheel.
 * Features:
 * - Rich visual preview on each card (mini canvas artwork, earthquake magnitude, subtitle, mitigation highlight)
 * - Dynamic "ACAK BENCANA (START RANDOM)" button that continuously scrolls cards with deceleration physics
 * - Manual left/right card browsing and pager indicator dots
 * - Responsive layout for both phones and tablet resolutions
 */
@Composable
fun DisasterCardCarousel(
    disasters: List<DisasterType> = DisasterType.ALL,
    isRandomizing: Boolean,
    onRandomizeStart: () -> Unit,
    onCardTick: () -> Unit,
    onDisasterSelected: (DisasterType) -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val random = remember { Random() }

    // Use a large virtual count for endless smooth scrolling
    val loopFactor = 1000
    val totalVirtualPages = disasters.size * loopFactor
    val initialPage = (totalVirtualPages / 2) - ((totalVirtualPages / 2) % disasters.size)

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { totalVirtualPages }
    )

    // Play tick on page change during manual swiping
    LaunchedEffect(pagerState) {
        var lastPage = pagerState.currentPage
        snapshotFlow { pagerState.currentPage }.collect { currentPage ->
            if (currentPage != lastPage && !isRandomizing) {
                onCardTick()
                lastPage = currentPage
            }
        }
    }

    // Function to execute the random card carousel spin
    fun startRandomSpin() {
        if (isRandomizing) return
        onRandomizeStart()

        coroutineScope.launch {
            val totalSteps = 22 + random.nextInt(10) // 22 to 31 card shifts
            for (step in 1..totalSteps) {
                val progress = step.toFloat() / totalSteps
                // Non-linear easing: starts fast (~45ms) and gradually decelerates up to ~480ms
                val stepDelay = (45L + (progress * progress * progress * 435L)).toLong()
                val targetPage = pagerState.currentPage + 1

                pagerState.animateScrollToPage(
                    page = targetPage,
                    animationSpec = tween(
                        durationMillis = (stepDelay * 0.72).toInt().coerceAtLeast(35),
                        easing = if (progress > 0.8f) FastOutSlowInEasing else LinearEasing
                    )
                )
                onCardTick()
                delay(stepDelay)
            }

            // Brief suspense pause on the landed card
            delay(400)
            val selectedIndex = pagerState.currentPage % disasters.size
            onDisasterSelected(disasters[selectedIndex])
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Main Horizontal Carousel with peeking neighboring cards
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(440.dp),
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 44.dp),
                pageSpacing = 16.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("disaster_card_carousel")
            ) { page ->
                val disasterIndex = page % disasters.size
                val disaster = disasters[disasterIndex]

                // Calculate carousel depth scale & alpha
                val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
                val scale = lerp(0.88f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                val alpha = lerp(0.60f, 1f, 1f - pageOffset.coerceIn(0f, 1f))

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                        },
                    contentAlignment = Alignment.Center
                ) {
                    DisasterPreviewCard(
                        disaster = disaster,
                        isCurrent = pageOffset < 0.5f,
                        onSimulateClick = {
                            if (!isRandomizing) {
                                onDisasterSelected(disaster)
                            }
                        }
                    )
                }
            }

            // Carousel Left Arrow Button
            IconButton(
                onClick = {
                    if (!isRandomizing && pagerState.currentPage > 0) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                },
                enabled = !isRandomizing,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 4.dp)
                    .size(40.dp)
                    .testTag("carousel_prev_button"),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White.copy(alpha = 0.9f),
                    contentColor = JoyTextPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Sebelumnya",
                    modifier = Modifier.size(20.dp)
                )
            }

            // Carousel Right Arrow Button
            IconButton(
                onClick = {
                    if (!isRandomizing) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                enabled = !isRandomizing,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp)
                    .size(40.dp)
                    .testTag("carousel_next_button"),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White.copy(alpha = 0.9f),
                    contentColor = JoyTextPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Selanjutnya",
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Pager Indicator Dots (11 disasters)
        val activeIndicatorIndex = pagerState.currentPage % disasters.size
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            disasters.forEachIndexed { idx, disaster ->
                val isActive = idx == activeIndicatorIndex
                Box(
                    modifier = Modifier
                        .padding(horizontal = 3.dp)
                        .size(if (isActive) 10.dp else 6.dp)
                        .clip(CircleShape)
                        .background(if (isActive) disaster.primaryColor else Color(0xFFCBD5E1))
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Primary "ACAK BENCANA (START RANDOM)" Button
        Button(
            onClick = { startRandomSpin() },
            enabled = !isRandomizing,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .widthIn(max = 480.dp)
                .height(56.dp)
                .testTag("start_random_button"),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRandomizing) Color(0xFF94A3B8) else Color(0xFFEF4444)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp, pressedElevation = 2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Casino,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isRandomizing) "MENGACAK BENCANA..." else "ACAK BENCANA (START RANDOM)",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

/**
 * Rich Preview Card representing a disaster in the carousel.
 */
@Composable
fun DisasterPreviewCard(
    disaster: DisasterType,
    isCurrent: Boolean,
    onSimulateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Generate dynamic earthquake magnitude preview for Gempa Bumi
    val magnitudePreview = remember(disaster) {
        if (disaster == DisasterType.GEMPA_BUMI) DisasterType.getRandomMagnitude() else 7.2
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 380.dp)
            .height(420.dp)
            .clickable { onSimulateClick() }
            .testTag("card_${disaster.id}"),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isCurrent) 2.5.dp else 1.dp,
            color = if (isCurrent) disaster.primaryColor else Color(0xFFE2E8F0)
        ),
        elevation = CardDefaults.cardElevation(if (isCurrent) 12.dp else 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Badge Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = disaster.softColor,
                    border = androidx.compose.foundation.BorderStroke(1.dp, disaster.primaryColor.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = disaster.emoji,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = disaster.title.uppercase(),
                            color = disaster.primaryColor,
                            fontWeight = FontWeight.Black,
                            fontSize = 11.5.sp
                        )
                    }
                }

                if (disaster == DisasterType.GEMPA_BUMI) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFFEF2F2),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5))
                    ) {
                        Text(
                            text = "💥 %.1f SR (Acak)".format(magnitudePreview),
                            color = Color(0xFFDC2626),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = "🚨 Bahaya Tinggi",
                            color = Color(0xFF475569),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Interactive Mini Artwork Preview Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(138.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF0F172A))
            ) {
                DisasterMiniArtworkPreview(disaster = disaster)

                // Overlay Mitigation Action Badge
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.Black.copy(alpha = 0.65f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.25f)),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Text(
                        text = getMitigationBadgeText(disaster),
                        color = Color.White,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Title and Emergency Subtitle
            Column {
                Text(
                    text = disaster.title,
                    color = JoyTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = disaster.emergencySubtitle,
                    color = JoyTextSecondary,
                    fontSize = 11.5.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }

            // Key Mitigation Highlight Card
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = disaster.primaryColor,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(top = 1.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = disaster.mitigationTips.firstOrNull() ?: "Tindakan mitigasi cepat kunci keselamatan.",
                        color = Color(0xFF334155),
                        fontSize = 11.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 15.sp
                    )
                }
            }

            // Bottom Action Button: "Simulasikan Sekarang"
            Button(
                onClick = onSimulateClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("preview_simulate_${disaster.id}"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = disaster.primaryColor),
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
                        text = "Simulasikan Bencana Langsung",
                        color = Color.White,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Animated Mini Artwork Canvas for each disaster inside its card preview.
 */
private fun getMitigationBadgeText(disaster: DisasterType): String {
    return when (disaster) {
        DisasterType.GEMPA_BUMI -> "🛡️ DROP, COVER, HOLD ON"
        DisasterType.TSUNAMI -> "🏃 EVAKUASI KE BUKIT TINGGI"
        DisasterType.BANJIR -> "⚡ MATIKAN MCB LISTRIK"
        DisasterType.TANAH_LONGSOR -> "↗️ LARI MENYAMPING DARI LERENG"
        DisasterType.KEBAKARAN -> "🧯 MERANGKAK RENDAH & PAKAI APAR"
        DisasterType.ANGIN_TOPAN -> "🚪 BERLINDUNG DI RUANG DASAR"
        DisasterType.GUNUNG_BERAPI -> "😷 MASKER N95 & KACAMATA GOGGLE"
        DisasterType.KEKERINGAN -> "💧 PANEN AIR HUJAN & HEMAT AIR"
        DisasterType.ABRASI -> "🌱 PENANAMAN SABUK MANGROVE"
        DisasterType.PERUBAHAN_IKLIM -> "🌿 ENERGI BERSIH & DAUR ULANG"
        DisasterType.KONFLIK_SOSIAL -> "🤝 JAUHI MASSA & CARI TEMPAT AMAN"
    }
}

/**
 * Animated High-Fidelity Vector Artwork Canvas for each disaster inside its card preview.
 */
@Composable
private fun DisasterMiniArtworkPreview(disaster: DisasterType) {
    val infiniteTransition = rememberInfiniteTransition(label = "mini_preview_loop")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        when (disaster) {
            DisasterType.GEMPA_BUMI -> drawGempaIllustration(phase)
            DisasterType.TSUNAMI -> drawTsunamiIllustration(phase)
            DisasterType.BANJIR -> drawBanjirIllustration(phase)
            DisasterType.TANAH_LONGSOR -> drawLongsorIllustration(phase)
            DisasterType.KEBAKARAN -> drawKebakaranIllustration(phase)
            DisasterType.ANGIN_TOPAN -> drawTopanIllustration(phase)
            DisasterType.GUNUNG_BERAPI -> drawGunungIllustration(phase)
            DisasterType.KEKERINGAN -> drawKekeringanIllustration(phase)
            DisasterType.ABRASI -> drawAbrasiIllustration(phase)
            DisasterType.PERUBAHAN_IKLIM -> drawIklimIllustration(phase)
            DisasterType.KONFLIK_SOSIAL -> drawKonflikIllustration(phase)
        }
    }
}

// 1. Gempa Bumi: Protective Shelter Table & Seismograph
private fun DrawScope.drawGempaIllustration(phase: Float) {
    val w = size.width
    val h = size.height
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF271B12), Color(0xFF140D09))
        )
    )
    // Seismic shockwaves
    for (i in 1..3) {
        val rad = (h * 0.4f) + ((phase * 15f + i * 22f) % (h * 0.55f))
        drawCircle(
            color = Color(0xFFF59E0B).copy(alpha = (1f - rad / (h * 0.95f)).coerceIn(0f, 0.4f)),
            radius = rad,
            center = Offset(w * 0.35f, h * 0.5f),
            style = Stroke(width = 2f)
        )
    }
    // Fissure
    val crack = Path().apply {
        moveTo(w * 0.1f, h)
        lineTo(w * 0.28f, h * 0.72f)
        lineTo(w * 0.22f, h * 0.55f)
        lineTo(w * 0.38f, h * 0.35f)
        lineTo(w * 0.34f, 0f)
    }
    drawPath(crack, color = Color(0xFFEF4444), style = Stroke(width = 3f))

    // Protective Table Shelter (Mitigation: Drop, Cover, Hold)
    val tableX = w * 0.56f
    val tableY = h * 0.35f
    val tableW = w * 0.36f
    val tableH = h * 0.48f

    // Table Top
    drawRoundRect(
        color = Color(0xFF38BDF8),
        topLeft = Offset(tableX, tableY),
        size = Size(tableW, 12f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
    )
    // Table Legs
    drawRect(
        color = Color(0xFF0284C7),
        topLeft = Offset(tableX + 4f, tableY + 12f),
        size = Size(8f, tableH - 12f)
    )
    drawRect(
        color = Color(0xFF0284C7),
        topLeft = Offset(tableX + tableW - 12f, tableY + 12f),
        size = Size(8f, tableH - 12f)
    )
    // Golden Shield inside table
    drawCircle(
        color = Color(0xFFFBBF24),
        radius = 16f,
        center = Offset(tableX + tableW / 2f, tableY + tableH * 0.55f)
    )
    drawCircle(
        color = Color.White,
        radius = 7f,
        center = Offset(tableX + tableW / 2f, tableY + tableH * 0.55f)
    )
}

// 2. Tsunami: Ocean Surge & High Evacuation Hill
private fun DrawScope.drawTsunamiIllustration(phase: Float) {
    val w = size.width
    val h = size.height
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF0C243B), Color(0xFF0369A1))
        )
    )
    // High Hill on right side (Safe Zone)
    val hill = Path().apply {
        moveTo(w * 0.58f, h)
        cubicTo(w * 0.65f, h * 0.4f, w * 0.78f, h * 0.2f, w, h * 0.15f)
        lineTo(w, h)
        close()
    }
    drawPath(
        hill,
        brush = Brush.verticalGradient(listOf(Color(0xFF10B981), Color(0xFF065F46)))
    )

    // Safety Evacuation Chevron Arrow on the hill
    val arrowX = w * 0.82f
    val arrowY = h * 0.38f + sin(phase.toDouble()).toFloat() * 4f
    drawCircle(color = Color.White, radius = 14f, center = Offset(arrowX, arrowY))
    val chevron = Path().apply {
        moveTo(arrowX - 5f, arrowY + 6f)
        lineTo(arrowX + 4f, arrowY)
        lineTo(arrowX - 5f, arrowY - 6f)
    }
    drawPath(chevron, color = Color(0xFF059669), style = Stroke(width = 3.5f))

    // Giant Tsunami Wave surging towards the left/center
    val wave = Path().apply {
        moveTo(0f, h)
        lineTo(0f, h * 0.4f)
        cubicTo(w * 0.22f, h * 0.32f + sin(phase.toDouble()).toFloat() * 8f, w * 0.42f, h * 0.18f, w * 0.56f, h * 0.48f)
        lineTo(w * 0.56f, h)
        close()
    }
    drawPath(
        wave,
        brush = Brush.verticalGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7)))
    )
    // White foam spray
    for (i in 0 until 9) {
        val fx = w * 0.42f + (i * 12f)
        val fy = h * 0.26f + sin((phase + i).toDouble()).toFloat() * 6f
        drawCircle(color = Color.White.copy(alpha = 0.85f), radius = 3.5f, center = Offset(fx, fy))
    }
}

// 3. Banjir: Water Level & Life-Saving Orange Buoy
private fun DrawScope.drawBanjirIllustration(phase: Float) {
    val w = size.width
    val h = size.height
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF082F49), Color(0xFF075985))
        )
    )
    // Water surface layers
    val wavePath = Path().apply {
        moveTo(0f, h)
        lineTo(0f, h * 0.45f)
        var x = 0f
        while (x <= w) {
            val y = h * 0.45f + sin((x / w * 4 * PI + phase).toDouble()).toFloat() * 8f
            lineTo(x, y)
            x += 16f
        }
        lineTo(w, h)
        close()
    }
    drawPath(wavePath, color = Color(0xFF0284C7).copy(alpha = 0.85f))

    // Floating Lifebuoy (Pelampung Penyelamat)
    val buoyX = w * 0.5f
    val buoyY = h * 0.46f + sin(phase.toDouble()).toFloat() * 7f
    drawCircle(color = Color(0xFFEA580C), radius = 28f, center = Offset(buoyX, buoyY))
    drawCircle(color = Color(0xFF075985), radius = 14f, center = Offset(buoyX, buoyY))
    // White reflective bands on buoy
    drawRect(color = Color.White, topLeft = Offset(buoyX - 4f, buoyY - 28f), size = Size(8f, 14f))
    drawRect(color = Color.White, topLeft = Offset(buoyX - 4f, buoyY + 14f), size = Size(8f, 14f))
    drawRect(color = Color.White, topLeft = Offset(buoyX - 28f, buoyY - 4f), size = Size(14f, 8f))
    drawRect(color = Color.White, topLeft = Offset(buoyX + 14f, buoyY - 4f), size = Size(14f, 8f))

    // Lightning bolt in red circle (Turn off electrical breaker)
    drawCircle(color = Color(0xFFDC2626), radius = 16f, center = Offset(w * 0.85f, h * 0.28f))
    val bolt = Path().apply {
        moveTo(w * 0.85f + 1f, h * 0.28f - 9f)
        lineTo(w * 0.85f - 4f, h * 0.28f + 1f)
        lineTo(w * 0.85f + 1f, h * 0.28f + 1f)
        lineTo(w * 0.85f - 1f, h * 0.28f + 9f)
        lineTo(w * 0.85f + 5f, h * 0.28f - 1f)
        lineTo(w * 0.85f, h * 0.28f - 1f)
        close()
    }
    drawPath(bolt, color = Color(0xFFFDE047))
}

// 4. Tanah Longsor: Terraced Slope & Retaining Barrier
private fun DrawScope.drawLongsorIllustration(phase: Float) {
    val w = size.width
    val h = size.height
    drawRect(Color(0xFF1C1917))
    // Steep slope
    val slope = Path().apply {
        moveTo(0f, 0f)
        lineTo(w * 0.45f, h * 0.65f)
        lineTo(0f, h * 0.65f)
        close()
    }
    drawPath(slope, color = Color(0xFF78350F))

    // Retaining Gabion Wall (Bronjong Penahan)
    for (b in 0 until 4) {
        val bx = w * 0.44f + b * 16f
        val by = h * 0.55f - b * 10f
        drawRoundRect(
            color = Color(0xFF94A3B8),
            topLeft = Offset(bx, by),
            size = Size(14f, 24f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f, 3f),
            style = Stroke(width = 2.5f)
        )
    }

    // Green Safety Detour Route (Lateral Evacuation)
    val arrowX = w * 0.72f
    val arrowY = h * 0.38f + sin(phase.toDouble()).toFloat() * 4f
    drawCircle(color = Color(0xFF10B981), radius = 22f, center = Offset(arrowX, arrowY))
    val diagArrow = Path().apply {
        moveTo(arrowX - 8f, arrowY + 8f)
        lineTo(arrowX + 8f, arrowY - 8f)
    }
    drawPath(diagArrow, color = Color.White, style = Stroke(width = 3.5f))
    val head = Path().apply {
        moveTo(arrowX + 2f, arrowY - 8f)
        lineTo(arrowX + 8f, arrowY - 8f)
        lineTo(arrowX + 8f, arrowY - 2f)
    }
    drawPath(head, color = Color.White, style = Stroke(width = 3.5f))
}

// 5. Kebakaran: Flame Glow & Red Fire Extinguisher (APAR)
private fun DrawScope.drawKebakaranIllustration(phase: Float) {
    val w = size.width
    val h = size.height
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF450A0A), Color(0xFF180404))
        )
    )
    // Flickering background flames
    for (i in 0 until 5) {
        val cx = w * (0.15f + i * 0.16f)
        val fh = h * (0.45f + 0.18f * sin((phase * 2f + i).toDouble()).toFloat())
        val fPath = Path().apply {
            moveTo(cx - 20f, h)
            cubicTo(cx - 10f, h - fh * 0.5f, cx + 8f, h - fh * 0.8f, cx, h - fh)
            cubicTo(cx + 8f, h - fh * 0.8f, cx + 10f, h - fh * 0.5f, cx + 20f, h)
            close()
        }
        drawPath(
            fPath,
            brush = Brush.verticalGradient(listOf(Color(0xFFFDE047), Color(0xFFEA580C), Color(0xFFB91C1C)))
        )
    }

    // Red Fire Extinguisher (APAR)
    val aparX = w * 0.75f
    val aparY = h * 0.28f
    // Canister
    drawRoundRect(
        color = Color(0xFFDC2626),
        topLeft = Offset(aparX, aparY + 12f),
        size = Size(24f, 54f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
    )
    // Handle & Gauge
    drawRect(color = Color(0xFF1E293B), topLeft = Offset(aparX + 7f, aparY), size = Size(10f, 12f))
    drawCircle(color = Color(0xFFFBBF24), radius = 4f, center = Offset(aparX + 22f, aparY + 6f))
    // Hose
    val hose = Path().apply {
        moveTo(aparX + 6f, aparY + 6f)
        cubicTo(aparX - 8f, aparY + 12f, aparX - 8f, aparY + 36f, aparX - 4f, aparY + 46f)
    }
    drawPath(hose, color = Color(0xFF0F172A), style = Stroke(width = 3.5f))
}

// 6. Angin Topan: Vortex Wind & Reinforced Shelter
private fun DrawScope.drawTopanIllustration(phase: Float) {
    val w = size.width
    val h = size.height
    drawRect(Color(0xFF0F172A))
    // Swirling funnel
    for (r in 0 until 6) {
        val prog = r / 6f
        val ry = h * (0.8f - prog * 0.6f)
        val rx = w * 0.35f + sin((phase + r).toDouble()).toFloat() * 12f
        val rw = 22f + prog * 62f
        drawOval(
            color = Color(0xFF38BDF8).copy(alpha = 0.35f + prog * 0.45f),
            topLeft = Offset(rx - rw / 2f, ry - 6f),
            size = Size(rw, 12f),
            style = Stroke(width = 2.5f)
        )
    }
    // Reinforced Concrete Safe Room on right
    val roomX = w * 0.68f
    val roomY = h * 0.38f
    drawRoundRect(
        color = Color(0xFF334155),
        topLeft = Offset(roomX, roomY),
        size = Size(w * 0.24f, h * 0.46f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f, 8f)
    )
    // Green Secure Door
    drawRect(
        color = Color(0xFF10B981),
        topLeft = Offset(roomX + 8f, roomY + 14f),
        size = Size(w * 0.12f, h * 0.36f)
    )
    drawCircle(color = Color.White, radius = 3f, center = Offset(roomX + 13f, roomY + 32f))
}

// 7. Gunung Berapi: Ash Plume & N95 Protection Mask
private fun DrawScope.drawGunungIllustration(phase: Float) {
    val w = size.width
    val h = size.height
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF2E1065), Color(0xFF180829))
        )
    )
    // Volcanic Mountain peak
    val mtn = Path().apply {
        moveTo(w * 0.1f, h)
        lineTo(w * 0.35f, h * 0.42f)
        lineTo(w * 0.45f, h * 0.42f)
        lineTo(w * 0.7f, h)
        close()
    }
    drawPath(mtn, color = Color(0xFF4C1D95))

    // Magma & Ash Cloud
    for (i in 0 until 5) {
        val cx = w * 0.4f + sin((phase + i).toDouble()).toFloat() * 14f
        val cy = h * (0.35f - i * 0.06f)
        drawCircle(
            color = Color(0xFFFB923C).copy(alpha = (0.8f - i * 0.12f).coerceAtLeast(0.2f)),
            radius = 16f + i * 4f,
            center = Offset(cx, cy)
        )
    }

    // Protective N95 Mask Icon (Mitigation)
    val maskX = w * 0.78f
    val maskY = h * 0.48f + sin(phase.toDouble()).toFloat() * 3f
    drawCircle(color = Color.White, radius = 22f, center = Offset(maskX, maskY))
    val maskPath = Path().apply {
        moveTo(maskX - 12f, maskY - 6f)
        cubicTo(maskX, maskY - 14f, maskX + 6f, maskY - 12f, maskX + 12f, maskY - 6f)
        lineTo(maskX + 9f, maskY + 10f)
        cubicTo(maskX, maskY + 16f, maskX - 4f, maskY + 14f, maskX - 9f, maskY + 10f)
        close()
    }
    drawPath(maskPath, color = Color(0xFF0284C7))
}

// 8. Kekeringan: Water Drop Conservation & Cistern
private fun DrawScope.drawKekeringanIllustration(phase: Float) {
    val w = size.width
    val h = size.height
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF78350F), Color(0xFF451A03))
        )
    )
    // Cracked dry ground
    drawRect(color = Color(0xFFB45309), topLeft = Offset(0f, h * 0.72f), size = Size(w, h * 0.28f))

    // Giant Sparkling Conservation Water Droplet
    val dropX = w * 0.5f
    val dropY = h * 0.42f + sin(phase.toDouble()).toFloat() * 6f
    val drop = Path().apply {
        moveTo(dropX, dropY - 26f)
        cubicTo(dropX + 18f, dropY - 4f, dropX + 22f, dropY + 16f, dropX, dropY + 26f)
        cubicTo(dropX - 22f, dropY + 16f, dropX - 18f, dropY - 4f, dropX, dropY - 26f)
        close()
    }
    drawPath(
        drop,
        brush = Brush.verticalGradient(listOf(Color(0xFF38BDF8), Color(0xFF0284C7)))
    )
    // Green Sprout inside drop (Renewed Life)
    drawCircle(color = Color(0xFF4ADE80), radius = 6f, center = Offset(dropX, dropY + 8f))
}

// 9. Abrasi: Mangrove Greenbelt Roots & Wave Dissipation
private fun DrawScope.drawAbrasiIllustration(phase: Float) {
    val w = size.width
    val h = size.height
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF042F2E), Color(0xFF134E4A))
        )
    )
    // Ocean surf on left
    val wave = Path().apply {
        moveTo(0f, h)
        lineTo(0f, h * 0.45f)
        cubicTo(w * 0.2f, h * 0.4f + sin(phase.toDouble()).toFloat() * 6f, w * 0.35f, h * 0.52f, w * 0.45f, h * 0.5f)
        lineTo(w * 0.45f, h)
        close()
    }
    drawPath(wave, color = Color(0xFF0D9488))

    // Coastal Mangrove Tree with deep interlocking roots (Mitigation Greenbelt)
    val treeX = w * 0.7f
    val treeY = h * 0.35f
    // Foliage canopy
    drawCircle(color = Color(0xFF22C55E), radius = 24f, center = Offset(treeX, treeY))
    drawCircle(color = Color(0xFF16A34A), radius = 18f, center = Offset(treeX - 10f, treeY + 4f))
    drawCircle(color = Color(0xFF15803D), radius = 16f, center = Offset(treeX + 12f, treeY + 6f))
    // Stilt Roots anchoring into the soil
    for (r in -3..3) {
        val root = Path().apply {
            moveTo(treeX, treeY + 18f)
            quadraticTo(treeX + r * 10f, h * 0.7f, treeX + r * 16f, h)
        }
        drawPath(root, color = Color(0xFF78350F), style = Stroke(width = 3.5f))
    }
}

// 10. Perubahan Iklim: Earth Globe with Clean Energy
private fun DrawScope.drawIklimIllustration(phase: Float) {
    val w = size.width
    val h = size.height
    drawRect(Color(0xFF0F172A))
    // Glowing Earth Globe
    val globeX = w * 0.5f
    val globeY = h * 0.48f
    drawCircle(color = Color(0xFF0284C7), radius = 34f, center = Offset(globeX, globeY))
    // Continents
    drawCircle(color = Color(0xFF22C55E), radius = 14f, center = Offset(globeX - 8f, globeY - 6f))
    drawCircle(color = Color(0xFF22C55E), radius = 12f, center = Offset(globeX + 10f, globeY + 8f))

    // Orbiting Leaf / Renewable energy ring
    val orbitAngle = phase
    val orbitX = globeX + cos(orbitAngle.toDouble()).toFloat() * 48f
    val orbitY = globeY + sin(orbitAngle.toDouble()).toFloat() * 22f
    drawCircle(color = Color(0xFFFBBF24), radius = 7f, center = Offset(orbitX, orbitY))
}

// 11. Konflik Sosial: Peace Dove Emblem & Safe Sanctuary
private fun DrawScope.drawKonflikIllustration(phase: Float) {
    val w = size.width
    val h = size.height
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(Color(0xFF1E1B4B), Color(0xFF0F172A))
        )
    )
    // Emergency red alert beacons in background
    val beaconAlpha = (0.3f + 0.3f * sin(phase.toDouble()).toFloat()).coerceIn(0.1f, 0.6f)
    drawCircle(color = Color(0xFFDC2626).copy(alpha = beaconAlpha), radius = 55f, center = Offset(w * 0.25f, h * 0.4f))

    // Central White Dove / Peace Crest (Mitigation: Peace & Safe Haven)
    val doveX = w * 0.55f
    val doveY = h * 0.48f + sin(phase.toDouble()).toFloat() * 4f
    drawCircle(color = Color(0xFF3B82F6), radius = 28f, center = Offset(doveX, doveY))
    drawCircle(color = Color.White, radius = 22f, center = Offset(doveX, doveY))
    // Olive branch / peace symbol
    val branch = Path().apply {
        moveTo(doveX - 10f, doveY + 6f)
        cubicTo(doveX, doveY, doveX + 4f, doveY - 8f, doveX + 10f, doveY - 8f)
    }
    drawPath(branch, color = Color(0xFF16A34A), style = Stroke(width = 3.5f))
    drawCircle(color = Color(0xFF16A34A), radius = 3.5f, center = Offset(doveX - 2f, doveY + 2f))
    drawCircle(color = Color(0xFF16A34A), radius = 3.5f, center = Offset(doveX + 6f, doveY - 5f))
}

