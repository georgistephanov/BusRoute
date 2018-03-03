package com.georgistephanov.android.busroute

import android.app.Application
import com.georgistephanov.android.busroute.di.component.ApplicationComponent
import com.georgistephanov.android.busroute.di.component.DaggerApplicationComponent
import com.georgistephanov.android.busroute.di.module.ApplicationModule

class MvpApp : Application() {

    private lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()

        applicationComponent.inject(this)
    }

    fun getComponent(): ApplicationComponent {
        return applicationComponent
    }
}