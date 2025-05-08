package com.realio.app.feature.authentication.presentation.screen

import ThemedImage
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.realio.app.R
import com.realio.app.core.common.Dimensions.PADDING_LARGE
import com.realio.app.core.navigation.RealioScreenConsts
import com.realio.app.core.ui.components.buttons.AppButton
import com.realio.app.core.ui.components.textfields.AppTextField

@Composable
fun SignUpScreen(navController: NavController) {
    val nameField = rememberSaveable { mutableStateOf("") }
    val emailField = rememberSaveable { mutableStateOf("") }
    val passwordField = rememberSaveable { mutableStateOf("") }
    val confirmPasswordField = rememberSaveable { mutableStateOf("") }
    val rememberMe = rememberSaveable { mutableStateOf(false) }
    val valid = remember(emailField.value, passwordField.value) {
        emailField.value.trim().isNotEmpty() && passwordField.value.trim().isNotEmpty()
    }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = PADDING_LARGE)
                .fillMaxSize(),
        ) {

            // Logo
            ThemedImage(
                darkImage = R.drawable.dark_logo,
                lightImage = R.drawable.light_logo,
                modifier = Modifier
                    .height(48.dp)
                    .width(100.dp)
            )

            // Sign up text
            Text(
                text = "Sign up",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            // create account text
            Text(
                text = "Create an account to get started ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            // Name
            AppTextField(
                placeHolder = "Name",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                value = nameField,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            AppTextField(
                placeHolder = "Email Address",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                value = emailField,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            AppTextField(
                placeHolder = "Password",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                value = emailField,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            AppTextField(
                placeHolder = "Confirm Password",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                value = emailField,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            // I've read and agree with the Terms and Conditions and the Privacy Policy.
            Row {
                Checkbox(
                   checked = rememberMe.value,
                    onCheckedChange = { rememberMe.value = it },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "I've read and agree with the Terms and Conditions and the Privacy Policy.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Sign up button
            AppButton(

                onClick = { },
                enabled = valid,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Continue with Email",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Already have an account?
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = "Sign in",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        navController.navigate(RealioScreenConsts.Login.name)
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Or continue with google
            SocialAppButton(
                icon = painterResource(id = R.drawable.google_white),
                text = "Continue with Google",
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            SocialAppButton(
                icon = painterResource(id = R.drawable.apple_white),
                text = "Continue with Apple",
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Or continue with facebook
            SocialAppButton(
                icon = painterResource(id = R.drawable.facebook),
                text = "Continue with Facebook",
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun SocialAppButton(
    icon: Painter,
    text: String,
    onClick: () -> Unit,
    color: ButtonColors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground),
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    AppButton(
        onClick = onClick,
        modifier = modifier,
        colors = color,
        enabled = true
    ) {
        Row {
            Image(
                painter = icon,
                contentDescription = text,
                modifier = Modifier.padding(7.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

}