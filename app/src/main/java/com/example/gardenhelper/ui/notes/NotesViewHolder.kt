package com.example.gardenhelper.ui.notes

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.gardenhelper.R
import com.example.gardenhelper.domain.models.notes.Note
import me.relex.circleindicator.CircleIndicator3

class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val noteHeader: TextView = itemView.findViewById(R.id.note_header)
    private val noteText: TextView = itemView.findViewById(R.id.note_text)
    private val indicator: CircleIndicator3 = itemView.findViewById(R.id.indicator)

    fun bind(note: Note) {
        noteHeader.text = note.name
        noteText.text = note.text

        val adapter = ImagePagerAdapter(mutableListOf())
        val viewPager: ViewPager2 = itemView.findViewById(R.id.viewPager)
        for (path in note.images) {
            adapter.images.add(path)
        }
        viewPager.adapter = adapter
        indicator.setViewPager(viewPager)
    }
}