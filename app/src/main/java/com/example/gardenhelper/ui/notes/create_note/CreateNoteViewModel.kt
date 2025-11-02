package com.example.gardenhelper.ui.notes.create_note

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenhelper.domain.api.interactors.CalendarInteractor
import com.example.gardenhelper.domain.api.interactors.NotesInteractor
import com.example.gardenhelper.domain.api.interactors.ObjectsInteractor
import com.example.gardenhelper.domain.models.notes.Note
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateNoteViewModel(
    private val notesInteractor: NotesInteractor,
    private val calendarInteractor: CalendarInteractor,
    private val objectsInteractor: ObjectsInteractor
) : ViewModel() {

    var note = Note()
    private var calendarDate = ""
    private var objectId = 0

    private val _newImage = MutableLiveData<String>()
    val newImage: LiveData<String> = _newImage

    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> = _isFinished

    fun setDate(date: String) {
        if (date.isNotEmpty()) {
            this.calendarDate = date
        }
    }

    fun setObjectId(objectId: Int) {
        this.objectId = objectId
    }

    fun addImage(path: String) {
        note.images.add(path)
        _newImage.postValue(path)
    }

    private val coroutineErrorHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("CoroutineException", "Ошибка в корутине: ${throwable.message}", throwable)
    }

    fun saveNoteFromCalendar(title: String, description: String) {
        note.name = title
        note.text = description

        viewModelScope.launch(Dispatchers.IO + coroutineErrorHandler) {
            notesInteractor.addNote(note)
            val day = calendarInteractor.getCalendarDay(calendarDate)
            day.notes.add(note.id)
            calendarInteractor.addWeatherData(day)
            _isFinished.postValue(true)
        }
    }

    fun saveNoteFromObject(title: String, description: String) {
        note.name = title
        note.text = description

        viewModelScope.launch(Dispatchers.IO + coroutineErrorHandler) {
            notesInteractor.addNote(note)
            val gardenObject = objectsInteractor.getObjectById(objectId)
            gardenObject.notes.add(note.id)
            objectsInteractor.addObject(gardenObject)
            _isFinished.postValue(true)
        }
    }
}