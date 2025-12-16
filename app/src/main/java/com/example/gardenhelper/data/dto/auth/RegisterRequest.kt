package com.example.gardenhelper.data.dto.auth

data class RegisterRequest(
    val email: String,
    val password: String,
    val confirmPassword: String
)
