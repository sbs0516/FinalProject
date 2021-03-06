package com.nepplus.finalproject

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.kakao.sdk.user.UserApiClient
import com.nepplus.finalproject.databinding.ActivityLoginBinding
import com.nepplus.finalproject.datas.BasicResponse
import com.nepplus.finalproject.utils.ContextUtil
import com.nepplus.finalproject.utils.GlobalData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.util.*


class LoginActivity : BaseActivity() {

    lateinit var callbackManager: CallbackManager

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.loginBtn.setOnClickListener {

            val inputId = binding.idEdt.text.toString()
            val inputPw = binding.pwEdt.text.toString()

            apiService.postRequestSignIn(inputId, inputPw).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful) {
                        val basicResponse = response.body()!!
//                        Toast.makeText(mContext, basicResponse.message, Toast.LENGTH_SHORT).show()
                        Log.d("일반 로그인 토큰", basicResponse.data.token)

                        val id = basicResponse.data.user.id
                        val provider = basicResponse.data.user.provider
                        val email = basicResponse.data.user.email
                        val nickname = basicResponse.data.user.nick_name

                        ContextUtil.setToken(mContext, basicResponse.data.token)

                        Toast.makeText(mContext, nickname, Toast.LENGTH_SHORT).show()

                        GlobalData.loginUser = basicResponse.data.user

                        moveToMain()

                    } else {
                        val errorBody = response.errorBody()!!.string()

                        val jsonObj = JSONObject(errorBody)
                        val message = jsonObj.getString("message")
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                }

            })

        }

        callbackManager = CallbackManager.Factory.create();

//        binding.loginButton.setReadPermissions("email")

        binding.signUpBtn.setOnClickListener {

            val myIntent = Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)

        }

        binding.kakaoLoginBtn.setOnClickListener {
            UserApiClient.instance.loginWithKakaoAccount(mContext) { token, error ->
                if (error != null) {
                    Log.e("카카오 로그인", "로그인 실패", error)
                }
                else if (token != null) {
                    Log.i("카카오 로그인", "로그인 성공 ${token.accessToken}")

                    UserApiClient.instance.me { user, error ->
                        if (error != null) {
                            Log.e("카카오 로그인", "사용자 정보 요청 실패", error)
                        }
                        else if (user != null) {
                            Log.i("카카오 로그인", "사용자 정보 요청 성공" +
                                    "\n회원번호: ${user.id}" +
                                    "\n이메일: ${user.kakaoAccount?.email}" +
                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
                        }

                        apiService
                            .postRequestSocialLogin("kakao",
                                user!!.id.toString(),
                                user.kakaoAccount?.profile?.nickname.toString()).enqueue(object : Callback<BasicResponse> {
                                override fun onResponse(
                                    call: Call<BasicResponse>,
                                    response: Response<BasicResponse>
                                ) {
                                    val basicResponse = response.body()!!

                                    ContextUtil.setToken(mContext, basicResponse.data.token)

                                    Toast.makeText(mContext, "로그인 성공", Toast.LENGTH_SHORT).show()
                                    Log.d("카카오 토큰", basicResponse.data.token)

                                    GlobalData.loginUser = basicResponse.data.user

                                    moveToMain()
                                }

                                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                                }

                            })
                }
            }

            }
        }

        binding.facebookLoginBtn.setOnClickListener {

            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {

                    var graphRequest = GraphRequest.newMeRequest(result!!.accessToken, object : GraphRequest.GraphJSONObjectCallback {
                        override fun onCompleted(jsonObj: JSONObject?, response: GraphResponse?) {
                            val name = jsonObj!!.getString("name")
                            val id = jsonObj!!.getString("id")
                            Log.d("이름",name)
                            Log.d("아이디",id)

                            apiService.postRequestSocialLogin("facebook", id, name).enqueue(object : Callback<BasicResponse> {
                                override fun onResponse(
                                    call: Call<BasicResponse>,
                                    response: Response<BasicResponse>
                                ) {
                                    val basicResponse = response.body()!!

                                    ContextUtil.setToken(mContext, basicResponse.data.token)

                                    Toast.makeText(mContext, basicResponse.message, Toast.LENGTH_SHORT).show()
                                    Log.d("페이스북 토큰", basicResponse.data.token)

                                    GlobalData.loginUser = basicResponse.data.user

                                    moveToMain()
                                }

                                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                                }

                            })

                        }
                    })
                    graphRequest.executeAsync()

                }

                override fun onCancel() {
                }

                override fun onError(error: FacebookException?) {
                }

            })

            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        }

    }

    override fun setValues() {

        val info = packageManager.getPackageInfo(
            "com.nepplus.finalproject",
            PackageManager.GET_SIGNATURES
        )
        for (signature in info.signatures) {
            val md: MessageDigest = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun moveToMain() {
        val myIntent = Intent(mContext, MainActivity::class.java)
        startActivity(myIntent)
    }
}