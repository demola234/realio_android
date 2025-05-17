package com.realio.app.feature.authentication.presentation.screen

import ThemedImage
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.MutableTransitionState
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.realio.app.R
import com.realio.app.core.common.Dimensions.PADDING_LARGE
import com.realio.app.core.common.Dimensions.PADDING_SMALL
import com.realio.app.core.navigation.RealioScreenConsts
import com.realio.app.core.ui.components.buttons.AppButton
import com.realio.app.core.ui.components.textfields.AppTextField
import com.realio.app.core.ui.theme.RealioTheme
import com.realio.app.core.utils.ValidationUtils
import com.realio.app.feature.authentication.presentation.components.LoadingPage
import com.realio.app.feature.authentication.presentation.viewModel.LoginState
import com.realio.app.feature.authentication.presentation.viewModel.LoginViewModel
import com.realio.app.feature.authentication.presentation.viewModel.RegisterState
import com.realio.app.feature.authentication.presentation.viewModel.RegisterViewModel

@Composable
fun SignUpScreen(
    navController: NavController? = null,
    registerViewModel: RegisterViewModel = hiltViewModel()
    ) {
    val nameField = rememberSaveable { mutableStateOf("") }
    val emailField = rememberSaveable { mutableStateOf("") }
    val passwordField = rememberSaveable { mutableStateOf("") }
    val confirmPasswordField = rememberSaveable { mutableStateOf("") }
    val rememberMe = rememberSaveable { mutableStateOf(false) }
    val valid = remember(emailField.value, passwordField.value) {
        emailField.value.trim().isNotEmpty() && passwordField.value.trim().isNotEmpty()
    }

    val scrollState = rememberScrollState()

    var errorDialog by remember { mutableStateOf(false) }
    var loggedInDialog by remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
    val showLoading = rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val registerState by registerViewModel.registerState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val dialogVisibilityState = remember {
        MutableTransitionState(false)
    }

    val emailError = remember {
        derivedStateOf {
            if (emailField.value.isEmpty()) {
                ""
            } else if (!ValidationUtils.isValidEmail(emailField.value)) {
                "Please enter a valid email address"
            } else {
                ""
            }
        }
    }

    val passwordError = remember {
        derivedStateOf {
            if (passwordField.value.isEmpty()) {
                ""
            } else if (ValidationUtils.getPasswordStrength(passwordField.value) == com.realio.app.core.utils.PasswordStrength.WEAK) {
                ValidationUtils.getPasswordRequirementsMessage()
            } else {
                ""
            }
        }
    }

    val confirmPasswordError = remember {
        derivedStateOf {
            if (confirmPasswordField.value.isEmpty()) {
                ""
            } else if (!ValidationUtils.doPasswordsMatch(passwordField.value, confirmPasswordField.value)) {
                "Passwords do not match"
            } else {
                ""
            }
        }
    }

    // Form validity
    val isValidForm = remember {
        derivedStateOf {
            nameField.value.isNotEmpty() &&
                    ValidationUtils.isValidEmail(emailField.value) &&
                    ValidationUtils.getPasswordStrength(passwordField.value) != com.realio.app.core.utils.PasswordStrength.WEAK &&
                    ValidationUtils.doPasswordsMatch(passwordField.value, confirmPasswordField.value) &&
                    rememberMe.value
        }
    }

    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterState.Loading -> {
                // Set dialogVisibilityState to true to show loading page
                dialogVisibilityState.targetState = true
            }
            is RegisterState.Success -> {
                // Keep dialog visible to show success message
                dialogVisibilityState.targetState = false
                loggedInDialog = true
            }
            is RegisterState.Error -> {
                // Hide loading dialog on error and show error dialog instead
                dialogVisibilityState.targetState = false
                errorDialog = true
            }
            is RegisterState.Idle -> {
                // Hide loading dialog in idle state
                dialogVisibilityState.targetState = false
            }
        }
    }

    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterState.Idle -> {
                isLoading.value = false
            }
            is RegisterState.Loading -> {
                isLoading.value = true
            }
            is RegisterState.Success -> {
                // Keep loading visible but change text
                isLoading.value = true
            }
            is RegisterState.Error -> {
                isLoading.value = false
                errorDialog = true
            }
        }
    }


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

        Box {
            when (val currentState = registerState) {
                is RegisterState.Idle -> Box {}
                is RegisterState.Loading -> {
                    LoadingPage(
                        onClose = {
                            // Set both states to handle closing
                            showLoading.value = false
                            dialogVisibilityState.targetState = false
                        },
                        dialogVisibilityState = dialogVisibilityState
                    )
                }

                is RegisterState.Success -> {
                    navController?.navigate(RealioScreenConsts.Otp.name + "/${emailField.value}")
                }

                is RegisterState.Error -> {
                    // Error dialog handling remains the same
                    if (errorDialog) {
                        AlertDialog(
                            onDismissRequest = { errorDialog = false },
                            title = { Text("Login Error") },
                            text = { Text(currentState.message) },
                            confirmButton = {
                                TextButton(onClick = { errorDialog = false }) {
                                    Text("OK")
                                }
                            }
                        )
                    }
                }
            }

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

                Spacer(modifier = Modifier.height(10.dp))
                AppTextField(
                    placeHolder = "Email Address",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    value = emailField,
                    onValueChange = {},
                    isError = emailError.value.isNotEmpty(),
                    errorMessage = emailError.value,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                PasswordTextField(
                    value = passwordField,
                    placeHolder = "Password",
                    isError = passwordError.value.isNotEmpty(),
                    errorMessage = passwordError.value,
                    showStrengthIndicator = true,
                    imeAction = ImeAction.Next,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                PasswordTextField(
                    value = confirmPasswordField,
                    placeHolder = "Confirm Password",
                    imeAction = ImeAction.Done,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    isError = confirmPasswordError.value.isNotEmpty(),
                    errorMessage = confirmPasswordError.value,
                    showStrengthIndicator = false,

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
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
//                        textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append("Terms and Conditions")
                        }
                        pop()

                        append(" and the ")

                        // Privacy Policy part
                        pushStringAnnotation(tag = "privacy", annotation = "privacy_link")
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
//                        textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
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
                            annotatedString.getStringAnnotations(
                                tag = "terms",
                                start = offset,
                                end = offset
                            )
                                .firstOrNull()?.let {
                                    // Open terms and conditions URL
                                    val termsUrl = "https://example.com/terms"
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(termsUrl))
                                    context.startActivity(intent)
                                }

                            annotatedString.getStringAnnotations(
                                tag = "privacy",
                                start = offset,
                                end = offset
                            )
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
                    enabled = isValidForm.value,
                    onClick = {
                        isLoading.value = true
                        registerViewModel.register(
                            name = nameField.value,
                            email = emailField.value,
                            password = passwordField.value
                        )
                        keyboardController?.hide()
                    },
                    borderEnabled = false,
                    borderColor = Color.Transparent,
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
                    color = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimary),
                    borderEnabled = true,
                    borderColor = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
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
}


@Composable
fun SocialAppButton(
    icon: Painter,
    text: String,
    borderColor: Color = Color.Black,
    borderEnabled: Boolean = false,
    onClick: () -> Unit,
    color: ButtonColors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onBackground),
    modifier: Modifier = Modifier.fillMaxWidth(),
    textStyle: TextStyle? = null,
) {
    AppButton(
        onClick = onClick,
        modifier = modifier,
        colors = color,
        borderColor = borderColor,
        borderEnabled = borderEnabled == true,
        enabled = true
    ) {
        Row {
            Image(
                painter = icon,
                contentDescription = text,
                modifier = Modifier.padding(3.dp),
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = text,
                style = textStyle ?: MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview()
@Composable
fun SignUpScreenPreview() {
    RealioTheme {
        SignUpScreen(
            navController = null,
        )
    }
}