package com.nepplus.finalproject

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.Marker
import com.nepplus.finalproject.databinding.ActivityMyDeparturePopUpBinding
import com.nepplus.finalproject.datas.PlaceData
import com.nepplus.finalproject.utils.GlobalData

class MyDeparturePopUpActivity : BaseActivity() {

    lateinit var binding: ActivityMyDeparturePopUpBinding

    var mMyDepartureMarker = Marker()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_departure_pop_up)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.naverMapFragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.naverMapFragment, it).commit()
            }

        mapFragment.getMapAsync {

            val uiSettings = it.uiSettings
            uiSettings.isCompassEnabled = true
            uiSettings.isScaleBarEnabled = false

            val data = intent.getSerializableExtra("myDeparture") as PlaceData
            val mMyLat = data.latitude
            val mMyLng = data.longitude

            mMyDepartureMarker.position = LatLng(mMyLat, mMyLng)
            mMyDepartureMarker.map = it

            val cameraUpdate = CameraUpdate.scrollTo(LatLng(mMyLat, mMyLng))
            it.moveCamera(cameraUpdate)

        }

    }
}