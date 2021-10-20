package com.auth0.androidexercise.ui.login.universal

import android.content.Context
import android.net.Uri
import com.auth0.androidexercise.R
import com.auth0.androidexercise.security.auth.AuthorizationUrlBuilder
import javax.inject.Inject

class UniversalLoginLocalRepository @Inject constructor(private val appContext: Context) {

    private fun getAuthorizationUrlBuilder(redirectUri: String): AuthorizationUrlBuilder {
        val scheme = appContext.getString(R.string.AUTH0_SCHEME)
        val domain = appContext.getString(R.string.AUTH0_DOMAIN)
        val clientId = appContext.getString(R.string.AUTH0_CLIENT_ID)

        return AuthorizationUrlBuilder.newInstance(scheme, domain, clientId, redirectUri)
    }

    fun getAuthorizationUrl(redirectUrl: String, state: String, codeChallenge: String): Uri {
        return getAuthorizationUrlBuilder(redirectUrl)
            .with(
                "scope",
                "openid profile offline_access name email email_verified nickname read:coffees"
            )
            .with("audience", "http://localhost:3000")
            .with("state", state)
            .with("code_challenge", codeChallenge)
            .with("code_challenge_method", "S256")
            .build()
    }
}