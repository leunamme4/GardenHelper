package com.example.gardenhelper.data.db

import com.example.gardenhelper.data.db.entity.CalendarDayEntity
import com.example.gardenhelper.data.db.entity.GardenEntity
import com.example.gardenhelper.data.db.entity.GardenObjectEntity
import com.example.gardenhelper.data.db.entity.NoteEntity
import com.example.gardenhelper.data.db.entity.NotificationEntity
import com.example.gardenhelper.domain.models.calendar.CalendarDay
import com.example.gardenhelper.domain.models.garden.Garden
import com.example.gardenhelper.domain.models.garden.GardenObject
import com.example.gardenhelper.domain.models.garden.ObjectSizes
import com.example.gardenhelper.domain.models.notes.Note
import com.example.gardenhelper.domain.models.notes.Notification
import com.example.gardenhelper.domain.models.weather.CurrentWeatherResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EntityConverter {
    fun toEntity(calendarDay: CalendarDay): CalendarDayEntity {
        val gson = Gson()
        val notesJson = gson.toJson(calendarDay.notes) // Конвертируем MutableList<Int> в JSON-строку
        val weatherJson = gson.toJson(calendarDay.weather)
        return CalendarDayEntity(
            date = calendarDay.date,
            weather = weatherJson,
            notes = notesJson
        )
    }

    fun toDomain(calendarDayEntity: CalendarDayEntity): CalendarDay {
        val gson = Gson()
        val typeNotes = object : TypeToken<MutableList<Int>>() {}.type
        val notesList = gson.fromJson<MutableList<Int>>(calendarDayEntity.notes, typeNotes)

        val typeWeather = object : TypeToken<CurrentWeatherResponse>() {}.type
        val weather = gson.fromJson<CurrentWeatherResponse>(calendarDayEntity.weather, typeWeather)

        return CalendarDay(
            date = calendarDayEntity.date,
            weather = weather,
            notes = notesList ?: mutableListOf() // На случай, если notes был null (возвращаем пустой список)
        )
    }

    fun noteToEntity(note: Note): NoteEntity {
        val images = Gson().toJson(note.images)
        return NoteEntity(
            note.id,
            note.name,
            note.text,
            images
        )
    }

    fun noteToDomain(note: NoteEntity): Note {

        val typeImages = object : TypeToken<List<String>>() {}.type
        val images = Gson().fromJson<List<String>>(note.images, typeImages).toMutableList()

        return Note(
            note.id,
            note.name,
            note.text,
            images
        )
    }

    fun gardenObjectToEntity(gardenObject: GardenObject): GardenObjectEntity {
        val notesJson = Gson().toJson(gardenObject.notes)
        val notificationsJson = Gson().toJson(gardenObject.notifications)

        return GardenObjectEntity(
            gardenObject.id,
            gardenObject.name,
            gardenObject.type,
            gardenObject.description,
            gardenObject.icon,
            notesJson,
            notificationsJson
        )
    }

    fun gardenObjectToDomain(gardenObjectEntity: GardenObjectEntity): GardenObject {
        val notesType = object : TypeToken<List<Int>>() {}.type
        val notesList = Gson().fromJson<List<Int>>(gardenObjectEntity.notes, notesType).toMutableList()

        val notificationsType = object : TypeToken<List<Int>>() {}.type
        val notificationsList = Gson().fromJson<List<Int>>(gardenObjectEntity.notifications, notificationsType).toMutableList()

        return GardenObject(
            gardenObjectEntity.id,
            gardenObjectEntity.name,
            gardenObjectEntity.type,
            gardenObjectEntity.description,
            gardenObjectEntity.icon,
            notesList,
            notificationsList
        )
    }

    fun notificationToEntity(notification: Notification): NotificationEntity {
        return NotificationEntity(
            notification.id,
            notification.title,
            notification.message,
            notification.time,
            notification.isActive
        )
    }

    fun notificationToDomain(notificationEntity: NotificationEntity): Notification {
        return Notification(
            notificationEntity.id,
            notificationEntity.title,
            notificationEntity.message,
            notificationEntity.time,
            notificationEntity.isActive
        )
    }

    fun gardenToEntity(garden: Garden): GardenEntity {
        val objectsJson = Gson().toJson(garden.objects)

        return GardenEntity(
            garden.id,
            garden.name,
            objectsJson,
            garden.icon,
            garden.width,
            garden.height
        )
    }

    fun gardenToDomain(gardenEntity: GardenEntity): Garden {
        val objectsType = object : TypeToken<List<ObjectSizes>>() {}.type
        val objectsList = Gson().fromJson<List<ObjectSizes>>(gardenEntity.objects, objectsType).toMutableList()

        return Garden(
            gardenEntity.id,
            gardenEntity.name,
            objectsList,
            icon = gardenEntity.icon,
            width = gardenEntity.width,
            height = gardenEntity.height
        )
    }
}