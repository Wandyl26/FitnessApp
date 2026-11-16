package com.example.infrastructure.api.dto

data class LoginResponseDto(
    val token: String,
    val username: String,
    val message: String
)