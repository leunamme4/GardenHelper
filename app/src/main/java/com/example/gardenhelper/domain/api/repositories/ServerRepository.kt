package com.example.gardenhelper.domain.api.repositories

import com.example.gardenhelper.data.dto.auth.TokenResponse
import com.example.gardenhelper.data.network.Result

interface ServerRepository {
    suspend fun register(email: String, password: String, confirmPassword: String) : Result<String>

    suspend fun login(email: String, password: String) : Result<TokenResponse>
}