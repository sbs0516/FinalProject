package com.nepplus.finalproject

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.nepplus.finalproject.web.ServerAPI
import com.nepplus.finalproject.web.ServerAPIService
import org.w3c.dom.Text
import retrofit2.Retrofit

abstract class BaseActivity: AppCompatActivity() {

    lateinit var mContext: Context

    private lateinit var retrofit: Retrofit

    lateinit var apiService: ServerAPIService

    lateinit var barTitleTxt: TextView
    lateinit var barAddListImg: ImageView
    lateinit var barDepartureEdtTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this

        retrofit = ServerAPI.getRetrofit(mContext)
        apiService = retrofit.create(ServerAPIService::class.java)

        supportActionBar?.let {
            setActionBar()
        }
    }

    abstract fun setupEvents()

    abstract fun setValues()

    fun setActionBar() {
        val defaultBar = supportActionBar!!
        defaultBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        defaultBar.setCustomView(R.layout.action_bar_custom)

        val toolBar = defaultBar.customView.parent as Toolbar
        toolBar.setContentInsetsAbsolute(0,0)

        barTitleTxt =defaultBar.customView.findViewById(R.id.barTitleTxt)
        barAddListImg =defaultBar.customView.findViewById(R.id.barAddListImg)
        barDepartureEdtTxt = defaultBar.customView.findViewById(R.id.barDepartureEdtTxt)
    }

}