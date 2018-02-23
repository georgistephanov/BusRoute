package com.georgistephanov.android.busroute.data.room.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "bus_stop")
data class BusStop(@PrimaryKey val code: Int, val name: String)