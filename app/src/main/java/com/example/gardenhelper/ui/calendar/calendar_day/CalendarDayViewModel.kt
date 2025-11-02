package com.example.gardenhelper.ui.calendar.calendar_day

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenhelper.domain.api.interactors.CalendarInteractor
import com.example.gardenhelper.domain.api.interactors.NotesInteractor
import com.example.gardenhelper.domain.models.calendar.CalendarDay
import com.example.gardenhelper.domain.models.notes.Note
import com.example.gardenhelper.domain.models.weather.CurrentWeather
import com.example.gardenhelper.domain.models.weather.Location
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class CalendarDayViewModel(
    private val calendarInteractor: CalendarInteractor,
    private val notesInteractor: NotesInteractor
) : ViewModel() {
    private val _day = MutableLiveData<CalendarDay>()
    val day: LiveData<CalendarDay> = _day

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    fun getDay(date: String) {
        viewModelScope.launch {
            val day = calendarInteractor.getCalendarDay(date)
            _day.postValue(day)

            getNotes(day.notes)
        }
    }

    suspend fun getNotes(notesIds: List<Int>) {
        val notes = notesInteractor.getNotes(notesIds)
        if (notes.isNotEmpty()) {
            _notes.postValue(notes)
        }
    }

    fun getFormattedWeatherText(weather: CurrentWeather, location: Location): String {
        return StringBuilder().apply {
            // Основная информация о местоположении
            append("📍 ${location.name}, ${location.country}\n")
            append("🕒 ${weather.last_updated}\n\n")

            // Температура и ощущения
            append("🌡 ${weather.temp_c}°C (ощущается как ${weather.feelslike_c}°C)\n")

            // Погодные условия
            append("☁ Облачность: ${weather.cloud}%\n")
            append("💧 Влажность: ${weather.humidity}%\n")

            // Ветер
            append("🌬 Ветер: ${weather.wind_kph} км/ч, ${weather.wind_dir}\n")

            // Осадки и видимость
            append("🌧 Осадки: ${weather.precip_mm} мм\n")
            append("👁 Видимость: ${weather.vis_km} км\n")

            // Дополнительно
            append("📊 Давление: ${weather.pressure_mb} мбар\n")
            append("☀ УФ-индекс: ${weather.uv}")
        }.toString()
    }

    fun formatDateToRussian(dateString: String): String {
        return try {
            // Парсим исходную дату
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(dateString) ?: return dateString

            // Форматируем в русский вариант
            val outputFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
            outputFormat.format(date)
        } catch (e: Exception) {
            // В случае ошибки возвращаем исходную строку
            dateString
        }
    }

    fun deleteNoteById(noteId: Int) {
        viewModelScope.launch {
            val date = _day.value!!.date
            notesInteractor.deleteNoteById(noteId)
            val day = calendarInteractor.getCalendarDay(date)
            day.notes.remove(noteId)
            calendarInteractor.addWeatherData(day)
            getDay(date)
        }
    }
}