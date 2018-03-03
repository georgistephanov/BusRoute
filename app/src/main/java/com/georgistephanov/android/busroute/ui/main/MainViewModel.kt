package com.georgistephanov.android.busroute.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.georgistephanov.android.busroute.data.DataManager
import com.georgistephanov.android.busroute.data.room.entities.BusSequence
import com.georgistephanov.android.busroute.di.component.ApplicationComponent
import com.georgistephanov.android.busroute.utils.ocr.OcrCaptureActivity
import java.security.InvalidParameterException

class MainViewModel : ViewModel() {

    lateinit var applicationComponent: ApplicationComponent
    private val dataManager: DataManager by lazy { applicationComponent.getDataManager() }

    var mainMessage = MutableLiveData<String>()
    var listStops = MutableLiveData<List<String>>()

    init {
        mainMessage.value = "Search via the camera"
        listStops.value = listOf()
    }

    fun onCameraClicked() {
        listStops.value = listOf()
    }

    fun onCameraActionFinished() {
        findBusStop()
    }

    private fun findBusStop() {
        val cameraStringSet: Set<String> = OcrCaptureActivity.getCameraSourceSet()
        val iterator = cameraStringSet.iterator()

        val possibleStops: MutableList<String> = mutableListOf()
        val possibleBusNumbers: MutableSet<String> = mutableSetOf()

        iterator.forEach {
            var containsNumber = false
            val stringWithoutNumber = StringBuffer()

            // If the string contains a number (i.e. the potential bus number) store that substring
            // in the possibleBusNumbers set and generate a new containing the original one without the number
            for (string in it.split(" ")) {
                val currBusNumber = string.toUpperCase()

                if ( currBusNumber.matches(Regex(".*\\d+.*")) && !(possibleBusNumbers.contains(currBusNumber))) {
                    if (dataManager.busExists(currBusNumber)) {
                        possibleBusNumbers.add(currBusNumber)
                        containsNumber = true
                    }
                } else {
                    stringWithoutNumber.append(string)
                    stringWithoutNumber.append(" ")
                }
            }

            val possibleStop = if (containsNumber) stringWithoutNumber.toString().trim() else it

            // Add the string to the list of possible stops only if it doesn't contain new lines, its
            // length is greater than 2 and there exists a bus stop which contains that string
            if ( !(possibleStop.contains("\n")) &&
                    possibleStop.length > 2 &&
                    dataManager.getBusStop(possibleStop.toUpperCase()) != null) {

                possibleStops.add(possibleStop.toUpperCase())
            }
        }

        for (busNumber in possibleBusNumbers) {
            for (possibleStop in possibleStops.sortedByDescending {it.length }) {

                val stops: List<String> = try {
                    getBusStops(busNumber, possibleStop)
                } catch (ipe: InvalidParameterException) {
                    ipe.printStackTrace()
                    listOf()
                }

                if (stops.isNotEmpty()) {
                    mainMessage.value = busNumber
                    listStops.value = stops

                    return
                }
            }
        }

        mainMessage.value = "Could not detect the bus line"
    }

    private fun getBusStops(busNumber: String, stopName: String) : List<String> {

        val sequence: List<BusSequence>? = dataManager.getSequence(busNumber)
        sequence ?: throw InvalidParameterException("Invalid bus number")

        val busDirection = getBusDirection(sequence, stopName)
        if (busDirection != 1 && busDirection != 2) {
            throw InvalidParameterException("Bus direction must be 1 or 2")
        }

        val busStops: MutableList<String> = mutableListOf()

        sequence.sortedBy { it.sequence }
                .forEach {

                    if (it.direction == busDirection) {
                        busStops.add(it.stopName)
                    }
                }

        return busStops
    }

    private fun getBusDirection(sequence: List<BusSequence>, busStop: String) : Int {
        sequence.forEach {
            if (it.stopName.contains(busStop) && it.sequence > 5) {
                return it.direction
            }
        }

        return -1
    }
}