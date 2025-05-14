package com.realio.app.core.common

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EnhancedAvatarSelectionScreen(navController: NavController) {
    // State for the selected image
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Dialog state
    var showImageSourceDialog by remember { mutableStateOf(false) }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Top Bar
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
        ) }

    // Avatar display
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color.DarkGray)
            .border(2.dp, Color(0xFF7986CB), CircleShape)
            .clickable { showImageSourceDialog = true },
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
                tint = Color.LightGray,
                modifier = Modifier.size(60.dp)
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

    // Image source options
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Camera option
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { launchCamera() }
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.DarkGray.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ThumbUp,
                    contentDescription = "Take Photo",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
            Text(
                text = "Camera",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Gallery option
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable { launchGallery() }
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.DarkGray.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Choose from Gallery",
                    tint = Color.White,
                    modifier = Modifier.size(30.dp)
                )
            }
            Text(
                text = "Gallery",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }



    // Save button
    Button(
        onClick = {
            // Here you would typically save the URI or process the image
            Toast.makeText(context, "Avatar saved successfully", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF7986CB)
        ),
        shape = RoundedCornerShape(8.dp),
        enabled = selectedImageUri != null
    ) {
        Text(
            text = "Save Preference",
            color = Color.White,
            fontSize = 16.sp
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