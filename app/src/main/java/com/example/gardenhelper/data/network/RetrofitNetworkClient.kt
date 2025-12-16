package com.example.gardenhelper.data.network

import com.example.gardenhelper.data.api.AuthApi
import com.example.gardenhelper.data.api.NetworkClient
import com.example.gardenhelper.data.api.WeatherApi
import com.example.gardenhelper.data.dto.CurrentWeatherDto
import com.example.gardenhelper.data.dto.CurrentWeatherResponseDto
import com.example.gardenhelper.data.dto.auth.LoginRequest
import com.example.gardenhelper.data.dto.auth.RegisterRequest
import com.example.gardenhelper.data.dto.auth.TokenResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val weatherApi: WeatherApi, private val authApi: AuthApi): NetworkClient {

    override suspend fun getCurrentWeather(coords: String): Result<CurrentWeatherResponseDto> {
        return withContext(Dispatchers.IO) {
            try {
                val response =
                    weatherApi.getCurrentWeather(coords, "")

                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    val errorJson = response.errorBody()?.string()
                    val error = Gson().fromJson(errorJson, ErrorResponse::class.java)
                    Result.Error(
                        message = error.message,
                        code = error.code
                    )
                }
            } catch (e: Exception) {
                Result.Error(message = e.message.toString(), code = 0)
            }

        }
    }

    override suspend fun register(
        email: String,
        password: String,
        confirmPassword: String
    ): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApi.register(RegisterRequest(email, password, confirmPassword))

                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    val errorJson = response.errorBody()?.string()
                    val error = Gson().fromJson(errorJson, ErrorResponse::class.java)
                    Result.Error(
                        message = error.message,
                        code = error.code
                    )
                }
            } catch (e: Exception) {
                Result.Error(message = e.message.toString(), code = 0)
            }
        }
    }

    override suspend fun login(email: String, password: String): Result<TokenResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApi.login(LoginRequest(email, password))

                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    val errorJson = response.errorBody()?.string()
                    val error = Gson().fromJson(errorJson, ErrorResponse::class.java)
                    Result.Error(
                        message = error.message,
                        code = error.code
                    )
                }
            } catch (e: Exception) {
                Result.Error(message = e.message.toString(), code = 0)
            }
        }
    }
}