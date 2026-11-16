package com.example.infrastructure.api.dto

data class RegisterRequestDto(
    val identityDocument: String,
    val fullName: String,
    val username: String,
    val email: String,
    val password: String,
    val weight: Double,
    val height: Double
)