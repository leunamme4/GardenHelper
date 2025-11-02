package com.example.gardenhelper.domain.models.garden

import kotlin.random.Random

data class ObjectSizes(
    var id: Int = Random.nextInt(1000000), // + icon
    var width: Int = 100,
    var height: Int = 100,
    var x: Float = 100f,
    var y: Float = 100f,
    var icon: String = ""
)