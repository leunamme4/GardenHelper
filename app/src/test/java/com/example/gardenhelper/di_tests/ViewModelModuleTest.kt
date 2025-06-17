package com.example.gardenhelper.di_tests

import android.content.Context
import com.example.gardenhelper.di.dataModule
import com.example.gardenhelper.di.domainModule
import com.example.gardenhelper.di.viewModel
import com.example.gardenhelper.domain.api.interactors.CalendarInteractor
import com.example.gardenhelper.domain.api.interactors.NotesInteractor
import com.example.gardenhelper.domain.api.interactors.NotificationsInteractor
import com.example.gardenhelper.domain.api.interactors.ObjectsInteractor
import com.example.gardenhelper.domain.api.repositories.CalendarRepository
import com.example.gardenhelper.domain.api.repositories.NotesRepository
import com.example.gardenhelper.domain.api.repositories.NotificationsRepository
import com.example.gardenhelper.domain.api.repositories.ObjectsRepository
import com.example.gardenhelper.ui.calendar.CalendarViewModel
import com.example.gardenhelper.ui.calendar.calendar_day.CalendarDayViewModel
import com.example.gardenhelper.ui.garden.GardenListViewModel
import com.example.gardenhelper.ui.garden.garden_scheme.GardenSchemeViewModel
import com.example.gardenhelper.ui.notes.create_note.CreateNoteViewModel
import com.example.gardenhelper.ui.notes.edit_note.EditNoteViewModel
import com.example.gardenhelper.ui.notifications.create_notification.CreateNotificationViewModel
import com.example.gardenhelper.ui.notifications.edit_notification.EditNotificationViewModel
import com.example.gardenhelper.ui.objects.ObjectsViewModel
import com.example.gardenhelper.ui.objects.create_object.CreateObjectViewModel
import com.example.gardenhelper.ui.objects.edit_object.EditObjectViewModel
import com.example.gardenhelper.ui.objects.`object`.ObjectViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.koinApplication
import org.koin.test.check.checkModules
import org.koin.test.mock.declareMock
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.core.component.get
import org.mockito.Mockito.mock

class ViewModelModuleTest : BaseKoinTest() {

    private val appContext = mock(Context::class.java)

    @Test
    fun `should verify viewModel module dependencies`() {
        koinApplication {
            startKoin {
                modules(dataModule, domainModule, viewModel)
                androidContext(appContext)
            }
            checkModules {
                declareMock<CalendarInteractor>()
                declareMock<NotesInteractor>()
                declareMock<ObjectsInteractor>()
                declareMock<NotificationsInteractor>()
            }
        }
    }

    @Test
    fun `should provide all viewModels`() {
        startKoin {
            modules(dataModule, domainModule, viewModel)
            androidContext(appContext)
        }

        assertNotNull(get<CalendarViewModel>())
        assertNotNull(get<CalendarDayViewModel>())
        assertNotNull(get<ObjectsViewModel>())
        assertNotNull(get<CreateNoteViewModel>())
        assertNotNull(get<CreateObjectViewModel>())
        assertNotNull(get<CreateNotificationViewModel>())
        assertNotNull(get<ObjectViewModel>())
        assertNotNull(get<EditNotificationViewModel>())
        assertNotNull(get<EditNoteViewModel>())
        assertNotNull(get<EditObjectViewModel>())
        assertNotNull(get<GardenSchemeViewModel>())
    }
}