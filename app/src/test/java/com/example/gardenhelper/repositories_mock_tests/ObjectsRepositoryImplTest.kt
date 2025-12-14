package com.example.gardenhelper.repositories_mock_tests

import com.example.gardenhelper.data.db.AppDatabase
import com.example.gardenhelper.data.db.EntityConverter
import com.example.gardenhelper.data.db.dao.GardenObjectDao
import com.example.gardenhelper.data.db.entity.GardenObjectEntity
import com.example.gardenhelper.data.impl.ObjectsRepositoryImpl
import com.example.gardenhelper.domain.models.garden.GardenObject
import com.google.gson.Gson
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ObjectsRepositoryImplTest {

    @Mock
    private lateinit var database: AppDatabase

    @Mock
    private lateinit var gardenObjectDao: GardenObjectDao

    @Mock
    private lateinit var converter: EntityConverter

    private lateinit var repository: ObjectsRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        whenever(database.gardenObjectDao()).thenReturn(gardenObjectDao)
        repository = ObjectsRepositoryImpl(database, converter)
    }

    @Test
    fun `addObject should convert and insert entity`() = runTest {
        // Arrange
        val gardenObject = GardenObject(
            id = 1,
            name = "Rose",
            type = "Flower",
            description = "Red rose",
            notes = mutableListOf(1, 2),
            notifications = mutableListOf(3)
        )

        val entity = GardenObjectEntity(
            id = 1,
            name = "Rose",
            type = "Flower",
            description = "Red rose",
            icon = "",
            notes = Gson().toJson(listOf(1, 2)),
            notifications = Gson().toJson(listOf(3))
        )

        whenever(converter.gardenObjectToEntity(gardenObject)).thenReturn(entity)

        // Act
        repository.addObject(gardenObject)

        // Assert
        verify(converter).gardenObjectToEntity(gardenObject)
        verify(gardenObjectDao).addObject(entity)
    }


    @Test
    fun `getObjectById should return converted domain object`() = runTest {
        // Arrange
        val entity = GardenObjectEntity(
            id = 1,
            name = "Apple Tree",
            type = "Tree",
            description = "Green apples",
            icon = "tree_icon",
            notes = "[1,2,3]",
            notifications = "[4]"
        )

        val domain = GardenObject(
            id = 1,
            name = "Apple Tree",
            type = "Tree",
            description = "Green apples",
            icon = "tree_icon",
            notes = mutableListOf(1, 2, 3),
            notifications = mutableListOf(4)
        )

        whenever(gardenObjectDao.getObjectById(1)).thenReturn(entity)
        whenever(converter.gardenObjectToDomain(entity)).thenReturn(domain)

        // Act
        val result = repository.getObjectById(1)

        // Assert
        assertEquals(domain, result)
        verify(gardenObjectDao).getObjectById(1)
        verify(converter).gardenObjectToDomain(entity)
    }

    @Test
    fun `getObjects should return empty list when DAO returns null`() = runTest {
        // Arrange
        whenever(gardenObjectDao.getObjects()).thenReturn(null)

        // Act
        val result = repository.getObjects()

        // Assert
        assertTrue(result.isEmpty())
        verify(gardenObjectDao).getObjects()
    }

    @Test
    fun `getObjects should return converted list`() = runTest {
        // Arrange
        val entities = listOf(
            GardenObjectEntity(
                id = 1,
                name = "Rose",
                type = "Flower",
                description = "Red",
                icon = "",
                notes = "[1]",
                notifications = "[]"
            ),
            GardenObjectEntity(
                id = 2,
                name = "Oak",
                type = "Tree",
                description = "Big tree",
                icon = "",
                notes = "[]",
                notifications = "[1,2]"
            )
        )

        val domains = listOf(
            GardenObject(
                id = 1,
                name = "Rose",
                type = "Flower",
                description = "Red",
                notes = mutableListOf(1),
                notifications = mutableListOf()
            ),
            GardenObject(
                id = 2,
                name = "Oak",
                type = "Tree",
                description = "Big tree",
                notes = mutableListOf(),
                notifications = mutableListOf(1, 2)
            )
        )

        whenever(gardenObjectDao.getObjects()).thenReturn(entities)
        whenever(converter.gardenObjectToDomain(entities[0])).thenReturn(domains[0])
        whenever(converter.gardenObjectToDomain(entities[1])).thenReturn(domains[1])

        // Act
        val result = repository.getObjects()

        // Assert
        assertEquals(2, result.size)
        assertEquals("Rose", result[0].name)
        assertEquals("Oak", result[1].name)
        verify(gardenObjectDao).getObjects()
        verify(converter, times(2)).gardenObjectToDomain(any())
    }

    @Test
    fun `getObjects should preserve order`() = runTest {
        // Arrange
        val entity = GardenObjectEntity(
            id = 1,
            name = "Test",
            type = "Test",
            description = "Test",
            icon = "",
            notes = "[]",
            notifications = "[]"
        )
        val domain = GardenObject(
            id = 1,
            name = "Test",
            type = "Test",
            description = "Test"
        )

        whenever(gardenObjectDao.getObjects()).thenReturn(listOf(entity, entity, entity))
        whenever(converter.gardenObjectToDomain(any())).thenReturn(domain)

        // Act
        val result = repository.getObjects()

        // Assert
        assertEquals(3, result.size)
    }
}