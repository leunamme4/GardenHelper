package com.example.gardenhelper

import android.app.Application
import com.example.gardenhelper.di.dataModule
import com.example.gardenhelper.di.domainModule
import com.example.gardenhelper.di.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, viewModel)
        }
    }
}