package com.auth0.androidexercise.ui.login.universal

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // or whatever graph fits your need the best
object UniversalLoginModule {

    @Singleton
    @Provides
    fun provideUniversalLoginLocalRepository(@ApplicationContext context: Context): UniversalLoginLocalRepository {
        return UniversalLoginLocalRepository(context)
    }
}