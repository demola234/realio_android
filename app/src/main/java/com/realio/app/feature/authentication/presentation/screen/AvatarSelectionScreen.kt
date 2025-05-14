package com.realio.app.feature.authentication.presentation.screen

import ThemedImage
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.realio.app.R
import com.realio.app.core.ui.components.buttons.AppButton
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun AvatarSelectionScreen(navController: NavController) {
    // State for the selected image
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Dialog state
    var showImageSourceDialog by remember { mutableStateOf(false) }

    var showSavingDialog by remember { mutableStateOf(false) }

    // Add this for animation control
    val dialogVisibilityState = remember {
        MutableTransitionState(false)
    }

    // Connect the visibility state to the dialog state
    LaunchedEffect(showSavingDialog) {
        dialogVisibilityState.targetState = showSavingDialog
    }

    // Context for permissions and file operations
    val context = LocalContext.current

    // Create a temporary file for camera capture
    val tempImageFile = remember { createImageFile(context) }
    val tempImageUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempImageFile
        )
    }

    // Permission state
    val permissionState = remember { mutableStateOf(false) }

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            permissionState.value = true
        } else {
            Toast.makeText(
                context,
                "Camera permission is required to take photos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            selectedImageUri = tempImageUri
        }
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
        }
    }

    // Function to launch camera after permission check
    val launchCamera = {
        val permissionCheckResult = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )

        when (permissionCheckResult) {
            PackageManager.PERMISSION_GRANTED -> {
                cameraLauncher.launch(tempImageUri)
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // Function to launch gallery
    val launchGallery = {
        galleryLauncher.launch("image/*")
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
        bottomBar = {
            AppButton(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                content = {
                    Text(
                        text = "Save Preference",
                        modifier = Modifier.padding(horizontal = 24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                shape = MaterialTheme.shapes,
                onClick = {
                    showSavingDialog = true
                }
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 24.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable { navController.popBackStack() }
                )
            }

            // Title
            Text(
                text = "Select an avatar",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Subtitle
            Text(
                text = "Pick an avatar for your account!",
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 36.dp)
            )


            // Avatar placeholder
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.DarkGray)
                        .clickable {
                            showImageSourceDialog = true
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImageUri),
                            contentDescription = "Selected Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Text(
                    text = "Add a picture",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(top = 12.dp)

                )
            }


            // Save button

        }
    }

    // Create a pulsating animation for the loading icon
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    AnimatedVisibility(
        visibleState = dialogVisibilityState,
        enter = fadeIn(animationSpec = tween(300)) +
                slideInVertically(
                    initialOffsetY = { -50 },
                    animationSpec = tween(300)
                ),
        exit = fadeOut(animationSpec = tween(300)) +
                slideOutVertically(
                    targetOffsetY = { -50 },
                    animationSpec = tween(300)
                )
    ) {
        Box(
            modifier = Modifier
                .alpha(0.8f)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .clickable(enabled = true) {
                    showSavingDialog = false
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                // Custom loading indicator
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .scale(scale)
                    ) {
                        ThemedImage(
                            darkImage = R.drawable.loading_image_dark,
                            lightImage = R.drawable.loading_image_light,
                            modifier = Modifier
                                .height(37.dp)
                                .width(65.dp)
                        )
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Saving your preferences...",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
            }
        }
    }
    // Image source dialog
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Select Image Source") },
            text = { Text("Choose where to select your avatar from") },
            confirmButton = {
                Button(
                    onClick = {
                        launchCamera()
                        showImageSourceDialog = false
                    }
                ) {
                    Text("Camera")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        launchGallery()
                        showImageSourceDialog = false
                    }
                ) {
                    Text("Gallery")
                }
            },
            containerColor = Color(0xFF2C2C2C),
            textContentColor = Color.White,
            titleContentColor = Color.White
        )
    }
}

// Helper function to create image file
private fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = context.getExternalFilesDir("Pictures")
    return File.createTempFile(
        imageFileName,
        ".jpg",
        storageDir
    )
}


@Preview
@Composable
fun AvatarSelectionScreen() {

}