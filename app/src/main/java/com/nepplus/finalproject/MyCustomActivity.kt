package com.nepplus.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.nepplus.finalproject.databinding.ActivityMyCustomBinding

class MyCustomActivity : BaseActivity() {

    lateinit var binding: ActivityMyCustomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_custom)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
    }
}