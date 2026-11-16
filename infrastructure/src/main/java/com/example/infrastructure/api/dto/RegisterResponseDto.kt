package com.example.infrastructure.api.dto

data class RegisterResponseDto(
    val id: Int,
    val username: String,
    val email: String,
    val message: String
)