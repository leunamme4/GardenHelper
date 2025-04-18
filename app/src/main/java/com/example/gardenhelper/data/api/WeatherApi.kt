package com.example.gardenhelper.data.api


import com.example.gardenhelper.data.dto.CurrentWeatherResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") location: String,
        @Query("key") apiKey: String
    ): Response<CurrentWeatherResponseDto>
}