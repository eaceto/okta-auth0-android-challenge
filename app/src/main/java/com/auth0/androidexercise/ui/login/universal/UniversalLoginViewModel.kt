package com.auth0.androidexercise.ui.login.universal

import android.net.Uri
import androidx.lifecycle.*
import com.auth0.androidexercise.generateCodeChallenge
import com.auth0.androidexercise.generateCodeVerifier
import com.auth0.androidexercise.security.CredentialsRepository
import com.auth0.androidexercise.security.SecurityRepository
import com.auth0.androidexercise.security.auth.oauth.OAuthTokenLocalRepository
import com.auth0.androidexercise.security.auth.oauth.OAuthTokenResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
open class UniversalLoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: UniversalLoginLocalRepository,
    private val oauthTokenRepository: OAuthTokenLocalRepository,
    @Named("securityRepository") private val securityRepository: SecurityRepository,
    @Named("credentialsRepository") private val credentialsRepository: CredentialsRepository
) : ViewModel() {

    private lateinit var state: String
    private lateinit var redirectUrl: String
    private lateinit var codeChallenge: String
    private lateinit var codeVerifier: String

    private val _loginUrl = MutableLiveData<Uri>()
    val loginUrl: LiveData<Uri> = _loginUrl

    private val _loginState = MutableLiveData(UniversalLoginState.PENDING)
    val loginState: LiveData<UniversalLoginState> = _loginState

    init {
        restoreSession()
    }

    private fun restoreSession() {
        CoroutineScope(Dispatchers.IO).launch {

            val accessToken = credentialsRepository.getAccessToken()
            val idToken = credentialsRepository.getIDToken()
            val refreshToken = credentialsRepository.getRefreshToken()

            if (accessToken.isNullOrBlank()) {
                return@launch
            }
            if (idToken.isNullOrBlank()) {
                return@launch
            }

            CoroutineScope(Dispatchers.Main).launch {
                _loginState.value = UniversalLoginState.LOGGIN_IN
            }

            var validToken = false
            try {
                securityRepository.verify(idToken)
                validToken = true
            } catch (ignored: Exception) {
            }

            if (validToken) {
                CoroutineScope(Dispatchers.Main).launch {
                    _loginState.value = UniversalLoginState.LOGGED_IN
                }
                return@launch
            }

            if (refreshToken.isNullOrBlank()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = oauthTokenRepository.renewAcccessToken(refreshToken!!)
                    CoroutineScope(Dispatchers.Main).launch {
                        _loginState.value =
                            if (response is OAuthTokenResponse) UniversalLoginState.LOGGED_IN else UniversalLoginState.LOGGIN_FAILED
                    }
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                _loginState.value = UniversalLoginState.PENDING
            }
        }
    }

    fun login(redirectUrl: String) {
        state = UUID.randomUUID().toString()
        codeVerifier = generateCodeVerifier()
        codeChallenge = generateCodeChallenge(codeVerifier)

        this.redirectUrl = redirectUrl
        viewModelScope.launch {
            _loginState.value = UniversalLoginState.AUTHORIZING
            CoroutineScope(Dispatchers.IO).launch {
                val url = repository.getAuthorizationUrl(redirectUrl, state, codeChallenge)
                CoroutineScope(Dispatchers.Main).launch {
                    _loginUrl.value = url
                }
            }
        }
    }

    fun handleLoginResponse(uri: Uri): UniversalLoginResponse? {
        if (uri.toString().startsWith(redirectUrl)) {
            val code = uri.getQueryParameter("code")
            val state = uri.getQueryParameter("state")
            if (state != this.state) {
                throw SecurityException("received state does not match the original state.")
            }
            _loginState.value = UniversalLoginState.AUTHORIZED
            return AuthorizationResponse(code!!)
        }

        return null
    }

    fun resetLoginState() {
        viewModelScope.launch {
            _loginState.value = UniversalLoginState.PENDING
        }
    }

    fun getToken(authorizationResponse: AuthorizationResponse) {
        viewModelScope.launch {
            _loginState.value = UniversalLoginState.LOGGIN_IN
            CoroutineScope(Dispatchers.IO).launch {
                val response = oauthTokenRepository.getOAuthToken(
                    authorizationResponse.code,
                    codeVerifier,
                    redirectUrl
                )
                CoroutineScope(Dispatchers.Main).launch {
                    _loginState.value =
                        if (response is OAuthTokenResponse) UniversalLoginState.LOGGED_IN else UniversalLoginState.LOGGIN_FAILED
                }
            }
        }
    }

}