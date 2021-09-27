package com.nepplus.finalproject

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.nepplus.finalproject.databinding.ActivityMyDepartureListBinding

class MyDepartureListActivity : BaseActivity() {

    lateinit var binding: ActivityMyDepartureListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_departure_list)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}