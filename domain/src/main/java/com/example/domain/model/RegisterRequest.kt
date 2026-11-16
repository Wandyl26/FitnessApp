package com.example.domain.model

data class RegisterRequest(
    val identityDocument: String,
    val fullName: String,
    val username: String,
    val email: String,
    val password: String,
    val weight: Double,
    val height: Double
)