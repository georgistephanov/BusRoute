package com.georgistephanov.android.busroute.data

import com.georgistephanov.android.busroute.data.room.DbHelper
import com.georgistephanov.android.busroute.data.room.entities.BusSequence
import com.georgistephanov.android.busroute.data.room.entities.BusStop
import javax.inject.Inject

class AppDataManager @Inject constructor(private val appDbHelper: DbHelper) : DataManager {
    override fun initialiseDatabase() { appDbHelper.initialiseDatabase() }

    /* Bus stops database methods */
    override fun insertBusStop(busStop: BusStop) {
        appDbHelper.insertBusStop(busStop)
    }
    override fun deleteBusStop(busStop: BusStop) {
        appDbHelper.deleteBusStop(busStop)
    }
    override fun deleteBusStop(name: String) {
        appDbHelper.deleteBusStop(name)
    }
    override fun busStopExists(name: String): Boolean = appDbHelper.busStopExists(name)
    override fun getBusStop(name: String): BusStop? = appDbHelper.getBusStop(name)

    /* Bus stop sequence database methods */
    override fun insertSequence(busSequence: BusSequence) {
        appDbHelper.insertSequence(busSequence)
    }
    override fun deleteSequence(busSequence: BusSequence) {
        appDbHelper.deleteSequence(busSequence)
    }
    override fun busExists(line: String): Boolean = appDbHelper.busExists(line)
    override fun getSequence(line: String): List<BusSequence>? = appDbHelper.getSequence(line)
    override fun getSequence(line: String, direction: Int): List<BusSequence>? = appDbHelper.getSequence(line, direction)
}