package com.nepplus.finalproject

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.nepplus.finalproject.databinding.ActivityMyInformationBinding

class MyInformationActivity : BaseActivity() {

    lateinit var binding: ActivityMyInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_information)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}