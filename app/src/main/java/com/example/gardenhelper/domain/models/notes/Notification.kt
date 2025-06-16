package com.example.gardenhelper.domain.models.notes

import kotlin.random.Random

class Notification(
    var id: Int = Random.nextInt(1000000),
    var title: String = "",
    var message: String = "",
    var time: String = "",
    var isActive: Boolean = false
) {
}