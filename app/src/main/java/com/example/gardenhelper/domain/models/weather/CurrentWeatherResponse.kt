package com.example.gardenhelper.domain.models.weather

data class CurrentWeatherResponse(
    val location: Location,
    val current: CurrentWeather
)