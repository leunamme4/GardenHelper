package com.example.gardenhelper.ui.notes

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.gardenhelper.R
import com.example.gardenhelper.domain.models.notes.Note

class NotesAdapter(val list: List<Note>, val onDelete: (Int) -> Unit = {}, val onEdit: (Int) -> Unit = {}): RecyclerView.Adapter<NotesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NotesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.findViewById<ImageButton>(R.id.delete).setOnClickListener {
            onDelete(list[position].id)
        }
        holder.itemView.findViewById<ImageButton>(R.id.edit).setOnClickListener {
            onEdit(list[position].id)
        }
    }
}