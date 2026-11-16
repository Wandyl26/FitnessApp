package com.example.infrastructure.api

import com.example.infrastructure.api.dto.LoginRequestDto
import com.example.infrastructure.api.dto.LoginResponseDto
import com.example.infrastructure.api.dto.RegisterRequestDto
import com.example.infrastructure.api.dto.RegisterResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("8082/api/v1/auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>

    @POST("8081/api/v1/users/register")
    suspend fun register(@Body request: RegisterRequestDto): Response<RegisterResponseDto>

}