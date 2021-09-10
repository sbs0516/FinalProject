package com.nepplus.finalproject

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.kakao.sdk.user.UserApiClient
import com.nepplus.finalproject.databinding.ActivityLoginBinding
import org.json.JSONObject
import java.security.MessageDigest
import java.util.*
import java.util.logging.LogManager


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

        callbackManager = CallbackManager.Factory.create();

//        binding.loginButton.setReadPermissions("email")

        binding.kakaoLoginBtn.setOnClickListener {
            UserApiClient.instance.loginWithKakaoAccount(mContext) { token, error ->
                if (error != null) {
                    Log.e("카카오 로그인", "로그인 실패", error)
                }
                else if (token != null) {
                    Log.i("카카오 로그인", "로그인 성공 ${token.accessToken}")
                }
            }
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
            }
        }

        binding.facebookLoginBtn.setOnClickListener {

            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    Log.d("로그인 성공", result.toString())

                    var graphRequest = GraphRequest.newMeRequest(result!!.accessToken, object : GraphRequest.GraphJSONObjectCallback {
                        override fun onCompleted(jsonObj: JSONObject?, response: GraphResponse?) {
                            val name = jsonObj!!.getString("name")
                            val id = jsonObj!!.getString("id")
                            Log.d("이름",name)
                            Log.d("아이디",id)
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
}