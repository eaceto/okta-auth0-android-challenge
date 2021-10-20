package com.auth0.androidexercise.services.impl

import android.content.Context
import com.auth0.androidexercise.security.CredentialsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Singleton
    @Provides
    @Named("retrofitClient")
    fun providesRetrofitClient(
        @ApplicationContext context: Context,
        @Named("credentialsRepository") credentialsRepository: CredentialsRepository
    ): RetrofitClient {
        return RetrofitClientImpl(context, credentialsRepository)
    }

}