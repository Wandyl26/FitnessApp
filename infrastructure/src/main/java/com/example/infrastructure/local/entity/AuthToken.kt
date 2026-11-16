package com.example.infrastructure.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_token")
data class AuthToken(
    @PrimaryKey val id: Int = 1, // Solo un token activo
    val token: String,
    val username: String
)