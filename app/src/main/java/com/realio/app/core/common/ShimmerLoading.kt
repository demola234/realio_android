package com.realio.app.core.common
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape

@Composable
fun ShimmerLoading(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape) {
    // transition rememberInfiniteTransition is used to create a transition that repeats infinitely
    val transition = rememberInfiniteTransition("shimmer")

    // translateAnim is used to create a translation animation that repeats infinitely
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    // shimmerColors is used to create a gradient that repeats infinitely
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    // brush is used to create a gradient that repeats infinitely
    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim.value - 200, translateAnim.value - 200),
        end = Offset(translateAnim.value, translateAnim.value)
    )

    // Box is used to create a rectangle that repeats infinitely
    Box(
        modifier = modifier
            .clip(shape)
            .background(brush)
    )
}