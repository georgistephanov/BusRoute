package com.georgistephanov.android.busroute.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import com.georgistephanov.android.busroute.data.DataManager
import com.georgistephanov.android.busroute.data.room.entities.BusSequence
import com.georgistephanov.android.busroute.ui.base.BasePresenter
import com.georgistephanov.android.busroute.utils.busExists
import com.georgistephanov.android.busroute.utils.getBusDirection
import com.georgistephanov.android.busroute.utils.getBusStopsList
import com.georgistephanov.android.busroute.utils.ocr.OcrCaptureActivity
import java.security.InvalidParameterException
import javax.inject.Inject

class MainPresenter<V> @Inject constructor(dataManager: DataManager)
    : BasePresenter<V>(dataManager), MainMvpPresenter<V> where V : MainMvpView {

    private val viewActivity: MainActivity by lazy { mvpView as MainActivity }
    private val viewModel: MainViewModel by lazy { ViewModelProviders.of(viewActivity).get(MainViewModel::class.java) }

    private val RC_OCR_CAPTURE = 9003

    override fun onCameraActionClicked() {
        mvpView?.resetBusStopsList()

        val intent = Intent(viewActivity, OcrCaptureActivity::class.java)
        viewActivity.startActivityForResult(intent, RC_OCR_CAPTURE)
    }

    override fun onCameraActionFinished() {
        findBusStop()
    }

    private fun findBusStop() {
        val cameraStringSet: Set<String> = OcrCaptureActivity.getCameraSourceSet()
        val iterator = cameraStringSet.iterator()

        val possibleStops: MutableList<String> = mutableListOf()
        val possibleBusNumbers: MutableSet<String> = mutableSetOf()

        iterator.forEach {
            var containsNumber = false
            var stringWithoutNumber = StringBuffer()

            // If the string contains a number (i.e. the potential bus number) store that substring
            // in the possibleBusNumbers set and generate a new containing the original one without the number
            for (string in it.split(" ")) {
                val currBusNumber = string.toUpperCase()

                if ( currBusNumber.matches(Regex(".*\\d+.*")) && !(possibleBusNumbers.contains(currBusNumber))) {
                    if (busExists(viewActivity, currBusNumber)) {
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
                    listOf<String>()
                }

                if (stops.isNotEmpty()) {
                    viewModel.mainMessage.value = possibleStop
                    viewModel.listStops.value = stops

                    return
                }
            }
        }

        viewModel.mainMessage.value = "Could not detect the bus line"
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