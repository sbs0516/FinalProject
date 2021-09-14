package com.nepplus.finalproject

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nepplus.finalproject.web.ServerAPI
import com.nepplus.finalproject.web.ServerAPIService
import retrofit2.Retrofit

abstract class BaseActivity: AppCompatActivity() {

    lateinit var mContext: Context

    private lateinit var retrofit: Retrofit

    lateinit var apiService: ServerAPIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this

        retrofit = ServerAPI.getRetrofit(mContext)
        apiService = retrofit.create(ServerAPIService::class.java)
    }

    abstract fun setupEvents()

    abstract fun setValues()

}