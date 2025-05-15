package com.realio.app.feature.authentication.presentation.screen

import ThemedImage
import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.realio.app.R
import com.realio.app.core.common.Dimensions.PADDING_LARGE
import com.realio.app.core.common.Dimensions.PADDING_SMALL
import com.realio.app.core.navigation.RealioScreenConsts
import com.realio.app.core.ui.components.buttons.AppButton
import com.realio.app.core.ui.components.textfields.AppTextField
import com.realio.app.core.ui.theme.RealioTheme

@Composable
fun SignUpScreen(navController: NavController? = null) {
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
            Spacer(modifier = Modifier.height(PADDING_LARGE))

            // Logo
            ThemedImage(
                darkImage = R.drawable.dark_logo,
                lightImage = R.drawable.light_logo,
                modifier = Modifier
                    .height(28.dp)
                    .width(82.dp)
            )

            Spacer(modifier = Modifier.height(PADDING_LARGE))

            // Sign up text
            Text(
                text = "Sign up",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(800),
                    color = MaterialTheme.colorScheme.onBackground,
                ),
                color = MaterialTheme.colorScheme.onBackground,

            )
            Spacer(modifier = Modifier.height(PADDING_SMALL))
            // create account text
            Text(
                text = "Create an account to get started ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(PADDING_LARGE))
            // Name
            AppTextField(
                placeHolder = "Name",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                value = nameField,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))
            AppTextField(
                placeHolder = "Email Address",
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                value = emailField,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))
            AppTextField(
                placeHolder = "Password",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                value = passwordField,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))
            AppTextField(
                placeHolder = "Confirm Password",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                value = confirmPasswordField,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(14.dp))

            // I've read and agree with the Terms and Conditions and the Privacy Policy.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberMe.value,
                    onCheckedChange = { rememberMe.value = it },
                    modifier = Modifier.padding(end = 2.dp)
                )

                val context = LocalContext.current

                val annotatedString = buildAnnotatedString {
                    append("I've read and agree with the ")

                    // Terms and Conditions part
                    pushStringAnnotation(tag = "terms", annotation = "terms_link")
                    withStyle(style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
//                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                    )) {
                        append("Terms and Conditions")
                    }
                    pop()

                    append(" and the ")

                    // Privacy Policy part
                    pushStringAnnotation(tag = "privacy", annotation = "privacy_link")
                    withStyle(style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
//                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                    )) {
                        append("Privacy Policy")
                    }
                    pop()

                    append(".")
                }

                ClickableText(
                    text = annotatedString,
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                        fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(tag = "terms", start = offset, end = offset)
                            .firstOrNull()?.let {
                                // Open terms and conditions URL
                                val termsUrl = "https://example.com/terms"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(termsUrl))
                                context.startActivity(intent)
                            }

                        annotatedString.getStringAnnotations(tag = "privacy", start = offset, end = offset)
                            .firstOrNull()?.let {
                                // Open privacy policy URL
                                val privacyUrl = "https://example.com/privacy"
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyUrl))
                                context.startActivity(intent)
                            }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Sign up button
            AppButton(
                
                onClick = {
                    navController?.navigate(RealioScreenConsts.Otp.name + "/${emailField.value}")
                },
                enabled = valid,

                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Continue with Email",
                    style = MaterialTheme.typography.bodyMedium.copy(

                    )
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
                        navController?.navigate(RealioScreenConsts.Login.name)
                    }
                )
            }
           // Space to bottom
            Spacer(modifier = Modifier.height(64.dp))
            // Or continue with google
            SocialAppButton(
                icon = painterResource(id = R.drawable.google_colored),
                text = "Continue with Google",
                onClick = { },
                color =  ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                borderEnabled = true,
                borderColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            SocialAppButton(
                icon = painterResource(id = R.drawable.apple_white),
                text = "Continue with Apple",
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
    borderColor: Color? = null,
    borderEnabled: Boolean? = false,
    onClick: () -> Unit,
    color: ButtonColors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground),
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    AppButton(
        onClick = onClick,
        modifier = modifier,
        colors = color,
        borderColor = borderColor,
        borderEnabled = borderEnabled,
        enabled = true
    ) {
        Row {
            Image(
                painter = icon,
                contentDescription = text,
                modifier = Modifier.padding(3.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(

                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    RealioTheme {
        SignUpScreen(
            navController = null,
        )
    }
}