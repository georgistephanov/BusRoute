package com.georgistephanov.android.busroute

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.samples.vision.ocrreader.OcrCaptureActivity

class MainActivity : AppCompatActivity() {
    private var autoFocus: CompoundButton? = null
    private var useFlash: CompoundButton? = null
    private var statusMessage: TextView? = null
    private var textValue: TextView? = null

    private val RC_OCR_CAPTURE = 9003
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_activity, menu)

        statusMessage = findViewById(R.id.status_message) as TextView
        textValue = findViewById(R.id.text_value) as TextView

        autoFocus = findViewById(R.id.auto_focus) as CompoundButton
        useFlash = findViewById(R.id.use_flash) as CompoundButton

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_camera -> {
                val intent = Intent(this, OcrCaptureActivity::class.java)
                intent.putExtra(OcrCaptureActivity.AutoFocus, autoFocus?.isChecked())
                intent.putExtra(OcrCaptureActivity.UseFlash, useFlash?.isChecked())

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

// PAUL IS A SLAVE
