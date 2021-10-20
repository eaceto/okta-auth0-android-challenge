package com.auth0.androidexercise.security

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
@InstallIn(SingletonComponent::class)
object SecurityModule {

    @Singleton
    @Provides
    @Named("securityRepository")
    fun providesSecurityRepository(
        @ApplicationContext context: Context,
        @Named("retrofitClient") retrofitClient: RetrofitClient,
        credentialsRepository: CredentialsRepository
    ): SecurityRepository {
        return SecurityRepository(context, retrofitClient, credentialsRepository)
    }

    @Singleton
    @Provides
    @Named("credentialsRepository")
    fun providesCredentialsRepository(@ApplicationContext context: Context): CredentialsRepository {
        return CredentialsRepository(context)
    }
}