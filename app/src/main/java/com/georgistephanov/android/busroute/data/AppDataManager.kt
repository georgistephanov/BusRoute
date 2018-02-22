package com.georgistephanov.android.busroute.data

import android.content.Context
import com.georgistephanov.android.busroute.di.ApplicationContext
import javax.inject.Inject

class AppDataManager : DataManager {
    val context: Context

    @Inject constructor(@ApplicationContext context: Context) {
        this.context = context
    }
}