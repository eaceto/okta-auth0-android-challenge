package com.auth0.androidexercise

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class Auth0Application @Inject constructor() : Application()