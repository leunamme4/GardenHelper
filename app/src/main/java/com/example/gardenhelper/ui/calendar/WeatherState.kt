package com.example.gardenhelper.ui.calendar

import com.example.gardenhelper.domain.models.weather.CurrentWeatherResponse

sealed class WeatherState {
    data object Empty : WeatherState()

    data class Content(val weather: CurrentWeatherResponse) : WeatherState()
}