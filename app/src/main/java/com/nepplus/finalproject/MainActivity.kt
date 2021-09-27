package com.nepplus.finalproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nepplus.finalproject.adapters.AppointmentAdapter
import com.nepplus.finalproject.databinding.ActivityMainBinding
import com.nepplus.finalproject.datas.AppointmentData
import com.nepplus.finalproject.datas.BasicResponse
import com.nepplus.finalproject.fragments.InvitedAppointmentFragment
import com.nepplus.finalproject.fragments.MyAppointmentFragment
import com.nepplus.finalproject.fragments.MyInformationFragment
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupEvents()
        setValues()

    }

    override fun setupEvents() {

        replaceFragment(MyAppointmentFragment())

        barAddListImg.setOnClickListener {

            val myIntent = Intent(mContext, EditAppointmentActivity::class.java)
            startActivity(myIntent)

        }

        binding.bottomNavigationBar.setOnNavigationItemSelectedListener {

            when(it.itemId) {
                R.id.myAppointmentBottomItem -> {
                    replaceFragment(MyAppointmentFragment())
                    barTitleTxt.text = "내 약속 목록"
                    barAddListImg.visibility = View.VISIBLE
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.invitedBottomItem -> {
                    replaceFragment(InvitedAppointmentFragment())
                    barTitleTxt.text = "초대 목록"
                    barAddListImg.visibility = View.GONE
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    replaceFragment(MyInformationFragment())
                    barTitleTxt.text = "개인정보 설정"
                    barAddListImg.visibility = View.GONE
                    return@setOnNavigationItemSelectedListener true
                }
            }

        }

    }

    override fun setValues() {

        barTitleTxt.text = "내 약속 목록"
        barAddListImg.visibility = View.VISIBLE

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransAction = supportFragmentManager.beginTransaction()
        fragmentTransAction.replace(R.id.frameLayout, fragment)
        fragmentTransAction.commit()
    }
}