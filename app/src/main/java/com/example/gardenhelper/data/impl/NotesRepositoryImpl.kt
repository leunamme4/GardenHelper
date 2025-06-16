package com.example.gardenhelper.data.impl

import com.example.gardenhelper.data.db.AppDatabase
import com.example.gardenhelper.data.db.EntityConverter
import com.example.gardenhelper.domain.api.repositories.NotesRepository
import com.example.gardenhelper.domain.models.notes.Note
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotesRepositoryImpl(
    private val database: AppDatabase,
    private val converter: EntityConverter
) : NotesRepository {
    override suspend fun getNotes(ids: List<Int>): List<Note> {
        val notes =
            database.notesDao().getNotes()?.map { converter.noteToDomain(it) } ?: emptyList()
        val result: MutableList<Note> = mutableListOf()

        ids.forEach { id ->
            notes.forEach { note ->
                if (note.id == id) result.add(note)
            }
        }

        return result
    }

    override suspend fun getNoteById(id: Int): Note {
        return converter.noteToDomain(database.notesDao().getNoteById(id))
    }

    override suspend fun addNote(note: Note) {
        database.notesDao().addNote(converter.noteToEntity(note))
    }

    override suspend fun addNoteToCalendar(note: Note, date: String) {
        database.runInTransaction {
            GlobalScope.launch {
                database.notesDao().addNote(converter.noteToEntity(note))
                val day = database.calendarDao().getCalendarDay(date)
                val dayDom = converter.toDomain(day!!)
                dayDom.notes.add(note.id)
                database.calendarDao().addCalendarDay(converter.toEntity(dayDom))
            }
        }
    }

    override suspend fun deleteNoteById(noteId: Int) {
        database.notesDao().deleteNote(noteId)
    }
}