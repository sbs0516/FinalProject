package com.nepplus.finalproject.fragments

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.nepplus.finalproject.*
import com.nepplus.finalproject.databinding.FragmentMyInformationBinding
import com.nepplus.finalproject.datas.BasicResponse
import com.nepplus.finalproject.utils.ContextUtil
import com.nepplus.finalproject.utils.GlobalData
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.Okio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInformationFragment: BaseFragment() {

    lateinit var binding: FragmentMyInformationBinding

    val REQ_FOR_GALLERY = 1000

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

        binding.leavingLayout.setOnClickListener {

            val myIntent = Intent(mContext, LeavingMemberActivity::class.java)
            startActivity(myIntent)

        }

        binding.myPassWordSettingLayout.setOnClickListener {

            val myIntent = Intent(mContext, ChangePasswordActivity::class.java)
            startActivity(myIntent)

        }

        binding.myPlaceListLayout.setOnClickListener {

            val myIntent = Intent(mContext, MyDepartureListActivity::class.java)
            startActivity(myIntent)

        }

        binding.profileImg.setOnClickListener {

            val permissionListener = object : PermissionListener {
                override fun onPermissionGranted() {
                    val myIntent = Intent()
                    myIntent.action = Intent.ACTION_PICK
                    myIntent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
                    startActivityForResult(myIntent, REQ_FOR_GALLERY)
                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(mContext, "권한이 거절되어 갤러리에 접근이 불가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }
            TedPermission.create()
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .setDeniedMessage("[설정] > [권한]에서 갤러리 권한을 열어주세요.")
                .check()

        }

        binding.logoutLayout.setOnClickListener {

            val alert = AlertDialog.Builder(mContext)
            alert.setMessage("정말 로그아웃 하시겠습니까?").setPositiveButton("네", DialogInterface.OnClickListener { dialogInterface, i ->
                GlobalData.loginUser = null

                ContextUtil.setToken(mContext, "")

                val myIntent = Intent(mContext, LoginActivity::class.java)
                myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(myIntent)

            }).setNegativeButton("아니오", null).show()

        }

        binding.nickEditImg.setOnClickListener {

            val customView = LayoutInflater.from(mContext).inflate(R.layout.alert_custom_nickname_edit, null)
            val nickEditEdt = customView.findViewById<EditText>(R.id.nickEditEdt)
            val alert = AlertDialog.Builder(mContext)
            alert.setTitle("닉네임 변경").setView(customView).setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

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
            alert.setTitle("내 준비시간 변경").setView(customView).setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQ_FOR_GALLERY) {
            if(resultCode == RESULT_OK) {
                val dataUri = data?.data

                Log.d("데이터 Uri", dataUri.toString())

                val resolver = mContext.contentResolver

                val inputStream = resolver.openInputStream(dataUri!!)
                val contentLength = inputStream?.available()?.toLong()?:0

                val requestBody = object : RequestBody() {
                    override fun contentType(): MediaType? {
                        return MediaType.parse(resolver.getType(dataUri!!))
                    }

                    override fun contentLength(): Long {
                        return contentLength
                    }

                    override fun writeTo(sink: BufferedSink) {
                        sink.writeAll(Okio.source(resolver.openInputStream(dataUri!!)))
                    }

                }
                val body = MultipartBody.Part.createFormData("profile_image", "myFile", requestBody)

                apiService.postRequestProfileImg(body).enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if(response.isSuccessful) {
                            val basicResponse = response.body()!!

                            Log.d("통신 확인", "통신 확인")

                            GlobalData.loginUser = basicResponse.data.user
                            binding.profileImg.setImageURI(dataUri)

                            Glide.with(mContext).load(dataUri.toString()).into(binding.profileImg)

                            Log.d("프로필 uri", basicResponse.data.user.profileImg)

                            Toast.makeText(mContext, "프로필 사진이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            val basicResponse = response.errorBody()!!.string()
                            Log.d("통신 실패1", basicResponse.toString())
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                        Log.e("통신 실패", t.toString(), t)
                    }

                })
            }
        }

    }

    fun setMyInfo() {

        Glide.with(mContext).load(GlobalData.loginUser!!.profileImg).into(binding.profileImg)

        Log.d("프로필 url", GlobalData.loginUser!!.profileImg)

        when(GlobalData.loginUser!!.provider) {
            "facebook" -> {
                binding.socialLoginImg.setImageResource(R.drawable.facebook_icon)
                binding.socialLoginImg.visibility = View.VISIBLE
            }
            "kakao" -> {
                binding.socialLoginImg.setImageResource(R.drawable.kakao_social_logo_icon)
                binding.socialLoginImg.visibility = View.VISIBLE
            }
            else -> {
                binding.socialLoginImg.visibility = View.GONE
                binding.myPassWordSettingLayout.visibility = View.VISIBLE
            }
        }

        binding.nicknameTxt.text = GlobalData.loginUser!!.nick_name

        if(GlobalData.loginUser!!.readyMinute >= 60) {
            val hour = GlobalData.loginUser!!.readyMinute / 60
            val min = GlobalData.loginUser!!.readyMinute % 60
            binding.myReadyTimeTxt.text = "${hour}시간 ${min}분"
        } else {
            binding.myReadyTimeTxt.text = "${GlobalData.loginUser!!.readyMinute}분"
        }

    }
}