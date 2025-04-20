package com.example.gardenhelper.domain.impl

import com.example.gardenhelper.data.network.Result
import com.example.gardenhelper.domain.api.CalendarInteractor
import com.example.gardenhelper.domain.api.CalendarRepository
import com.example.gardenhelper.domain.models.weather.CurrentWeatherResponse

class CalendarInteractorImpl(private val repository: CalendarRepository): CalendarInteractor {
    override suspend fun getCurrentWeather(coords: String): Result<CurrentWeatherResponse> {
        return repository.getCurrentWeather(coords)
    }
}