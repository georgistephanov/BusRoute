package com.georgistephanov.android.busroute.di.module

import android.app.Application
import android.content.Context
import com.georgistephanov.android.busroute.data.AppDataManager
import com.georgistephanov.android.busroute.data.DataManager
import com.georgistephanov.android.busroute.data.room.AppDbHelper
import com.georgistephanov.android.busroute.data.room.DbHelper
import com.georgistephanov.android.busroute.di.ApplicationContext

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val mApplication: Application) {

    @Provides
    @ApplicationContext
    internal fun provideContext(): Context = mApplication

    @Provides
    internal fun provideApplication(): Application = mApplication

    @Provides
    @Singleton
    internal fun provideDataManager(appDataManager: AppDataManager) : DataManager = appDataManager

    @Provides
    @Singleton
    internal fun provideDbHelper(appDbHelper: AppDbHelper) : DbHelper = appDbHelper
}
