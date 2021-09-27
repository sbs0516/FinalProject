package com.nepplus.finalproject

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.nepplus.finalproject.databinding.ActivityMyDeparturePopUpBinding

class MyDeparturePopUpActivity : BaseActivity() {

    lateinit var binding: ActivityMyDeparturePopUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_departure_pop_up)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}