package com.realio.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.realio.app.core.ui.theme.RealioTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {true}

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            splashScreen.setKeepOnScreenCondition {false}
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RealioTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                   Text(
                        text = "Hello, World!",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(innerPadding)
                    )

                    Text(
                        text = "Hello, World!",
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

