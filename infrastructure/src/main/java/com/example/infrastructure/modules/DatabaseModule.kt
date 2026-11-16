package com.example.infrastructure.modules

import android.content.Context
import androidx.room.Room
import com.example.infrastructure.local.AppDatabase
import com.example.infrastructure.local.dao.AuthTokenDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "auth_db")
            .build()

    @Provides
    fun provideAuthTokenDao(db: AppDatabase): AuthTokenDao = db.authTokenDao()
}