package com.example.gardenhelper.di

import com.example.gardenhelper.ui.calendar.CalendarViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModel = module {
    viewModel {
        CalendarViewModel(get())
    }
}