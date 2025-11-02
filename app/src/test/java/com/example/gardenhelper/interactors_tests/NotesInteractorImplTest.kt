package com.example.gardenhelper.interactors_tests

import com.example.gardenhelper.domain.api.repositories.NotesRepository
import com.example.gardenhelper.domain.impl.NotesInteractorImpl
import com.example.gardenhelper.domain.models.notes.Note
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import kotlin.random.Random

class NotesInteractorImplTest {
    private lateinit var interactor: NotesInteractorImpl
    private lateinit var mockRepository: NotesRepository

    @Before
    fun setUp() {
        mockRepository = mock(NotesRepository::class.java)
        interactor = NotesInteractorImpl(mockRepository)
    }

    @Test
    fun `getNotes should return notes from repository`() = runTest {
        // Arrange
        val testIds = listOf(1, 2, 3)
        val expectedNotes = listOf(
            Note(id = 1, name = "Note 1"),
            Note(id = 2, name = "Note 2")
        )
        `when`(mockRepository.getNotes(testIds)).thenReturn(expectedNotes)

        // Act
        val result = interactor.getNotes(testIds)

        // Assert
        assertEquals(expectedNotes, result)
        verify(mockRepository).getNotes(testIds)
    }

    @Test
    fun `getNoteById should return note from repository`() = runTest {
        // Arrange
        val testId = 123
        val expectedNote = Note(id = testId, name = "Test Note")
        `when`(mockRepository.getNoteById(testId)).thenReturn(expectedNote)

        // Act
        val result = interactor.getNoteById(testId)

        // Assert
        assertEquals(expectedNote, result)
        verify(mockRepository).getNoteById(testId)
    }

    @Test
    fun `addNote should call repository addNote`() = runTest {
        // Arrange
        val testNote = Note(
            id = Random.nextInt(1000000),
            name = "New Note",
            text = "Note content"
        )

        // Act
        interactor.addNote(testNote)

        // Assert
        verify(mockRepository).addNote(testNote)
    }

    @Test
    fun `addNoteToCalendar should call repository with correct parameters`() = runTest {
        // Arrange
        val testNote = Note(id = 1, name = "Calendar Note")
        val testDate = "2023-12-31"

        // Act
        interactor.addNoteToCalendar(testNote, testDate)

        // Assert
        verify(mockRepository).addNoteToCalendar(testNote, testDate)
    }

    @Test
    fun `deleteNoteById should call repository with correct id`() = runTest {
        // Arrange
        val testId = 456

        // Act
        interactor.deleteNoteById(testId)

        // Assert
        verify(mockRepository).deleteNoteById(testId)
    }
}

// Helper function for coroutine tests
fun runTest(block: suspend () -> Unit) = kotlinx.coroutines.test.runTest { block() }