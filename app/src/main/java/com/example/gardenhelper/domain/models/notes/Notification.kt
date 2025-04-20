package com.example.gardenhelper.domain.models.notes

import kotlin.random.Random

class Notification(
    val id: Int = Random.nextInt(1000000),
    var title: String,
    var message: String,
    val time: String
) {
}