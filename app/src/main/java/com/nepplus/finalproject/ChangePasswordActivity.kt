package com.nepplus.finalproject

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.nepplus.finalproject.databinding.ActivityChangePasswordBinding
import com.nepplus.finalproject.datas.BasicResponse
import com.nepplus.finalproject.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : BaseActivity() {

    lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.cancelBtn.setOnClickListener {

            val alert = AlertDialog.Builder(mContext)
            alert.setMessage("비밀번호 변경을 취소하시겠습니까? 이전 화면으로 돌아갑니다.")
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                    finish()
                }).setNegativeButton("취소", null).show()

        }

        binding.okBtn.setOnClickListener {

            val currentPw = binding.currentPwEdt.text.toString()
            val newPw = binding.changePwEdt.text.toString()
            val repeatPw = binding.repeatPwEdt.text.toString()

            if(currentPw == null) {
                Toast.makeText(mContext, "현재 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                binding.currentPwEdt.setBackgroundResource(R.drawable.underline_red_rect)
                return@setOnClickListener
            } else if(currentPw.length < 4) {
                Toast.makeText(mContext, "현재 비밀번호를 네 자리 이상 입력해주세요", Toast.LENGTH_SHORT).show()
                binding.currentPwEdt.setBackgroundResource(R.drawable.underline_red_rect)
                return@setOnClickListener
            }

            binding.currentPwEdt.setBackgroundResource(0)


            if(newPw.length < 4) {
                Toast.makeText(mContext, "변경할 비밀번호를 네 자리 이상 입력해주세요", Toast.LENGTH_SHORT).show()
                binding.changePwEdt.text = null
                binding.repeatPwEdt.text = null
                return@setOnClickListener
            } else {
                binding.circleImg.visibility = View.VISIBLE
                binding.crossImg.visibility = View.GONE
            }

            if(newPw != repeatPw) {
                Toast.makeText(mContext, "변경할 비밀번호가 다르게 입력되었습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                binding.changePwEdt.text = null
                binding.repeatPwEdt.text = null
                binding.circleImg.visibility = View.GONE
                binding.crossImg.visibility = View.VISIBLE
                binding.repeatCircleImg.visibility = View.GONE
                binding.repeatCrossImg.visibility = View.VISIBLE
                return@setOnClickListener
            } else {
                binding.repeatCircleImg.visibility = View.VISIBLE
                binding.repeatCrossImg.visibility = View.GONE
            }

            val alert = AlertDialog.Builder(mContext)
            alert.setMessage("비밀번호를 변경하시겠습니까?")
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

                    apiService.patchRequestChangePassword(currentPw, newPw).enqueue(object : Callback<BasicResponse> {
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if(response.isSuccessful) {
                                ContextUtil.setToken(mContext, response.body()!!.data.token)
                                Toast.makeText(mContext, "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(mContext, "현재 비밀번호가 틀렸습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                                binding.currentPwEdt.setBackgroundResource(R.drawable.underline_red_rect)
                                binding.currentPwEdt.text = null
                            }
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        }

                    })

                }).setNegativeButton("취소", null).show()

        }

    }

    override fun setValues() {

        binding.changePwEdt.addTextChangedListener {
            if(it.toString().length < 4) {
                binding.circleImg.visibility = View.GONE
                binding.crossImg.visibility = View.VISIBLE
            } else {
                binding.circleImg.visibility = View.VISIBLE
                binding.crossImg.visibility = View.GONE
            }

            if(it.toString() != binding.repeatPwEdt.text.toString()) {
                binding.repeatCircleImg.visibility = View.GONE
                binding.repeatCrossImg.visibility = View.VISIBLE
            }
        }
        binding.repeatPwEdt.addTextChangedListener {
            if(it.toString() == binding.changePwEdt.text.toString() && it.toString().length >= 4) {
                binding.repeatCircleImg.visibility = View.VISIBLE
                binding.repeatCrossImg.visibility = View.GONE
            } else {
                binding.repeatCircleImg.visibility = View.GONE
                binding.repeatCrossImg.visibility = View.VISIBLE
            }
        }


    }
}