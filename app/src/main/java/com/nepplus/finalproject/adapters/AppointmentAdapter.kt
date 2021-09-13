package com.nepplus.finalproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.nepplus.finalproject.R
import com.nepplus.finalproject.datas.AppointmentData

class AppointmentAdapter(
    val mContext: Context,
    resId: Int,
    mList: List<AppointmentData>): ArrayAdapter<AppointmentData>(mContext, resId, mList) {

    val mInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var row = convertView
        if(row == null) {
            row = mInflater.inflate(R.id.)
        }
    }
}