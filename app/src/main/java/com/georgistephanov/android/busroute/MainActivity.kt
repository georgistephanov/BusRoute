package com.georgistephanov.android.busroute

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.content.Context
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import com.georgistephanov.android.busroute.ocr.OcrCaptureActivity
import com.georgistephanov.android.busroute.tfl.*
import org.jetbrains.anko.*


class MainActivity : AppCompatActivity() {

    private val RC_OCR_CAPTURE = 9003

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        if (OcrCaptureActivity.isCaptured()) {
            findBusStop()
        }
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
                    if (busExists(this, currBusNumber)) {
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
                    isContainedInBusStops(this, possibleStop)) {

                    possibleStops.add(possibleStop.toUpperCase())
            }
        }

        for (busNumber in possibleBusNumbers) {
            for (possibleStop in possibleStops
                                    .filter { it.length > 2 }
                                    .sortedByDescending {it.length }) {

                val stops: List<String> = getBusStopsList(this, busNumber, getBusDirection(this, busNumber, possibleStop))

                if (stops.isNotEmpty()) {
                    find<TextView>(R.id.bm_title).text = possibleStop

                    val listView: ListView = find(R.id.nextStopsList)
                    listView.adapter = CustomAdapter(this@MainActivity, stops)

                    return
                }
            }
        }

        find<TextView>(R.id.bm_title).text = "Could not detect the bus line"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_activity, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_camera -> {
                val intent = Intent(this, OcrCaptureActivity::class.java)
                startActivityForResult(intent, RC_OCR_CAPTURE)
            }
        }

        return true
    }

    private inner class CustomAdapter(context: Context, val busStops: List<String>)
        : ArrayAdapter<String>(context, R.layout.next_stops_adapter, R.id.name, busStops) {

    }
}
