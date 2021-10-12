package com.auth0.androidexercise.security.auth.oauth

import com.auth0.androidexercise.ui.login.universal.UniversalLoginResponse

data class OAuthUnauthorizedResponse(val code: Int, val message: String) :
    UniversalLoginResponse
