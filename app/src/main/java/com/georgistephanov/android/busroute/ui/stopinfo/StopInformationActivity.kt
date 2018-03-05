package com.georgistephanov.android.busroute.ui.stopinfo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.georgistephanov.android.busroute.R
import org.jetbrains.anko.find

class StopInformationActivity : AppCompatActivity() {

    private val stopName: String by lazy { intent.extras.getString("stopName") }
    private val stopCode: String by lazy { intent.extras.getString("stopCode") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stop_information)

        find<TextView>(R.id.stop_name).text = stopCode
    }
}
