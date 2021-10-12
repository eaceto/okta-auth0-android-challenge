package com.auth0.androidexercise

import android.content.Context
import androidx.core.text.htmlEncode
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.auth0.androidexercise.security.auth.AuthorizationUrlBuilder
import com.auth0.androidexercise.ui.login.universal.UniversalLoginLocalRepository
import org.junit.Assert.assertEquals
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UniversalLoginLocalRepositoryInstrumentedTest {

    companion object {
        lateinit var instrumentationContext: Context
        lateinit var repository: UniversalLoginLocalRepository

        @BeforeClass
        @JvmStatic
        fun initRepository() {
            instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
            repository = UniversalLoginLocalRepository(instrumentationContext)
        }
    }

    @Test
    fun loginUrlWithPKCE_isCorrect() {
        val resources = instrumentationContext.resources

        val redirectUri = resources.getString(R.string.AUTH0_REDIRECT)
        val scheme = resources.getString(R.string.AUTH0_SCHEME)
        val domain = resources.getString(R.string.AUTH0_DOMAIN)
        val clientId = resources.getString(R.string.AUTH0_CLIENT_ID)

        val scope = "openid profile offline_access name email email_verified nickname read:coffees"
        val state = "xyzABC123"
        val codeVerifier = generateCodeVerifier()
        val codeChallenge = generateCodeChallenge(codeVerifier)
        val codeChallengeMethod = "S256"

        val url = repository.getAuthorizationUrl(redirectUri, state, codeChallenge)

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
}