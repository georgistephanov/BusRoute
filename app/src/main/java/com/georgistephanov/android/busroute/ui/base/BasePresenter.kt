package com.georgistephanov.android.busroute.ui.base

import com.georgistephanov.android.busroute.data.DataManager

abstract class BasePresenter<V>(val dataManager: DataManager) : MvpPresenter<V> where V : MvpView{

    var mvpView: V? = null

    override fun onAttach(mvpView: V) {
        this.mvpView = mvpView
    }

    override fun onDetach() {
        mvpView = null
    }

    fun isViewAttached(): Boolean = mvpView != null
}