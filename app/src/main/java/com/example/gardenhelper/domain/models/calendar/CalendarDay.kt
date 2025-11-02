package com.example.gardenhelper.domain.models.calendar

import com.example.gardenhelper.domain.models.weather.CurrentWeatherResponse


class CalendarDay(
    val date: String,
    val weather: CurrentWeatherResponse,
    var notes: MutableList<Int> = mutableListOf()
) {
}