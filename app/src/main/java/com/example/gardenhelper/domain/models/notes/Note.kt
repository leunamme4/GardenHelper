package com.example.gardenhelper.domain.models.notes

import kotlin.random.Random

class Note(var id: Int = Random.nextInt(1000000), var name: String, var text: String, var images: MutableList<String>) {
}