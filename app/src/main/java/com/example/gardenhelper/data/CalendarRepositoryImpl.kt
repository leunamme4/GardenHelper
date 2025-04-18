package com.example.gardenhelper.data

import com.example.gardenhelper.data.api.NetworkClient
import com.example.gardenhelper.data.dto.CurrentWeatherDto
import com.example.gardenhelper.domain.api.CalendarRepository
import com.example.gardenhelper.domain.models.CurrentWeather
import com.example.gardenhelper.data.network.Result
import com.example.gardenhelper.domain.models.CurrentWeatherResponse

class CalendarRepositoryImpl(private val networkClient: NetworkClient, private val converter: WeatherConverter): CalendarRepository {

    override suspend fun getCurrentWeather(coords: String): Result<CurrentWeatherResponse> {
        val result = networkClient.getCurrentWeather(coords)

        return when(result) {
            is Result.Success -> {
                Result.Success(converter.mapCurrentWeatherResponse(result.data))
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