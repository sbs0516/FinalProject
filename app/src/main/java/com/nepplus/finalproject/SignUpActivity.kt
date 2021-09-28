package com.nepplus.finalproject

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import com.nepplus.finalproject.databinding.ActivitySignUpBinding
import com.nepplus.finalproject.datas.BasicResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : BaseActivity() {

    lateinit var binding: ActivitySignUpBinding

    private val Circle_GONE_Cross_VISI = 1
    private val Circle_VISI_Cross_GONE = 2
    private val rCircle_GONE_rCross_VISI = 3
    private val rCircle_VISI_rCross_GONE = 4

    var idDuplCheck = false
    var nickDuplCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.signUpBtn.setOnClickListener {

            val inputId = binding.idEdt.text.toString()
            val inputPw = binding.pwEdt.text.toString()
            val repeatPw = binding.repeatPwEdt.text.toString()
            val inputNick = binding.nicknameEdt.text.toString()

            if(inputPw.length < 4) {

                Toast.makeText(mContext, "변경할 비밀번호를 네 자리 이상 입력해주세요", Toast.LENGTH_SHORT).show()
                resetPwUI()
                return@setOnClickListener

            } else {
                crossImgAndCirCleImgVisible(Circle_VISI_Cross_GONE)
            }

            if(inputPw != repeatPw) {

                Toast.makeText(mContext, "변경할 비밀번호가 다르게 입력되었습니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show()
                resetPwUI()
                return@setOnClickListener

            } else {
                crossImgAndCirCleImgVisible(rCircle_VISI_rCross_GONE)
            }

            val alert = AlertDialog.Builder(mContext)

            if(!idDuplCheck) {
                Toast.makeText(mContext, "아이디 중복 체크를 해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!nickDuplCheck) {
                Toast.makeText(mContext, "닉네임 중복 체크를 해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            alert.setMessage("회원으로 가입하시겠습니까?").setPositiveButton("네", DialogInterface.OnClickListener { dialogInterface, i ->

                apiService.putRequestSignUp(inputId,inputPw,inputNick).enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if(response.isSuccessful) {
                            val basicResponse = response.body()!!
                            Toast.makeText(mContext, basicResponse.message, Toast.LENGTH_SHORT).show()

                            idDuplCheck = false
                            nickDuplCheck = false

                            finish()
                        } else {
                            val errorBody = response.errorBody()!!.string()

                            val jsonObj =JSONObject(errorBody)
                            val message = jsonObj.getString("message")

                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }

                })

            }).setNegativeButton("아니오", null).show()

        }

        binding.idDulpCheckBtn.setOnClickListener {

            val email = binding.idEdt.text.toString()

            apiService.getRequestDuplicationCheck("EMAIL", email).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful) {
                        Toast.makeText(mContext, "사용 가능한 메일입니다.", Toast.LENGTH_SHORT).show()
                        binding.idDulpCheckCircleImg.visibility = View.VISIBLE
                        idDuplCheck = true
                    } else {
                        Toast.makeText(mContext, "이미 사용 중인 메일입니다.", Toast.LENGTH_SHORT).show()
                        binding.idEdt.text = null
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })

        }

        binding.nickDuplCheckBtn.setOnClickListener {

            val nickName = binding.nicknameEdt.text.toString()

            apiService.getRequestDuplicationCheck("NICK_NAME", nickName).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful) {
                        Toast.makeText(mContext, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show()

                        binding.nickDuplCheckCircleImg.visibility = View.VISIBLE
                        nickDuplCheck = true
                    } else {
                        Toast.makeText(mContext, "이미 사용 중인 닉네임입니다.", Toast.LENGTH_SHORT).show()
                        binding.nicknameEdt.text = null
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })

        }

    }

    override fun setValues() {

        barTitleTxt.text = "회원가입"

        binding.pwEdt.addTextChangedListener {
            if(it.toString().length < 4) {
                crossImgAndCirCleImgVisible(Circle_GONE_Cross_VISI)
            } else {
                crossImgAndCirCleImgVisible(Circle_VISI_Cross_GONE)
            }

            if(it.toString() != binding.repeatPwEdt.text.toString()) {
                crossImgAndCirCleImgVisible(rCircle_GONE_rCross_VISI)
            }
        }

        binding.repeatPwEdt.addTextChangedListener {

            if(it.toString() == binding.pwEdt.text.toString() && it.toString().length >= 4) {
                crossImgAndCirCleImgVisible(rCircle_VISI_rCross_GONE)
            } else {
                crossImgAndCirCleImgVisible(rCircle_GONE_rCross_VISI)
            }
        }

        binding.idEdt.addTextChangedListener {

            idDuplCheck = false
            binding.idDulpCheckCircleImg.visibility = View.GONE

        }

        binding.nicknameEdt.addTextChangedListener {

            nickDuplCheck = false
            binding.nickDuplCheckCircleImg.visibility = View.GONE

        }

    }

    fun resetPwUI() {

        binding.pwEdt.text = null
        binding.repeatPwEdt.text = null
        binding.circleImg.visibility = View.GONE
        binding.crossImg.visibility = View.VISIBLE
        binding.repeatCircleImg.visibility = View.GONE
        binding.repeatCrossImg.visibility = View.VISIBLE
    }

    fun crossImgAndCirCleImgVisible(case: Int) {

        when(case) {
            1 -> {
                binding.circleImg.visibility = View.GONE
                binding.crossImg.visibility = View.VISIBLE
            }
            2 -> {
                binding.circleImg.visibility = View.VISIBLE
                binding.crossImg.visibility = View.GONE
            }
            3 -> {
                binding.repeatCircleImg.visibility = View.GONE
                binding.repeatCrossImg.visibility = View.VISIBLE
            }
            4 -> {
                binding.repeatCircleImg.visibility = View.VISIBLE
                binding.repeatCrossImg.visibility = View.GONE
            }
        }
    }
}