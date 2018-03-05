package com.georgistephanov.android.busroute.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import com.georgistephanov.android.busroute.data.DataManager
import com.georgistephanov.android.busroute.data.room.entities.BusSequence
import com.georgistephanov.android.busroute.di.component.ApplicationComponent
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

class MainViewModel : ViewModel() {

    lateinit var applicationComponent: ApplicationComponent
    private val dataManager: DataManager by lazy { applicationComponent.getDataManager() }

    var mainMessage = MutableLiveData<String>()
    var listStops = MutableLiveData<List<BusSequence>>()

    init {
        mainMessage.value = "Search via the camera"
        listStops.value = listOf()

        Handler().postDelayed({

            findBusStop()

        }, 1000)
    }

    fun onCameraClicked() {
        listStops.value = listOf()
    }

    fun onCameraActionFinished() {
        findBusStop()
    }

    private fun findBusStop() {
        //val cameraStringSet: Set<String> = OcrCaptureActivity.getCameraSourceSet()
        val cameraStringSet: Set<String> = setOf("Mile end D7")

        val possibleStops: MutableList<String> = mutableListOf()
        val possibleBusNumbers: MutableSet<String> = mutableSetOf()

        mainMessage.value = "Looking for the correct route..."

        launch {

            for (string in cameraStringSet) {
                var containsNumber = false
                val stringWithoutNumber = StringBuffer()

                // If the string contains a number (i.e. the potential bus number) store that substring
                // in the possibleBusNumbers set and generate a new containing the original one without the number
                for (word in string.split(" ")) {
                    val currWord = word.toUpperCase()

                    if (currWord.matches(Regex(".*\\d+.*"))
                            && !(possibleBusNumbers.contains(currWord))
                            && runBlocking { dataManager.busExists(currWord) }) {

                            possibleBusNumbers.add(currWord)
                            containsNumber = true
                    } else {
                        stringWithoutNumber.append(word)
                        stringWithoutNumber.append(" ")
                    }
                }

                val possibleStop = if (containsNumber) stringWithoutNumber.toString().trim() else string

                // Add the string to the list of possible stops only if it doesn't contain new lines, its
                // length is greater than 2 and there exists a bus stop which contains that string
                if ( !(possibleStop.contains("\n")) &&
                        possibleStop.length > 2 &&
                        runBlocking { dataManager.busStopExists(possibleStop.toUpperCase()) }) {

                    possibleStops.add(possibleStop.toUpperCase())
                }
            }


            for (busNumber in possibleBusNumbers) {
                val sequence: List<BusSequence>? = runBlocking { dataManager.getSequence(busNumber) }
                sequence ?: continue

                if (possibleStops.isNotEmpty()) {

                    for (possibleStop in possibleStops.sortedByDescending { it.length }) {
                        val stops: List<BusSequence> = getBusStops(sequence, possibleStop)

                        if (stops.isNotEmpty()) {
                            mainMessage.postValue(busNumber)
                            listStops.postValue(stops)

                            return@launch
                        }
                    }

                } else {
                    // There are no stop names captured through the camera

                    val stops: List<BusSequence> = getBusStops(sequence)

                    if (stops.isNotEmpty()) {
                        mainMessage.postValue(busNumber)
                        listStops.postValue(stops)

                        return@launch
                    }
                }
            }

            mainMessage.postValue("No stops found")
        }
    }

    private fun getBusStops(sequence: List<BusSequence>, stopName: String = "") : List<BusSequence> {
        val busDirection = if (stopName != "") getBusDirection(sequence, stopName) else 1

        val busStops: MutableList<BusSequence> = mutableListOf()

        sequence.sortedBy{ it.sequence }.forEach {
            if (it.direction == busDirection) {
                busStops.add(it)
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

        // Fallback in case no direction is returned just return the outbound route
        return 1
    }
}