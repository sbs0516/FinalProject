package com.nepplus.finalproject.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.nepplus.finalproject.R
import com.nepplus.finalproject.databinding.FragmentMyInformationBinding
import com.nepplus.finalproject.datas.BasicResponse
import com.nepplus.finalproject.utils.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInformationFragment: BaseFragment() {

    lateinit var binding: FragmentMyInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_information, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.nickEditImg.setOnClickListener {

            val customView = LayoutInflater.from(mContext).inflate(R.layout.alert_custom_nickname_edit, null)
            val nickEditEdt = customView.findViewById<EditText>(R.id.nickEditEdt)
            val alert = AlertDialog.Builder(mContext)
            alert.setView(customView).setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

                apiService.patchRequestNickName("nickname", nickEditEdt.text.toString())
                    .enqueue(object : Callback<BasicResponse> {
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if(response.isSuccessful) {
                                val basicResponse = response.body()!!
                                GlobalData.loginUser = basicResponse.data.user
                                setMyInfo()
                            }
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                        }

                    })
            }).setNegativeButton("취소", null).show()

        }

        binding.myReadyTimeLayout.setOnClickListener {

            val customView = LayoutInflater.from(mContext).inflate(R.layout.alert_custom_readytime_edit, null)
            val readyTimeEdit = customView.findViewById<EditText>(R.id.readyTimeEdit)

            val alert = AlertDialog.Builder(mContext)
            alert.setView(customView).setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

                apiService.patchRequestReadyTime("ready_minute", readyTimeEdit.text.toString())
                    .enqueue(object : Callback<BasicResponse> {
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if(response.isSuccessful) {

                                GlobalData.loginUser = response.body()!!.data.user
                                setMyInfo()

                            }
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                        }

                    })

            }).setNegativeButton("취소", null).show()

        }

    }

    override fun setValues() {

        setMyInfo()

    }

    fun setMyInfo() {

        when(GlobalData.loginUser!!.provider) {
            "facebook" -> {
                binding.socialLoginImg.setBackgroundResource(R.drawable.facebook_icon)
            }
            "kakao" -> {
                binding.socialLoginImg.setBackgroundResource(R.drawable.kakao_social_logo_icon)
            }
            else -> {
                binding.socialLoginImg.visibility = View.GONE
                binding.myInfoSettingLayout.visibility = View.VISIBLE
            }
        }

        binding.nicknameTxt.text = GlobalData.loginUser!!.nick_name

//        binding.myReadyTimeTxt.text = GlobalData.loginUser!!.readyMinute.toString()

        if(GlobalData.loginUser!!.readyMinute >= 60) {
            val hour = GlobalData.loginUser!!.readyMinute / 60
            val min = GlobalData.loginUser!!.readyMinute % 60
            binding.myReadyTimeTxt.text = "${hour}시간 ${min}분"
        } else {
            binding.myReadyTimeTxt.text = "${GlobalData.loginUser!!.readyMinute}분"
        }

    }
}