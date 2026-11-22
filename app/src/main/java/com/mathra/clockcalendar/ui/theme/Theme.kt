package com.mathra.clockcalendar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.mathra.clockcalendar.data.settings.ThemeMode

/** The signature Mathra sky gradient used behind every screen (follows the active theme). */
val MathraBackgroundGradient: Brush
    get() = Brush.verticalGradient(
        colors = listOf(ThemeState.palette.bg, ThemeState.palette.primaryDeep, ThemeState.palette.bg)
    )

@Composable
fun MathraTheme(
    themeId: String = "royal",
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val dark = when (themeMode) {
        ThemeMode.SYSTEM -> systemDark
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }
    val palette = remember(themeId, dark) { MathraThemes.palette(themeId, dark) }

    // Publish to the global state that the Mathra* colour getters read.
    SideEffect {
        ThemeState.palette = palette
        ThemeState.isDark = dark
    }

    val scheme = if (dark) {
        darkColorScheme(
            primary = palette.primary, onPrimary = palette.text,
            secondary = palette.accent, onSecondary = palette.bg,
            tertiary = palette.blue,
            background = palette.bg, onBackground = palette.text,
            surface = palette.bgDeep, onSurface = palette.text,
            surfaceVariant = palette.primaryDeep, onSurfaceVariant = palette.text,
        )
    } else {
        lightColorScheme(
            primary = palette.primary, onPrimary = palette.bg,
            secondary = palette.accent, onSecondary = palette.bg,
            tertiary = palette.blue,
            background = palette.bg, onBackground = palette.text,
            surface = palette.bgDeep, onSurface = palette.text,
            surfaceVariant = palette.primaryDeep, onSurfaceVariant = palette.text,
        )
    }

    // Status bar colour + icon contrast follow the theme.
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
            @Suppress("DEPRECATION")
            window.statusBarColor = palette.bg.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !dark
        }
    }

    MaterialTheme(colorScheme = scheme, typography = MathraTypography, content = content)
}
