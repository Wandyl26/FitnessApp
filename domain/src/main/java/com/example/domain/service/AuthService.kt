package com.example.domain.service

import com.example.domain.model.LoginRequest
import com.example.domain.model.LoginResult
import com.example.domain.model.RegisterRequest
import com.example.domain.model.RegisterResult
import com.example.domain.repository.AuthRepository

class AuthService(private val authRepository: AuthRepository) {

    suspend fun login(request: LoginRequest): Result<LoginResult> =
        authRepository.login(request)

    suspend fun register(request: RegisterRequest): Result<RegisterResult> =
        authRepository.register(request)

    suspend fun getStoredToken(): String? = authRepository.getStoredToken()
}