package com.example.fitnessapp
import com.example.domain.model.LoginRequest
import com.example.domain.model.LoginResult
import com.example.domain.model.RegisterRequest
import com.example.domain.model.RegisterResult
import com.example.domain.repository.AuthRepository

class FakeAuthRepository(
    var loginResult: Result<LoginResult> = Result.failure(Exception("not configured")),
    var registerResult: Result<RegisterResult> = Result.failure(Exception("not configured")),
    var storedToken: String? = null
) : AuthRepository {

    override suspend fun login(request: LoginRequest): Result<LoginResult> = loginResult
    override suspend fun register(request: RegisterRequest): Result<RegisterResult> = registerResult
    override suspend fun getStoredToken(): String? = storedToken
}