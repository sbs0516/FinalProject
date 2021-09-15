package com.nepplus.finalproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.nepplus.finalproject.datas.BasicResponse
import com.nepplus.finalproject.utils.ContextUtil
import com.nepplus.finalproject.utils.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        apiService.getRequestMyInfo().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {

                if(response.isSuccessful) {

                    val basicResponse = response.body()!!
                    GlobalData.loginUser = basicResponse.data.user

                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })

        val myHandler = Handler(Looper.getMainLooper())
        myHandler.postDelayed( {

            val myIntent: Intent

            if(ContextUtil.getToken(mContext) != "") {
                myIntent = Intent(mContext, MainActivity::class.java)
                startActivity(myIntent)
                finish()
            } else {
                myIntent = Intent(mContext, LoginActivity::class.java)
                startActivity(myIntent)
                finish()
            }

        }, 2000)

    }
}