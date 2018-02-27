package com.georgistephanov.android.busroute.di.module

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.georgistephanov.android.busroute.di.ActivityContext
import com.georgistephanov.android.busroute.di.PerActivity
import com.georgistephanov.android.busroute.ui.main.MainMvpPresenter
import com.georgistephanov.android.busroute.ui.main.MainMvpView

import dagger.Module
import dagger.Provides

@Module
class ActivityModule(private val mActivity: AppCompatActivity) {

    @Provides
    @ActivityContext
    fun provideContext() : Context = mActivity

    @Provides
    fun provideActivity() : AppCompatActivity = mActivity

}