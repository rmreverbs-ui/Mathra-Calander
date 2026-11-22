package com.mathra.clockcalendar.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** One complete set of colours for a theme in either dark or light mode. */
data class MathraPalette(
    val bg: Color,
    val bgDeep: Color,
    val primary: Color,
    val primaryDeep: Color,
    val primaryLight: Color,
    val accent: Color,
    val accentDeep: Color,
    val blue: Color,
    val text: Color,
    val glass: Color,
)

/** Id + display names + preview swatches for the Theme Gallery. */
data class MathraThemeInfo(val id: String, val nameEn: String, val nameSi: String)

object MathraThemes {
    val all = listOf(
        MathraThemeInfo("royal", "Royal Purple", "රාජකීය දම්"),
        MathraThemeInfo("ocean", "Ocean", "සාගර නිල්"),
        MathraThemeInfo("forest", "Forest", "වන කොළ"),
        MathraThemeInfo("sunset", "Sunset", "හිරු බැස යන රතු"),
    )

    fun palette(id: String, dark: Boolean): MathraPalette = when (id) {
        "ocean" -> if (dark) OceanDark else OceanLight
        "forest" -> if (dark) ForestDark else ForestLight
        "sunset" -> if (dark) SunsetDark else SunsetLight
        else -> if (dark) RoyalDark else RoyalLight
    }

    // ---------- Royal Purple (original Mathra look) ----------
    private val RoyalDark = MathraPalette(
        bg = Color(0xFF0A0612), bgDeep = Color(0xFF160B29), primary = Color(0xFF7B2FF7),
        primaryDeep = Color(0xFF3B0A6B), primaryLight = Color(0xFFB07CFF),
        accent = Color(0xFFF6C453), accentDeep = Color(0xFFC9942B), blue = Color(0xFF3ED3FF),
        text = Color(0xFFF5F2FF), glass = Color(0x33FFFFFF)
    )
    private val RoyalLight = MathraPalette(
        bg = Color(0xFFFAF7FF), bgDeep = Color(0xFFEFE7FF), primary = Color(0xFF6A1FE0),
        primaryDeep = Color(0xFFE4D7FF), primaryLight = Color(0xFF5B2BC0),
        accent = Color(0xFF9A6400), accentDeep = Color(0xFF7A4F00), blue = Color(0xFF0B79B0),
        text = Color(0xFF1B1030), glass = Color(0x14000000)
    )

    // ---------- Ocean ----------
    private val OceanDark = MathraPalette(
        bg = Color(0xFF03101C), bgDeep = Color(0xFF082338), primary = Color(0xFF1E88E5),
        primaryDeep = Color(0xFF0B3A63), primaryLight = Color(0xFF7CC4FF),
        accent = Color(0xFF4DD0E1), accentDeep = Color(0xFF00A5B8), blue = Color(0xFF9AE6FF),
        text = Color(0xFFEAF6FF), glass = Color(0x33FFFFFF)
    )
    private val OceanLight = MathraPalette(
        bg = Color(0xFFF3FAFF), bgDeep = Color(0xFFDDF0FF), primary = Color(0xFF0F6DC2),
        primaryDeep = Color(0xFFCDE6FA), primaryLight = Color(0xFF0B5A9E),
        accent = Color(0xFF00788A), accentDeep = Color(0xFF005F6E), blue = Color(0xFF0A6C94),
        text = Color(0xFF0A1C2B), glass = Color(0x14000000)
    )

    // ---------- Forest ----------
    private val ForestDark = MathraPalette(
        bg = Color(0xFF07120B), bgDeep = Color(0xFF0F2418), primary = Color(0xFF2E9E5B),
        primaryDeep = Color(0xFF12452A), primaryLight = Color(0xFF8FE0A8),
        accent = Color(0xFFE6C65A), accentDeep = Color(0xFFB8963A), blue = Color(0xFF7FE3C4),
        text = Color(0xFFEFFAF1), glass = Color(0x33FFFFFF)
    )
    private val ForestLight = MathraPalette(
        bg = Color(0xFFF5FBF6), bgDeep = Color(0xFFDFF2E3), primary = Color(0xFF1E7A43),
        primaryDeep = Color(0xFFCDE9D4), primaryLight = Color(0xFF176238),
        accent = Color(0xFF8A6A00), accentDeep = Color(0xFF6B5200), blue = Color(0xFF0E7A5F),
        text = Color(0xFF0F2016), glass = Color(0x14000000)
    )

    // ---------- Sunset ----------
    private val SunsetDark = MathraPalette(
        bg = Color(0xFF140808), bgDeep = Color(0xFF2B1010), primary = Color(0xFFF4511E),
        primaryDeep = Color(0xFF6B1F0A), primaryLight = Color(0xFFFF9E7A),
        accent = Color(0xFFFFC857), accentDeep = Color(0xFFD99A1E), blue = Color(0xFFFF8FB1),
        text = Color(0xFFFFF3EE), glass = Color(0x33FFFFFF)
    )
    private val SunsetLight = MathraPalette(
        bg = Color(0xFFFFF8F4), bgDeep = Color(0xFFFFE6DA), primary = Color(0xFFD1380A),
        primaryDeep = Color(0xFFFFD9C8), primaryLight = Color(0xFFB02D07),
        accent = Color(0xFF9C6200), accentDeep = Color(0xFF7A4C00), blue = Color(0xFFB02560),
        text = Color(0xFF2A1008), glass = Color(0x14000000)
    )
}

/**
 * Holds the palette currently in use. Compose tracks reads of [palette], so every
 * screen that reads a Mathra* colour below recomposes automatically when the user
 * changes theme or switches between dark / light.
 */
object ThemeState {
    var palette by mutableStateOf(MathraThemes.palette("royal", dark = true))
    var isDark by mutableStateOf(true)
}

// The existing screens keep using these names; they now follow the active theme.
val MathraBgBlack: Color get() = ThemeState.palette.bg
val MathraBgDeep: Color get() = ThemeState.palette.bgDeep
val MathraPurple: Color get() = ThemeState.palette.primary
val MathraPurpleDeep: Color get() = ThemeState.palette.primaryDeep
val MathraPurpleLight: Color get() = ThemeState.palette.primaryLight
val MathraGold: Color get() = ThemeState.palette.accent
val MathraGoldDeep: Color get() = ThemeState.palette.accentDeep
val MathraBlue: Color get() = ThemeState.palette.blue
val MathraWhite: Color get() = ThemeState.palette.text
val MathraGlassSurface: Color get() = ThemeState.palette.glass
