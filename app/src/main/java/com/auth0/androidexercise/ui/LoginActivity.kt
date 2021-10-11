package com.auth0.androidexercise.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.auth0.androidexercise.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.buttonLogin.setOnClickListener { requestAuthentication() }
        setContentView(binding.root)
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
        TODO()
    }

}