package com.example.gardenhelper.data.network

import com.example.gardenhelper.data.api.NetworkClient
import com.example.gardenhelper.data.api.WeatherApi
import com.example.gardenhelper.data.dto.CurrentWeatherDto
import com.example.gardenhelper.data.dto.CurrentWeatherResponseDto
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val weatherApi: WeatherApi): NetworkClient {

    override suspend fun getCurrentWeather(coords: String): Result<CurrentWeatherResponseDto> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    weatherApi.getCurrentWeather(coords, "87d4f25962514a74a1d105852251704")

                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    val errorJson = response.errorBody()?.string()
                    val error = Gson().fromJson(errorJson, ErrorResponse::class.java)
                    Result.Error(
                        message = error.message ?: "Не удалось разобрать ошибку",
                        code = error.code
                    )
                }
            } catch (e: Exception) {
                Result.Error(message = e.message.toString(), code = 0)
            }

        }
    }
}