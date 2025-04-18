package com.example.gardenhelper.data.api

import com.example.gardenhelper.data.network.Result
import com.example.gardenhelper.data.dto.CurrentWeatherDto
import com.example.gardenhelper.data.dto.CurrentWeatherResponseDto

interface NetworkClient {

    suspend fun getCurrentWeather(coords: String): Result<CurrentWeatherResponseDto>
}
