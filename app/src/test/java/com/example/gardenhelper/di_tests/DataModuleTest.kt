package com.example.gardenhelper.di_tests

import androidx.work.WorkManager
import com.example.gardenhelper.data.api.NetworkClient
import com.example.gardenhelper.data.api.WeatherApi
import com.example.gardenhelper.data.db.AppDatabase
import com.example.gardenhelper.data.network.RetrofitNetworkClient
import com.example.gardenhelper.di.dataModule
import com.example.gardenhelper.domain.api.repositories.CalendarRepository
import com.example.gardenhelper.domain.api.repositories.NotesRepository
import com.example.gardenhelper.domain.api.repositories.NotificationsRepository
import com.example.gardenhelper.domain.api.repositories.ObjectsRepository
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.manipulation.Ordering
import org.koin.dsl.koinApplication
import org.koin.test.check.checkModules
import kotlin.test.DefaultAsserter.assertNotNull
import org.koin.core.context.GlobalContext.startKoin
import org.koin.test.mock.declareMock
import org.mockito.Mockito.mock
import kotlin.test.assertNotNull
import org.koin.core.component.get as get

class DataModuleTest : BaseKoinTest() {
    @Test
    fun `should verify data module dependencies`() {
        koinApplication {
            startKoin { modules(dataModule) }
            checkModules {
                withInstance<Ordering.Context>(mock(Ordering.Context::class.java))
                declareMock<AppDatabase>()
                declareMock<WorkManager>()
                declareMock<WeatherApi>()
            }
        }
    }

    @Test
    fun `should provide Retrofit NetworkClient`() {
        startKoin { modules(dataModule) }
        val client: NetworkClient = get()
        assertTrue(client is RetrofitNetworkClient)
    }
}