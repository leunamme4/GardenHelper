package com.example.gardenhelper.interactors_tests

import com.example.gardenhelper.domain.api.repositories.ObjectsRepository
import com.example.gardenhelper.domain.impl.ObjectsInteractorImpl
import com.example.gardenhelper.domain.models.garden.GardenObject
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import kotlin.random.Random

class ObjectsIntaractorImplTest {
    private lateinit var interactor: ObjectsInteractorImpl
    private lateinit var mockRepository: ObjectsRepository

    @Before
    fun setUp() {
        mockRepository = mock(ObjectsRepository::class.java)
        interactor = ObjectsInteractorImpl(mockRepository)
    }

    @Test
    fun `addObject should call repository addObject`() = runTest {
        // Arrange
        val testObject = GardenObject(
            id = 1,
            name = "Test Object",
            type = "Test Type",
            description = "Test Description",
            icon = "test_icon"
        )

        // Act
        interactor.addObject(testObject)

        // Assert
        verify(mockRepository).addObject(testObject)
    }

    @Test
    fun `deleteObject should call repository deleteObject with correct id`() = runTest {
        // Arrange
        val testId = 123

        // Act
        interactor.deleteObject(testId)

        // Assert
        verify(mockRepository).deleteObject(testId)
    }

    @Test
    fun `getObjectById should return object from repository`() = runTest {
        // Arrange
        val testId = 456
        val expectedObject = GardenObject(
            id = testId,
            name = "Expected Object"
        )
        `when`(mockRepository.getObjectById(testId)).thenReturn(expectedObject)

        // Act
        val result = interactor.getObjectById(testId)

        // Assert
        assertEquals(expectedObject, result)
        verify(mockRepository).getObjectById(testId)
    }

    @Test
    fun `getObjects should return list from repository`() = runTest {
        // Arrange
        val expectedList = listOf(
            GardenObject(id = 1, name = "Object 1"),
            GardenObject(id = 2, name = "Object 2"),
            GardenObject(id = 3, name = "Object 3")
        )
        `when`(mockRepository.getObjects()).thenReturn(expectedList)

        // Act
        val result = interactor.getObjects()

        // Assert
        assertEquals(expectedList, result)
        verify(mockRepository).getObjects()
    }

    @Test
    fun `getObjects should return empty list when repository returns null`() = runTest {
        // Arrange
        `when`(mockRepository.getObjects()).thenReturn(null)

        // Act
        val result = interactor.getObjects()

        // Assert
        assertEquals(null, result)
        verify(mockRepository).getObjects()
    }

    // Helper function for coroutine tests
    private fun runTest(block: suspend () -> Unit) = kotlinx.coroutines.test.runTest { block() }
}