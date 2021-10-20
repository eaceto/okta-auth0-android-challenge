package com.auth0.androidexercise.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CredentialsRepository @Inject constructor(@ApplicationContext appContext: Context) {

    private val masterKey: MasterKey = MasterKey.Builder(appContext)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        appContext,
        CredentialsRepository.repositoryName,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    @Throws(Exception::class)
    fun store(accessToken: String, refreshToken: String?, idToken: String) {
        val editor = sharedPreferences.edit()

        editor.putString(ACCESS_TOKEN, accessToken)
            .putString(ID_TOKEN, idToken)

        refreshToken?.let { editor.putString(REFRESH_TOKEN, refreshToken) }

        editor.apply()
    }

    fun getAccessToken(): String? = getSecretPreference(ACCESS_TOKEN)
    fun getRefreshToken(): String? = getSecretPreference(REFRESH_TOKEN)
    fun getIDToken(): String? = getSecretPreference(ID_TOKEN)
    fun hasAccessToken(): Boolean = hasSecretPreference(ACCESS_TOKEN)


    fun clearCredentials() {
        sharedPreferences.edit()
            .remove(ACCESS_TOKEN)
            .remove(REFRESH_TOKEN)
            .remove(ID_TOKEN)
            .apply()
    }

    private fun getSecretPreference(key: String) = sharedPreferences.getString(key, null)
    private fun hasSecretPreference(key: String) = sharedPreferences.contains(key)

    companion object {
        private const val TAG = "SecurityRepository"
        private const val repositoryName = "gp_security_repo"
        private const val ACCESS_TOKEN = "at"
        private const val REFRESH_TOKEN = "rt"
        private const val ID_TOKEN = "idt"
    }
}