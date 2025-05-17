package com.realio.app.feature.authentication.presentation.screen

import OtpInputField
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.realio.app.core.navigation.RealioScreenConsts
import com.realio.app.core.ui.components.buttons.AppButton
import com.realio.app.core.ui.theme.NeutralThreeColorLight
import com.realio.app.core.ui.theme.RealioTheme
import com.realio.app.feature.authentication.presentation.components.LoadingPage
import com.realio.app.feature.authentication.presentation.viewModel.LoginState
import com.realio.app.feature.authentication.presentation.viewModel.RegisterState
import com.realio.app.feature.authentication.presentation.viewModel.ResendState
import com.realio.app.feature.authentication.presentation.viewModel.VerificationState
import com.realio.app.feature.authentication.presentation.viewModel.VerificationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    navController: NavController? = null,
    email: String,
    verificationViewModel: VerificationViewModel = hiltViewModel()
) {

    val isLoading = remember { mutableStateOf(false) }
    var otpValue by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var resendOtpLoading = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val verificationState by verificationViewModel.verificationState.collectAsState()
    val resetState by verificationViewModel.resendState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val dialogVisibilityState = remember {
        MutableTransitionState(false)
    }

    LaunchedEffect(verificationState, resetState) {
        when (verificationState) {
            is VerificationState.Loading -> {
                // Set dialogVisibilityState to true to show loading page
                dialogVisibilityState.targetState = true
            }
            is VerificationState.Success -> {
                // Keep dialog visible to show success message
                dialogVisibilityState.targetState = false
                successMessage = "Otp Verified Successfully"
            }
            is VerificationState.Error -> {
                // Hide loading dialog on error and show error dialog instead
                dialogVisibilityState.targetState = false
                isError = true
                errorMessage = (verificationState as VerificationState.Error).message
            }
            is VerificationState.Idle -> {
                // Hide loading dialog in idle state
                dialogVisibilityState.targetState = false
            }

        }

        when (resetState) {
            is ResendState.Loading -> {
                // Set dialogVisibilityState to true to show loading page
                dialogVisibilityState.targetState = true
            }

            is ResendState.Success -> {
                // Keep dialog visible to show success message
                dialogVisibilityState.targetState = false
                verificationViewModel.resetResendState()
                successMessage = "Otp Resent Successfully!"
            }

            is ResendState.Error -> {
                // Hide loading dialog on error and show error dialog instead
                dialogVisibilityState.targetState = false
                isError = true
                errorMessage = (verificationState as VerificationState.Error).message
            }

            is ResendState.Idle -> {
                // Hide loading dialog in idle state
                dialogVisibilityState.targetState = false
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
            when (val currentState = verificationState) {
                is VerificationState.Idle -> Box {}
                is VerificationState.Loading -> {
                    LoadingPage(
                        onClose = {
                            // Set both states to handle closing
                            isLoading.value = false
                            dialogVisibilityState.targetState = false
                        },
                        dialogVisibilityState = dialogVisibilityState
                    )
                }

                is VerificationState.Success -> {

                    navController?.navigate(RealioScreenConsts.PersonalInfo.name)
                }

                is VerificationState.Error -> {
                    // Error dialog handling remains the same

                }
            }

            Column(
                modifier = Modifier
                    .padding(paddingValues),

                ) {



                IconButton(onClick = { navController?.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Enter confirmation code",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            letterSpacing = 0.08.sp,
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "A 4-digit code was sent to\n${email}",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            color = NeutralThreeColorLight,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    OtpInputField(
                        otpLength = 6,
                        isError = isError,
                        errorMessage = errorMessage,
                        enableSmsAutofill = true,
                        onOtpComplete = { code ->
                            // Handle OTP completion

                            if (code.length == 6 && code.all { it.isDigit() }) {
                                otpValue = code
                            }  else {
                                isError = true
                                errorMessage = "Please enter only digits"
                            }
                        },
                        modifier = Modifier.padding(vertical = 16.dp)
                    )


                    if (successMessage.isNotEmpty()) {
                        Text(
                            text = successMessage,
                            color = Color(0xFF4CAF50),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            isError = false
                            errorMessage = ""
                            successMessage = ""

                            verificationViewModel.resendOtp(email = email)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Resend Code")
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    AppButton(
                        onClick = {
                            verificationViewModel.verifyOtp(email = email, otp = otpValue)
                        },
                        enabled = true,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Verify OTP",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun OtpVerificationScreenPreview() {
    RealioTheme {
        OtpVerificationScreen(
            navController = null,
            email = "example@example.com"
        )
    }
}
