package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.audio.DisasterSoundEngine
import com.example.data.AppSettingsManager

@Composable
fun SettingsDialog(
    soundEngine: DisasterSoundEngine,
    settingsManager: AppSettingsManager,
    onDismiss: () -> Unit
) {
    var isSoundEnabled by remember { mutableStateOf(settingsManager.isSoundEffectsEnabled) }
    var isComfortVolume by remember { mutableStateOf(settingsManager.isComfortVolume) }
    var isHapticsEnabled by remember { mutableStateOf(settingsManager.isHapticsEnabled) }
    var isTestingSound by remember { mutableStateOf(false) }

    fun applySettings() {
        settingsManager.isSoundEffectsEnabled = isSoundEnabled
        settingsManager.isComfortVolume = isComfortVolume
        settingsManager.isHapticsEnabled = isHapticsEnabled

        soundEngine.isMuted = !isSoundEnabled
        soundEngine.isMaxLoudness = !isComfortVolume
        soundEngine.isHapticsEnabled = isHapticsEnabled
    }

    Dialog(
        onDismissRequest = {
            applySettings()
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp)
                    .clip(RoundedCornerShape(26.dp))
                    .testTag("settings_dialog"),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                shape = RoundedCornerShape(26.dp),
                border = BorderStroke(1.5.dp, Color(0xFF1E293B)),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                        .verticalScroll(rememberScrollState())
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
                                color = Color(0xFF0284C7).copy(alpha = 0.2f),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null,
                                        tint = Color(0xFF38BDF8),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "PENGATURAN",
                                    color = Color(0xFF38BDF8),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Kenyamanan & Suara",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                applySettings()
                                onDismiss()
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("close_settings_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Tutup Pengaturan",
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // 1. SOUND EFFECTS TOGGLE (PRIMARY FEATURE)
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFF1E293B),
                        border = BorderStroke(
                            1.dp,
                            if (isSoundEnabled) Color(0xFF0284C7) else Color(0xFF334155)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isSoundEnabled) Color(0xFF0284C7).copy(alpha = 0.25f) else Color(0xFF475569).copy(alpha = 0.3f),
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = if (isSoundEnabled) Icons.AutoMirrored.Filled.VolumeUp else Icons.AutoMirrored.Filled.VolumeOff,
                                                contentDescription = null,
                                                tint = if (isSoundEnabled) Color(0xFF38BDF8) else Color(0xFF94A3B8),
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Efek Suara Bencana",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.5.sp
                                        )
                                        Text(
                                            text = if (isSoundEnabled) "Aktif (Sirine & Efek Alam Menyala)" else "Nonaktif / Hening (Tanpa Suara)",
                                            color = if (isSoundEnabled) Color(0xFF34D399) else Color(0xFF94A3B8),
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                Switch(
                                    checked = isSoundEnabled,
                                    onCheckedChange = { checked ->
                                        isSoundEnabled = checked
                                        soundEngine.isMuted = !checked
                                        if (!checked) {
                                            soundEngine.stopAudio()
                                            isTestingSound = false
                                        }
                                    },
                                    modifier = Modifier.testTag("sound_effects_toggle"),
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color(0xFF0284C7),
                                        uncheckedThumbColor = Color(0xFF94A3B8),
                                        uncheckedTrackColor = Color(0xFF334155)
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Menonaktifkan efek suara membuat simulasi tetap hening dan nyaman saat Anda berada di tempat umum, perpustakaan, atau saat belajar dengan tenang.",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.5.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // 2. VOLUME LEVEL OPTIONS (WHEN SOUND IS ON)
                    AnimatedVisibility(
                        visible = isSoundEnabled,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "INTENSITAS & VOLUME SUARA",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Comfort volume card
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isComfortVolume) Color(0xFF0C4A6E) else Color(0xFF1E293B),
                                border = BorderStroke(
                                    1.dp,
                                    if (isComfortVolume) Color(0xFF38BDF8) else Color(0xFF334155)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable {
                                        isComfortVolume = true
                                        soundEngine.isMaxLoudness = false
                                    }
                                    .testTag("volume_comfort_option")
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isComfortVolume) Color(0xFF38BDF8) else Color(0xFF475569),
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        if (isComfortVolume) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(5.dp)
                                                    .background(Color.White, CircleShape)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Suara Nyaman & Seimbang (65%)",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.5.sp
                                        )
                                        Text(
                                            text = "Direkomendasikan agar tidak mengejutkan telinga saat simulasi",
                                            color = Color(0xFFBAE6FD),
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Max volume card
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (!isComfortVolume) Color(0xFF7F1D1D).copy(alpha = 0.4f) else Color(0xFF1E293B),
                                border = BorderStroke(
                                    1.dp,
                                    if (!isComfortVolume) Color(0xFFEF4444) else Color(0xFF334155)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable {
                                        isComfortVolume = false
                                        soundEngine.isMaxLoudness = true
                                    }
                                    .testTag("volume_loud_option")
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (!isComfortVolume) Color(0xFFEF4444) else Color(0xFF475569),
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        if (!isComfortVolume) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(5.dp)
                                                    .background(Color.White, CircleShape)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Suara Sirine Nyaring (100%)",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.5.sp
                                        )
                                        Text(
                                            text = "Kekuatan suara maksimal untuk atmosfer darurat realistis",
                                            color = Color(0xFFFECACA),
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3. HAPTIC FEEDBACK TOGGLE
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = Color(0xFF1E293B),
                        border = BorderStroke(
                            1.dp,
                            if (isHapticsEnabled) Color(0xFF0284C7) else Color(0xFF334155)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isHapticsEnabled) Color(0xFFF59E0B).copy(alpha = 0.25f) else Color(0xFF475569).copy(alpha = 0.3f),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Vibration,
                                            contentDescription = null,
                                            tint = if (isHapticsEnabled) Color(0xFFFBBF24) else Color(0xFF94A3B8),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Getaran Haptik Perangkat",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.5.sp
                                    )
                                    Text(
                                        text = if (isHapticsEnabled) "Getaran alarm & kuis aktif" else "Getaran dimatikan",
                                        color = if (isHapticsEnabled) Color(0xFFFBBF24) else Color(0xFF94A3B8),
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            Switch(
                                checked = isHapticsEnabled,
                                onCheckedChange = { checked ->
                                    isHapticsEnabled = checked
                                    soundEngine.isHapticsEnabled = checked
                                },
                                modifier = Modifier.testTag("haptics_toggle"),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = Color(0xFFF59E0B),
                                    uncheckedThumbColor = Color(0xFF94A3B8),
                                    uncheckedTrackColor = Color(0xFF334155)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4. TEST SOUND BUTTON
                    if (isSoundEnabled) {
                        OutlinedButton(
                            onClick = {
                                if (isTestingSound) {
                                    soundEngine.stopAudio()
                                    isTestingSound = false
                                } else {
                                    isTestingSound = true
                                    soundEngine.isMaxLoudness = !isComfortVolume
                                    soundEngine.isMuted = false
                                    soundEngine.playSampleSound()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("test_sound_button"),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFF38BDF8)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF38BDF8))
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Uji Coba Bunyi Suara (1.5 detik)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // 5. SAVE & CLOSE BUTTON
                    Button(
                        onClick = {
                            applySettings()
                            onDismiss()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("save_settings_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
                    ) {
                        Text(
                            text = "Simpan & Terapkan",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
