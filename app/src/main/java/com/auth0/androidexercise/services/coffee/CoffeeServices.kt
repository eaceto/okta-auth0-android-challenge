package com.auth0.androidexercise.services.coffee

import com.auth0.androidexercise.services.coffee.model.Coffee
import retrofit2.Call
import retrofit2.http.GET

interface CoffeeServices {

    @GET("/coffees")
    fun getCoffees(): Call<List<Coffee>?>?

}