package com.example.gardenhelper.ui.garden

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenhelper.di.viewModel
import com.example.gardenhelper.domain.api.interactors.GardenInteractor
import com.example.gardenhelper.domain.models.garden.Garden
import com.example.gardenhelper.ui.objects.ObjectsState
import kotlinx.coroutines.launch

class GardenListViewModel(private val gardenInteractor: GardenInteractor) : ViewModel() {

    private val _myGardens = MutableLiveData<GardenListState>()
    val myGardens: LiveData<GardenListState> = _myGardens

    fun getGardens() {
        viewModelScope.launch {
            val gardens = gardenInteractor.getGardens()
            if (gardens.isEmpty()) {
                _myGardens.postValue(GardenListState.Empty)
            } else {
                _myGardens.postValue(GardenListState.Content(gardens))
            }
        }
    }

    fun addGarden(name: String) {
        viewModelScope.launch {
            gardenInteractor.addGarden(Garden(name = name))
            getGardens()
        }
    }

    fun deleteGarden(id: Int) {
        viewModelScope.launch {
            gardenInteractor.deleteGarden(id)
            getGardens()
        }
    }
}