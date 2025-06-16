package com.example.gardenhelper.data.work_manager

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.gardenhelper.data.network.Result.*
import com.example.gardenhelper.domain.api.repositories.CalendarRepository
import com.example.gardenhelper.domain.models.calendar.CalendarDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NetworkRequestWorker(
    val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {
    override suspend fun doWork(): Result {
        Log.d("worker", "started")

        val repository: CalendarRepository by inject()

        return withContext(Dispatchers.IO) {
            try {
                val prefs = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)
                val latitude = prefs.getString("latitude", "59.9343")
                val longitude = prefs.getString("longitude", "30.3351")
                val weather = repository.getCurrentWeather("$latitude,$longitude")
                when(weather) {
                    is Error -> {}
                    is NetworkError -> {}
                    is Success -> {
                        val calendarDay = CalendarDay(
                            date = getCurrentDate(),
                            weather = weather.data
                        )
                        repository.addWeatherData(calendarDay)
                        Log.d("worker", "saved")
                    }
                }

                Result.success()
            } catch (e: Exception) {
                // При ошибке можно повторить попытку
                Result.retry()
            }
        }
    }

    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }
}