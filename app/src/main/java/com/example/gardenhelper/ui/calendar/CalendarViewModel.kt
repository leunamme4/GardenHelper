package com.example.gardenhelper.ui.calendar

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenhelper.data.network.Result
import com.example.gardenhelper.domain.api.CalendarInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CalendarViewModel(private val calendarInteractor: CalendarInteractor) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val weather = calendarInteractor.getCurrentWeather("Paris")
            Log.d("weather", "$weather")
            when (weather) {
                is Result.Success -> {
                    _weather.postValue(WeatherState.Content(weather.data))
                }

                is Result.Error -> _weather.postValue(WeatherState.Empty)
                Result.NetworkError -> _weather.postValue(WeatherState.Empty)
            }
        }
    }

    private val _weather = MutableLiveData<WeatherState>()
    val weather: LiveData<WeatherState> = _weather
}