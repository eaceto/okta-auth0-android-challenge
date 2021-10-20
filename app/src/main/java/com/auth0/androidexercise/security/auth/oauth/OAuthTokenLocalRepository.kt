package com.auth0.androidexercise.security.auth.oauth

import android.content.Context
import com.auth0.androidexercise.R
import com.auth0.androidexercise.security.CredentialsRepository
import com.auth0.androidexercise.security.SecurityRepository
import com.auth0.androidexercise.services.auth0.Auth0Services
import com.auth0.androidexercise.services.impl.RetrofitClient
import com.auth0.androidexercise.ui.login.universal.UniversalLoginResponse
import javax.inject.Inject


class OAuthTokenLocalRepository @Inject constructor(
    private val appContext: Context,
    private val securityRepository: SecurityRepository,
    private val credentialsRepository: CredentialsRepository,
    private val retrofit: RetrofitClient
) {

    fun getOAuthToken(
        code: String,
        codeVerifier: String,
        redirectUri: String
    ): UniversalLoginResponse {
        val clientId = appContext.getString(R.string.AUTH0_CLIENT_ID)
        val baseUrl =
            "${appContext.getString(R.string.AUTH0_SCHEME)}://${appContext.getString(R.string.AUTH0_DOMAIN)}"

        val response = retrofit
            .getClient(baseUrl)
            .create(Auth0Services::class.java)
            .getAccessToken(AUTHORIZATION_CODE, clientId, codeVerifier, code, redirectUri)
            ?.execute()

        response?.body()?.let { loginResponse ->
            if (response.isSuccessful) {
                return try {
                    val accessToken = loginResponse.accessToken
                    val refreshToken = loginResponse.refreshToken
                    val idToken = loginResponse.idToken

                    securityRepository.verify(idToken)
                    credentialsRepository.store(accessToken, refreshToken, idToken)
                    loginResponse
                } catch (e: SecurityException) {
                    val message = e.message ?: "Security exception"
                    val code = response.code()
                    OAuthUnauthorizedResponse(code, message)
                } catch (e: Exception) {
                    val message = e.message ?: "Bad response"
                    val code = response.code()
                    OAuthUnauthorizedResponse(code, message)
                }
            }
        }

        val message = response?.message() ?: "Bad request"
        val code = response?.code() ?: 400
        return OAuthUnauthorizedResponse(code, message)
    }

    fun renewAcccessToken(
        refreshToken: String,
    ): UniversalLoginResponse {
        val clientId = appContext.getString(R.string.AUTH0_CLIENT_ID)
        val baseUrl =
            "${appContext.getString(R.string.AUTH0_SCHEME)}://${appContext.getString(R.string.AUTH0_DOMAIN)}"

        val response = retrofit
            .getClient(baseUrl)
            .create(Auth0Services::class.java)
            .renewAccessToken(GRANT_TYPE_TOKEN, clientId, refreshToken)
            ?.execute()

        response?.body()?.let { loginResponse ->
            if (response.isSuccessful) {
                return try {
                    val accessToken = loginResponse.accessToken
                    val refreshToken = loginResponse.refreshToken
                    val idToken = loginResponse.idToken

                    securityRepository.verify(idToken)
                    credentialsRepository.store(accessToken, refreshToken, idToken)
                    loginResponse
                } catch (e: SecurityException) {
                    val message = e.message ?: "Security exception"
                    val code = response.code()
                    OAuthUnauthorizedResponse(code, message)
                } catch (e: Exception) {
                    val message = e.message ?: "Bad response"
                    val code = response.code()
                    OAuthUnauthorizedResponse(code, message)
                }
            }
        }

        val message = response?.message() ?: "Bad request"
        val code = response?.code() ?: 400
        return OAuthUnauthorizedResponse(code, message)
    }


    companion object {
        private const val AUTHORIZATION_CODE = "authorization_code"
        private const val GRANT_TYPE_TOKEN = "token"
    }

}