package com.auth0.androidexercise.services.impl

import android.content.Context
import com.auth0.androidexercise.BuildConfig
import com.auth0.androidexercise.security.CredentialsRepository
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitClientImpl @Inject constructor(
    private val appContext: Context,
    private val credentialsRepository: CredentialsRepository
) : RetrofitClient {


    private val retrofitClients = hashMapOf<String, Retrofit>()

    override fun getClient(baseUrl: String): Retrofit {
        var client = retrofitClients[baseUrl]
        if (client == null) {
            client = initRetrofit(baseUrl)
            retrofitClients[baseUrl] = client
        }
        return client!!
    }

    private fun initRetrofit(baseUrl: String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(createHttpClient())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    private fun createHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.NONE)
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        return OkHttpClient
            .Builder()
            .addInterceptor(logging)
            .addInterceptor(Interceptor { chain ->
                val newRequest: Request = if (credentialsRepository.hasAccessToken()) {
                    chain.request().newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer ${credentialsRepository.getAccessToken()}"
                        )
                        .build()
                } else {
                    chain.request()
                }
                chain.proceed(newRequest)
            })
            .build()
    }

}