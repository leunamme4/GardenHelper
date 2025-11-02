package com.example.gardenhelper.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenhelper.domain.api.interactors.ObjectsInteractor
import kotlinx.coroutines.launch

class SettingsViewModel(private val objectsInteractor: ObjectsInteractor) : ViewModel() {
    fun uploadAllToFirestore() {
        viewModelScope.launch {
            objectsInteractor.uploadAllToFirestore()
            _uploadIsFinished.postValue(true)
            _uploadIsFinished.postValue(false)
        }
    }

    fun downloadAllFromFirestore() {
        viewModelScope.launch {
            objectsInteractor.downloadAllFromFirestore()
            _downloadIsFinished.postValue(true)
            _downloadIsFinished.postValue(false)
        }
    }

    private val _uploadIsFinished = MutableLiveData<Boolean>()
    val uploadIsFinished: LiveData<Boolean> = _uploadIsFinished

    private val _downloadIsFinished = MutableLiveData<Boolean>()
    val downloadIsFinished: LiveData<Boolean> = _downloadIsFinished
}