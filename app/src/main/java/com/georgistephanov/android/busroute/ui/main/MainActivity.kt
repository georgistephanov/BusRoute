package com.georgistephanov.android.busroute.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.widget.TextView
import android.content.Context
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ListView
import com.georgistephanov.android.busroute.R
import com.georgistephanov.android.busroute.ui.base.BaseActivity
import org.jetbrains.anko.find
import javax.inject.Inject


class MainActivity : BaseActivity(), MainMvpView {

    @Inject
    lateinit var presenter: MainMvpPresenter<MainMvpView>

    private val mMainMessage by lazy { find<TextView>(R.id.bm_title) }
    private val mListStops by lazy { find<ListView>(R.id.nextStopsList) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activityComponent.inject(this)

        // Get the ViewModel bounded to this activity
        val model = ViewModelProviders.of(this).get(MainViewModel::class.java)

        // Set the main message text view observer
        model.mainMessage.observe(this, Observer<String> { mainMessage ->
            mMainMessage.text = mainMessage
        })

        // Set the list of stops observer
        model.listStops.observe(this, Observer<List<String>> { stopsList ->
            stopsList?.let {
                mListStops.adapter = CustomAdapter(this, it)
            }
        })

        presenter.onAttach(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.onDetach()
    }

    override fun onResume() {
        super.onResume()

        if (com.georgistephanov.android.busroute.utils.ocr.OcrCaptureActivity.isCaptured()) {
            presenter.onCameraActionFinished()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_activity, menu)

        return true
    }

    override fun setMainMessage(message: String) {
        mMainMessage.text = message
    }

    override fun populateBusStopsListResults(busStops: List<String>) {
        mListStops.adapter = CustomAdapter(this@MainActivity, busStops)
    }

    override fun resetBusStopsList() {
        mListStops.adapter = null
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_camera -> presenter.onCameraActionClicked()
        }

        return true
    }

    private inner class CustomAdapter(context: Context, val busStops: List<String>)
        : ArrayAdapter<String>(context, R.layout.next_stops_adapter, R.id.name, busStops) { }
}
