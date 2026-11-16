package com.example.infrastructure.service

import com.example.domain.model.LoginRequest
import com.example.domain.model.LoginResult
import com.example.domain.model.RegisterRequest
import com.example.domain.model.RegisterResult
import com.example.domain.repository.AuthRepository
import com.example.infrastructure.api.AuthApi
import com.example.infrastructure.api.dto.ApiErrorDto
import com.example.infrastructure.api.dto.LoginRequestDto
import com.example.infrastructure.api.dto.RegisterRequestDto
import com.example.infrastructure.local.dao.AuthTokenDao
import com.example.infrastructure.local.entity.AuthToken
import com.google.gson.Gson
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val authTokenDao: AuthTokenDao,
    private val gson: Gson
) : AuthRepository {

    override suspend fun login(request: LoginRequest): Result<LoginResult> {
        return try {
            val response = authApi.login(
                LoginRequestDto(
                    username = request.username,
                    password = request.password
                )
            )
            if (response.isSuccessful) {
                val body = response.body()!!
                authTokenDao.insertToken(
                    AuthToken(token = body.token, username = body.username)
                )
                Result.success(
                    LoginResult(
                        token = body.token,
                        username = body.username,
                        message = body.message
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = gson.fromJson(errorBody, ApiErrorDto::class.java)
                Result.failure(Exception(apiError?.detail ?: "Error desconocido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterRequest): Result<RegisterResult> {
        return try {
            val response = authApi.register(
                RegisterRequestDto(
                    identityDocument = request.identityDocument,
                    fullName = request.fullName,
                    username = request.username,
                    email = request.email,
                    password = request.password,
                    weight = request.weight,
                    height = request.height
                )
            )
            if (response.isSuccessful) {
                val body = response.body()!!
                Result.success(
                    RegisterResult(
                        id = body.id,
                        username = body.username,
                        email = body.email,
                        message = body.message
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = gson.fromJson(errorBody, ApiErrorDto::class.java)
                Result.failure(Exception(apiError?.detail ?: "Error desconocido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getStoredToken(): String? =
        authTokenDao.getToken()?.token
}