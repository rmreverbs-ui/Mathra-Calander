package com.mathra.clockcalendar.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/**
 * Simple vector festival decorations drawn with Canvas (no image files needed).
 * These are decorative motifs, not the illustrated festival-panda characters.
 *
 * [key] is Holiday.festivalKey (vesak, avurudu, pongal, eid, christmas, deepavali,
 * independence, poson) or "poya" for an ordinary Poya day. Unknown keys draw nothing.
 */
@Composable
fun FestiveDecor(key: String?, modifier: Modifier = Modifier, animate: Boolean = true, size: Dp = 64.dp) {
    if (key == null) return
    val transition = rememberInfiniteTransition(label = "festive")
    val phase by transition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(2400, easing = LinearEasing), RepeatMode.Reverse),
        label = "festive_phase"
    )
    val t = if (animate) phase else 0.5f

    // Vesak lanterns gently sway; everything else is drawn still with a soft pulse.
    val sway = if (key == "vesak") (t - 0.5f) * 10f else 0f
    Canvas(modifier = modifier.size(size).rotate(sway)) {
        when (key) {
            "vesak" -> lantern()
            "avurudu", "pongal" -> sunAndPot(t)
            "eid" -> crescent()
            "christmas" -> tree(t)
            "deepavali" -> diya(t)
            "independence" -> flag()
            "poya", "poson" -> fullMoon(t)
        }
    }
}

private fun DrawScope.star(center: Offset, outer: Float, inner: Float, points: Int, color: Color) {
    val path = Path()
    for (i in 0 until points * 2) {
        val r = if (i % 2 == 0) outer else inner
        val a = Math.PI / points * i - Math.PI / 2
        val p = Offset(center.x + (r * cos(a)).toFloat(), center.y + (r * sin(a)).toFloat())
        if (i == 0) path.moveTo(p.x, p.y) else path.lineTo(p.x, p.y)
    }
    path.close()
    drawPath(path, color)
}

private fun DrawScope.lantern() {
    val w = size.width; val h = size.height
    val cx = w / 2
    drawLine(Color(0xFFC9942B), Offset(cx, 0f), Offset(cx, h * 0.12f), strokeWidth = 3f)
    val body = Path().apply {
        moveTo(cx, h * 0.12f)
        lineTo(w * 0.80f, h * 0.38f)
        lineTo(w * 0.70f, h * 0.70f)
        lineTo(cx, h * 0.80f)
        lineTo(w * 0.30f, h * 0.70f)
        lineTo(w * 0.20f, h * 0.38f)
        close()
    }
    drawPath(body, Brush.verticalGradient(listOf(Color(0xFFFFE27A), Color(0xFFFF8A3D), Color(0xFFE53935))))
    drawPath(body, Color(0xFFC62828), style = Stroke(width = 3f))
    // inner facets
    drawLine(Color(0xFFC62828), Offset(cx, h * 0.12f), Offset(cx, h * 0.80f), strokeWidth = 2f)
    drawLine(Color(0xFFC62828), Offset(w * 0.20f, h * 0.38f), Offset(w * 0.80f, h * 0.38f), strokeWidth = 2f)
    // tassels
    listOf(0.38f, 0.5f, 0.62f).forEach { x ->
        drawLine(Color(0xFFFFD54F), Offset(w * x, h * 0.78f), Offset(w * x, h * 0.96f), strokeWidth = 3f, cap = StrokeCap.Round)
    }
}

private fun DrawScope.sunAndPot(t: Float) {
    val w = size.width; val h = size.height
    val c = Offset(w * 0.5f, h * 0.38f)
    val r = w * 0.2f
    val rays = 12
    for (i in 0 until rays) {
        val a = Math.PI * 2 / rays * i
        val len = r * (1.5f + 0.25f * t)
        drawLine(
            Color(0xFFFFB300),
            Offset(c.x + (r * 1.2f * cos(a)).toFloat(), c.y + (r * 1.2f * sin(a)).toFloat()),
            Offset(c.x + (len * cos(a)).toFloat(), c.y + (len * sin(a)).toFloat()),
            strokeWidth = 3f, cap = StrokeCap.Round
        )
    }
    drawCircle(Brush.radialGradient(listOf(Color(0xFFFFEB3B), Color(0xFFFF9800)), center = c, radius = r), r, c)
    // clay pot with rising milk-rice foam
    val pot = Path().apply {
        moveTo(w * 0.30f, h * 0.74f); lineTo(w * 0.70f, h * 0.74f)
        lineTo(w * 0.64f, h * 0.97f); lineTo(w * 0.36f, h * 0.97f); close()
    }
    drawPath(pot, Color(0xFFB5532E))
    drawCircle(Color(0xFFFFFFFF), w * 0.07f, Offset(w * 0.40f, h * 0.73f))
    drawCircle(Color(0xFFFFFFFF), w * 0.09f, Offset(w * 0.52f, h * (0.71f - 0.02f * t)))
    drawCircle(Color(0xFFFFFFFF), w * 0.07f, Offset(w * 0.62f, h * 0.73f))
}

