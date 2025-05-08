package com.realio.app.core.ui.components.textfields

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AppTextField(
    value:  MutableState<String>,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeHolder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Animate the background color when the text field is focused
    val animatedBgColor by animateColorAsState(
        targetValue = if (isFocused) MaterialTheme.colorScheme.surface else Color.Transparent,
        label = "FocusBackgroundAnimation"
    )

    OutlinedTextField(
        value = value.value,
        onValueChange = { value.value = it },
        label = { Text(text = placeHolder) },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = onAction,
        singleLine = true,
        isError = isError,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(10.dp),
        suffix = trailingIcon,
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.inversePrimary,
            unfocusedTextColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.onBackground,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.LightGray
        ),
    )
}