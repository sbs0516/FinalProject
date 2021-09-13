package com.nepplus.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.nepplus.finalproject.adapters.AppointmentAdapter
import com.nepplus.finalproject.databinding.ActivityMainBinding
import com.nepplus.finalproject.datas.AppointmentData

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var mAdapter: AppointmentAdapter

    val mAppointmentList = ArrayList<AppointmentData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.uploadBtn.setOnClickListener {

            val myIntent = Intent(mContext, EditAppointmentActivity::class.java)
            startActivity(myIntent)

        }

    }

    override fun setValues() {

        mAdapter = AppointmentAdapter(mContext, R.layout.appointment_list_item, mAppointmentList)
        binding.appointmentListView.adapter = mAdapter

    }
}