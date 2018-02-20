package com.georgistephanov.android.busroute.tfl

import android.app.Activity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


fun isContainedInBusStops(activity: Activity, searchQuery: String) : Boolean {
    val reader: BufferedReader
    val query = searchQuery.toUpperCase()

    try {
        val file = activity.assets.open("stops.txt")
        reader = BufferedReader(InputStreamReader(file))

        var line = reader.readLine()

        while (line != null) {
            if (line.contains(query)) {
                return true
            }

            line = reader.readLine()
        }

    } catch (ioe: IOException) {
        ioe.printStackTrace()
    }

    return false
}

fun getBusDirection(activity: Activity, busLine: String, destination: String) : Int {
    val reader: BufferedReader

    try {
        val file = activity.assets.open("bus_lines.txt")
        reader = BufferedReader(InputStreamReader(file))

        var line = reader.readLine()

        while (line != null) {
            val strings = line.split("\t")

            if (strings[0] == busLine) {
                if (line.contains(destination) && strings[2].toInt() > 10) {
                    return strings[1].toInt()
                }
            }

            line = reader.readLine()
        }

    } catch (ioe: IOException) {
        ioe.printStackTrace()
    }

    return -1
}

fun busExists(activity: Activity, busNumber: String) : Boolean {
    val reader: BufferedReader

    try {
        val file = activity.assets.open("bus_lines.txt")
        reader = BufferedReader(InputStreamReader(file))

        var line = reader.readLine()

        while (line != null) {

            if (line.split("\t")[0] == busNumber) {
                return true
            }

            line = reader.readLine()
        }

    } catch (ioe: IOException) {
        ioe.printStackTrace()
    }

    return false
}

fun getBusStopsList(activity: Activity, busNumber: String, direction: Int) : List<String> {
    val reader: BufferedReader
    val busStops: MutableList<String> = mutableListOf()

    try {
        val file = activity.assets.open("bus_lines.txt")
        reader = BufferedReader(InputStreamReader(file))

        var line = reader.readLine()

        while (line != null) {

            val row = line.split("\t")
            if (row[0] == busNumber && row[1] == direction.toString()) {
                busStops.add(row[3])
            }

            line = reader.readLine()
        }

    } catch (ioe: IOException) {
        ioe.printStackTrace()
    }

    return busStops
}