package com.georgistephanov.android.busroute.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.georgistephanov.android.busroute.data.room.entities.BusSequence
import com.georgistephanov.android.busroute.data.room.entities.BusStop

@Dao
interface BusStopDao {
    @Query("SELECT * FROM bus_stop LIMIT 1")
    fun initDatabase() : BusStop?

    @Query("Select * FROM bus_stop WHERE instr(name, :name) > 0 LIMIT 1")
    fun getFirstBusStop(name: String) : BusStop?

    @Query("SELECT * FROM bus_stop WHERE instr(name, (:name)) > 0")
    fun getStop(name: String) : BusStop

    @Insert
    fun insert(busStop: BusStop)

    @Delete
    fun delete(busStop: BusStop)

    @Query("DELETE FROM bus_stop WHERE name = (:name)")
    fun delete(name: String)
}