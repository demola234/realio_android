package com.realio.app.feature.authentication.presentation.viewModel

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.flow.MutableStateFlow


class PreviewGoogleViewModel : IGoogleViewModel {
    override val authState = MutableStateFlow(
        AuthState.Idle
    )

    override fun beginSignIn(context: Context) {}
    override fun handleSignInResult(data: Intent?) {}
    override fun signOut() {}
    override fun initGoogleSignIn(context: Context, clientId: String) {}
    override fun updateAuthState(authState: AuthState) {}
}

