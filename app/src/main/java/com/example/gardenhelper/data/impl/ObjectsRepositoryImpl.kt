package com.example.gardenhelper.data.impl

import com.example.gardenhelper.data.db.AppDatabase
import com.example.gardenhelper.data.db.EntityConverter
import com.example.gardenhelper.data.db.entity.CalendarDayEntity
import com.example.gardenhelper.data.db.entity.GardenEntity
import com.example.gardenhelper.data.db.entity.GardenObjectEntity
import com.example.gardenhelper.data.db.entity.NoteEntity
import com.example.gardenhelper.data.db.entity.NotificationEntity
import com.example.gardenhelper.domain.api.repositories.ObjectsRepository
import com.example.gardenhelper.domain.models.garden.GardenObject
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class ObjectsRepositoryImpl(
    private val database: AppDatabase,
    private val converter: EntityConverter
) : ObjectsRepository {
    override suspend fun addObject(obj: GardenObject) {
        database.gardenObjectDao().addObject(converter.gardenObjectToEntity(obj))
    }

    override suspend fun deleteObject(id: Int) {
        val gardenObject = database.gardenObjectDao().getObjectById(id)
        if (gardenObject.notes.isNotEmpty()) {
            for (note in gardenObject.notes) {
                database.notesDao().deleteNote(id)
            }
        }
        if (gardenObject.notifications.isNotEmpty()) {
            for (note in gardenObject.notifications) {
                database.notificationsDao().deleteNotification(id)
            }
        }

        database.gardenObjectDao().deleteObject(id)
    }

    override suspend fun getObjectById(id: Int): GardenObject {
        return converter.gardenObjectToDomain(database.gardenObjectDao().getObjectById(id))
    }

    override suspend fun getObjects(): List<GardenObject> {
        return database.gardenObjectDao().getObjects()?.map { converter.gardenObjectToDomain(it) }
            ?: emptyList()
    }

    override suspend fun uploadAllToFirestore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("User not logged in")

        val userDoc = Firebase.firestore.collection("users").document(userId)

        // Удалить подколлекции (асинхронно)
        suspend fun clearCollection(path: String) {
            val collectionRef = userDoc.collection(path)
            val snapshot = collectionRef.get().await()
            for (doc in snapshot.documents) {
                doc.reference.delete()
            }
        }

        // Очистка
        listOf("calendar_days", "gardens", "garden_objects", "notes", "notifications").forEach {
            clearCollection(it)
        }

        val calendarDays = database.calendarDao().getCalendarDaysEntities() ?: emptyList()
        val gardens = database.gardenDao().getGardens() ?: emptyList()
        val objects = database.gardenObjectDao().getObjects() ?: emptyList()
        val notes = database.notesDao().getNotes() ?: emptyList()
        val notifications = database.notificationsDao().getNotifications() ?: emptyList()

        calendarDays.forEach {
            userDoc.collection("calendar_days").document(it.date).set(it)
        }

        gardens.forEach {
            userDoc.collection("gardens").document(it.id.toString()).set(it)
        }

        objects.forEach {
            userDoc.collection("garden_objects").document(it.id.toString()).set(it)
        }

        notes.forEach {
            userDoc.collection("notes").document(it.id.toString()).set(it)
        }

        notifications.forEach {
            userDoc.collection("notifications").document(it.id.toString()).set(it)
        }
    }

    override suspend fun downloadAllFromFirestore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException("User not logged in")
        val userDoc = Firebase.firestore.collection("users").document(userId)

        // Очистка локальной базы
        database.calendarDao().clearCalendarDays()
        database.gardenDao().clearGardens()
        database.gardenObjectDao().clearGardenObjects()
        database.notesDao().clearNotes()
        database.notificationsDao().clearNotifications()

        // Получаем данные из Firestore
        val calendarDays = userDoc.collection("calendar_days").get().await()
            .toObjects(CalendarDayEntity::class.java)

        val gardens = userDoc.collection("gardens").get().await()
            .toObjects(GardenEntity::class.java)

        val gardenObjects = userDoc.collection("garden_objects").get().await()
            .toObjects(GardenObjectEntity::class.java)

        val notes = userDoc.collection("notes").get().await()
            .toObjects(NoteEntity::class.java)

        val notifications = userDoc.collection("notifications").get().await()
            .toObjects(NotificationEntity::class.java)

        // Сохраняем в Room
        database.calendarDao().insertCalendarDays(calendarDays)
        database.gardenDao().insertGardens(gardens)
        database.gardenObjectDao().insertObjects(gardenObjects)
        database.notesDao().insertNotes(notes)
        database.notificationsDao().insertNotifications(notifications)
    }
}