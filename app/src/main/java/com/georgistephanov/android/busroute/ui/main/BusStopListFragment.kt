package com.georgistephanov.android.busroute.ui.main

import android.content.Context
import android.os.Bundle
import android.app.Fragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.georgistephanov.android.busroute.R
import com.georgistephanov.android.busroute.ui.base.BaseActivity

/**
 * A fragment representing a list of Items.
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
class BusStopListFragment : Fragment() {

    private var mListener: OnListFragmentInteractionListener? = null
    private val model by lazy { ViewModelProviders.of(activity as BaseActivity).get(MainViewModel::class.java) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_stop_list, container, false)

        if (view is RecyclerView) {

            model.listStops.observe(activity as BaseActivity, Observer<List<String>> { stopList ->
                stopList?.let {
                    view.adapter = BusStopRecyclerViewAdapter(it, mListener)
                }
            })
        }

        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnListFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: String)
    }
}