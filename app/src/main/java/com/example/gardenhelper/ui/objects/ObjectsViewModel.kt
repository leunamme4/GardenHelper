package com.example.gardenhelper.ui.objects

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenhelper.domain.api.interactors.ObjectsInteractor
import com.example.gardenhelper.domain.models.garden.GardenObject
import kotlinx.coroutines.launch

class ObjectsViewModel(private val objectsInteractor: ObjectsInteractor) : ViewModel() {

    private val _myObjects = MutableLiveData<ObjectsState>()
    val myObjects: LiveData<ObjectsState> = _myObjects

    private var gardenObjects = listOf<GardenObject>()

    fun getObjects() {
        viewModelScope.launch {
            val objects = objectsInteractor.getObjects()
            if (objects.isEmpty()) {
                _myObjects.postValue(ObjectsState.Empty)
            } else {
                _myObjects.postValue(ObjectsState.Content(objects))
                gardenObjects = objects
            }
        }
    }

    fun deleteObject(id: Int) {
        viewModelScope.launch {
            objectsInteractor.deleteObject(id)
            getObjects()
        }
    }

    fun searchObjectsByName(query: String) {
        if (gardenObjects.isNotEmpty()) {
            val foundedObjects = mutableListOf<GardenObject>()
            for (gardenObject in gardenObjects) {
                if (gardenObject.name.contains(query, ignoreCase = true)) {
                    foundedObjects.add(gardenObject)
                }
            }
            if (foundedObjects.isNotEmpty()) {
                _myObjects.postValue(ObjectsState.Content(foundedObjects))
            }
        }
    }
}