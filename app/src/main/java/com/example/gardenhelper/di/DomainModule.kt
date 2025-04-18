package com.example.gardenhelper.di

import com.example.gardenhelper.domain.api.CalendarInteractor
import com.example.gardenhelper.domain.impl.CalendarInteractorImpl
import org.koin.dsl.module

val domainModule = module {
    single<CalendarInteractor> {
        CalendarInteractorImpl(get())
    }
}