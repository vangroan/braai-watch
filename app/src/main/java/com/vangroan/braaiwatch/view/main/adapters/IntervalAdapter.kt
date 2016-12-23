package com.vangroan.braaiwatch.view.main.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

/**
 * Created by Victor on 2016/12/23.
 */
class IntervalAdapter(context : Context, textRes : Int) : ArrayAdapter<Long>(context, textRes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return super.getView(position, convertView, parent)
    }
}