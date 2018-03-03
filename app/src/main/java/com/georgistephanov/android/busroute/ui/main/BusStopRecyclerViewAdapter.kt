package com.georgistephanov.android.busroute.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.georgistephanov.android.busroute.R

import com.georgistephanov.android.busroute.ui.main.BusStopListFragment.OnListFragmentInteractionListener
import org.jetbrains.anko.find

/**
 * [RecyclerView.Adapter] that can display a [String] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class BusStopRecyclerViewAdapter(private val mValues: List<String>, private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<BusStopRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_stop, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        holder.mContentView.text = mValues[position]

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem as String)
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mContentView by lazy { mView.find<TextView>(R.id.name) }
        var mItem: String? = null
    }
}
