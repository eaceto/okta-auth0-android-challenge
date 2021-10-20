package com.auth0.androidexercise.security.auth

import android.net.Uri
import androidx.core.text.htmlEncode

class AuthorizationUrlBuilder private constructor(
    scheme: String,
    domain: String,
    clientId: String,
    redirectUri: String
) : UrlBuilder {

    private var urlBuilder: Uri.Builder = Uri.Builder()
        .scheme(scheme)
        .authority(domain)
        .appendPath("authorize")
        .appendQueryParameter(CLIENT_ID, clientId)
        .appendQueryParameter(REDIRECT_URI, redirectUri.htmlEncode())
        .appendQueryParameter(RESPONSE_TYPE, RESPONSE_TYPE_CODE)
    private var additionalQueryParameters = HashMap<String, String>()

    override fun build(): Uri {
        for ((key, value) in additionalQueryParameters) {
            urlBuilder.appendQueryParameter(key, value)
        }
        return urlBuilder.build()
    }

    fun with(key: String, value: String): AuthorizationUrlBuilder {
        additionalQueryParameters[key] = value
        return this
    }

    companion object {
        const val CLIENT_ID = "client_id"
        const val REDIRECT_URI = "redirect_uri"
        const val RESPONSE_TYPE = "response_type"
        const val RESPONSE_TYPE_CODE = "code"

        fun newInstance(
            scheme: String = "https",
            domain: String,
            clientId: String,
            redirectUri: String
        ): AuthorizationUrlBuilder {
            if (scheme.isBlank()) throw IllegalArgumentException("'scheme' cannot be empty")
            if (domain.isBlank()) throw IllegalArgumentException("'domain' cannot be empty")
            if (clientId.isBlank()) throw IllegalArgumentException("'clientId' cannot be empty")
            if (redirectUri.isBlank()) throw IllegalArgumentException("'redirectUri' cannot be empty")

            return AuthorizationUrlBuilder(scheme, domain, clientId, redirectUri)
        }
    }
}
