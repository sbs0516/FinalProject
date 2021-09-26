package com.nepplus.finalproject.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.nepplus.finalproject.web.ServerAPI
import com.nepplus.finalproject.web.ServerAPIService
import retrofit2.Retrofit

abstract class BaseFragment: Fragment() {

    lateinit var mContext: Context

    lateinit var retrofit: Retrofit

    lateinit var apiService: ServerAPIService

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mContext = requireContext()

        retrofit =ServerAPI.getRetrofit(mContext)

        apiService = retrofit.create(ServerAPIService::class.java)

    }

    abstract fun setupEvents()

    abstract fun setValues()

}