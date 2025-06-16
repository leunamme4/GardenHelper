package com.example.gardenhelper.domain.impl

import com.example.gardenhelper.data.network.Result
import com.example.gardenhelper.domain.api.interactors.CalendarInteractor
import com.example.gardenhelper.domain.api.repositories.CalendarRepository
import com.example.gardenhelper.domain.models.calendar.CalendarDay
import com.example.gardenhelper.domain.models.weather.CurrentWeatherResponse

class CalendarInteractorImpl(private val repository: CalendarRepository): CalendarInteractor {
    override suspend fun getCurrentWeather(coords: String): Result<CurrentWeatherResponse> {
        return repository.getCurrentWeather(coords)
    }

    override suspend fun containsDay(date: String): Boolean {
        return repository.containsDay(date)
    }

    override suspend fun getCalendarDay(date: String): CalendarDay {
        return repository.getCalendarDay(date)
    }

    override suspend fun addWeatherData(calendarDay: CalendarDay) {
        repository.addWeatherData(calendarDay)
    }
}