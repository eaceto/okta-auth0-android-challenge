package com.auth0.androidexercise.ui

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auth0.androidexercise.R
import com.auth0.androidexercise.databinding.ActivityMainBinding
import com.auth0.androidexercise.services.coffee.model.Coffee
import com.auth0.androidexercise.toRoundedCorners
import com.auth0.androidexercise.ui.coffee.CoffeeListViewAdapter
import com.auth0.androidexercise.ui.main.MainViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), CoffeeListViewAdapter.CoffeeSelectionListener {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: CoffeeListViewAdapter

    companion object {
        private const val MENU_LOGOUT: Int = 121
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = getString(R.string.toolbar_string)
        binding.toolbar.subtitle = getString(R.string.loading)
        setContentView(binding.root)

        val recyclerView = findViewById<RecyclerView>(R.id.coffeeList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = createAdapter(viewModel.coffees.value ?: listOf())
        bindViewModel(binding, recyclerView)
    }

    private fun bindViewModel(
        binding: ActivityMainBinding,
        recyclerView: RecyclerView
    ) {
        viewModel.loggedIn.observe(this) {
            if (!it) {
                showLoginActivity()
            }
        }

        viewModel.userInfo.observe(this) {
            it?.let {
                binding.toolbar.subtitle = it.formattedUserName()
            }
        }

        viewModel.userPictureUrl.observe(this) {
            it?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    val avatar = BitmapDrawable(
                        resources, Picasso.get()
                            .load(it)
                            .resize(86, 86)
                            .centerCrop()
                            .get()
                            .toRoundedCorners(43f)
                    )

                    CoroutineScope(Dispatchers.Main).launch {
                        binding.toolbar.logo = avatar
                    }
                }
            }
        }

        viewModel.coffees.observe(this) {
            it?.let { coffees ->
                val adapter = createAdapter(coffees)
                CoroutineScope(Dispatchers.Main).launch {
                    recyclerView.adapter = adapter
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(Menu.NONE, MENU_LOGOUT, Menu.NONE, getString(R.string.logout_title))
            .setIcon(R.drawable.ic_exit)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            MENU_LOGOUT -> {
                presentLogoutAlert()
                true
            }
            else -> false
        }
    }

    private fun presentLogoutAlert() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(getString(R.string.logout_title))
        alert.setMessage(getString(R.string.logout_message))
        alert.setPositiveButton(R.string.yes) { _, _ ->
            viewModel.logout()
        }

        alert.setNegativeButton(
            R.string.no
        ) { dialog, _ ->
            dialog.dismiss()
        }

        alert.show()
    }

    private fun showLoginActivity() {
        val next = Intent(this, LoginActivity::class.java)
        startActivity(next)
        finish()
    }

    private fun createAdapter(coffees: List<Coffee>): CoffeeListViewAdapter {
        val adapter = CoffeeListViewAdapter(this, coffees = coffees)
        adapter.onCoffeeSelectionListener = this
        return adapter
    }

    override fun onCoffeeSelected(coffee: Coffee) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Do you want a ${coffee.type} ${coffee.title}? Made with ${
                coffee.ingredients.joinToString(", ")
            }"
        )
        startActivity(Intent.createChooser(intent, getString(R.string.share_coffee)))
    }
}