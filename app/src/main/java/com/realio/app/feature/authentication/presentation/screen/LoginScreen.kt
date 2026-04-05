package com.realio.app.feature.authentication.presentation.screen

import ThemedImage
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.MutableTransitionState
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.realio.app.R
import com.realio.app.core.common.Dimensions.PADDING_LARGE
import com.realio.app.core.navigation.RealioScreenConsts
import com.realio.app.core.ui.components.buttons.AppButton
import com.realio.app.core.ui.components.textfields.AppTextField
import com.realio.app.core.ui.theme.PrimaryColorLight
import com.realio.app.core.ui.theme.RealioTheme
import com.realio.app.feature.authentication.presentation.components.LoadingPage
import com.realio.app.feature.authentication.presentation.viewModel.AuthState
import com.realio.app.feature.authentication.presentation.viewModel.IGoogleViewModel
import com.realio.app.feature.authentication.presentation.viewModel.LoginState
import com.realio.app.feature.authentication.presentation.viewModel.LoginViewModel
import com.realio.app.feature.authentication.presentation.viewModel.PreviewGoogleViewModel
import com.realio.app.feature.authentication.presentation.viewModel.UserData

@Composable
fun LoginScreen(
    navController: NavController? = null,
    webClientId: String? = null,
    onSignInSuccess: (UserData) -> Unit,
    authViewModel: IGoogleViewModel,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val authState by authViewModel.authState.collectAsState()
    val loginState by loginViewModel.loginState.collectAsState()

    val isLoginLoading = loginState is LoginState.Loading
    val isGoogleLoading =
        authState is AuthState.Loading || authState is AuthState.RedirectingToBrowser
    val loadingVisibility = remember { MutableTransitionState(false) }

    LaunchedEffect(Unit) {
        authViewModel.initGoogleSignIn(context, webClientId.orEmpty())
    }

    LaunchedEffect(loginState) {
        loadingVisibility.targetState = isLoginLoading
        when (val state = loginState) {
            is LoginState.Loading -> keyboardController?.hide()
            is LoginState.Error -> errorMessage = state.message
            is LoginState.Success -> { /* TODO: navigate to home screen */
            }

            is LoginState.Idle -> {}
        }
    }

    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        when (result.resultCode) {
            Activity.RESULT_OK -> authViewModel.handleSignInResult(result.data)
            else -> authViewModel.updateAuthState(AuthState.Error("Sign-in failed or was canceled"))
        }
    }

    LaunchedEffect(authState) {
        when (val state = authState) {
            is AuthState.OneTapSignInReady -> {
                signInLauncher.launch(IntentSenderRequest.Builder(state.intentSender).build())
            }

            is AuthState.RedirectingToBrowser -> context.startActivity(state.intent)
            is AuthState.Authenticated -> onSignInSuccess(state.userData)
            is AuthState.Error -> errorMessage = state.message
            else -> {}
        }
    }

    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            title = { Text(R.string.error.toString()) },
            text = { Text(errorMessage!!) },
            confirmButton = {
                TextButton(onClick = { errorMessage = null }) { Text("OK") }
            })
    }

    Scaffold { paddingValues ->
        Box {
            if (isLoginLoading) {
                LoadingPage(
                    onClose = { loadingVisibility.targetState = false },
                    dialogVisibilityState = loadingVisibility
                )
            }

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = PADDING_LARGE)
                    .fillMaxSize()
            ) {
                LoginHeader()

                LoginForm(
                    email = email,
                    password = password,
                    isLoading = isLoginLoading,
                    onLoginClick = {
                        loginViewModel.login(email = email.value, password = password.value)
                    },
                    onRegisterClick = {
                        navController?.navigate(RealioScreenConsts.SignUp.name)
                    })

                SocialLoginSection(
                    isGoogleLoading = isGoogleLoading,
                    onGoogleClick = { authViewModel.beginSignIn(context) })
            }
        }
    }
}

@Composable
private fun LoginHeader() {
    Spacer(modifier = Modifier.height(24.dp))

    ThemedImage(
        darkImage = R.drawable.dark_logo,
        lightImage = R.drawable.light_logo,
        modifier = Modifier
            .height(48.dp)
            .width(100.dp)
    )

    Image(
        painter = painterResource(id = R.drawable.onboarding1),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .padding(24.dp)
            .height(220.dp)
            .fillMaxWidth()
    )

    Text(
        text = R.string.welcome_to_realio.toString(),
        style = MaterialTheme.typography.displaySmall.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp
        ),
        modifier = Modifier.padding(vertical = 15.dp)
    )
}

@Composable
private fun LoginForm(
    email: MutableState<String>,
    password: MutableState<String>,
    isLoading: Boolean,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    AppTextField(
        placeHolder = R.string.email_address.toString(),
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next,
        value = email,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(10.dp))

    PasswordTextField(
        value = password,
        placeHolder = R.string.password.toString(),
        imeAction = ImeAction.Done,
        showStrengthIndicator = false,
        modifier = Modifier.fillMaxWidth()
    )

    Text(
        text = R.string.forgot_password.toString(),
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier
            .padding(vertical = 16.dp)
            .clickable { /* TODO: navigate to forgot password screen */ })

    AppButton(
        onClick = onLoginClick,
        enabled = !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(text = R.string.login.toString(), style = MaterialTheme.typography.titleMedium)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(text = R.string.not_a_member.toString(), style = MaterialTheme.typography.bodyMedium)
        Text(
            text = R.string.register_now.toString(),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.clickable { onRegisterClick() })
    }
}

@Composable
private fun SocialLoginSection(
    isGoogleLoading: Boolean,
    onGoogleClick: () -> Unit,
) {
    HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f))

    Text(
        text = R.string.or_continue_with.toString(),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .padding(vertical = 24.dp)
            .fillMaxWidth(),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (isGoogleLoading) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
        } else {
            SocialLoginButton(
                color = MaterialTheme.colorScheme.error,
                iconResId = R.drawable.google_white,
                contentDescription = R.string.continue_with_google.toString(),
                onClick = onGoogleClick
            )
        }

        SocialLoginButton(
            color = MaterialTheme.colorScheme.onSurface,
            iconResId = R.drawable.apple_white,
            contentDescription = R.string.continue_with_apple.toString(),
            onClick = {})

        SocialLoginButton(
            color = PrimaryColorLight,
            iconResId = R.drawable.facebook,
            contentDescription = R.string.continue_with_facebook.toString(),
            onClick = {})
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
            .size(48.dp)
            .clip(CircleShape)
            .background(color)
            .border(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f), CircleShape)
            .clickable { onClick() }, contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = contentDescription,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    RealioTheme {
        LoginScreen(
            navController = null,
            webClientId = "dummy-web-client-id",
            onSignInSuccess = {},
            authViewModel = PreviewGoogleViewModel()
        )
    }
}