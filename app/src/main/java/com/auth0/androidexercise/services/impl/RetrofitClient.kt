package com.auth0.androidexercise.services.impl

import retrofit2.Retrofit

interface RetrofitClient {

    fun getClient(baseUrl: String): Retrofit

}