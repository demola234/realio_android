package com.realio.app.feature.authentication.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realio.app.R
import com.realio.app.core.utils.PasswordStrength
import com.realio.app.core.utils.ValidationUtils

@Composable
fun PasswordTextField(
    value: MutableState<String>,
    placeHolder: String = "Password",
    imeAction: ImeAction = ImeAction.Done,
    onValueChange: (String) -> Unit = {},
    showStrengthIndicator: Boolean = true,
    modifier: Modifier = Modifier.fillMaxWidth(),
    isError: Boolean = false,
    errorMessage: String = ""
) {
    // Local state to track password visibility - using var instead of MutableState
    var passwordVisible by remember { mutableStateOf(false) }
    val passwordStrength = remember(value.value) {
        ValidationUtils.getPasswordStrength(value.value)
    }

    Column(modifier = modifier) {

        OutlinedTextField(
            value = value.value,
            onValueChange = {
                value.value = it
                onValueChange(it)
            },
            label = { Text(placeHolder) },
            singleLine = true,
            modifier = modifier,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = imeAction
            ),
            // Important: This is where we control the password visibility
            visualTransformation = if (passwordVisible) {
                // When visible, use no transformation (show the actual text)
                VisualTransformation.None
            } else {
                // When not visible, use password transformation (hide with dots)
                PasswordVisualTransformation()
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        // Toggle the visibility state
                        passwordVisible = !passwordVisible
                        // Debug log
                        println("Password visibility toggled: $passwordVisible")
                    }
                ) {
                    // Show the appropriate icon based on visibility state
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.eye_open else R.drawable.eye_closed
                        ),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.secondary,
                unfocusedTextColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.LightGray,
            ),
        )

        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }

        if (showStrengthIndicator && value.value.isNotEmpty()) {
            Box(modifier = Modifier.padding(top = 8.dp)) {
                // Background track
                LinearProgressIndicator(
                    progress = 1f,
                    color = Color.Gray.copy(alpha = 0.3f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    strokeCap = StrokeCap.Round
                )

                // Foreground progress
                LinearProgressIndicator(
                    progress = when(passwordStrength) {
                        PasswordStrength.WEAK -> 0.33f
                        PasswordStrength.MEDIUM -> 0.67f
                        PasswordStrength.STRONG -> 1f
                    },
                    color = passwordStrength.getColor(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    strokeCap = StrokeCap.Round
                )
            }

            Text(
                text = "Password strength: ${passwordStrength.getLabel()}",
                color = passwordStrength.getColor(),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}