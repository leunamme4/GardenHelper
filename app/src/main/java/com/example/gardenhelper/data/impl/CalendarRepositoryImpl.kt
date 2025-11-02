package com.example.gardenhelper.data.impl

import com.example.gardenhelper.data.api.NetworkClient
import com.example.gardenhelper.data.db.AppDatabase
import com.example.gardenhelper.data.db.EntityConverter
import com.example.gardenhelper.data.dto.WeatherConverter
import com.example.gardenhelper.data.network.Result
import com.example.gardenhelper.domain.api.repositories.CalendarRepository
import com.example.gardenhelper.domain.models.calendar.CalendarDay
import com.example.gardenhelper.domain.models.weather.CurrentWeatherResponse

class CalendarRepositoryImpl(
    private val networkClient: NetworkClient,
    private val converter: WeatherConverter,
    private val database: AppDatabase,
    private val calendarConverter: EntityConverter
) : CalendarRepository {

    override suspend fun getCurrentWeather(coords: String): Result<CurrentWeatherResponse> {
        val result = networkClient.getCurrentWeather(coords)

        return when (result) {
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

    override suspend fun containsDay(date: String): Boolean {
        val days = database.calendarDao().getCalendarDay(date)
        return days != null
    }

    override suspend fun getCalendarDay(date: String): CalendarDay {
        return calendarConverter.toDomain(database.calendarDao().getCalendarDay(date)!!)
    }

    override suspend fun addWeatherData(calendarDay: CalendarDay) {
        database.calendarDao().addCalendarDay(calendarConverter.toEntity(calendarDay))
    }

}