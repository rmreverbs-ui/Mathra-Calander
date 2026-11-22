package com.mathra.clockcalendar.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.mathra.clockcalendar.ui.theme.MathraGlassSurface
import com.mathra.clockcalendar.ui.theme.MathraGold
import com.mathra.clockcalendar.ui.theme.MathraPurple

/**
 * The reusable "premium 3D glassmorphism card" used across the dashboard,
 * calendar and settings. Gives every screen the same neon-purple/gold feel.
 */
@Composable
fun GlowCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MathraGlassSurface)
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(listOf(MathraPurple, MathraGold)),
                shape = RoundedCornerShape(20.dp)
            )
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        content()
    }
}
