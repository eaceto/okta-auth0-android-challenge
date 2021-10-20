package com.auth0.androidexercise.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.auth0.androidexercise.R
import com.auth0.androidexercise.databinding.ActivityLoginBinding
import com.auth0.androidexercise.ui.login.universal.AuthorizationResponse
import com.auth0.androidexercise.ui.login.universal.UniversalLoginState
import com.auth0.androidexercise.ui.login.universal.UniversalLoginViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: UniversalLoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.buttonLogin.setOnClickListener { requestAuthentication() }
        setContentView(binding.root)

        bindViewModel()

        handleNewIntent(intent)
    }

    private fun bindViewModel() {
        viewModel.loginUrl.observe(this) {
            openURLInBrowser(it)
        }

        viewModel.loginState.observe(this) {
            val loginEnabled =
                it == UniversalLoginState.PENDING || it == UniversalLoginState.LOGGIN_FAILED || it == UniversalLoginState.AUTHORIZING

            findViewById<Button>(R.id.button_login).isEnabled = loginEnabled
            findViewById<CircularProgressIndicator>(R.id.loading_indicator).visibility = if (loginEnabled) View.INVISIBLE else View.VISIBLE

            if (it == UniversalLoginState.LOGGED_IN) {
                showMainActivity()
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleNewIntent(intent)
    }

    private fun handleNewIntent(intent: Intent?) {
        intent?.data?.let {
            val response = viewModel.handleLoginResponse(it)
            if (response == null) {
                viewModel.resetLoginState()
            } else if (response is AuthorizationResponse) {
                viewModel.getToken(response)
            }
        }
    }

    private fun showMainActivity() {
        val next = Intent(this, MainActivity::class.java)
        startActivity(next)
        finish()
    }


    /**
     * Authenticate using the Auth0 Universal Login
     * and show the MainActivity
     */
    private fun requestAuthentication() {
        viewModel.login(getString(R.string.AUTH0_REDIRECT))
    }

    /**
     * Given an Uri it ask the OS to open it on an external app
     */
    private fun openURLInBrowser(uri: Uri) {
        val next = Intent(Intent.ACTION_VIEW, uri)

        val title = getString(R.string.app_name)
        val chooser = Intent.createChooser(next, title)

        try {
            startActivity(chooser)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "Could not find an activity to open our intent. ${e.message}")
        }

    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}