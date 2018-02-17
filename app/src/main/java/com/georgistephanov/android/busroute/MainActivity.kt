package com.georgistephanov.android.busroute

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.android.gms.common.api.CommonStatusCodes
import org.jetbrains.anko.find

class MainActivity : AppCompatActivity() {
    private val statusMessage by lazy { find<TextView>(R.id.status_message) }
    private val textValue by lazy { find<TextView>(R.id.text_value) }

    private val RC_OCR_CAPTURE = 9003
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        if (OcrCaptureActivity.isCaptured()) {
            val set : MutableSet<String> = OcrCaptureActivity.getCameraSourceSet()

            for (string in set) {
                Log.d("Blabla", string)
            }
        } else {
            Log.d("Blabla", "EMPTYYYYYYYYY========================================")
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
                val intent = Intent(this, OcrCaptureActivity::class.java)
                startActivityForResult(intent, RC_OCR_CAPTURE)
            }
        }

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    val text = data.getStringExtra(OcrCaptureActivity.TextBlockObject)
                    statusMessage?.setText(R.string.ocr_success)
                    textValue?.setText(text)
                    Log.d(TAG, "Text read: " + text)
                } else {
                    statusMessage?.setText(R.string.ocr_failure)
                    Log.d(TAG, "No Text captured, intent data is null")
                }
            } else {
                statusMessage?.setText(String.format(getString(R.string.ocr_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
