package com.example.data

import android.content.Context
import android.content.SharedPreferences

class AppSettingsManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("disaster_game_settings", Context.MODE_PRIVATE)

    var isSoundEffectsEnabled: Boolean
        get() = prefs.getBoolean("sound_effects_enabled", true)
        set(value) = prefs.edit().putBoolean("sound_effects_enabled", value).apply()

    var isComfortVolume: Boolean
        get() = prefs.getBoolean("sound_comfort_volume", true)
        set(value) = prefs.edit().putBoolean("sound_comfort_volume", value).apply()

    var isHapticsEnabled: Boolean
        get() = prefs.getBoolean("haptics_enabled", true)
        set(value) = prefs.edit().putBoolean("haptics_enabled", value).apply()
}
