package com.nepplus.finalproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nepplus.finalproject.R
import com.nepplus.finalproject.datas.PlaceData

class MyDepartureListRecyclerAdapter(
    val mContext: Context,
    val mList: List<PlaceData>): RecyclerView.Adapter<MyDepartureListRecyclerAdapter.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val myPlaceTxt = view.findViewById<TextView>(R.id.myPlaceTxt)
        val myPrimaryPlaceText = view.findViewById<TextView>(R.id.myPrimaryPlaceText)
        val myPlaceImg = view.findViewById<ImageView>(R.id.myPlaceImg)
        val myDepartureLayout = view.findViewById<LinearLayout>(R.id.myDepartureLayout)

        fun bind(data: PlaceData) {

            if(data.isPrimary) {
                myPrimaryPlaceText.visibility = View.VISIBLE
            } else {
                myPrimaryPlaceText.visibility = View.GONE
            }
            myPlaceTxt.text = data.name

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.departure_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])

        holder.myPlaceImg.setOnClickListener {
            Toast.makeText(mContext, "장소 지도 클릭", Toast.LENGTH_SHORT).show()
        }

        holder.myDepartureLayout.setOnClickListener {
            Toast.makeText(mContext, "장소 레이아웃 클릭", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}