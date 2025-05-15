package com.realio.app.feature.authentication.presentation.viewModel

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.net.Uri.parse
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

interface IGoogleViewModel {
    val authState: StateFlow<AuthState>
    fun beginSignIn(context: Context)
    fun handleSignInResult(data: Intent?)
    fun signOut()
    fun initGoogleSignIn(context: Context, clientId: String)
    fun updateAuthState(newState: AuthState)
}


class GoogleViewModel : ViewModel(), IGoogleViewModel {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    override val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // For Identity Services
    private var oneTapClient: SignInClient? = null
    private var webClientId: String = ""

    override fun initGoogleSignIn(context: Context, clientId: String) {
        webClientId = clientId
        // Initialize Identity Services client
        oneTapClient = Identity.getSignInClient(context)
    }

    override fun updateAuthState(newState: AuthState) {
        _authState.value = newState
    }


    override fun beginSignIn(context: Context) {
        _authState.value = AuthState.Loading

        Log.d("GoogleSignIn", "Starting sign-in process with client ID: $webClientId")

        viewModelScope.launch {
            try {
                // Create a sign-in request with simplified options
                val signInRequest = BeginSignInRequest.builder()
                    .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                            .setSupported(true)
                            .setServerClientId(webClientId)
                            .setFilterByAuthorizedAccounts(false) // Don't filter accounts
                            .build()
                    )
                    .setAutoSelectEnabled(false) // Don't auto-select
                    .build()

                Log.d("GoogleSignIn", "Configured sign-in request")

                // Start the sign-in flow
                val result = oneTapClient?.beginSignIn(signInRequest)?.await()
                if (result != null) {
                    Log.d("GoogleSignIn", "Sign-in request succeeded")
                    _authState.value = AuthState.OneTapSignInReady(result.pendingIntent.intentSender)
                } else {
                    Log.e("GoogleSignIn", "Sign-in result was null")
                    _authState.value = AuthState.Error("Sign-in result was null")
                }
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Sign-in failed", e)

                // Handle specific error cases
                when {
                    e is ApiException && e.statusCode == 16 -> {
                        // Status code 16 means no matching accounts found
                        Log.e("GoogleSignIn", "No matching accounts found, trying sign-up")
                        trySignUp(context)
                    }
                    e is ApiException -> {
                        Log.e("GoogleSignIn", "API Exception: ${e.statusCode}", e)
                        _authState.value = AuthState.Error("Sign-in failed (code ${e.statusCode})")
                    }
                    else -> {
                        Log.e("GoogleSignIn", "Sign-in failed", e)
                        _authState.value = AuthState.Error("Sign-in failed: ${e.message}")
                    }
                }
            }
        }
    }

    // In your GoogleViewModel
    fun handleSignInCancellation(intentData: Intent?) {
        val extras = intentData?.extras
        if (extras != null) {
            // Log all extras for debugging
            extras.keySet().forEach { key ->
                Log.d("GoogleSignIn", "Extra: $key = ${extras.get(key)}")
            }

            // Extract Google-specific error codes if available
            val statusCode = extras.getInt("googleSignInStatus", 0)
            val apiError = extras.getInt("googleSignInApiError", 0)

            val errorMessage = when {
                statusCode == 12501 -> "Sign-in was canceled by user"
                statusCode != 0 -> "Sign-in failed with status code: $statusCode"
                apiError != 0 -> "Google API error: $apiError"
                else -> "Sign-in was canceled"
            }

            _authState.value = AuthState.Error(errorMessage)
        } else {
            _authState.value = AuthState.Error("Sign-in was canceled")
        }

        // Option: Automatically try browser-based fallback
        // launchGoogleSignInActivity(context)
    }

    // Method for sign-up
    private fun trySignUp(context: Context) {
        viewModelScope.launch {
            try {
                val signUpRequest = BeginSignInRequest.builder()
                    .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                            .setSupported(true)
                            .setServerClientId(webClientId)
                            .setFilterByAuthorizedAccounts(false)
                            .build()
                    )
                    .build()

                val result = oneTapClient?.beginSignIn(signUpRequest)?.await()
                if (result != null) {
                    _authState.value = AuthState.OneTapSignInReady(result.pendingIntent.intentSender)
                } else {
                    _authState.value = AuthState.Error("Sign-up failed")
                    // Fallback to browser sign-in
                    launchGoogleSignInActivity(context)
                }
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Sign-up failed", e)
                _authState.value = AuthState.Error("Sign-up failed: ${e.message}")
                // Fallback to browser sign-in
                launchGoogleSignInActivity(context)
            }
        }
    }

    // ⚠️ NEW METHOD: Launch a traditional Google Sign-In intent
    private fun launchGoogleSignInActivity(context: Context) {
        try {
            Log.d("GoogleSignIn", "Falling back to browser sign-in")

            // Create an intent to open a browser for OAuth
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = parse(
                "https://accounts.google.com/o/oauth2/auth" +
                        "?client_id=$webClientId" +
                        "&redirect_uri=com.realio.app:/oauth2redirect" +
                        "&response_type=code" +
                        "&scope=email%20profile"
            )

            // Tell the user we're redirecting them
            _authState.value = AuthState.RedirectingToBrowser(intent)
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Even browser sign-in failed", e)
            _authState.value = AuthState.Error("All sign-in methods failed: ${e.message}")
        }
    }

    // Handle the result from One Tap sign-in
    override fun handleSignInResult(data: Intent?) {
        if (data == null) {
            Log.e("GoogleSignIn", "Sign-in intent was null")
            _authState.value = AuthState.Error("Sign-in intent was null")
            return
        }

        viewModelScope.launch {
            try {
                val credential = oneTapClient?.getSignInCredentialFromIntent(data)
                Log.d("GoogleSignIn", "Got credential from intent: ${credential != null}")
                processCredential(credential)
            } catch (e: ApiException) {
                Log.e("GoogleSignIn", "API Exception when getting credential", e)
                _authState.value = AuthState.Error("Sign-in failed: ${e.statusCode}")
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Unexpected error when getting credential", e)
                _authState.value = AuthState.Error("Unexpected error: ${e.message}")
            }
        }
    }

    private fun processCredential(credential: SignInCredential?) {
        if (credential == null) {
            Log.e("GoogleSignIn", "Credential was null")
            _authState.value = AuthState.Error("Credential was null")
            return
        }

        val idToken = credential.googleIdToken
        Log.d("GoogleSignIn", "ID Token present: ${idToken != null}")

        if (idToken != null) {
            // Token can be sent to your backend for verification
            _authState.value = AuthState.Authenticated(
                UserData(
                    id = credential.id,
                    name = credential.displayName ?: "",
                    email = credential.id, // For Google, the ID is typically the email
                    photoUrl = credential.profilePictureUri?.toString()
                )
            )
            Log.d("GoogleSignIn", "Authentication successful for user: ${credential.id}")
        } else {
            Log.e("GoogleSignIn", "ID token was null")
            _authState.value = AuthState.Error("ID token was null")
        }
    }

    override fun signOut() {
        viewModelScope.launch {
            try {
                oneTapClient?.signOut()?.await()
                Log.d("GoogleSignIn", "Sign out successful")
                _authState.value = AuthState.SignedOut
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Sign out failed", e)
                _authState.value = AuthState.Error("Sign out failed: ${e.message}")
            }
        }
    }
}

// Updated AuthState with consistent naming
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class OneTapSignInReady(val intentSender: IntentSender) : AuthState()
    data class RedirectingToBrowser(val intent: Intent) : AuthState()
    data class Authenticated(val userData: UserData) : AuthState()
    data class Error(val message: String) : AuthState()
    object SignedOut : AuthState()
}

data class UserData(
    val id: String,
    val name: String,
    val email: String,
    val photoUrl: String? = null
)