private fun DrawScope.crescent() {
    val w = size.width; val h = size.height
    val outer = Path().apply { addOval(androidx.compose.ui.geometry.Rect(Offset(w * 0.10f, h * 0.10f), Size(w * 0.75f, h * 0.75f))) }
    val cut = Path().apply { addOval(androidx.compose.ui.geometry.Rect(Offset(w * 0.30f, h * 0.04f), Size(w * 0.70f, h * 0.70f))) }
    drawPath(Path.combine(PathOperation.Difference, outer, cut), Color(0xFFFFD54F))
    star(Offset(w * 0.68f, h * 0.38f), w * 0.13f, w * 0.05f, 5, Color(0xFFFFD54F))
}

private fun DrawScope.tree(t: Float) {
    val w = size.width; val h = size.height
    for (i in 0 until 3) {
        val top = h * (0.18f + i * 0.2f)
        val half = w * (0.18f + i * 0.1f)
        val tri = Path().apply {
            moveTo(w / 2, top); lineTo(w / 2 + half, top + h * 0.3f); lineTo(w / 2 - half, top + h * 0.3f); close()
        }
        drawPath(tri, Color(0xFF2E7D32))
    }
    drawRect(Color(0xFF6D4C41), Offset(w * 0.45f, h * 0.78f), Size(w * 0.10f, h * 0.14f))
    star(Offset(w / 2, h * 0.13f), w * 0.09f, w * 0.04f, 5, Color(0xFFFFD54F).copy(alpha = 0.6f + 0.4f * t))
    listOf(Offset(0.18f, 0.2f), Offset(0.82f, 0.3f), Offset(0.12f, 0.6f), Offset(0.88f, 0.7f)).forEach {
        drawCircle(Color.White.copy(alpha = 0.5f + 0.5f * t), 3f, Offset(w * it.x, h * (it.y + 0.03f * t)))
    }
}

private fun DrawScope.diya(t: Float) {
    val w = size.width; val h = size.height
    val flame = Path().apply {
        moveTo(w / 2, h * (0.14f - 0.02f * t))
        quadraticBezierTo(w * 0.70f, h * 0.38f, w / 2, h * 0.56f)
        quadraticBezierTo(w * 0.30f, h * 0.38f, w / 2, h * (0.14f - 0.02f * t))
    }
    drawPath(flame, Brush.verticalGradient(listOf(Color(0xFFFFF176), Color(0xFFFF6F00))))
    val bowl = Path().apply {
        moveTo(w * 0.12f, h * 0.62f); lineTo(w * 0.88f, h * 0.62f)
        quadraticBezierTo(w * 0.80f, h * 0.92f, w / 2, h * 0.92f)
        quadraticBezierTo(w * 0.20f, h * 0.92f, w * 0.12f, h * 0.62f); close()
    }
    drawPath(bowl, Color(0xFFB5532E))
    drawPath(bowl, Color(0xFFFFB300), style = Stroke(width = 3f))
}

private fun DrawScope.flag() {
    // Simplified: yellow border, green and orange stripes, maroon field (no lion emblem).
    val w = size.width; val h = size.height
    val top = h * 0.2f; val fh = h * 0.6f
    drawRect(Color(0xFFFFB300), Offset(0f, top), Size(w, fh))
    val b = 3f
    drawRect(Color(0xFF00693E), Offset(w * 0.06f, top + b), Size(w * 0.14f, fh - 2 * b))
    drawRect(Color(0xFFEB7400), Offset(w * 0.20f, top + b), Size(w * 0.14f, fh - 2 * b))
    drawRect(Color(0xFF8D2029), Offset(w * 0.37f, top + b), Size(w * 0.57f, fh - 2 * b))
    drawLine(Color(0xFF795548), Offset(0f, top), Offset(0f, h), strokeWidth = 4f)
}

private fun DrawScope.fullMoon(t: Float) {
    val w = size.width; val h = size.height
    val c = Offset(w / 2, h / 2)
    drawCircle(Brush.radialGradient(listOf(Color(0x55FFF3C4), Color.Transparent), center = c, radius = w * 0.5f), w * (0.48f + 0.02f * t), c)
    drawCircle(Color(0xFFFFF3C4), w * 0.28f, c)
    drawCircle(Color(0x22B09A5B), w * 0.06f, Offset(w * 0.44f, h * 0.44f))
    drawCircle(Color(0x22B09A5B), w * 0.04f, Offset(w * 0.58f, h * 0.55f))
    drawCircle(Color(0x22B09A5B), w * 0.03f, Offset(w * 0.52f, h * 0.36f))
}
