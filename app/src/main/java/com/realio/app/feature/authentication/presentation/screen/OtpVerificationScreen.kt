package com.realio.app.feature.authentication.presentation.screen

import OtpInputField
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.realio.app.core.navigation.RealioScreenConsts
import com.realio.app.core.ui.components.buttons.AppButton
import com.realio.app.core.ui.theme.NeutralThreeColorLight
import com.realio.app.core.ui.theme.RealioTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(navController: NavController? = null, email: String) {
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
                .padding(paddingValues),

        ) {

            var otpValue by remember { mutableStateOf("") }
            var isError by remember { mutableStateOf(false) }
            var errorMessage by remember { mutableStateOf("") }
            var successMessage by remember { mutableStateOf("") }

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
                            println("OTP entered: $code")
                            if (code.length == 6 && code.all { it.isDigit() } && code == "123456") {
                                // OTP is valid, proceed with verification
                                successMessage = "OTP verification successful!"
                                isError = false
                                // You can navigate to the next screen or perform other actions
                            } else {
                                isError = true
                                errorMessage = "Please enter a valid OTP"
                            }
                        },
                        modifier = Modifier.padding(vertical = 16.dp)
                    )


                    if (successMessage.isNotEmpty()) {
                        Text(
                            text = successMessage,
                            color = Color(0xFF4CAF50), // Green
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            // TODO: Trigger resend logic here
                            isError = false
                            errorMessage = ""
                            successMessage = ""
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
                            navController?.navigate(RealioScreenConsts.PersonalInfo.name)
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
