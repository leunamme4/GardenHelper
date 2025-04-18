package com.example.gardenhelper.di

import com.example.gardenhelper.data.CalendarRepositoryImpl
import com.example.gardenhelper.data.WeatherConverter
import com.example.gardenhelper.data.api.NetworkClient
import com.example.gardenhelper.data.api.WeatherApi
import com.example.gardenhelper.data.network.RetrofitNetworkClient
import com.example.gardenhelper.domain.api.CalendarRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<WeatherApi> {
        Retrofit.Builder()
            .baseUrl("http://api.weatherapi.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single {
        WeatherConverter()
    }

    single<CalendarRepository> {
        CalendarRepositoryImpl(get(), get())
    }
}