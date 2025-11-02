package com.example.gardenhelper.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gardenhelper.R
import com.example.gardenhelper.domain.models.garden.GardenObject
import com.example.gardenhelper.domain.models.notes.Notification

class NotificationsAdapter(
    private val notifications: List<Notification>,
    private val onDelete: (Int) -> Unit = {},
    private val onEdit: (Int) -> Unit = {}
) : RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        holder.bind(notifications[position])
    }

    override fun getItemCount(): Int = notifications.size

    inner class NotificationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.note_header)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.notification_text)
        private val activeTextView: TextView = itemView.findViewById(R.id.notification_active)
        private val delete: ImageButton = itemView.findViewById(R.id.delete)
        private val edit: ImageButton = itemView.findViewById(R.id.edit)

        fun bind(notification: Notification) {
            nameTextView.text = notification.title
            descriptionTextView.text = notification.message
            activeTextView.text = if (notification.isActive) "Активно" else "Не активно"

            delete.setOnClickListener { onDelete(notification.id) }
            edit.setOnClickListener { onEdit(notification.id) }
        }
    }
}