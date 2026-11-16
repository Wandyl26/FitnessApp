package com.example.infrastructure.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.infrastructure.local.entity.AuthToken

@Dao
interface AuthTokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(authToken: AuthToken)

    @Query("SELECT * FROM auth_token WHERE id = 1")
    suspend fun getToken(): AuthToken?

    @Query("DELETE FROM auth_token")
    suspend fun clearToken()
}