package com.nepplus.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nepplus.finalproject.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}