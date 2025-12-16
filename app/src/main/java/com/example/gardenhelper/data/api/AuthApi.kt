package com.example.gardenhelper.data.api

import com.example.gardenhelper.data.dto.auth.LoginRequest
import com.example.gardenhelper.data.dto.auth.RegisterRequest
import com.example.gardenhelper.data.dto.auth.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<String>

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<TokenResponse>

    @GET("me")
    suspend fun getMe(
        @Header("Authorization") token: String
    ): Response<Map<String, String>>
}
