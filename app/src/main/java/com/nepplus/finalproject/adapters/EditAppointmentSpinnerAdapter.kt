package com.nepplus.finalproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.nepplus.finalproject.R
import com.nepplus.finalproject.datas.PlaceData

class EditAppointmentSpinnerAdapter(
    val mContext: Context,
    resId: Int,
    val mList: List<PlaceData>): ArrayAdapter<PlaceData>(mContext, resId, mList) {

    val mInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var row = convertView

        if(row == null) {
            row = mInflater.inflate(R.layout.departure_list_item, null)
        }
        row!!

        val data = mList[position]

        val myPlaceTxt = row.findViewById<TextView>(R.id.myPlaceTxt)
        val myPrimaryPlaceText = row.findViewById<TextView>(R.id.myPrimaryPlaceText)
        val myPlaceImg = row.findViewById<ImageView>(R.id.myPlaceImg)

        myPlaceTxt.text = data.name
        if(data.isPrimary) {
            myPrimaryPlaceText.visibility = View.VISIBLE
        } else {
            myPrimaryPlaceText.visibility = View.GONE
        }
        myPlaceImg.visibility = View.GONE

        return row
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {

        var row = convertView

        if(row == null) {
            row = mInflater.inflate(R.layout.departure_list_item, null)
        }
        row!!

        val data = mList[position]

        val myPlaceTxt = row.findViewById<TextView>(R.id.myPlaceTxt)
        val myPrimaryPlaceText = row.findViewById<TextView>(R.id.myPrimaryPlaceText)
        val myPlaceImg = row.findViewById<ImageView>(R.id.myPlaceImg)

        myPlaceTxt.text = data.name
        if(data.isPrimary) {
            myPrimaryPlaceText.visibility = View.VISIBLE
        } else {
            myPrimaryPlaceText.visibility = View.GONE
        }
        myPlaceImg.visibility = View.GONE

        return row
    }

}