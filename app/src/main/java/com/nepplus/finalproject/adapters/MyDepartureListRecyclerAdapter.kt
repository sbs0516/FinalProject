package com.nepplus.finalproject.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.nepplus.finalproject.EditMyDepartureActivity
import com.nepplus.finalproject.MyDeparturePopUpActivity
import com.nepplus.finalproject.R
import com.nepplus.finalproject.datas.PlaceData


class MyDepartureListRecyclerAdapter(
    val mContext: Context,
    val mList: List<PlaceData>): RecyclerView.Adapter<MyDepartureListRecyclerAdapter.MyViewHolder>() {

    var isEditLayout = false

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val myPlaceTxt = view.findViewById<TextView>(R.id.myPlaceTxt)
        val myPrimaryPlaceText = view.findViewById<TextView>(R.id.myPrimaryPlaceText)
        val myPlaceImg = view.findViewById<ImageView>(R.id.myPlaceImg)
        val myDepartureLayout = view.findViewById<LinearLayout>(R.id.myDepartureLayout)
        val myDepartureEdtLayout = view.findViewById<LinearLayout>(R.id.myDepartureEdtLayout)
        val myDepartureEditBtn = view.findViewById<Button>(R.id.myDepartureEditBtn)

        fun bind(data: PlaceData, isEditLayout: Boolean) {

            if(data.isPrimary) {
                myPrimaryPlaceText.visibility = View.VISIBLE
            } else {
                myPrimaryPlaceText.visibility = View.GONE
            }
            myPlaceTxt.text = data.name

            if(isEditLayout) {
                myDepartureEdtLayout.visibility = View.VISIBLE
                val animationVisible = AnimationUtils.loadAnimation(mContext, R.anim.sweep_visible_item_anime)
                myDepartureEdtLayout.startAnimation(animationVisible)

            } else {
                myDepartureEdtLayout.visibility = View.GONE
                val animationGone = AnimationUtils.loadAnimation(mContext, R.anim.sweep_gone_item_anime)
                myDepartureEdtLayout.startAnimation(animationGone)

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.departure_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position], isEditLayout)

        holder.myPlaceImg.setOnClickListener {

            val myIntent = Intent(mContext, MyDeparturePopUpActivity::class.java)
            myIntent.putExtra("myDeparture", mList[position])
            mContext.startActivity(myIntent)

        }

        holder.myDepartureLayout.setOnClickListener {
            Toast.makeText(mContext, "?????? ???????????? ??????", Toast.LENGTH_SHORT).show()
        }

        holder.myDepartureEditBtn.setOnClickListener {

            val myIntent = Intent(mContext, EditMyDepartureActivity::class.java)
            mContext.startActivity(myIntent)

        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}