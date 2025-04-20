package com.example.gardenhelper.domain.api

import com.example.gardenhelper.data.network.Result
import com.example.gardenhelper.domain.models.weather.CurrentWeatherResponse

interface CalendarInteractor {
    suspend fun getCurrentWeather(coords: String): Result<CurrentWeatherResponse>
}