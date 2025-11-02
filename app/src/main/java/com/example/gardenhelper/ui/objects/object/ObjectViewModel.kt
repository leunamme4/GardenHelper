package com.example.gardenhelper.ui.objects.`object`

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenhelper.domain.api.interactors.NotesInteractor
import com.example.gardenhelper.domain.api.interactors.NotificationsInteractor
import com.example.gardenhelper.domain.api.interactors.ObjectsInteractor
import com.example.gardenhelper.domain.models.garden.GardenObject
import com.example.gardenhelper.domain.models.notes.Note
import com.example.gardenhelper.domain.models.notes.Notification
import com.example.gardenhelper.ui.notifications.AlarmReceiver
import kotlinx.coroutines.launch

class ObjectViewModel(
    private val objectsInteractor: ObjectsInteractor,
    private val notesInteractor: NotesInteractor,
    private val notificationsInteractor: NotificationsInteractor
) : ViewModel() {

    private val _gardenObject: MutableLiveData<GardenObject> = MutableLiveData()
    val gardenObject: LiveData<GardenObject> = _gardenObject

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    private val _notifications = MutableLiveData<List<Notification>>()
    val notifications: LiveData<List<Notification>> = _notifications

    fun getObject(objectId: Int) {
        viewModelScope.launch {
            val obj = objectsInteractor.getObjectById(objectId)
            _gardenObject.postValue(obj)

            getNotes(obj.notes)
            getNotifications(obj.notifications)
        }
    }

    private suspend fun getNotes(notesIds: List<Int>) {
        val notes = notesInteractor.getNotes(notesIds)
        _notes.postValue(notes)
    }

    private suspend fun getNotifications(notificationsIds: List<Int>) {
        val notifications = notificationsInteractor.getNotifications(notificationsIds)
        _notifications.postValue(notifications)
    }

    fun deleteNoteById(noteId: Int) {
        viewModelScope.launch {
            notesInteractor.deleteNoteById(noteId)
            val objectId = _gardenObject.value!!.id
            val gardenObject = objectsInteractor.getObjectById(objectId)
            gardenObject.notes.remove(noteId)
            objectsInteractor.addObject(gardenObject)
            getObject(objectId)
        }
    }

    fun deleteNotificationById(notificationId: Int) {
        viewModelScope.launch {
            notificationsInteractor.deleteNotification(notificationId)
            val objectId = _gardenObject.value!!.id
            val gardenObject = objectsInteractor.getObjectById(objectId)
            gardenObject.notifications.remove(notificationId)
            objectsInteractor.addObject(gardenObject)
            getObject(objectId)
        }
    }

    fun cancelAlarm(context: Context, requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode, // Тот же requestCode, что и при создании
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel() // Дополнительная отмена
    }
}