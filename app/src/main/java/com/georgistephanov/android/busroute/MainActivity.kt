package com.georgistephanov.android.busroute

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.content.Context
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import com.georgistephanov.android.busroute.ocr.OcrCaptureActivity
import com.georgistephanov.android.busroute.tfl.*
import com.google.gson.Gson
import kotlinx.coroutines.experimental.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.*
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private val RC_OCR_CAPTURE = 9003

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        if (OcrCaptureActivity.isCaptured()) {
            val set : MutableSet<String> = OcrCaptureActivity.getCameraSourceSet()

            findBusStop()
//            displayBusInformation()
        }
    }

    private fun displayBusInformation() {
        doAsync {
            val res = run(constructStopPointSearchUrl("Streatham Hill"))
            val stopPoints: StopPoint = Gson().fromJson(res, StopPoint::class.java)

            val busStopsLink = run(constructBusStopsSearchUrl("d7"))
            val busStops: List<BusStop> = Gson().fromJson(busStopsLink, Array<BusStop>::class.java).toList()

            uiThread {
                // Get a reference of the ListView and populate it
//                val listView: ListView = find(R.id.nextStopsList)
//                listView.adapter = CustomAdapter(this@MainActivity, busStops)
            }
        }
    }

    private fun findBusStop() {
        val cameraStringSet: Set<String> = OcrCaptureActivity.getCameraSourceSet()
        val iterator = cameraStringSet.iterator()

        val possibleStops: MutableList<String> = mutableListOf()
        val possibleBusNumber: MutableList<String> = mutableListOf()

        iterator.forEach {
            var containsNumber = false
            var stringWithoutNumber: StringBuffer = StringBuffer()

            for (string in it.split(" ")) {
                if (!possibleBusNumber.contains(string) && string.matches(Regex(".*\\d+.*"))) {
                    if (busExists(this, string)) {
                        possibleBusNumber.add(string)
                        containsNumber = true
                    }
                } else {
                    stringWithoutNumber.append(string)
                    stringWithoutNumber.append(" ")
                }
            }

            val possibleStop = if (containsNumber) stringWithoutNumber.toString().trim() else it
            if ( !(possibleStop.contains("\n") && possibleStop.length > 2)) {
                if (isContainedInBusStops(this, possibleStop)) {
                    possibleStops.add(possibleStop.toUpperCase())
                }
            }
        }

        possibleStops.apply {
            filter {
                it.length > 2
            }
            sortedByDescending {
                it.length
            }
        }

        val firstStopResult = possibleStops
                .filter { it.length > 2 }.sortedByDescending {it.length }[0]
        val firstBusResult = possibleBusNumber[0]

        if (possibleStops.size > 0) {
            find<TextView>(R.id.bm_title).text = firstStopResult
        }

        val stops: List<String> = getBusStopsList(this, firstBusResult, getBusDirection(this, firstBusResult, firstStopResult))

        val listView: ListView = find(R.id.nextStopsList)
        listView.adapter = CustomAdapter(this@MainActivity, stops)
    }

    private fun constructStopPointSearchUrl(string : String) : String {
        val searchParam = string.replace(" ", "%20", true)

        return "https://api.tfl.gov.uk/Stoppoint/Search/$searchParam"
    }

    private fun constructBusStopsSearchUrl(busNumber : String) : String {
        return "https://api.tfl.gov.uk/line/$busNumber/stoppoints"
    }

    private fun run(url: String) : String {
        val request = Request.Builder().url(url).build()

        try {
            val response = OkHttpClient().newCall(request).execute()
            return response.body()!!.string()
        }
        catch (e : Exception) {
            e.printStackTrace()
        }

        return ""
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

//        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//            val listItem: View = if (convertView == null) {
//                LayoutInflater.from(this@MainActivity).inflate(R.layout.next_stops_adapter, parent,false)
//            } else {
//                convertView
//            }
//
//            val busStop = busStops[position]
//
//            listItem.find<TextView>(R.id.name).text = busStop.commonName
//            listItem.find<TextView>(R.id.indicator).text = busStop.indicator
//
//            return listItem
//        }
    }
}
