package com.example.gardenhelper.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gardenhelper.db.entity.NoteEntity

@Dao
interface NotesDao {

    @Insert(entity = NoteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: NoteEntity)

    @Query("DELETE FROM notes WHERE id == :id")
    suspend fun deleteNote(id: Int)

    @Query("SELECT * FROM notes WHERE id == :id")
    suspend fun getNoteById(id: Int): NoteEntity

    @Query("SELECT * FROM notes")
    suspend fun getNotes(): List<NoteEntity>
}