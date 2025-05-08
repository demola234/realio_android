package com.realio.app.feature.authentication.presentation.screen

import ThemedImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.realio.app.R
import com.realio.app.core.common.Dimensions.PADDING_LARGE
import com.realio.app.core.common.Dimensions.PADDING_SMALL
import com.realio.app.core.navigation.RealioAppNavigation
import com.realio.app.core.navigation.RealioScreenConsts
import com.realio.app.core.ui.components.buttons.AppButton
import com.realio.app.core.ui.components.textfields.AppTextField
import com.realio.app.core.ui.theme.PrimaryColorLight

@Composable
fun LoginScreen(navController: NavController) {
    val emailField = rememberSaveable { mutableStateOf("") }
    val passwordField = rememberSaveable { mutableStateOf("") }
    val valid = remember(emailField.value, passwordField.value) {
        emailField.value.trim().isNotEmpty() && passwordField.value.trim().isNotEmpty()
    }

    val scrollState = rememberScrollState()

    Scaffold(
//        remove top bar
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

            // App illustration/image
            Image(
                painter = painterResource(id = R.drawable.onboarding1),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(24.dp)
                    .height(220.dp)
                    .fillMaxWidth()
            )

            // Welcome text
            Text(
                text = "Welcome!",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(vertical = 15.dp)
            )

            // Email field
            AppTextField(
                placeHolder = "Email Address",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                value = emailField,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field with visibility toggle
            AppTextField(
                value = passwordField,
                placeHolder = "Password",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                onValueChange = {},
                trailingIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.logo_dark_wrapper),
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Forgot password
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Forgot password?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    ),
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .align(Alignment.CenterStart)
                )
            }

            // Login button
            AppButton (
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Sign up option
            Row(
                modifier = Modifier.padding(vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Not a member? ",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Register now",
                    modifier = Modifier.clickable(){
                        navController.navigate(RealioScreenConsts.SignUp.name)
                    },
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary
                    )

                )
            }

            // Divider
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
            )

            // Continue with text
            Text(
                text = "Or continue with",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 24.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )

            // Social login options
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                // Google
                SocialLoginButton(
                    color = MaterialTheme.colorScheme.error,
                    iconResId = R.drawable.google_white,
                    contentDescription = "Continue with Google"
                )

                // Apple
                SocialLoginButton(
                    color = MaterialTheme.colorScheme.onSurface,
                    iconResId = R.drawable.apple_white,
                    contentDescription = "Continue with Apple"
                )

                // Facebook
                SocialLoginButton(
                    color = PrimaryColorLight,
                    iconResId = R.drawable.facebook,
                    contentDescription = "Continue with Facebook"
                )
            }
        }
    }
}

@Composable
private fun SocialLoginButton(
    iconResId: Int,
    contentDescription: String,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}