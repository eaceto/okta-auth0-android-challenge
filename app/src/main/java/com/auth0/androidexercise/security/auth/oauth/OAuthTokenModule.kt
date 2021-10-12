package com.auth0.androidexercise.security.auth.oauth

import android.content.Context
import com.auth0.androidexercise.security.CredentialsRepository
import com.auth0.androidexercise.security.SecurityModule
import com.auth0.androidexercise.security.SecurityRepository
import com.auth0.androidexercise.services.impl.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module(
    includes = [
        SecurityModule::class
    ]
)
@InstallIn(SingletonComponent::class) // or whatever graph fits your need the best
object OAuthTokenModule {

    @Singleton
    @Provides
    fun provideOAuthTokenLocalRepository(
        @ApplicationContext context: Context,
        @Named("securityRepository") securityRepository: SecurityRepository,
        @Named("credentialsRepository") credentialsRepository: CredentialsRepository,
        @Named("retrofitClient") retrofitClient: RetrofitClient
    ): OAuthTokenLocalRepository {
        return OAuthTokenLocalRepository(
            context,
            securityRepository,
            credentialsRepository,
            retrofitClient
        )
    }
}