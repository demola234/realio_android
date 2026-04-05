package com.realio.app.feature.authentication.presentation.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.realio.app.R
import com.realio.app.core.navigation.RealioScreenConsts
import com.realio.app.core.ui.components.buttons.AppButton
import com.realio.app.core.ui.theme.RealioTheme
import com.realio.app.feature.authentication.presentation.components.LoadingPage
import com.realio.app.feature.authentication.presentation.viewModel.UploadImageState
import com.realio.app.feature.authentication.presentation.viewModel.UploadProfileImageViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AvatarSelectionScreen(
    navController: NavController? = null,
    uploadProfileImageViewModel: UploadProfileImageViewModel = hiltViewModel()
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var errorDialog by remember { mutableStateOf(false) }
    var successDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val dialogVisibilityState = remember { MutableTransitionState(false) }
    val uploadProfileState by uploadProfileImageViewModel.uploadProfileState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val tempImageFile = remember { createImageFile(context) }
    val tempImageUri = remember {
        FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            tempImageFile
        )
    }

    // Handle state changes for upload process
    LaunchedEffect(uploadProfileState) {
        when (uploadProfileState) {
            is UploadImageState.Loading -> {
                dialogVisibilityState.targetState = true
            }

            is UploadImageState.Success -> {
                dialogVisibilityState.targetState = false
                successDialog = true
            }

            is UploadImageState.Error -> {
                dialogVisibilityState.targetState = false
                errorMessage = (uploadProfileState as UploadImageState.Error).message
                errorDialog = true
            }

            is UploadImageState.Idle -> {
                dialogVisibilityState.targetState = false
            }
        }
    }

    // Permission state
    val permissionState = remember { mutableStateOf(false) }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            selectedImageUri = tempImageUri
        }
    }

    // Camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            permissionState.value = true
            cameraLauncher.launch(tempImageUri)
        } else {
            Toast.makeText(
                context,
                "Camera permission is required to take photos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            // We need to copy the content URI to our app's storage
            coroutineScope.launch {
                val copiedFile = withContext(Dispatchers.IO) {
                    copyUriToFile(context, uri)
                }
                if (copiedFile != null) {
                    // Store the file reference for upload
                    uploadProfileImageViewModel.setSelectedImageFile(copiedFile)
                }
            }
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
                // When using camera, set the temp file as the selected file for upload
                uploadProfileImageViewModel.setSelectedImageFile(tempImageFile)
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
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                        .clickable { navController?.popBackStack() }
                )

                Text(
                    text = "Avatar Selection",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
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
                shape = MaterialTheme.shapes.medium,
                enabled = selectedImageUri != null,
                onClick = {
                    // Let the ViewModel handle the upload with the proper file
                    uploadProfileImageViewModel.uploadProfileImage()
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
                        .size(120.dp)
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
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Tap to change avatar",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }

    // Overlay dialogs and loading screens
    Box(modifier = Modifier.fillMaxSize()) {
        // Loading dialog
        AnimatedVisibility(
            visibleState = dialogVisibilityState,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            LoadingPage(
                onClose = {
                    dialogVisibilityState.targetState = false
                },
                dialogVisibilityState = dialogVisibilityState
            )
        }

        // Error dialog
        if (errorDialog) {
            AlertDialog(
                onDismissRequest = { errorDialog = false },
                title = { Text("Upload Error") },
                text = { Text(errorMessage) },
                confirmButton = {
                    TextButton(onClick = { errorDialog = false }) {
                        Text("OK")
                    }
                },
                containerColor = Color(0xFF2C2C2C),
                textContentColor = Color.White,
                titleContentColor = Color.White
            )
        }

        // Success dialog
        if (successDialog) {
            AlertDialog(
                onDismissRequest = {
                    successDialog = false
                    navController?.navigate(RealioScreenConsts.Login.name) {
                        popUpTo(RealioScreenConsts.Login.name) { inclusive = true }
                    }
                },
                title = { Text("Success") },
                text = { Text("Profile picture uploaded successfully") },
                confirmButton = {
                    TextButton(onClick = {
                        successDialog = false
                        navController?.navigate(RealioScreenConsts.Login.name) {
                            popUpTo(RealioScreenConsts.Login.name) { inclusive = true }
                        }
                    }) {
                        Text("OK")
                    }
                },
                containerColor = Color(0xFF2C2C2C),
                textContentColor = Color.White,
                titleContentColor = Color.White
            )
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

// Helper function to copy content URI to app's file storage
private suspend fun copyUriToFile(context: Context, uri: Uri): File? {
    return withContext(Dispatchers.IO) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val tempFile = createImageFile(context)

            inputStream?.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AvatarSelectionScreenPreview() {
    RealioTheme {
        AvatarSelectionScreen(
            navController = null,
        )
    }
}