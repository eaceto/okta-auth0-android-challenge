package com.auth0.androidexercise.security.auth

import android.net.Uri

interface UrlBuilder {
    fun build(): Uri
}