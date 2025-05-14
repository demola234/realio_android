package com.realio.app.feature.authentication.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnimatedDotLoadingIndicator() {
    val dots = 3
    val delayUnit = 200

    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        repeat(dots) { index ->
            LoadingDot(
                delay = index * delayUnit
            )
        }
    }
}

@Composable
fun LoadingDot(delay: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "dot")

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1200
                0f at 0 with LinearEasing
                -8f at 300 with LinearEasing
                0f at 600 with LinearEasing
                0f at 1200 with LinearEasing
            },
            initialStartOffset = StartOffset(delay)
        ),
        label = "offset"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.2f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1200
                0.2f at 0 with LinearEasing
                1f at 300 with LinearEasing
                0.2f at 600 with LinearEasing
                0.2f at 1200 with LinearEasing
            },
            initialStartOffset = StartOffset(delay)
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .offset(y = offsetY.dp)
            .alpha(alpha)
            .size(10.dp)
            .clip(CircleShape)
            .background(Color.White)
    )
}