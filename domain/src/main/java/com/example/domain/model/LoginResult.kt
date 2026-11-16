package com.example.domain.model

data class LoginResult(
    val token: String,
    val username: String,
    val message: String
)