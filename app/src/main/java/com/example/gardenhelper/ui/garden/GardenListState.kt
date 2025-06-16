package com.example.gardenhelper.ui.garden

import com.example.gardenhelper.domain.models.garden.Garden
import com.example.gardenhelper.domain.models.garden.GardenObject
import com.example.gardenhelper.ui.objects.ObjectsState

sealed class GardenListState {
    data object Empty : GardenListState()

    class Content(val objects: List<Garden>) : GardenListState()
}