package com.example.gardenhelper

import com.example.gardenhelper.data.db.AppDatabase
import com.example.gardenhelper.data.db.EntityConverter
import com.example.gardenhelper.data.db.dao.CalendarDao
import com.example.gardenhelper.data.db.dao.NotesDao
import com.example.gardenhelper.data.db.entity.NoteEntity
import com.example.gardenhelper.data.impl.NotesRepositoryImpl
import com.example.gardenhelper.domain.models.notes.Note
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class NotesRepositoryImplTest {

    @Mock
    private lateinit var database: AppDatabase
    @Mock private lateinit var notesDao: NotesDao
    @Mock private lateinit var calendarDao: CalendarDao
    @Mock private lateinit var converter: EntityConverter

    private lateinit var repository: NotesRepositoryImpl

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(dispatcher)

        whenever(database.notesDao()).thenReturn(notesDao)
        whenever(database.calendarDao()).thenReturn(calendarDao)

        repository = NotesRepositoryImpl(database, converter)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getNotes should return only matching notes`() = runTest(dispatcher) {
        val noteEntity1 = NoteEntity(1, "Note1", "Text1", "")
        val noteEntity2 = NoteEntity(2, "Note2", "Text2", "")
        val domainNote1 = Note(1, "Note1", "Text1", mutableListOf())
        val domainNote2 = Note(2, "Note2", "Text2", mutableListOf())

        `when`(notesDao.getNotes()).thenReturn(listOf(noteEntity1, noteEntity2))
        `when`(converter.noteToDomain(noteEntity1)).thenReturn(domainNote1)
        `when`(converter.noteToDomain(noteEntity2)).thenReturn(domainNote2)

        val result = repository.getNotes(listOf(1))

        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
    }

    @Test
    fun `addNote should convert and insert`() = runTest(dispatcher) {
        val note = Note(5, "Title", "Body", mutableListOf())
        val entity = NoteEntity(5, "Title", "Body", "")

        `when`(converter.noteToEntity(note)).thenReturn(entity)

        repository.addNote(note)

        verify(notesDao).addNote(entity)
    }

    @Test
    fun `deleteNoteById should call DAO`() = runTest(dispatcher) {
        repository.deleteNoteById(42)

        verify(notesDao).deleteNote(42)
    }
}

