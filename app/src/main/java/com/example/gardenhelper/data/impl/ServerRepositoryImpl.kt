package com.example.gardenhelper.data.impl

import com.example.gardenhelper.data.api.NetworkClient
import com.example.gardenhelper.data.dto.auth.TokenResponse
import com.example.gardenhelper.data.network.Result
import com.example.gardenhelper.data.network.RetrofitNetworkClient
import com.example.gardenhelper.domain.api.repositories.ServerRepository

class ServerRepositoryImpl(private val networkClient: NetworkClient) : ServerRepository {
    override suspend fun register(
        email: String,
        password: String,
        confirmPassword: String
    ): Result<String> {
        val result = networkClient.register(email, password, confirmPassword)

        return when (result) {
            is Result.Success -> {
                Result.Success(result.data)
            }

            is Result.Error -> {
                Result.Error(result.message, result.code)
            }

            Result.NetworkError -> {
                Result.NetworkError
            }
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<TokenResponse> {
        val result = networkClient.login(email, password)

        return when (result) {
            is Result.Success -> {
                Result.Success(result.data)
            }

            is Result.Error -> {
                Result.Error(result.message, result.code)
            }

            Result.NetworkError -> {
                Result.NetworkError
            }
        }
    }
}