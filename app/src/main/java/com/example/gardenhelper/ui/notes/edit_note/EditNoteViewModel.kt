package com.example.gardenhelper.ui.notes.edit_note

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenhelper.domain.api.interactors.NotesInteractor
import com.example.gardenhelper.domain.models.notes.Note
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditNoteViewModel(
    private val notesInteractor: NotesInteractor
) : ViewModel() {

    private var noteToSave = Note()

    private val _note = MutableLiveData<Note>()
    val note: LiveData<Note> = _note

    private val _newImage = MutableLiveData<String>()
    val newImage: LiveData<String> = _newImage

    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> = _isFinished

    fun getNote(id: Int) {
        viewModelScope.launch {
            val noteValue = notesInteractor.getNoteById(id)
            _note.postValue(noteValue)
            noteToSave.images.addAll(noteValue.images)
        }
    }

    fun addImage(path: String) {
        noteToSave.images.add(path)
        _newImage.postValue(path)
    }

    private val coroutineErrorHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e("CoroutineException", "Ошибка в корутине: ${throwable.message}", throwable)
    }

    fun saveNote(title: String, description: String) {
        noteToSave.id = note.value!!.id
        noteToSave.name = title
        noteToSave.text = description

        viewModelScope.launch(Dispatchers.IO + coroutineErrorHandler) {
            notesInteractor.addNote(noteToSave)
            _isFinished.postValue(true)
        }
    }
}