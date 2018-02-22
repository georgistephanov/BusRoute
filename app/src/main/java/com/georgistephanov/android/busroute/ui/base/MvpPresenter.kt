package com.georgistephanov.android.busroute.ui.base

interface MvpPresenter<V> where V : MvpView {
    fun onAttach(mvpView: V)

    fun onDetach()
}