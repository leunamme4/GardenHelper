package com.example.gardenhelper.ui.garden.garden_scheme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenhelper.di.viewModel
import com.example.gardenhelper.domain.api.interactors.GardenInteractor
import com.example.gardenhelper.domain.api.interactors.ObjectsInteractor
import com.example.gardenhelper.domain.models.garden.Garden
import com.example.gardenhelper.domain.models.garden.GardenObject
import com.example.gardenhelper.domain.models.garden.ObjectSizes
import com.example.gardenhelper.ui.objects.ObjectsState
import kotlinx.coroutines.launch

class GardenSchemeViewModel(
    private val gardenInteractor: GardenInteractor,
    private val objectsInteractor: ObjectsInteractor
) : ViewModel() {

    private val _savedObjects = MutableLiveData<Garden>()
    val savedObjects: LiveData<Garden> = _savedObjects

    private val _myObjects = MutableLiveData<List<GardenObject>>()
    val myObjects: LiveData<List<GardenObject>> = _myObjects

    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> = _isFinished

    private var gardenToSave = Garden()

    fun getGardenObjects(id: Int) {
        viewModelScope.launch {
            val garden = gardenInteractor.getGardenById(id)
            if (garden.objects.isNotEmpty()) {
                for (gardenObject in garden.objects) {
                    val obj = objectsInteractor.getObjectById(gardenObject.id)
                    gardenObject.icon = obj.icon
                }
            }

            gardenToSave = garden
            _savedObjects.postValue(garden)
        }
    }

    fun addObject(gardenObject: ObjectSizes) {
        gardenToSave.objects.add(gardenObject)
    }

    fun getObjects() {
        viewModelScope.launch {
            val gardenObjects = objectsInteractor.getObjects()
            _myObjects.postValue(gardenObjects)
        }
    }

    fun saveGarden(changedObjects: MutableList<ObjectSizes>, gardenWidth: Int, gardenHeight: Int, exit: Boolean) {
        gardenToSave.objects = changedObjects
        gardenToSave.width = gardenWidth
        gardenToSave.height = gardenHeight
        viewModelScope.launch {
            gardenInteractor.addGarden(gardenToSave)
            if (exit) {
                _isFinished.postValue(true)
            }
        }
    }
}