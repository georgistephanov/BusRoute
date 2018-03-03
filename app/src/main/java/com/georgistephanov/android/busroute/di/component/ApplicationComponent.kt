package com.georgistephanov.android.busroute.di.component

import android.app.Application
import android.content.Context
import com.georgistephanov.android.busroute.MvpApp
import com.georgistephanov.android.busroute.data.DataManager
import com.georgistephanov.android.busroute.di.ApplicationContext
import com.georgistephanov.android.busroute.di.module.ApplicationModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(app: MvpApp)

    @ApplicationContext
    fun context(): Context

    fun application(): Application

    fun getDataManager(): DataManager
}