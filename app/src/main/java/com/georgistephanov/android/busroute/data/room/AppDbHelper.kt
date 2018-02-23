package com.georgistephanov.android.busroute.data.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase.CONFLICT_NONE
import android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
import com.georgistephanov.android.busroute.data.room.dao.BusStopDao
import com.georgistephanov.android.busroute.data.room.entities.BusStop
import com.georgistephanov.android.busroute.di.ApplicationContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDbHelper @Inject constructor(@ApplicationContext context: Context) : DbHelper {

    private val busStopDao: BusStopDao

    private val busStopCallback = object: RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            // Populate the database from the bus_stops.txt file when the database is created

            val reader: BufferedReader

            try {
                val file = context.assets.open("bus_stops.txt")
                reader = BufferedReader(InputStreamReader(file))

                var line = reader.readLine()

                var negativeIndex = -1

                while (line != null) {

                    val code: Int =
                            if (line.split("\t".toRegex(), 2)[0] != "NONE")
                                line.split("\t".toRegex(), 2)[0].toInt()
                            else
                                negativeIndex--

                    val name: String = line.split("\t".toRegex(), 2)[1]

                    val cv = ContentValues()
                    cv.put("code", code)
                    cv.put("name", name)

                    db.insert("bus_stop", CONFLICT_REPLACE, cv)

                    line = reader.readLine()
                }

            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }
    }

    init {
        busStopDao = Room.databaseBuilder(context, BusStopDatabase::class.java, "bus")
                .allowMainThreadQueries().addCallback(busStopCallback).build().busStopDao()
    }

    override fun insertBusStop(busStop: BusStop) {
        busStopDao.insert(busStop)
    }

    override fun getBusStop(name: String) : BusStop = busStopDao.getStop(name)

    override fun deleteBusStop(busStop: BusStop) {
        busStopDao.delete(busStop)
    }

    override fun deleteBusStop(name: String) {
        busStopDao.delete(name)
    }
}