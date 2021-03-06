package com.georgistephanov.android.busroute.data.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.georgistephanov.android.busroute.data.room.dao.BusSequenceDao
import com.georgistephanov.android.busroute.data.room.dao.BusStopDao
import com.georgistephanov.android.busroute.data.room.entities.BusSequence
import com.georgistephanov.android.busroute.data.room.entities.BusStop

@Database(entities = [BusStop::class, BusSequence::class], version = 1, exportSchema = false)
abstract class BusStopDatabase : RoomDatabase() {

    abstract fun busStopDao() : BusStopDao

    abstract fun busSequenceDao() : BusSequenceDao
}