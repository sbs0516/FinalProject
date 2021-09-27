package com.nepplus.finalproject.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.nepplus.finalproject.EditAppointmentActivity
import com.nepplus.finalproject.R
import com.nepplus.finalproject.adapters.AppointmentAdapter
import com.nepplus.finalproject.databinding.FragmentMyAppointmentBinding
import com.nepplus.finalproject.datas.AppointmentData
import com.nepplus.finalproject.datas.BasicResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyAppointmentFragment: BaseFragment() {

    lateinit var binding: FragmentMyAppointmentBinding

    val mAppointmentList = ArrayList<AppointmentData>()

    lateinit var mAdapter: AppointmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_appointment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

//        binding.uploadBtn.setOnClickListener {
//
//            val myIntent = Intent(mContext, EditAppointmentActivity::class.java)
//            startActivity(myIntent)
//
//        }

    }

    override fun setValues() {

        mAdapter = AppointmentAdapter(mContext, R.layout.appointment_list_item, mAppointmentList)
        binding.appointmentListView.adapter = mAdapter

    }

    override fun onResume() {
        super.onResume()
        Log.d("resume 확인", "resume")

        apiService.getRequestMyAppointment().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful) {
                    val basicResponse = response.body()!!
                    Log.d("약속 목록 가져오기", basicResponse.toString())

                    mAppointmentList.clear()
                    mAppointmentList.addAll(basicResponse.data.appointments)

                    mAdapter.notifyDataSetChanged()
                } else {
                    val error = JSONObject(response.errorBody()!!.string())

                    Log.d("실패", error.toString())
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d("실패 확인", t.toString())
            }

        })
    }
}