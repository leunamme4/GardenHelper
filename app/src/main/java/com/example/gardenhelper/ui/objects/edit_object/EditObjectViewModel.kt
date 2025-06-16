package com.example.gardenhelper.ui.objects.edit_object

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenhelper.domain.api.interactors.ObjectsInteractor
import com.example.gardenhelper.domain.models.garden.GardenObject
import kotlinx.coroutines.launch

class EditObjectViewModel(private val objectsInteractor: ObjectsInteractor): ViewModel() {

    private val gardenObjectToSave = GardenObject()

    private val _myObject = MutableLiveData<GardenObject>()
    val myObject: LiveData<GardenObject> = _myObject

    fun getObject(id: Int) {
        viewModelScope.launch {
            val obj = objectsInteractor.getObjectById(id)
            _myObject.postValue(obj)
        }
    }

    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> = _isFinished

    private fun setObjectParameters(name: String, type: String, description: String) {
        gardenObjectToSave.id = _myObject.value!!.id
        gardenObjectToSave.name = name
        gardenObjectToSave.type = type
        gardenObjectToSave.description = description
        gardenObjectToSave.notes = _myObject.value!!.notes
        gardenObjectToSave.notifications = _myObject.value!!.notifications
    }

    fun saveObject(name: String, type: String, description: String) {
        setObjectParameters(name, type, description)
        viewModelScope.launch {
            objectsInteractor.addObject(gardenObjectToSave)
            _isFinished.postValue(true)
        }
    }
}