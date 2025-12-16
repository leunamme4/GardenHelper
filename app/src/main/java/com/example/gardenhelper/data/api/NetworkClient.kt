package com.example.gardenhelper.data.api

import com.example.gardenhelper.data.network.Result
import com.example.gardenhelper.data.dto.CurrentWeatherDto
import com.example.gardenhelper.data.dto.CurrentWeatherResponseDto
import com.example.gardenhelper.data.dto.auth.TokenResponse

interface NetworkClient {
    suspend fun getCurrentWeather(coords: String): Result<CurrentWeatherResponseDto>

    suspend fun register(email: String, password: String, confirmPassword: String) : Result<String>

    suspend fun login(email: String, password: String) : Result<TokenResponse>
}
