package com.georgistephanov.android.busroute.data.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase.CONFLICT_NONE
import android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
import android.util.Log
import com.georgistephanov.android.busroute.data.room.dao.BusSequenceDao
import com.georgistephanov.android.busroute.data.room.dao.BusStopDao
import com.georgistephanov.android.busroute.data.room.entities.BusSequence
import com.georgistephanov.android.busroute.data.room.entities.BusStop
import com.georgistephanov.android.busroute.di.ApplicationContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDbHelper @Inject constructor(@ApplicationContext context: Context) : DbHelper {

    private val database: BusStopDatabase
    private val busStopDao: BusStopDao
    private val busSequenceDao: BusSequenceDao

    private val busStopCallback = object: RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            // Populate the databases from the asset files when the database is first created
            var reader: BufferedReader

            // Populate the bus_stop table
            try {
                val file = context.assets.open("bus_stops.txt")
                reader = BufferedReader(InputStreamReader(file))

                var line = reader.readLine()
                var negativeIndex = -1

                var counter = 0
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

                    if (++counter % 1000 == 0) {
                        Log.d("Database", "${counter / 1000}000 rows inserted in bus_stop" )
                    }

                    line = reader.readLine()
                }

            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }

            // Populate the bus_stop_sequence table
            try {
                val file = context.assets.open("bus_lines.txt")
                reader = BufferedReader(InputStreamReader(file))

                var line = reader.readLine()

                var counter = 0
                while (line != null) {
                    val split = line.split("\t")

                    // Prevent invalid lines
                    if (split.size != 5) {
                        line = reader.readLine()
                        continue
                    }

                    val cv = ContentValues()
                    cv.put("bus_line", split[0])
                    cv.put("direction", split[1].toInt())
                    cv.put("sequence", split[2].toInt())
                    cv.put("stop_code", split[3])
                    cv.put("stop_name", split[4])

                    db.insert("bus_stop_sequence", CONFLICT_NONE, cv)

                    if (++counter % 1000 == 0) {
                        Log.d("Database", "${counter / 1000}000 rows inserted in bus_stop_sequence" )
                    }

                    line = reader.readLine()
                }

            } catch(ioe: IOException) {
                ioe.printStackTrace()
            }

            Log.d("Database", "Finished")
        }
    }

    init {
        database = Room.databaseBuilder(context, BusStopDatabase::class.java, "bus")
                .addCallback(busStopCallback)
                .build()

        busStopDao = database.busStopDao()
        busSequenceDao = database.busSequenceDao()
    }

    /**
     * Empty method to be called from the application object to create the database if it doesn't exist.
     * This creation happens only the first time the app is used and/or if its data had been deleted.
     */
    override fun initialiseDatabase() { busStopDao.initDatabase() }

    /* Bus stop methods */
    override fun insertBusStop(busStop: BusStop) {
        busStopDao.insert(busStop)
    }
    override fun deleteBusStop(busStop: BusStop) {
        busStopDao.delete(busStop)
    }
    override fun deleteBusStop(name: String) {
        busStopDao.delete(name)
    }
    override fun busStopExists(name: String) : Boolean = busStopDao.getFirstBusStop(name) != null
    override fun getBusStop(name: String) : BusStop = busStopDao.getStop(name)

    /* Bus sequence methods */
    override fun insertSequence(busSequence: BusSequence) {
        busSequenceDao.insert(busSequence)
    }
    override fun deleteSequence(busSequence: BusSequence) {
        busSequenceDao.delete(busSequence)
    }
    override fun busExists(line: String) : Boolean = busSequenceDao.getFirstBusLine(line) != null
    override fun getSequence(line: String) : List<BusSequence>? = busSequenceDao.getSequence(line)
    override fun getSequence(line: String, direction: Int) : List<BusSequence>? = busSequenceDao.getSequence(line, direction)
}