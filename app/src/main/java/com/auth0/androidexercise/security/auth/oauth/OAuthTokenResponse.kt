package com.auth0.androidexercise.security.auth.oauth

import com.auth0.androidexercise.ui.login.universal.UniversalLoginResponse
import com.google.gson.annotations.SerializedName

data class OAuthTokenResponse(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String?,
    @SerializedName("id_token") val idToken: String,
    @SerializedName("scope") val scope: String?,
    @SerializedName("token_type") val tokenType: String?,
    @SerializedName("expires_in") val expiresIn: Int?
) :
    UniversalLoginResponse