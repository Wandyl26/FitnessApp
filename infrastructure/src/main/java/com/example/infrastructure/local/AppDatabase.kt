package com.example.infrastructure.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.infrastructure.local.dao.AuthTokenDao
import com.example.infrastructure.local.entity.AuthToken

@Database(entities = [AuthToken::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authTokenDao(): AuthTokenDao
}