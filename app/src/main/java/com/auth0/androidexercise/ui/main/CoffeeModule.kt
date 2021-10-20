package com.auth0.androidexercise.ui.main

import android.content.Context
import com.auth0.androidexercise.services.impl.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // or whatever graph fits your need the best
object CoffeeModule {

    @Singleton
    @Provides
    @Named("coffeeRepository")
    fun provideMainRepository(
        @ApplicationContext context: Context,
        @Named("retrofitClient") retrofitClient: RetrofitClient
    ): CoffeeRepository {
        return CoffeeRepository(context, retrofitClient)
    }
}
