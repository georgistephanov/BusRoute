package com.georgistephanov.android.busroute.data

import com.georgistephanov.android.busroute.data.room.DbHelper
import com.georgistephanov.android.busroute.data.room.entities.BusStop
import javax.inject.Inject

class AppDataManager @Inject constructor(val appDbHelper: DbHelper) : DataManager {

    override fun insertBusStop(busStop: BusStop) {
        appDbHelper.insertBusStop(busStop)
    }

    override fun getBusStop(name: String): BusStop? = appDbHelper.getBusStop(name)

    override fun deleteBusStop(busStop: BusStop) {
        appDbHelper.deleteBusStop(busStop)
    }

    override fun deleteBusStop(name: String) {
        appDbHelper.deleteBusStop(name)
    }
}