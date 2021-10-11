package com.auth0.androidexercise.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.auth0.androidexercise.R
import com.auth0.androidexercise.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val MENU_LOGOUT: Int = 121
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitle(R.string.app_name)
        binding.toolbar.subtitle = "~Unknown user~"
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(Menu.NONE, MENU_LOGOUT, Menu.NONE, "Log out")
            .setIcon(R.drawable.ic_exit)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            MENU_LOGOUT -> {
                true
            }
            else -> false
        }
    }

}