package com.nepplus.finalproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.nepplus.finalproject.adapters.MyDepartureListRecyclerAdapter
import com.nepplus.finalproject.databinding.ActivityMyDepartureListBinding
import com.nepplus.finalproject.datas.BasicResponse
import com.nepplus.finalproject.datas.PlaceData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyDepartureListActivity : BaseActivity() {

    lateinit var binding: ActivityMyDepartureListBinding

    lateinit var mDepartureRecyclerAdapter: MyDepartureListRecyclerAdapter

    val mMyDepartureList = ArrayList<PlaceData>()

    var isPushEditTxt = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_departure_list)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        val departureListInflater = LayoutInflater.from(mContext).inflate(R.layout.departure_list_item, null)
        val editLayout = departureListInflater.findViewById<LinearLayout>(R.id.editLayout)

        barAddListImg.setOnClickListener {

            val myIntent = Intent(mContext, AddMyDepartureActivity::class.java)
            startActivity(myIntent)
        }

        barDepartureEdtTxt.setOnClickListener {

            if(!isPushEditTxt) {
                isPushEditTxt = true
                editLayout.visibility = View.VISIBLE
                barDepartureEdtTxt.setBackgroundResource(R.drawable.border_black_rect_press)
            } else {
                isPushEditTxt = false
                editLayout.visibility = View.GONE
                barDepartureEdtTxt.setBackgroundResource(R.drawable.border_black_rect_not_press)
            }

        }

    }

    override fun setValues() {

        barTitleTxt.text = "내 출발지 목록"
        barAddListImg.visibility = View.VISIBLE
        barDepartureEdtTxt.visibility = View.VISIBLE

        mDepartureRecyclerAdapter = MyDepartureListRecyclerAdapter(mContext, mMyDepartureList)
        binding.myDepartureRecyclerView.layoutManager = LinearLayoutManager(mContext)
        binding.myDepartureRecyclerView.adapter = mDepartureRecyclerAdapter

        getMyDepartureListFromServer()

    }

    override fun onResume() {
        super.onResume()

        getMyDepartureListFromServer()
    }

    fun getMyDepartureListFromServer() {

        apiService.getRequestMyPlace().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful) {
                    val basicResponse = response.body()!!
                    mMyDepartureList.clear()
                    mMyDepartureList.addAll(basicResponse.data.places)

                    mDepartureRecyclerAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

    }
}