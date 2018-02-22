package com.georgistephanov.android.busroute.di.component

import com.georgistephanov.android.busroute.di.PerActivity
import com.georgistephanov.android.busroute.di.module.ActivityModule
import com.georgistephanov.android.busroute.ui.main.MainActivity
import dagger.Component

@PerActivity
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(activity: MainActivity)

}