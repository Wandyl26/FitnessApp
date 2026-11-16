package com.example.domain.repository

import com.example.domain.model.LoginRequest
import com.example.domain.model.LoginResult
import com.example.domain.model.RegisterRequest
import com.example.domain.model.RegisterResult

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<LoginResult>
    suspend fun register(request: RegisterRequest): Result<RegisterResult>
    suspend fun getStoredToken(): String?
}