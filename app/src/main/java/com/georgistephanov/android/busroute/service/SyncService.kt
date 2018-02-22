package com.georgistephanov.android.busroute.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.georgistephanov.android.busroute.MvpApp
import com.georgistephanov.android.busroute.data.DataManager
import com.georgistephanov.android.busroute.di.component.DaggerServiceComponent

import javax.inject.Inject

class SyncService : Service() {

    @Inject
    @JvmField
    var dataManager: DataManager? = null

    override fun onCreate() {
        super.onCreate()
        val component = DaggerServiceComponent.builder()
                .applicationComponent((application as MvpApp).getComponent())
                .build()
        component.inject(this)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
