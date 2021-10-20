package com.auth0.androidexercise.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.auth0.androidexercise.security.CredentialsRepository
import com.auth0.androidexercise.security.SecurityRepository
import com.auth0.androidexercise.services.auth0.model.UserInfo
import com.auth0.androidexercise.services.coffee.model.Coffee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @Named("securityRepository") private val securityRepository: SecurityRepository,
    @Named("credentialsRepository") private val credentialsRepository: CredentialsRepository,
    @Named("coffeeRepository") private val coffeeRepository: CoffeeRepository
) : ViewModel() {

    private val _loggedIn = MutableLiveData(true)
    val loggedIn: LiveData<Boolean> = _loggedIn

    private val _userInfo = MutableLiveData<UserInfo?>(null)
    val userInfo: LiveData<UserInfo?> = _userInfo

    private val _userPictureUrl = MutableLiveData<String?>(null)
    val userPictureUrl: LiveData<String?> = _userPictureUrl

    private val _coffees = MutableLiveData<List<Coffee>>(listOf())
    val coffees: LiveData<List<Coffee>> = _coffees

    init {
        loadUserInfo()
        getCoffees()
    }

    fun logout() {
        credentialsRepository.getAccessToken()?.let { accessToken ->
            CoroutineScope(Dispatchers.IO).launch {
                val loggedOut = securityRepository.logout(accessToken)
                CoroutineScope(Dispatchers.Main).launch {
                    _loggedIn.value = !loggedOut
                }
            }
        }
    }

    private fun loadUserInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            val idToken = credentialsRepository.getIDToken()
            if (idToken.isNullOrBlank()) return@launch
            CoroutineScope(Dispatchers.Main).launch {
                UserInfo.from(idToken)?.let {
                    _userInfo.value = it
                    _userPictureUrl.value = it.picture
                }
            }
        }
    }

    private fun getCoffees() {
        CoroutineScope(Dispatchers.IO).launch {
            val coffees = coffeeRepository.getCoffees()
            CoroutineScope(Dispatchers.Main).launch {
                _coffees.value = coffees
            }
        }
    }
}