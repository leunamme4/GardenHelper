package com.example.gardenhelper.ui_tests.recycler_tests

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.bumptech.glide.RequestManager
import com.example.gardenhelper.domain.models.garden.GardenObject
import com.example.gardenhelper.domain.models.notes.Note
import com.example.gardenhelper.domain.models.notes.Notification
import com.example.gardenhelper.ui.notes.NotesAdapter
import com.example.gardenhelper.ui.objects.ObjectsAdapter
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class AdaptersTest {

    private val testNotes = listOf(
        Note(id = 1, name = "Note 1", text = "Content 1"),
        Note(id = 2, name = "Note 2", text = "Content 2")
    )

    private val testObjects = listOf(
        GardenObject(1, "Plant 1", "Type 1", "icon1.jpg"),
        GardenObject(2, "Plant 2", "Type 2", "icon2.jpg")
    )

    private val testNotifications = listOf(
        Notification(1, "Title 1", "Message 1"),
        Notification(2, "Title 2", "Message 2")
    )

    @Mock
    lateinit var mockTitleView: TextView
    @Mock
    lateinit var mockMessageView: TextView
    @Mock
    lateinit var mockActiveView: TextView
    @Mock
    lateinit var mockDeleteBtn: ImageButton
    @Mock
    lateinit var mockEditBtn: ImageButton

    @Mock
    private lateinit var mockView: View

    @Mock
    private lateinit var mockDeleteButton: ImageButton

    @Mock
    private lateinit var mockEditButton: ImageButton

    @Test
    fun `getItemCountNotes returns correct size`() {
        val adapter = NotesAdapter(testNotes)
        assert(adapter.itemCount == testNotes.size)
    }

    @Test
    fun `getItemCountObjects returns correct size`() {
        val adapter = ObjectsAdapter(testObjects) {}
        assertEquals(testObjects.size, adapter.itemCount)
    }
}

private fun mockContextWithGlide(glide: RequestManager): android.content.Context {
    val mockContext = mock<android.content.Context>()
    // Здесь должна быть логика привязки Glide к контексту
    return mockContext
}
