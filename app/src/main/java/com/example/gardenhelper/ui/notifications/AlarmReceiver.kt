package com.example.gardenhelper.ui.notifications

import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.gardenhelper.R
import com.example.gardenhelper.ui.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    private val CHANNEL_ID = "channel_id"

    override fun onReceive(context: Context, intent: Intent) {
        // Получаем параметры из Intent
        val title = intent.getStringExtra("title") ?: "Напоминание"
        val text = intent.getStringExtra("text") ?: "Время пришло!"
        val notificationId = intent.getIntExtra("notificationId", 123)

        sendNotification(context, title, text, notificationId)
    }

    private fun sendNotification(
        context: Context,
        title: String,
        text: String,
        notificationId: Int
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_plant)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle())

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@with
            }
            notify(notificationId, builder.build())
        }
    }
}