package com.nepplus.finalproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.nepplus.finalproject.utils.ContextUtil

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        val myHandler = Handler(Looper.getMainLooper())
        myHandler.postDelayed( {

            val myIntent: Intent

            if(ContextUtil.getToken(mContext) != "") {
                myIntent = Intent(mContext, MainActivity::class.java)
                startActivity(myIntent)
            } else {
                myIntent = Intent(mContext, LoginActivity::class.java)
                startActivity(myIntent)
            }

        }, 2000)

    }
}