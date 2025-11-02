package com.example.gardenhelper.ui.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenhelper.data.network.Result
import com.example.gardenhelper.domain.api.interactors.CalendarInteractor
import com.example.gardenhelper.domain.models.calendar.CalendarDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarViewModel(private val calendarInteractor: CalendarInteractor) : ViewModel() {

//    init {
//        getWeather()
//    }

    private fun getWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            val weather = calendarInteractor.getCurrentWeather("Moscow")
            Log.d("weather", "$weather")
            when (weather) {
                is Result.Success -> {
                    saveWeather(CalendarDay(getCurrentDate(), weather.data, mutableListOf()))
                    //_weather.postValue(WeatherState.Content(weather.data))
                }

                is Result.Error -> {} //_weather.postValue(WeatherState.Empty)
                Result.NetworkError -> {} //_weather.postValue(WeatherState.Empty)
            }
        }
    }

    private fun saveWeather(calendarDay: CalendarDay) {
        viewModelScope.launch {
            if (!calendarInteractor.containsDay(getCurrentDate())) {
                calendarInteractor.addWeatherData(calendarDay)
            }
        }
    }

    fun hasWeather(date: String) {
        viewModelScope.launch {
            _weather.postValue(calendarInteractor.containsDay(date))
        }
    }

    private fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentDate.format(formatter)
    }

    private val _weather = MutableLiveData<Boolean>()
    val weather: LiveData<Boolean> = _weather
}