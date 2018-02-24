package com.georgistephanov.android.busroute.data.room.entities

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "bus_stop_sequence")
class BusSequence(@PrimaryKey(autoGenerate = true) val _id: Int,
                  @ColumnInfo(name = "stop_code") val stopCode: Int,
                  @ColumnInfo(name = "bus_line") val busLine: String,
                  val direction: Int,
                  val sequence: Int,
                  @ColumnInfo(name = "stop_name") val stopName: String)