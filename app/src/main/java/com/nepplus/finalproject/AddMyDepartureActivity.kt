package com.nepplus.finalproject

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.nepplus.finalproject.databinding.ActivityAddMyDepartureBinding

class AddMyDepartureActivity : BaseActivity() {

    lateinit var binding: ActivityAddMyDepartureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_my_departure)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {


    }

    override fun setValues() {
        barTitleTxt.text = "출발지 등록하기"
    }
}