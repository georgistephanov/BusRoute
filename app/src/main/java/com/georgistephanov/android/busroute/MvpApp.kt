package com.georgistephanov.android.busroute

import android.app.Application
import com.georgistephanov.android.busroute.di.component.ApplicationComponent
import com.georgistephanov.android.busroute.di.component.DaggerApplicationComponent
import com.georgistephanov.android.busroute.di.module.ApplicationModule
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

class MvpApp : Application() {

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()

        applicationComponent.inject(this)


        // Runs a dummy query just to initialise the database if it hasn't been yet created
        async {
            applicationComponent.getDataManager().initialiseDatabase()
        }
    }

    fun getComponent(): ApplicationComponent {
        return applicationComponent
    }
}