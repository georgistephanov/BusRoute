package com.georgistephanov.android.busroute.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var mainMessage = MutableLiveData<String>()
    var listStops = MutableLiveData<List<String>>()

    init {
        mainMessage.value = "Search via the camera"
        listStops.value = listOf()
    }
}