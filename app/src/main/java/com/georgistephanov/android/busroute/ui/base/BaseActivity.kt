package com.georgistephanov.android.busroute.ui.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.georgistephanov.android.busroute.MvpApp
import com.georgistephanov.android.busroute.di.component.ActivityComponent
import com.georgistephanov.android.busroute.di.component.DaggerActivityComponent
import com.georgistephanov.android.busroute.di.module.ActivityModule

abstract class BaseActivity : AppCompatActivity(), MvpView {

    val activityComponent: ActivityComponent by lazy {
        DaggerActivityComponent.builder()
                .activityModule(ActivityModule(this))
                .applicationComponent((application as MvpApp).getComponent())
                .build()
    }

    override fun showLoading() {
        TODO("not implemented")
    }

    override fun hideLoading() {
        TODO("not implemented")
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(resId: Int) {
        Toast.makeText(this, resources.getString(resId), Toast.LENGTH_SHORT).show()
    }

    override fun hideKeyboard() {
        val view = this.currentFocus

        if (view != null) {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}