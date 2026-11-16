package com.example.infrastructure.api.dto

data class ApiErrorDto(
    val title: String,
    val status: Int,
    val detail: String
)