package com.georgistephanov.android.busroute

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import org.jetbrains.anko.find
import android.content.Context
import android.support.annotation.LayoutRes
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import com.georgistephanov.android.busroute.tfl.BusStop
import com.georgistephanov.android.busroute.tfl.StopPoint
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    private val RC_OCR_CAPTURE = 9003

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

//        if (OcrCaptureActivity.isCaptured()) {
//            val set : MutableSet<String> = OcrCaptureActivity.getCameraSourceSet()

            doAsync {
                val res = run(constructStopPointSearchUrl("Streatham Hill"))
                Log.d("OkHttp3", res)
                val stopPoints: StopPoint = Gson().fromJson(res, StopPoint::class.java)

                val busStopsLink = run(constructBusStopsSearchUrl("d7"))
                val busStops: List<BusStop> = Gson().fromJson(busStopsLink, Array<BusStop>::class.java).toList()

                uiThread {
                    // Get a reference of the ListView and populate it
                    val listView: ListView = find(R.id.nextStopsList)
                    listView.adapter = CustomAdapter(this@MainActivity, busStops)
                }
            }
//        }
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

    private inner class CustomAdapter(context: Context, val busStops: List<BusStop>)
        : ArrayAdapter<BusStop>(context, R.layout.next_stops_adapter, busStops) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val listItem: View = if (convertView == null) {
                LayoutInflater.from(this@MainActivity).inflate(R.layout.next_stops_adapter, parent,false)
            } else {
                convertView
            }

            val busStop = busStops[position]

            listItem.find<TextView>(R.id.name).text = busStop.commonName
            listItem.find<TextView>(R.id.indicator).text = busStop.indicator

            return listItem
        }
    }
}
