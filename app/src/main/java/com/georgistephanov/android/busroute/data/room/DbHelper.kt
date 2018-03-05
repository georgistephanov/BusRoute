package com.georgistephanov.android.busroute.data.room

import com.georgistephanov.android.busroute.data.room.entities.BusSequence
import com.georgistephanov.android.busroute.data.room.entities.BusStop

interface DbHelper {

    /* Initialise database upon creation */
    fun initialiseDatabase()

    /* Bus stop methods */
    fun insertBusStop(busStop: BusStop)
    fun busStopExists(name: String) : Boolean
    fun getBusStop(name: String) : BusStop?
    fun deleteBusStop(busStop: BusStop)
    fun deleteBusStop(name: String)

    /* Bus sequence methods */
    fun insertSequence(busSequence: BusSequence)
    fun busExists(line: String) : Boolean
    fun getSequence(line: String) : List<BusSequence>?
    fun getSequence(line: String, direction: Int) : List<BusSequence>?
    fun deleteSequence(busSequence: BusSequence)
}