package com.georgistephanov.android.busroute.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.widget.TextView
import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.ArrayAdapter
import com.georgistephanov.android.busroute.MvpApp
import com.georgistephanov.android.busroute.R
import com.georgistephanov.android.busroute.ui.base.BaseActivity
import com.georgistephanov.android.busroute.utils.ocr.OcrCaptureActivity
import org.jetbrains.anko.find


class MainActivity : BaseActivity(), BusStopListFragment.OnListFragmentInteractionListener {

    private val model by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    private val mMainMessage by lazy { find<TextView>(R.id.bm_title) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityComponent.inject(this)

        // Get the ViewModel bounded to this activity
        model.applicationComponent = (application as MvpApp).getComponent()

        // Set the main message text view observer
        model.mainMessage.observe(this, Observer<String> { mainMessage ->
            mMainMessage.text = mainMessage
        })
    }

    override fun onResume() {
        super.onResume()

        if (com.georgistephanov.android.busroute.utils.ocr.OcrCaptureActivity.isCaptured()) {
            model.onCameraActionFinished()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_activity, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_camera -> {
                model.onCameraClicked()

                val intent = Intent(this, OcrCaptureActivity::class.java)
                startActivity(intent)
            }
        }

        return true
    }

    override fun onListFragmentInteraction(item: String) {
        // TODO: Open new activity here with more bus stop info
    }
}
