package com.example.gardenhelper.ui_tests.view_model_tests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gardenhelper.domain.api.interactors.CalendarInteractor
import com.example.gardenhelper.domain.api.interactors.NotesInteractor
import com.example.gardenhelper.domain.models.calendar.CalendarDay
import com.example.gardenhelper.domain.models.notes.Note
import com.example.gardenhelper.domain.models.weather.Location
import com.example.gardenhelper.ui.calendar.calendar_day.CalendarDayViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CalendarDayViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var calendarInteractor: CalendarInteractor

    @Mock
    private lateinit var notesInteractor: NotesInteractor

    private lateinit var viewModel: CalendarDayViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CalendarDayViewModel(calendarInteractor, notesInteractor)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }


    @Test
    fun `getNotes should not update notes when empty`() = testDispatcher.runBlockingTest {
        // Arrange
        val notesIds = listOf(1, 2)
        val emptyNotes = emptyList<Note>()

        whenever(notesInteractor.getNotes(notesIds)).thenReturn(emptyNotes)

        // Act
        viewModel.getNotes(notesIds)

        // Assert
        assert(viewModel.notes.value == null)
    }


    @Test
    fun `formatDateToRussian should format date correctly`() {
        // Arrange
        val dateString = "2023-01-01"
        val expected = "1 января 2023" // Ожидаемый результат для русской локали

        // Act
        val result = viewModel.formatDateToRussian(dateString)

        // Assert
        assert(result == expected)
    }

    @Test
    fun `formatDateToRussian should return original string on parse error`() {
        // Arrange
        val invalidDateString = "invalid-date"

        // Act
        val result = viewModel.formatDateToRussian(invalidDateString)

        // Assert
        assert(result == invalidDateString)
    }
}