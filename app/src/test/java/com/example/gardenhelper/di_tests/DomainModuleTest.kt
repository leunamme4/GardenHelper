package com.example.gardenhelper.di_tests

import android.app.Application
import android.content.Context
import com.example.gardenhelper.di.dataModule
import com.example.gardenhelper.di.domainModule
import com.example.gardenhelper.domain.api.interactors.CalendarInteractor
import com.example.gardenhelper.domain.api.interactors.NotesInteractor
import com.example.gardenhelper.domain.api.interactors.NotificationsInteractor
import com.example.gardenhelper.domain.api.interactors.ObjectsInteractor
import com.example.gardenhelper.domain.api.repositories.CalendarRepository
import com.example.gardenhelper.domain.api.repositories.NotesRepository
import com.example.gardenhelper.domain.api.repositories.NotificationsRepository
import com.example.gardenhelper.domain.api.repositories.ObjectsRepository
import org.junit.runner.manipulation.Ordering
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.koinApplication
import org.koin.test.check.checkModules
import org.koin.test.mock.declareMock
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.component.get
import org.mockito.Mockito.mock

class DomainModuleTest : BaseKoinTest() {
    private val appContext = mock(Context::class.java)

    @Test
    fun `should verify domain module dependencies`() {
        koinApplication {
            startKoin {
                modules(dataModule, domainModule)
                androidContext(appContext)
            }
            checkModules {
                declareMock<CalendarRepository>()
                declareMock<NotesRepository>()
                declareMock<ObjectsRepository>()
                declareMock<NotificationsRepository>()
            }
        }
    }

    @Test
    fun `should provide all interactors`() {
        startKoin {
            modules(dataModule, domainModule)
            androidContext(appContext)
        }

        assertNotNull(get<CalendarInteractor>())
        assertNotNull(get<NotesInteractor>())
        assertNotNull(get<ObjectsInteractor>())
        assertNotNull(get<NotificationsInteractor>())
    }
}