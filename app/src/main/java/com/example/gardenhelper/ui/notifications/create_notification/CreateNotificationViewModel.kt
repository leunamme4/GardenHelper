package com.example.gardenhelper.ui.notifications.create_notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gardenhelper.domain.api.interactors.NotificationsInteractor
import com.example.gardenhelper.domain.api.interactors.ObjectsInteractor
import com.example.gardenhelper.domain.models.notes.Notification
import com.example.gardenhelper.ui.notifications.AlarmReceiver
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CreateNotificationViewModel(
    private val objectsInteractor: ObjectsInteractor,
    private val notificationsInteractor: NotificationsInteractor
) : ViewModel() {

    var notification = Notification()

    private val _isSaved: MutableLiveData<Boolean> = MutableLiveData()
    val isSaved: LiveData<Boolean> = _isSaved

    fun saveNotification(objectId: Int) {
        viewModelScope.launch {
            notificationsInteractor.addNotification(notification)
            val obj = objectsInteractor.getObjectById(objectId)
            obj.notifications.add(notification.id)
            objectsInteractor.addObject(obj)
            _isSaved.postValue(true)
        }
    }

    fun setParameters(
        context: Context,
        title: String,
        message: String,
        time: String,
        isActive: Boolean
    ) {
        notification.title = title
        notification.message = message
        notification.time = time
        notification.isActive = isActive

        val parsedTime = parseDateTime(time)

        if (isActive) {
            setAlarm(
                context,
                parsedTime[0],
                parsedTime[1],
                parsedTime[2],
                parsedTime[3],
                parsedTime[4],
                title,
                message
            )
        } else {
            cancelAlarm(context, notification.id)
        }
    }

    private fun setAlarm(
        context: Context,
        year: Int,
        month: Int, // 0-11 (как в Calendar)
        day: Int,
        hour: Int,
        minute: Int,
        notificationTitle: String,
        notificationText: String,
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            // Передаем все параметры уведомления
            putExtra("title", notificationTitle)
            putExtra("text", notificationText)
            putExtra("notificationId", notification.id)
        }

        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DATE, 1)
            }
        }
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    private fun cancelAlarm(context: Context, requestCode: Int) {
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

    private fun parseDateTime(dateTimeStr: String): List<Int> {
        try {
            // Создаем форматтер для строки "26.5.2025 12:01"
            val sdf = SimpleDateFormat("d.M.yyyy HH:mm", Locale.getDefault())
            val date: Date = sdf.parse(dateTimeStr) ?: return emptyList() // Парсим строку в Date

            // Получаем Calendar для извлечения компонентов
            val calendar = Calendar.getInstance()
            calendar.time = date

            // Извлекаем день, месяц, год, часы, минуты
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) // Месяцы в Calendar от 0 до 11
            val year = calendar.get(Calendar.YEAR)
            val hours = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes = calendar.get(Calendar.MINUTE)

            return listOf(year, month, day, hours, minutes)
        } catch (e: Exception) {
            println("Ошибка парсинга: ${e.message}")
        }
        return emptyList()
    }
}