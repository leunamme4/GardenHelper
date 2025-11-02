package com.example.gardenhelper.di

import com.example.gardenhelper.domain.api.interactors.CalendarInteractor
import com.example.gardenhelper.domain.api.interactors.GardenInteractor
import com.example.gardenhelper.domain.api.interactors.NotesInteractor
import com.example.gardenhelper.domain.api.interactors.NotificationsInteractor
import com.example.gardenhelper.domain.api.interactors.ObjectsInteractor
import com.example.gardenhelper.domain.impl.CalendarInteractorImpl
import com.example.gardenhelper.domain.impl.GardenInteractorImpl
import com.example.gardenhelper.domain.impl.NotesInteractorImpl
import com.example.gardenhelper.domain.impl.NotificationsInteractorImpl
import com.example.gardenhelper.domain.impl.ObjectsInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    single<CalendarInteractor> {
        CalendarInteractorImpl(get())
    }

    single<NotesInteractor> {
        NotesInteractorImpl(get())
    }

    single<ObjectsInteractor> {
        ObjectsInteractorImpl(get())
    }

    single<NotificationsInteractor> {
        NotificationsInteractorImpl(get())
    }

    single<GardenInteractor> {
        GardenInteractorImpl(get())
    }
}