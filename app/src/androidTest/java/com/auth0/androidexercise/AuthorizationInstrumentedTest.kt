package com.auth0.androidexercise

import androidx.core.text.htmlEncode
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.auth0.androidexercise.security.auth.AuthorizationUrlBuilder
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AuthorizationInstrumentedTest {

    private val scheme = "https"
    private val domain = "eaceto.auth0.com"
    private val clientId = "clientId"
    private val redirectUri = "https://redirect.host/callback?"

    @Test
    fun loginUrl_isCorrect() {
        val url = AuthorizationUrlBuilder.newInstance(scheme, domain, clientId, redirectUri).build()

        assertEquals(url.scheme, scheme)
        assertEquals(url.authority, domain)
        assertEquals(url.path, "/authorize")
        assertEquals(
            url.getQueryParameter(AuthorizationUrlBuilder.CLIENT_ID),
            clientId.htmlEncode()
        )
        assertEquals(url.getQueryParameter(AuthorizationUrlBuilder.REDIRECT_URI), redirectUri)
    }

    @Test
    fun loginUrlWithPKCE_isCorrect() {
        val scope = "openid profile"
        val state = "xyzABC123"
        val codeChallenge = "YOUR_CODE_CHALLENGE"
        val codeChallengeMethod = "S256"

        val url = AuthorizationUrlBuilder.newInstance(scheme, domain, clientId, redirectUri)
            .with("scope", scope)
            .with("state", state)
            .with("code_challenge", codeChallenge)
            .with("code_challenge_method", codeChallengeMethod)
            .build()

        assertEquals(url.scheme, scheme)
        assertEquals(url.authority, domain)
        assertEquals(url.path, "/authorize")
        assertEquals(
            url.getQueryParameter(AuthorizationUrlBuilder.CLIENT_ID),
            clientId.htmlEncode()
        )
        assertEquals(url.getQueryParameter(AuthorizationUrlBuilder.REDIRECT_URI), redirectUri)
        assertEquals(url.getQueryParameter("scope"), scope)
        assertEquals(url.getQueryParameter("state"), state)
        assertEquals(url.getQueryParameter("code_challenge"), codeChallenge)
        assertEquals(url.getQueryParameter("code_challenge_method"), codeChallengeMethod)
    }

    @Test
    fun loginUrl_emptyScheme() {
        try {
            AuthorizationUrlBuilder.newInstance("", "", "", "").build()
        } catch (e: IllegalArgumentException) {
            assert(e.message!!.contains("scheme"))
        }
    }

    @Test
    fun loginUrl_emptyDomain() {
        try {
            AuthorizationUrlBuilder.newInstance(scheme, "", "", "").build()
        } catch (e: IllegalArgumentException) {
            assert(e.message!!.contains("domain"))
        }
    }

    @Test
    fun loginUrl_emptyClientId() {
        try {
            AuthorizationUrlBuilder.newInstance(scheme, domain, "", "").build()
        } catch (e: IllegalArgumentException) {
            assert(e.message!!.contains("clientId"))
        }
    }

    @Test
    fun loginUrl_emptyRedirectUri() {
        try {
            AuthorizationUrlBuilder.newInstance(scheme, domain, clientId, "").build()
        } catch (e: IllegalArgumentException) {
            assert(e.message!!.contains("redirectUri"))
        }
    }

}