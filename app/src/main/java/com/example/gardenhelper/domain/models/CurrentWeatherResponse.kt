package com.example.gardenhelper.domain.models

data class CurrentWeatherResponse(
    val location: Location,
    val current: CurrentWeather
)