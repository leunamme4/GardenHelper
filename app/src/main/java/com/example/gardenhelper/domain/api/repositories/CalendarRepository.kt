package com.example.gardenhelper.domain.api.repositories

import com.example.gardenhelper.data.network.Result
import com.example.gardenhelper.domain.models.calendar.CalendarDay
import com.example.gardenhelper.domain.models.weather.CurrentWeatherResponse

interface CalendarRepository {
    suspend fun getCurrentWeather(coords: String): Result<CurrentWeatherResponse>

    suspend fun containsDay(date: String): Boolean

    suspend fun getCalendarDay(date: String): CalendarDay

    suspend fun addWeatherData(calendarDay: CalendarDay)
}