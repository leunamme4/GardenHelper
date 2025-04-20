package com.example.gardenhelper.domain.models.garden

import kotlin.random.Random

class GardenObject(
    var id: Int = Random.nextInt(1000000),
    var name: String,
    var type: String,
    var description: String,
    val icon: String,
    var notes: MutableList<Int>,
    var notifications: MutableList<Int>
) {
}