package com.realio.app.feature.authentication.presentation.screen

import ThemedImage
import android.app.Activity
import android.util.Log
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.realio.app.R
import com.realio.app.core.common.Dimensions.PADDING_LARGE
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.realio.app.core.navigation.RealioScreenConsts
import com.realio.app.core.ui.components.buttons.AppButton
import com.realio.app.core.ui.components.textfields.AppTextField
import com.realio.app.core.ui.theme.PrimaryColorLight
import com.realio.app.core.ui.theme.RealioTheme
import com.realio.app.feature.authentication.presentation.viewModel.AuthState
import com.realio.app.feature.authentication.presentation.viewModel.IGoogleViewModel
import com.realio.app.feature.authentication.presentation.viewModel.PreviewGoogleViewModel
import com.realio.app.feature.authentication.presentation.viewModel.UserData

@Composable
fun LoginScreen(
    navController: NavController? = null,
    webClientId: String? = null,
    onSignInSuccess: (UserData) -> Unit?,
    authViewModel: IGoogleViewModel
    ) {
    val emailField = rememberSaveable { mutableStateOf("") }
    val passwordField = rememberSaveable { mutableStateOf("") }
    val valid = remember(emailField.value, passwordField.value) {
        emailField.value.trim().isNotEmpty() && passwordField.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current

    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()

    // Initialize Google Sign-In when the composable is first created
    LaunchedEffect(Unit) {
        authViewModel.initGoogleSignIn(context, webClientId.toString())
    }

    // Set up the activity result launcher for Google Sign-In
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        result.data?.extras?.keySet()?.forEach { key ->
            val value = result.data?.extras?.get(key)
            Log.d("GoogleSignIn", "Extra: $key = $value")
        }
        Log.d("Google Result", "Result Code: ${result}")
        when (result.resultCode) {

            Activity.RESULT_OK -> {
                // Success path
                authViewModel.handleSignInResult(result.data)
            }
            Activity.RESULT_CANCELED -> {
                // Extract error information if available
                val statusCode = result.data?.extras?.getInt("googleSignInStatus", 0) ?: 0
                val errorMessage = if (statusCode != 0) {
                    "Sign-in failed with status code: $statusCode"
                } else {
                    "Sign-in was canceled"
                }

                Log.d("GoogleSignIn", "Sign-in canceled. Status code: $statusCode")

                // Update the auth state to show the error
                authViewModel.updateAuthState(AuthState.Error(errorMessage))
            }
            else -> {
                Log.d("GoogleSignIn", "Unexpected result code: ${result.resultCode}")
                authViewModel.updateAuthState(AuthState.Error("Unexpected error during sign-in"))
            }
        }
    }

    // Observe authentication state changes
// In your LaunchedEffect for authState
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.OneTapSignInReady -> {
                // Launch Sign-In UI
                val request = IntentSenderRequest.Builder(
                    (authState as AuthState.OneTapSignInReady).intentSender
                ).build()
                signInLauncher.launch(request)
            }
            is AuthState.RedirectingToBrowser -> {
                // Launch browser for OAuth
                val intent = (authState as AuthState.RedirectingToBrowser).intent
                context.startActivity(intent)
            }
            is AuthState.Authenticated -> {
                // Handle successful sign-in
                val userData = (authState as AuthState.Authenticated).userData
                onSignInSuccess(userData)
            }
            else -> {}
        }
    }

    Scaffold(
        // remove top bar
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

            Spacer(modifier = Modifier.height(24.dp))
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
                text = "Welcome to Realio!",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 24.sp
                ),
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
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .align(Alignment.CenterStart)
                )
            }

            // Login button
            AppButton (
                onClick = {
                    keyboardController?.hide()

                },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
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
                        navController?.navigate(RealioScreenConsts.SignUp.name)
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
                modifier = Modifier.padding(vertical = 24.dp, horizontal = 30.dp)
                    .align(alignment = Alignment.CenterHorizontally)

            )

            // Social login options
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,

            ) {
                // Google
                // In your when (authState) block in the SocialLoginButton section:

                when (authState) {
                    is AuthState.Idle -> {
                        SocialLoginButton(
                            color = MaterialTheme.colorScheme.error,
                            iconResId = R.drawable.google_white,
                            contentDescription = "Continue with Google",
                            onClick = {
                                authViewModel.beginSignIn(context)
                            })
                    }

                    is AuthState.Loading -> {
                        CircularProgressIndicator()
                    }

                    is AuthState.Error -> {
                        val errorMessage = (authState as AuthState.Error).message
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        SocialLoginButton (
                            color = MaterialTheme.colorScheme.error,
                            iconResId = R.drawable.google_white,
                            contentDescription = "Try again with Google",
                            onClick = {
                                authViewModel.beginSignIn(context)
                            })
                    }

                    is AuthState.Authenticated -> {
                        // This UI will be shown briefly before navigation occurs
                        Text("Sign in successful!")
                    }

                    is AuthState.SignedOut -> {
                        Text("You have been signed out.")
                        Spacer(modifier = Modifier.height(16.dp))
                        SocialLoginButton(
                            color = MaterialTheme.colorScheme.error,
                            iconResId = R.drawable.google_white,
                            contentDescription = "Continue with Google",
                            onClick = {
                                authViewModel.beginSignIn(context)
                            })
                    }

                    // Replace TODO() with actual implementations for these states
                    is AuthState.OneTapSignInReady -> {
                        // This state is handled by the LaunchedEffect, no UI needed
                        // (The UI will automatically show the Google sign-in dialog)
                        SocialLoginButton(
                            color = MaterialTheme.colorScheme.error,
                            iconResId = R.drawable.google_white,
                            contentDescription = "Continue with Google",
                            onClick = {
                                authViewModel.beginSignIn(context)
                            })
                    }

                    is AuthState.RedirectingToBrowser -> {
                        // This state is handled by the LaunchedEffect, show loading or message
                        CircularProgressIndicator()
                        Text(
                            text = "Redirecting to browser...",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
                // Apple
                SocialLoginButton(
                    color = MaterialTheme.colorScheme.onSurface,
                    iconResId = R.drawable.apple_white,
                    contentDescription = "Continue with Apple",
                    onClick = {}
                )

                // Facebook
                SocialLoginButton(
                    color = PrimaryColorLight,
                    iconResId = R.drawable.facebook,
                    contentDescription = "Continue with Facebook",
                    onClick = {}
                )
            }
        }
    }
}

@Composable
private fun SocialLoginButton(
    iconResId: Int,
    contentDescription: String,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clickable { onClick() }
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
            modifier = Modifier.size(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val fakeViewModel = PreviewGoogleViewModel()
    RealioTheme {
        LoginScreen(
            navController = null,
            webClientId = "dummy-web-client-id",
            onSignInSuccess = {},
            authViewModel = fakeViewModel
        )
    }
}
