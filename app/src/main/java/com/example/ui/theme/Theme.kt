package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val FreshJoyLightColorScheme = lightColorScheme(
    primary = JoyPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = JoySecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFFB45309),
    tertiary = JoyTertiary,
    onTertiary = Color.White,
    background = JoyBackground,
    onBackground = JoyTextPrimary,
    surface = JoySurface,
    onSurface = JoyTextPrimary,
    surfaceVariant = JoySurfaceVariant,
    onSurfaceVariant = JoyTextSecondary,
    outline = JoyBorder
)

private val FreshJoyDarkColorScheme = darkColorScheme(
    primary = Color(0xFF38BDF8),
    onPrimary = Color(0xFF0C4A6E),
    secondary = Color(0xFFFBBF24),
    onSecondary = Color(0xFF451A03),
    tertiary = Color(0xFF34D399),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    outline = Color(0xFF334155)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Default to joyful, fresh bright light theme
    dynamicColor: Boolean = false, // Keep consistent fresh and joy theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) FreshJoyDarkColorScheme else FreshJoyLightColorScheme
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
