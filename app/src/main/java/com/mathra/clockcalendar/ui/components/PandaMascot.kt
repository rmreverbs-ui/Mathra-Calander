package com.mathra.clockcalendar.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mathra.clockcalendar.R

/**
 * The Mathra panda mascot: the official logo artwork with a gentle floating animation.
 *
 * [festivalKey] (Holiday.festivalKey, or "poya") dresses the panda for the occasion using
 * vector overlays drawn on top of the logo: a Santa hat for Christmas, and a festival motif
 * (Vesak lantern, Avurudu sun, Eid crescent, ...) in the corner for the others.
 * These are simple vector accessories, not hand-illustrated character art.
 *
 * ILLUSTRATED ART SLOT: if a drawable named `panda_<festivalKey>` exists in res/drawable
 * (e.g. panda_christmas.png, panda_avurudu.png, panda_vesak.png, panda_poya.png), it is shown
 * instead of the logo + vector costume. No code change is needed when the illustrator's files
 * are added; festivals without a file keep the vector costume. See ILLUSTRATOR_BRIEF.md.
 */
@Composable
fun PandaMascot(modifier: Modifier = Modifier, size: Int = 140, festivalKey: String? = null) {
    val infiniteTransition = rememberInfiniteTransition(label = "panda_float")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "panda_offset"
    )

    val context = LocalContext.current
    val artRes = remember(festivalKey) {
        if (festivalKey == null) 0
        else context.resources.getIdentifier("panda_$festivalKey", "drawable", context.packageName)
    }

    Box(
        modifier = modifier
            .size(size.dp)
            .graphicsLayer { translationY = offsetY }
    ) {
        if (artRes != 0) {
            Image(
                painter = painterResource(id = artRes),
                contentDescription = "Mathra Panda mascot",
                modifier = Modifier.size(size.dp)
            )
            return@Box
        }
        Image(
            painter = painterResource(id = R.drawable.mathra_logo),
            contentDescription = "Mathra Panda mascot",
            modifier = Modifier.size(size.dp)
        )
        when (festivalKey) {
            null -> Unit
            "christmas" -> Canvas(modifier = Modifier.size(size.dp)) { santaHat() }
            else -> FestiveDecor(
                key = festivalKey,
                size = (size * 0.32f).dp,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}

/** Santa hat positioned on the panda's head in the logo (coordinates are fractions of the image). */
private fun androidx.compose.ui.graphics.drawscope.DrawScope.santaHat() {
    val w = size.width
    fun p(x: Float, y: Float) = Offset(w * x, w * y)
    val body = Path().apply {
        moveTo(p(.37f, .20f).x, p(.37f, .20f).y)
        lineTo(p(.60f, .20f).x, p(.60f, .20f).y)
        lineTo(p(.64f, .10f).x, p(.64f, .10f).y)
        lineTo(p(.59f, .045f).x, p(.59f, .045f).y)
        lineTo(p(.47f, .07f).x, p(.47f, .07f).y)
        close()
    }
    drawPath(body, Color(0xFFD62828))
    drawRoundRect(
        Color.White, topLeft = p(.345f, .185f), size = Size(w * .27f, w * .05f),
        cornerRadius = CornerRadius(w * .02f)
    )
    drawCircle(Color.White, w * .035f, p(.615f, .045f))
}
