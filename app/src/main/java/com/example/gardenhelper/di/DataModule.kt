package com.example.gardenhelper.di

import androidx.room.Room
import androidx.work.WorkManager
import com.example.gardenhelper.data.api.AuthApi
import com.example.gardenhelper.data.impl.CalendarRepositoryImpl
import com.example.gardenhelper.data.dto.WeatherConverter
import com.example.gardenhelper.data.api.NetworkClient
import com.example.gardenhelper.data.api.WeatherApi
import com.example.gardenhelper.data.network.RetrofitNetworkClient
import com.example.gardenhelper.data.db.AppDatabase
import com.example.gardenhelper.data.db.EntityConverter
import com.example.gardenhelper.data.impl.GardenRepositoryImpl
import com.example.gardenhelper.data.impl.NotesRepositoryImpl
import com.example.gardenhelper.data.impl.NotificationsRepositoryImpl
import com.example.gardenhelper.data.impl.ObjectsRepositoryImpl
import com.example.gardenhelper.data.impl.ServerRepositoryImpl
import com.example.gardenhelper.data.work_manager.WorkManagerScheduler
import com.example.gardenhelper.domain.api.repositories.CalendarRepository
import com.example.gardenhelper.domain.api.repositories.GardenRepository
import com.example.gardenhelper.domain.api.repositories.NotesRepository
import com.example.gardenhelper.domain.api.repositories.NotificationsRepository
import com.example.gardenhelper.domain.api.repositories.ObjectsRepository
import com.example.gardenhelper.domain.api.repositories.ServerRepository
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
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

    single<AuthApi> {
        Retrofit.Builder()
            .baseUrl("http://server:8080/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(AuthApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(authApi = get<AuthApi>(),
            weatherApi = get<WeatherApi>())
    }

    single {
        WeatherConverter()
    }

    single {
        EntityConverter()
    }

    single<CalendarRepository> {
        CalendarRepositoryImpl(get(), get(), get(), get())
    }

    single<NotesRepository> {
        NotesRepositoryImpl(get(), get())
    }

    single<ObjectsRepository> {
        ObjectsRepositoryImpl(get(), get())
    }

    single<GardenRepository> {
        GardenRepositoryImpl(get(), get())
    }

    single<NotificationsRepository> {
        NotificationsRepositoryImpl(get(), get())
    }

    single<ServerRepository> {
        ServerRepositoryImpl(get())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db").build()
    }

    // WorkManager (если нужно передавать)
    single { WorkManager.getInstance(get()) }

    // Планировщик задач
    single { WorkManagerScheduler(get()) }
}