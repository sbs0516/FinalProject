package com.nepplus.finalproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.nepplus.finalproject.R
import com.nepplus.finalproject.datas.AppointmentData
import java.text.SimpleDateFormat
import java.util.*

class AppointmentAdapter(
    val mContext: Context,
    resId: Int,
    val mList: List<AppointmentData>): ArrayAdapter<AppointmentData>(mContext, resId, mList) {

    val mInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var row = convertView
        if(row == null) {
            row = mInflater.inflate(R.layout.appointment_list_item, null)
        }
        row!!

        val data = mList[position]

        val titleTxt = row.findViewById<TextView>(R.id.titleTxt)
//        val appointmentPlaceMapImg = row.findViewById<ImageView>(R.id.appointmentPlaceMapImg)
        val dateTimeTxt = row.findViewById<TextView>(R.id.dateTimeTxt)
        val placeTxt = row.findViewById<TextView>(R.id.placeTxt)
        val friendNameTxt = row.findViewById<TextView>(R.id.friendNameTxt)

        titleTxt.text = data.title

//        val sdf = SimpleDateFormat("M월 d일 a h시 mm분")
//        dateTimeTxt.text = sdf.format(data.datetime.time)

        placeTxt.text = data.place

        if(data.invitedFriends.size == 1) {
            friendNameTxt.text = "${data.invitedFriends[0].nick_name}"
        } else if(data.invitedFriends.size > 1){
            friendNameTxt.text = "${data.invitedFriends[0].nick_name}님 외 ${data.invitedFriends.size - 1}명"
        } else {
            friendNameTxt.text = ""
        }

        return row
    }
}