package com.example.gardenhelper.domain.api.interactors

import com.example.gardenhelper.domain.models.notes.Note

interface NotesInteractor {
    suspend fun getNotes(ids: List<Int>): List<Note>

    suspend fun getNoteById(id: Int): Note

    suspend fun addNote(note: Note)

    suspend fun addNoteToCalendar(note: Note, date: String)

    suspend fun deleteNoteById(noteId: Int)
}