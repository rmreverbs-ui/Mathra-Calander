package com.mathra.clockcalendar.viewmodel

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mathra.clockcalendar.data.settings.AppLanguage
import com.mathra.clockcalendar.data.settings.SettingsRepository
import com.mathra.clockcalendar.data.settings.ThemeMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SettingsRepository(application)

    val language: StateFlow<AppLanguage> = repository.language.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppLanguage.ENGLISH)
    val use24HourFormat: StateFlow<Boolean> = repository.use24HourFormat.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    val notificationsEnabled: StateFlow<Boolean> = repository.notificationsEnabled.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val vibrationEnabled: StateFlow<Boolean> = repository.vibrationEnabled.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val animationsEnabled: StateFlow<Boolean> = repository.animationsEnabled.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
    val selectedTheme: StateFlow<String> = repository.selectedTheme.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "royal")
    val themeMode: StateFlow<ThemeMode> = repository.themeMode.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemeMode.SYSTEM)

    fun setLanguage(lang: AppLanguage) {
        viewModelScope.launch { repository.setLanguage(lang) }
        // Switches every string resource (values/ vs values-si/) and recreates the activity.
        val tag = if (lang == AppLanguage.SINHALA) "si" else "en"
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
    }
    fun setUse24HourFormat(value: Boolean) = viewModelScope.launch { repository.setUse24HourFormat(value) }
    fun setNotificationsEnabled(value: Boolean) = viewModelScope.launch { repository.setNotificationsEnabled(value) }
    fun setVibrationEnabled(value: Boolean) = viewModelScope.launch { repository.setVibrationEnabled(value) }
    fun setAnimationsEnabled(value: Boolean) = viewModelScope.launch { repository.setAnimationsEnabled(value) }
    fun setThemeMode(mode: ThemeMode) = viewModelScope.launch { repository.setThemeMode(mode) }
    fun setSelectedTheme(name: String) = viewModelScope.launch { repository.setSelectedTheme(name) }
}
