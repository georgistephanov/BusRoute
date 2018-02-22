package com.georgistephanov.android.busroute.ui.main

import com.georgistephanov.android.busroute.ui.base.MvpView

interface MainMvpView : MvpView {

    fun setMainMessage(message: String)

    fun populateBusStopsListResults(busStops: List<String>)

    fun resetBusStopsList()
}