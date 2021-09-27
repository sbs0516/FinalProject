package com.nepplus.finalproject

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.Marker
import com.nepplus.finalproject.databinding.ActivityAddMyDepartureBinding
import com.nepplus.finalproject.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMyDepartureActivity : BaseActivity() {

    lateinit var binding: ActivityAddMyDepartureBinding

    var mSelectedLat = 0.0
    var mSelectedLng = 0.0

    var mSelectedMarker = Marker()
    
    var isPrimary = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_my_departure)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        
        binding.yesBtn.setOnClickListener { 
            isPrimary = true
            binding.yesBtn.setBackgroundResource(R.drawable.background_grey_rect)
            binding.noBtn.setBackgroundResource(R.drawable.background_white_rect)
        }
        
        binding.noBtn.setOnClickListener {
            isPrimary = false
            binding.noBtn.setBackgroundResource(R.drawable.background_grey_rect)
            binding.yesBtn.setBackgroundResource(R.drawable.background_white_rect)
        }
        
        binding.addBtn.setOnClickListener {

            val alert = AlertDialog.Builder(mContext)
            val myDepartName = binding.myDepartureEdt.text.toString()
            alert.setMessage("출발지로 등록하시겠습니까?").setPositiveButton("네", DialogInterface.OnClickListener { dialogInterface, i -> 
                
                apiService.postRequestMyDeparture(myDepartName, mSelectedLat, mSelectedLng, isPrimary)
                    .enqueue(object : Callback<BasicResponse> {
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if(response.isSuccessful) {
                                Toast.makeText(mContext, "등록에 성공했습니다.", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        }

                    })
                
            }).setNegativeButton("아니오", null).show()
            
        }
        
    }

    override fun setValues() {
//        barTitleTxt.text = "출발지 등록하기"

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.naverMapFragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.naverMapFragment, it).commit()
            }

        mapFragment.getMapAsync {

            val uiSettings = it.uiSettings
            uiSettings.isCompassEnabled = true
            uiSettings.isScaleBarEnabled = false

            it.setOnMapClickListener { pointF, latLng ->

                mSelectedLat = latLng.latitude
                mSelectedLng = latLng.longitude

                mSelectedMarker.position = LatLng(mSelectedLat, mSelectedLng)
                mSelectedMarker.map = it

            }
        }
        
    }
}