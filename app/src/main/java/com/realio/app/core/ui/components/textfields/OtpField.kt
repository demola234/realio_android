import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.phone.SmsRetriever
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.content.BroadcastReceiver
import android.content.Intent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.core.content.ContextCompat.checkSelfPermission

/**
 * A comprehensive OTP (One-Time Password) input field implementation in Jetpack Compose
 *
 * Features:
 * - Customizable digit count
 * - Auto-focus and keyboard management
 * - Individual digit cells with customizable styling
 * - Support for paste functionality
 * - Error state handling with shake animation
 * - SMS auto-fill capability
 * - Accessibility features
 * - Animation effects
 * - Success/error feedback
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpInputField(
    otpLength: Int = 6,
    onOtpComplete: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String = "",
    cellWidth: Int = 45,
    cellHeight: Int = 50,
    cellSpacing: Int = 8,
    cellBorderWidth: Int = 1,
    cellCornerRadius: Int = 8,
    cellBackgroundColor: Color = Color.White,
    cellBorderColor: Color = Color.Gray,
    cellActiveColor: Color = Color.Blue,
    cellErrorColor: Color = Color.Red,
    cellSuccessColor: Color = Color.Green,
    textColor: Color = Color.Black,
    fontSize: Int = 20,
    keyboardType: KeyboardType = KeyboardType.Number,
    enableSmsAutofill: Boolean = true,
    requestPermissions: Boolean = false
) {
    val otpText = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val showSuccess = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    var successJob by remember { mutableStateOf<Job?>(null) }
    val isFocusedOnce = remember { mutableStateOf(false) }

    // Track composition state to ensure parent components are placed
    val isCompositionReady = remember { mutableStateOf(false) }

    // State for shake animation
    val shakeOffset = remember { Animatable(0f) }

    // Context for SMS auto-fill and permissions
    val context = LocalContext.current
    val density = LocalDensity.current

    // Check permissions
    val hasVibratePermission = remember {
        mutableStateOf(
            checkSelfPermission(context, Manifest.permission.VIBRATE) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    val hasSmsPermissions = remember {
        mutableStateOf(
            checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) ==
                    PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(context, Manifest.permission.READ_SMS) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    // Request permissions if needed
    LaunchedEffect(requestPermissions) {
        if (requestPermissions && context is Activity) {
            val permissionsToRequest = mutableListOf<String>()

            if (!hasVibratePermission.value) {
                permissionsToRequest.add(Manifest.permission.VIBRATE)
            }

            if (enableSmsAutofill && !hasSmsPermissions.value) {
                permissionsToRequest.add(Manifest.permission.RECEIVE_SMS)
                permissionsToRequest.add(Manifest.permission.READ_SMS)
            }

            if (permissionsToRequest.isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    context,
                    permissionsToRequest.toTypedArray(),
                    100 // Request code
                )
            }
        }
    }

    // SMS auto-fill setup - only if we have permissions
    if (enableSmsAutofill && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && hasSmsPermissions.value) {
        LaunchedEffect(Unit) {
            startSmsRetriever(context) { smsCode ->
                if (smsCode.length >= otpLength) {
                    val otp = smsCode.take(otpLength)
                    otpText.value = otp
                }
            }
        }
    }

    // Ensure composition is ready before attempting focus
    LaunchedEffect(Unit) {
        delay(100)
        isCompositionReady.value = true
    }

    // Only request focus when composition is ready
    LaunchedEffect(isCompositionReady.value) {
        if (isCompositionReady.value && !isFocusedOnce.value) {
            delay(150) // Additional delay to ensure parent layout is stable
            focusRequester.requestFocus()
            isFocusedOnce.value = true
        }
    }

    // Trigger shake animation when error state changes to true
    LaunchedEffect(isError) {
        if (isError) {
            // Vibrate pattern for error feedback (accessible) - only if permission granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && hasVibratePermission.value) {
                provideHapticFeedback(context)
            }

            // Shake animation
            val shakePattern = floatArrayOf(0f, -10f, 10f, -8f, 8f, -5f, 5f, -2f, 2f, 0f)
            shakePattern.forEach { target ->
                shakeOffset.animateTo(
                    targetValue = target,
                    animationSpec = tween(
                        durationMillis = 50,
                        easing = LinearEasing
                    )
                )
            }
            shakeOffset.snapTo(0f)
        }
    }

    // Clean up on component disposal
    DisposableEffect(Unit) {
        onDispose {
            // Cancel any pending jobs
            successJob?.cancel()
            // Reset focus state when component is disposed
            isFocusedOnce.value = false
        }
    }

    val enteredDigits = otpText.value
        .padEnd(otpLength, ' ')
        .take(otpLength)
        .map { if (it == ' ') null else it }

    LaunchedEffect(otpText.value) {
        if (otpText.value.length == otpLength) {
            keyboardController?.hide()
            onOtpComplete(otpText.value)

            if (!isError) {
                successJob?.cancel()
                successJob = coroutineScope.launch {
                    showSuccess.value = true
                    delay(1500)
                    showSuccess.value = false
                }
            }
        } else {
            showSuccess.value = false
        }
    }

    // OTP Input Field with shake animation
    Column(
        modifier = modifier
            .offset(x = with(density) { shakeOffset.value.toDp() }),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // SMS Auto-fill hint text - only show if we have permissions
        if (enableSmsAutofill && hasSmsPermissions.value) {
            Text(
                text = "Waiting for SMS verification code...",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .semantics {
                        contentDescription = "Waiting for SMS verification code"
                    }
            )
        }

        // Hidden input field - Wrap with Box to ensure proper layout
        Box(
            modifier = Modifier
                .size(1.dp)
                .background(Color.Transparent)
        ) {
            BasicTextField(
                value = TextFieldValue(
                    text = otpText.value,
                    selection = TextRange(otpText.value.length)
                ),
                onValueChange = { newValue ->
                    val filtered = if (keyboardType == KeyboardType.Number)
                        newValue.text.filter { it.isDigit() }
                    else newValue.text

                    if (filtered.length <= otpLength) {
                        otpText.value = filtered
                    }
                },
                modifier = Modifier
                    .size(0.dp)
                    .focusRequester(focusRequester)
                    .semantics {
                        contentDescription = "OTP input field, enter $otpLength digit code"
                        testTag = "otp_input_field"
                    },
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                textStyle = TextStyle(color = Color.Transparent),
            )
        }

        // Add a small spacer to ensure proper layout
        Spacer(modifier = Modifier.height(4.dp))

        // OTP UI Cells with improved accessibility
        Row(
            horizontalArrangement = Arrangement.spacedBy(cellSpacing.dp),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .pointerInput(Unit) {
                    detectTapGestures {
                        if (isCompositionReady.value) {
                            focusRequester.requestFocus()
                            keyboardController?.show()
                        }
                    }
                }
                .semantics {
                    contentDescription = "OTP digit cells, ${otpText.value.length} of $otpLength digits entered"
                }
        ) {
            repeat(otpLength) { index ->
                val isCellFilled = index < otpText.value.length
                val isCurrentCell = index == otpText.value.length

                val borderColor = when {
                    isError -> cellErrorColor
                    showSuccess.value -> cellSuccessColor
                    isCurrentCell -> cellActiveColor
                    else -> cellBorderColor
                }

                val backgroundColor = if (isCellFilled) {
                    when {
                        isError -> cellErrorColor.copy(alpha = 0.1f)
                        showSuccess.value -> cellSuccessColor.copy(alpha = 0.1f)
                        else -> cellBackgroundColor
                    }
                } else {
                    cellBackgroundColor
                }

                // Individual cell with accessibility properties
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(cellWidth.dp)
                        .height(cellHeight.dp)
                        .clip(RoundedCornerShape(cellCornerRadius.dp))
                        .background(backgroundColor)
                        .border(
                            width = cellBorderWidth.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(cellCornerRadius.dp)
                        )
                        .semantics {
                            contentDescription = if (isCellFilled) {
                                "Digit ${index + 1}: ${enteredDigits[index]}"
                            } else if (isCurrentCell) {
                                "Current digit ${index + 1}, waiting for input"
                            } else {
                                "Digit ${index + 1}, not filled"
                            }
                            testTag = "otp_cell_$index"
                        }
                ) {
                    if (isCellFilled) {
                        Text(
                            text = enteredDigits[index]?.toString() ?: "",
                            color = textColor,
                            fontSize = fontSize.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    } else if (isCurrentCell) {
                        CursorAnimation(
                            color = cellActiveColor,
                            modifier = Modifier
                                .width(1.dp)
                                .height(24.dp)
                        )
                    }
                }
            }
        }

        // Manual input option or permissions message
        if (enableSmsAutofill) {
            if (!hasSmsPermissions.value && requestPermissions) {
                // Show message about missing permissions
                Text(
                    text = "SMS auto-fill requires SMS permissions",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun CursorAnimation(
    color: Color,
    modifier: Modifier = Modifier
) {
    val isVisible = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(500)
            isVisible.value = !isVisible.value
        }
    }

    AnimatedVisibility(
        visible = isVisible.value,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = modifier.background(color))
    }
}

// SMS Retriever for auto-fill functionality
@RequiresApi(Build.VERSION_CODES.O)
fun startSmsRetriever(context: Context, onCodeReceived: (String) -> Unit) {
    // First check if we have necessary permissions
    if (checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
        checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
        return  // Exit if permissions not granted
    }

    val smsRetrieverClient = SmsRetriever.getClient(context)
    val task = smsRetrieverClient.startSmsRetriever()

    task.addOnSuccessListener {
        // Successfully started SMS retriever
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                    val extras = intent.extras
                    val status = extras?.get(SmsRetriever.EXTRA_STATUS)

                    if (status == SmsRetriever.SMS_RETRIEVED_ACTION) {
                        // Get SMS message content
                        val message = extras.getString(SmsRetriever.EXTRA_SMS_MESSAGE)
                        // Extract the OTP code from SMS message
                        message?.let {
                            val otpPattern = Regex("\\d{4,6}")
                            val matchResult = otpPattern.find(message)

                            matchResult?.value?.let { otp ->
                                onCodeReceived(otp)
                            }
                        }
                    }
                }

                // Unregister the receiver after receiving SMS
                context.unregisterReceiver(this)
            }
        }

        // Register the BroadcastReceiver
        ContextCompat.registerReceiver(
            context,
            receiver,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    task.addOnFailureListener {
        // Failed to start SMS retriever
        // Fallback to manual input
    }
}

// Haptic feedback for error states
@RequiresApi(Build.VERSION_CODES.O)
fun provideHapticFeedback(context: Context) {
    try {
        // Check for vibration permission
        if (checkSelfPermission(context, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            return  // Exit if permission not granted
        }

        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as android.os.Vibrator
        if (vibrator.hasVibrator()) {
            // Create a vibration pattern for error - 3 short vibrations
            val pattern = longArrayOf(0, 100, 100, 100, 100, 100)
            vibrator.vibrate(pattern, -1)
        }
    } catch (e: Exception) {
        // Fallback silently if haptic feedback fails
    }
}