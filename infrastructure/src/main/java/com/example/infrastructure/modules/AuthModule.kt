package com.example.infrastructure.modules

import com.example.domain.repository.AuthRepository
import com.example.domain.service.AuthService
import com.example.infrastructure.api.AuthApi
import com.example.infrastructure.local.dao.AuthTokenDao
import com.example.infrastructure.service.AuthRepositoryImpl
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        authApi: AuthApi,
        authTokenDao: AuthTokenDao,
        gson: Gson
    ): AuthRepository = AuthRepositoryImpl(authApi, authTokenDao, gson)

    @Provides
    fun provideAuthService(repository: AuthRepository): AuthService =
        AuthService(repository)
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)
}