package com.nepplus.finalproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.nepplus.finalproject.databinding.ActivitySignUpBinding
import com.nepplus.finalproject.datas.BasicResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : BaseActivity() {

    lateinit var binding: ActivitySignUpBinding

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
            val inputNick = binding.nicknameEdt.text.toString()

            apiService.putRequestSignUp(inputId,inputPw,inputNick).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful) {
                        val basicResponse = response.body()!!
                        Toast.makeText(mContext, basicResponse.message, Toast.LENGTH_SHORT).show()
                    } else {
                        val errorBody = response.errorBody()!!.string()

                        val jsonObj =JSONObject(errorBody)
                        val message = jsonObj.getString("message")

                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })

        }

    }

    override fun setValues() {

    }
}