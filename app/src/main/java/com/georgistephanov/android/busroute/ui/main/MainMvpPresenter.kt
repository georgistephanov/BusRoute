package com.georgistephanov.android.busroute.ui.main

import com.georgistephanov.android.busroute.ui.base.MvpPresenter

interface MainMvpPresenter<V> : MvpPresenter<V> where V : MainMvpView {

    fun onCameraActionClicked()

    fun onCameraActionFinished()
}