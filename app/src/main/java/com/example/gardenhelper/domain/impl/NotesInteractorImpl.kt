package com.example.gardenhelper.domain.impl

import com.example.gardenhelper.domain.api.interactors.NotesInteractor
import com.example.gardenhelper.domain.api.repositories.NotesRepository
import com.example.gardenhelper.domain.models.notes.Note

class NotesInteractorImpl(private val repository: NotesRepository) : NotesInteractor {
    override suspend fun getNotes(ids: List<Int>): List<Note> {
        return repository.getNotes(ids)
    }

    override suspend fun getNoteById(id: Int): Note {
        return repository.getNoteById(id)
    }

    override suspend fun addNote(note: Note) {
        return repository.addNote(note)
    }

    override suspend fun addNoteToCalendar(note: Note, date: String) {
        repository.addNoteToCalendar(note, date)
    }

    override suspend fun deleteNoteById(noteId: Int) {
        repository.deleteNoteById(noteId)
    }
}