package com.example.gardenhelper.ui.objects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gardenhelper.R
import com.example.gardenhelper.domain.models.garden.GardenObject

class ObjectsAdapter(
    private val objects: List<GardenObject>,
    private val onItemClick: (GardenObject) -> Unit,
    private val onLongClick: (GardenObject) -> Unit = {}
) : RecyclerView.Adapter<ObjectsAdapter.GardenObjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GardenObjectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_object, parent, false)
        return GardenObjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: GardenObjectViewHolder, position: Int) {
        holder.bind(objects[position])
    }

    override fun getItemCount(): Int = objects.size

    inner class GardenObjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconImageView: ImageView = itemView.findViewById(R.id.object_icon)
        private val nameTextView: TextView = itemView.findViewById(R.id.object_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.object_info)

        fun bind(gardenObject: GardenObject) {
            Glide.with(itemView.context)
                .load(gardenObject.icon)
                .placeholder(R.drawable.icon_plant) // добавьте свой placeholder
                .into(iconImageView)

            nameTextView.text = gardenObject.name
            descriptionTextView.text =
                "Тип: ${gardenObject.type},\nзаметки: ${gardenObject.notes.size}, напоминания: ${gardenObject.notifications.size}"

            itemView.setOnClickListener { onItemClick(gardenObject) }
            itemView.setOnLongClickListener {
                onLongClick(gardenObject)
                true
            }
        }
    }
}