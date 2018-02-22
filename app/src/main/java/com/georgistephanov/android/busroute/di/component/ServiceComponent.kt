package com.georgistephanov.android.busroute.di.component

import com.georgistephanov.android.busroute.di.PerService
import com.georgistephanov.android.busroute.di.module.ServiceModule
import com.georgistephanov.android.busroute.service.SyncService
import dagger.Component

@PerService
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ServiceModule::class))
interface ServiceComponent {

    fun inject(service: SyncService)

}