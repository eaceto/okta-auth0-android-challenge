package com.auth0.androidexercise

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.auth0.androidexercise.security.CredentialsRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class CredentialsInstrumentedTest {

    companion object {
        lateinit var instrumentationContext: Context
        lateinit var repository: CredentialsRepository

        @BeforeClass
        @JvmStatic
        fun initRepository() {
            instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
            repository = CredentialsRepository(instrumentationContext)
        }
    }

    @Before
    fun resetCredentials() {
        repository.clearCredentials()
    }

    @Test
    fun credentialsAreEmpty() {
        val accessToken = repository.getAccessToken()
        val idToken = repository.getIDToken()
        val refreshToken = repository.getRefreshToken()

        assertNull(accessToken)
        assertNull(idToken)
        assertNull(refreshToken)
    }

    @Test
    fun credentialsStoredCorrectly() {
        repository.store("AT", "RT", "ID")

        val accessToken = repository.getAccessToken()
        val idToken = repository.getIDToken()
        val refreshToken = repository.getRefreshToken()

        assertEquals("AT", accessToken)
        assertEquals("ID", idToken)
        assertEquals("RT", refreshToken)
    }

    @Test
    fun credentialsClearedCorrectly() {
        repository.store("AT", "RT", "ID")
        repository.clearCredentials()

        val accessToken = repository.getAccessToken()
        val idToken = repository.getIDToken()
        val refreshToken = repository.getRefreshToken()

        assertNull(accessToken)
        assertNull(idToken)
        assertNull(refreshToken)

    }

}