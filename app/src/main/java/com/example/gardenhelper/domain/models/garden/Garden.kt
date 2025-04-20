package com.example.gardenhelper.domain.models.garden

import kotlin.random.Random

class Garden(val id: Int = Random.nextInt(1000000), var name: String, var objects: MutableList<Int>) {
}