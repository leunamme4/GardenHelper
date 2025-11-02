package com.example.gardenhelper.ui.objects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gardenhelper.R
import com.example.gardenhelper.domain.models.garden.Garden

class GardenListAdapter(
    private val objects: List<Garden>,
    private val onItemClick: (Int) -> Unit,
    private val onLongClick: (Int) -> Unit
) : RecyclerView.Adapter<GardenListAdapter.GardenListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GardenListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_object, parent, false)
        return GardenListViewHolder(view)
    }

    override fun onBindViewHolder(holder: GardenListViewHolder, position: Int) {
        holder.bind(objects[position])
    }

    override fun getItemCount(): Int = objects.size

    inner class GardenListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconImageView: ImageView = itemView.findViewById(R.id.object_icon)
        private val nameTextView: TextView = itemView.findViewById(R.id.object_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.object_info)

        fun bind(gardenObject: Garden) {
            Glide.with(itemView.context)
                .load(gardenObject.icon)
                .placeholder(R.drawable.icon_garden)
                .into(iconImageView)

            nameTextView.text = gardenObject.name
            descriptionTextView.text =
                "${gardenObject.objects.size} ${getObjectWordForm(gardenObject.objects.size)}"

            itemView.setOnClickListener { onItemClick(gardenObject.id) }
            itemView.setOnLongClickListener {
                onLongClick(gardenObject.id)
                true
            }
        }

        private fun getObjectWordForm(count: Int): String {
            val mod10 = count % 10
            val mod100 = count % 100
            return when {
                mod10 == 1 && mod100 != 11 -> "объект"
                mod10 in 2..4 && (mod100 !in 12..14) -> "объекта"
                else -> "объектов"
            }
        }

    }
}