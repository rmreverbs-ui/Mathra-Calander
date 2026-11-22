package com.mathra.clockcalendar.data.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "mathra_settings")

enum class AppLanguage { ENGLISH, SINHALA }

enum class ThemeMode { SYSTEM, DARK, LIGHT }

/** Thin wrapper around Jetpack DataStore for all user-configurable settings. */
class SettingsRepository(private val context: Context) {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("language")
        val USE_24H = booleanPreferencesKey("use_24h")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val ANIMATIONS_ENABLED = booleanPreferencesKey("animations_enabled")
        val SELECTED_THEME = stringPreferencesKey("selected_theme")
        val THEME_MODE = stringPreferencesKey("theme_mode")
    }

    val language: Flow<AppLanguage> = context.dataStore.data.map {
        if (it[Keys.LANGUAGE] == "si") AppLanguage.SINHALA else AppLanguage.ENGLISH
    }
    val use24HourFormat: Flow<Boolean> = context.dataStore.data.map { it[Keys.USE_24H] ?: false }
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { it[Keys.NOTIFICATIONS_ENABLED] ?: true }
    val vibrationEnabled: Flow<Boolean> = context.dataStore.data.map { it[Keys.VIBRATION_ENABLED] ?: true }
    val animationsEnabled: Flow<Boolean> = context.dataStore.data.map { it[Keys.ANIMATIONS_ENABLED] ?: true }
    val selectedTheme: Flow<String> = context.dataStore.data.map { it[Keys.SELECTED_THEME] ?: "royal" }
    val themeMode: Flow<ThemeMode> = context.dataStore.data.map {
        runCatching { ThemeMode.valueOf(it[Keys.THEME_MODE] ?: "SYSTEM") }.getOrDefault(ThemeMode.SYSTEM)
    }

    suspend fun setLanguage(lang: AppLanguage) {
        context.dataStore.edit { it[Keys.LANGUAGE] = if (lang == AppLanguage.SINHALA) "si" else "en" }
    }
    suspend fun setUse24HourFormat(value: Boolean) { context.dataStore.edit { it[Keys.USE_24H] = value } }
    suspend fun setNotificationsEnabled(value: Boolean) { context.dataStore.edit { it[Keys.NOTIFICATIONS_ENABLED] = value } }
    suspend fun setVibrationEnabled(value: Boolean) { context.dataStore.edit { it[Keys.VIBRATION_ENABLED] = value } }
    suspend fun setAnimationsEnabled(value: Boolean) { context.dataStore.edit { it[Keys.ANIMATIONS_ENABLED] = value } }
    suspend fun setThemeMode(mode: ThemeMode) { context.dataStore.edit { it[Keys.THEME_MODE] = mode.name } }
    suspend fun setSelectedTheme(name: String) { context.dataStore.edit { it[Keys.SELECTED_THEME] = name } }
}
