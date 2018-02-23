package com.georgistephanov.android.busroute

import android.app.Application
import com.georgistephanov.android.busroute.data.DataManager
import com.georgistephanov.android.busroute.di.component.ApplicationComponent
import com.georgistephanov.android.busroute.di.component.DaggerApplicationComponent
import com.georgistephanov.android.busroute.di.module.ApplicationModule
import javax.inject.Inject

class MvpApp : Application() {
    @Inject
    lateinit var dataManager: DataManager

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