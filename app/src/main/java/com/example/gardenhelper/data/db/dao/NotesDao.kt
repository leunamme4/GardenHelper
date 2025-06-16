package com.example.gardenhelper.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gardenhelper.data.db.entity.NoteEntity

@Dao
interface NotesDao {

    @Insert(entity = NoteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id == :id")
    suspend fun deleteNote(id: Int)

    @Query("SELECT * FROM notes WHERE id == :id")
    suspend fun getNoteById(id: Int): NoteEntity

    @Query("SELECT * FROM notes")
    suspend fun getNotes(): List<NoteEntity>?

    @Query("DELETE FROM notes")
    suspend fun clearNotes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(notes: List<NoteEntity>)
}