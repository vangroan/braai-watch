package com.vangroan.braaiwatch.main.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.vangroan.braaiwatch.main.view.enums.IntervalOption

/**
 * Created by Victor on 2016/12/23.
 */
class IntervalAdapter(context : Context, val resource: Int) : ArrayAdapter<IntervalOption>(context, resource) {

    init {
        addAll(IntervalOption.values().asList())
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var textView : TextView?
        if (convertView == null)
            textView = LayoutInflater.from(context).inflate(resource, parent, false) as TextView
        else
            textView = convertView as TextView

        textView.text = getItem(position).getTitleString(context)
        return textView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent)
    }
}