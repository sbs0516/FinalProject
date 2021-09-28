package com.nepplus.finalproject

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.nepplus.finalproject.databinding.ActivityLeavingMemberBinding
import com.nepplus.finalproject.datas.BasicResponse
import com.nepplus.finalproject.utils.ContextUtil
import com.nepplus.finalproject.utils.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeavingMemberActivity : BaseActivity() {

    lateinit var binding: ActivityLeavingMemberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_leaving_member)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.cancelBtn.setOnClickListener {

            val alert = AlertDialog.Builder(mContext)

            alert.setMessage("회원 탈퇴를 취소하시겠습니까?").setPositiveButton("네", DialogInterface.OnClickListener { dialogInterface, i ->
                finish()
            }).setNegativeButton("아니오", null).show()

        }

        binding.okBtn.setOnClickListener {

            val agreeStr = binding.agreeEdt.text.toString()

            if(agreeStr != "동의") {
                Toast.makeText(mContext, "탈퇴를 원하시면 [동의] 라고 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val alert = AlertDialog.Builder(mContext)

            alert.setMessage("정말 탈퇴하시겠습니까?").setPositiveButton("네", DialogInterface.OnClickListener { dialogInterface, i ->

                apiService.deleteRequestMember(agreeStr).enqueue(object :
                    Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>, response: Response<BasicResponse>
                    ) {
                        if(response.isSuccessful) {
                            Toast.makeText(mContext, "회원 탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show()

                            GlobalData.loginUser = null

                            ContextUtil.setToken(mContext, "")

                            val myIntent = Intent(mContext, LoginActivity::class.java)
                            myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(myIntent)
                        }
                    }

                    override fun onFailure(
                        call: Call<BasicResponse>, t: Throwable
                    ) {
                    }


                })

            }).setNegativeButton("아니오", null).show()

        }

    }

    override fun setValues() {
    }
}