package com.georgistephanov.android.busroute.ui.base

import android.support.v7.app.AppCompatActivity
import com.georgistephanov.android.busroute.MvpApp
import com.georgistephanov.android.busroute.di.component.ActivityComponent
import com.georgistephanov.android.busroute.di.component.DaggerActivityComponent
import com.georgistephanov.android.busroute.di.module.ActivityModule

abstract class BaseActivity : AppCompatActivity() {

    @Suppress("DEPRECATION")
    val activityComponent: ActivityComponent by lazy {
        DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .applicationComponent((application as MvpApp).getComponent())
                .build()
    }
}