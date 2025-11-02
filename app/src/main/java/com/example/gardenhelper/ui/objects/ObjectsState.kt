package com.example.gardenhelper.ui.objects

import com.example.gardenhelper.domain.models.garden.GardenObject

sealed class ObjectsState {
    data object Empty : ObjectsState()

    class Content(val objects: List<GardenObject>) : ObjectsState()
}