package com.example.gardenhelper.di

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
import com.example.gardenhelper.ui.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel = module {
    viewModel {
        CalendarViewModel(get())
    }

    viewModel {
        CalendarDayViewModel(get(), get())
    }

    viewModel {
        ObjectsViewModel(get())
    }

    viewModel {
        CreateNoteViewModel(get(), get(), get())
    }

    viewModel {
        CreateObjectViewModel(get())
    }

    viewModel {
        CreateNotificationViewModel(get(), get())
    }

    viewModel {
        ObjectViewModel(get(), get(), get())
    }

    viewModel {
        EditNotificationViewModel(get())
    }

    viewModel {
        EditNoteViewModel(get())
    }

    viewModel {
        EditObjectViewModel(get())
    }

    viewModel {
        GardenSchemeViewModel(get(), get())
    }

    viewModel {
        SettingsViewModel(get())
    }

    viewModel {
        GardenListViewModel(get())
    }
}