package com.realio.app.core.ui.components.buttons

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shapes = MaterialTheme.shapes,
    contentPadding: PaddingValues = PaddingValues(vertical = 16.dp),
    enabled: Boolean = true,
    borderEnabled: Boolean? = true,
    borderColor: Color? = Color.Black,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    content: @Composable (() -> Unit)
) {
    var isPressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        label = "ScaleAnimation"
    )

    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.6f,
        label = "AlphaAnimation"
    )

    Button(
        onClick = {
            if (enabled) {
                scope.launch {
                    isPressed = true
                    delay(100)
                    isPressed = false
                    onClick()
                }
            }
        },
        modifier = modifier
            .scale(scale)
            .border(
                width = if (borderEnabled == true) 1.dp else 0.dp,
                color = borderColor ?: MaterialTheme.colorScheme.primary
            )
            .alpha(alpha),
        shape = shape.medium,
        contentPadding = contentPadding,
        enabled = enabled,
        colors = colors,
        interactionSource = remember { MutableInteractionSource() },
    ) {
        content()
    }
}