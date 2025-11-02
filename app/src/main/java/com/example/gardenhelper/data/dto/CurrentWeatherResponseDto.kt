package com.example.gardenhelper.data.dto

data class CurrentWeatherResponseDto(
    val location: LocationDto,
    val current: CurrentWeatherDto
)
