package com.georgistephanov.android.busroute.data.room.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.georgistephanov.android.busroute.data.room.entities.BusSequence

@Dao
interface BusSequenceDao {
    @Query("Select * FROM bus_stop_sequence WHERE bus_line = :line LIMIT 1")
    fun getFirstBusLine(line: String) : BusSequence?

    @Query("SELECT * FROM bus_stop_sequence WHERE bus_line = :line")
    fun getSequence(line: String) : List<BusSequence>

    @Query("SELECT * FROM bus_stop_sequence WHERE bus_line = :line AND direction = :direction")
    fun getSequence(line: String, direction: Int) : List<BusSequence>

    @Insert
    fun insert(busSequence: BusSequence)

    @Delete
    fun delete(busSequence: BusSequence)
}