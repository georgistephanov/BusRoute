package com.georgistephanov.android.busroute.ui.main

import android.content.Intent
import com.georgistephanov.android.busroute.data.DataManager
import com.georgistephanov.android.busroute.ui.base.BasePresenter
import com.georgistephanov.android.busroute.utils.busExists
import com.georgistephanov.android.busroute.utils.getBusDirection
import com.georgistephanov.android.busroute.utils.getBusStopsList
import com.georgistephanov.android.busroute.utils.isContainedInBusStops
import com.georgistephanov.android.busroute.utils.ocr.OcrCaptureActivity
import javax.inject.Inject

class MainPresenter<V> @Inject constructor(dataManager: DataManager) : BasePresenter<V>(dataManager), MainMvpPresenter<V>
        where V : MainMvpView {

    private val RC_OCR_CAPTURE = 9003
    private val viewActivity: MainActivity by lazy { mvpView as MainActivity }

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
            if ( !(possibleStop.contains("\n")) &&
                    possibleStop.length > 2 &&
                    isContainedInBusStops(viewActivity, possibleStop)) {

                possibleStops.add(possibleStop.toUpperCase())
            }
        }

        for (busNumber in possibleBusNumbers) {
            for (possibleStop in possibleStops
                    .filter { it.length > 2 }
                    .sortedByDescending {it.length }) {

                val stops: List<String> = getBusStopsList(viewActivity, busNumber, getBusDirection(viewActivity, busNumber, possibleStop))

                if (stops.isNotEmpty()) {

                    mvpView?.let {
                        it.setMainMessage(possibleStop)
                        it.populateBusStopsListResults(stops)
                    }

                    return
                }
            }
        }

        mvpView?.setMainMessage("Could not detect the bus line")
    }
}