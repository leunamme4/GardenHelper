package com.example.gardenhelper

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.gardenhelper.data.work_manager.WorkManagerScheduler
import com.example.gardenhelper.di.dataModule
import com.example.gardenhelper.di.domainModule
import com.example.gardenhelper.di.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.android.ext.android.get
import org.koin.java.KoinJavaComponent.getKoin

class App : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, viewModel)
        }

        Handler(Looper.getMainLooper()).post {
            getKoin().get<WorkManagerScheduler>().scheduleDailyNetworkRequest()
        }
    }

    override val workManagerConfiguration: Configuration
        get() = getKoin().get()
}