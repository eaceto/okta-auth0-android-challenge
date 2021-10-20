package com.auth0.androidexercise.ui.main

import android.content.Context
import com.auth0.androidexercise.R
import com.auth0.androidexercise.services.coffee.CoffeeServices
import com.auth0.androidexercise.services.coffee.model.Coffee
import com.auth0.androidexercise.services.impl.RetrofitClient
import javax.inject.Inject


class CoffeeRepository @Inject constructor(
    private val appContext: Context,
    private val retrofit: RetrofitClient
) {

    @Throws
    fun getCoffees(): List<Coffee> {
        try {
            val response = retrofit.getClient(appContext.getString(R.string.COFFEE_SERVICE_URL))
                .create(CoffeeServices::class.java)
                .getCoffees()
                ?.execute()

            if (response?.isSuccessful == true) {
                response.body()?.let {
                    return it
                }
            }
        } catch (ignored: Exception) {
        }
        return listOf()
    }

}