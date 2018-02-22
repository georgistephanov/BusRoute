package com.georgistephanov.android.busroute.ui.base

import android.support.annotation.StringRes

interface MvpView {
    fun showLoading()

    fun hideLoading()

    fun showMessage(@StringRes resId: Int)

    fun showMessage(message: String)

    fun hideKeyboard()
}