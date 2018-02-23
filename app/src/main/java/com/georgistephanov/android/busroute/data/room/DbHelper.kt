package com.georgistephanov.android.busroute.data.room

import com.georgistephanov.android.busroute.data.room.entities.BusStop

interface DbHelper {
    fun insertBusStop(busStop: BusStop)

    fun getBusStop(name: String) : BusStop?

    fun deleteBusStop(busStop: BusStop)

    fun deleteBusStop(name: String)
}