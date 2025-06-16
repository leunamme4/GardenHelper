package com.example.gardenhelper.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey
    var id: Int,
    var name: String,
    var text: String,
    var images: String
) {
    constructor() : this(0, "", "", "")
}
