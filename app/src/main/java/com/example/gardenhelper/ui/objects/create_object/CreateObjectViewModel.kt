package com.example.gardenhelper.ui.objects.create_object

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenhelper.domain.api.interactors.ObjectsInteractor
import com.example.gardenhelper.domain.models.garden.GardenObject
import kotlinx.coroutines.launch

class CreateObjectViewModel(private val objectsInteractor: ObjectsInteractor): ViewModel() {

    private val gardenObject = GardenObject()

    ////
    private val _myObject = MutableLiveData<GardenObject>()
    val myObject: LiveData<GardenObject> = _myObject

    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> = _isFinished

    private fun setObjectParameters(name: String, type: String, description: String) {
        gardenObject.name = name
        gardenObject.type = type
        gardenObject.description = description
    }

    fun saveObject(name: String, type: String, description: String) {
        setObjectParameters(name, type, description)
        viewModelScope.launch {
            objectsInteractor.addObject(gardenObject)
            _isFinished.postValue(true)
        }
    }

    fun addImage(path: String) {
        gardenObject.icon = path
    }
}