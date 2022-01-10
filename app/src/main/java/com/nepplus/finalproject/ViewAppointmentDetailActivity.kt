package com.nepplus.finalproject

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
import com.nepplus.finalproject.databinding.ActivityViewAppointmentDetailBinding
import com.nepplus.finalproject.datas.AppointmentData
import com.nepplus.finalproject.datas.BasicResponse
import com.odsay.odsayandroidsdk.API
import com.odsay.odsayandroidsdk.ODsayData
import com.odsay.odsayandroidsdk.ODsayService
import com.odsay.odsayandroidsdk.OnResultCallbackListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class ViewAppointmentDetailActivity : BaseActivity() {
    lateinit var binding: ActivityViewAppointmentDetailBinding

    lateinit var mAppointmentData: AppointmentData

    val arrivalMarker = Marker()

    val depatureMarker = Marker()

    val pathOverLay = PathOverlay()

    val mInfoWindow = InfoWindow()

    var needLocationServerSendServer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_appointment_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        binding.refreshBtn.setOnClickListener {

            getAppointmentFromServer()

        }

        binding.scrollHelpTxt.setOnTouchListener { view, motionEvent ->
            binding.scrollView.requestDisallowInterceptTouchEvent(true)

            return@setOnTouchListener false
        }

        binding.arrivedBtn.setOnClickListener {

            needLocationServerSendServer = true

            val pl = object : PermissionListener {
                override fun onPermissionGranted() {

                    if (ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {

                        return
                    }

                    val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        0L, 0f, object : LocationListener {
                            override fun onLocationChanged(p0: Location) {

                                if(needLocationServerSendServer) {
                                    Log.d("위도", p0.latitude.toString())
                                    Log.d("위도", p0.longitude.toString())

                                    apiService.postRequestArrival(mAppointmentData.id, p0.latitude, p0.longitude)
                                        .enqueue(object : Callback<BasicResponse> {
                                            override fun onResponse(
                                                call: Call<BasicResponse>,
                                                response: Response<BasicResponse>
                                            ) {
                                                if(response.isSuccessful) {

                                                    needLocationServerSendServer = false
                                                    Toast.makeText(mContext, "약속 인증에 성공했습니다.", Toast.LENGTH_SHORT)
                                                        .show()

                                                } else {
                                                    val jsonObj = JSONObject(response.errorBody()!!.string())

                                                    val message = jsonObj.getString("message")

                                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT)
                                                        .show()
                                                }
                                            }

                                            override fun onFailure(
                                                call: Call<BasicResponse>,
                                                t: Throwable
                                            ) {
                                            }

                                        })
                                }
                            }

                            override fun onStatusChanged(
                                provider: String?,
                                status: Int,
                                extras: Bundle?
                            ) {

                            }

                            override fun onProviderEnabled(provider: String) {

                            }

                            override fun onProviderDisabled(provider: String) {

                            }

                        })

                }

                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    Toast.makeText(mContext, "현재 위치 정보를 파악해야 도착 인증이 가능합니다.", Toast.LENGTH_SHORT).show()
                }

            }
            TedPermission.create().setPermissionListener(pl)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION).check()

        }

    }

    override fun setValues() {
        barTitleTxt.text = "약속 상세 정보"

        mAppointmentData = intent.getSerializableExtra("appointment") as AppointmentData

        binding.titleTxt.text = mAppointmentData.title
        binding.placeNameTxt.text = mAppointmentData.place

        val invitedFriendsCount = "(참여 인원 : ${mAppointmentData.invitedFriends.size}명)"
        binding.invitedFriendCountTxt.text = "$invitedFriendsCount"

        val sdf = SimpleDateFormat("M/d a h:mm")
        binding.timeTxt.text = sdf.format(mAppointmentData.datetime.time)

        setNaverMap()

        getAppointmentFromServer()

    }

    fun getAppointmentFromServer() {

        apiService.getRequestAppointmentDetail(mAppointmentData.id).enqueue(object :
            Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful) {
                    val basicResponse = response.body()!!
                    mAppointmentData = basicResponse.data.appointment

                    binding.invitedFriendLayout.removeAllViews()

                    val inflater = LayoutInflater.from(mContext)

                    val sdf = SimpleDateFormat("H:mm 도착")

                    for(friend in mAppointmentData.invitedFriends) {
                        val view = inflater.inflate(R.layout.invited_friend_list_item, null)

                        val friendProfileImg = view.findViewById<ImageView>(R.id.friendProfileImg)
                        val friendNicknameTxt = view.findViewById<TextView>(R.id.friendNicknameTxt)
                        val friendStatusTxt = view.findViewById<TextView>(R.id.friendStatusTxt)

                        if(friend.arrivedAt == null) {
                            friendStatusTxt.text = "도착 전"
                        } else {
                            friendStatusTxt.text = sdf.format(friend.arrivedAt!!)
                        }

                        Glide.with(mContext).load(friend.profileImg).into(friendProfileImg)
                        friendNicknameTxt.text = friend.nickName
                        friendStatusTxt.text = "기능 추가 예정"

                        binding.invitedFriendLayout.addView(view)
                    }
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }

        })

    }

    fun setNaverMap() {

        val fm = supportFragmentManager
        val arrival = LatLng(mAppointmentData.latitude, mAppointmentData.longitude)
        val depature = LatLng(mAppointmentData.startLat, mAppointmentData.startLng)
        val mapFragment = fm.findFragmentById(R.id.naverMapFragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.naverMapFragment, it).commit()
            }

        mapFragment.getMapAsync {

            val points = ArrayList<LatLng>()

            points.add(depature)

            arrivalMarker.position = arrival
            arrivalMarker.map = it

            depatureMarker.position = depature
            depatureMarker.iconTintColor = Color.RED
            depatureMarker.map = it

            val centerOfDepartAndArrive = LatLng((mAppointmentData.startLat + mAppointmentData.latitude)/2,
                (mAppointmentData.startLng + mAppointmentData.longitude)/2)

            val cameraUpdate = CameraUpdate.scrollTo(centerOfDepartAndArrive)
            it.moveCamera(cameraUpdate)

            val odsay = ODsayService.init(mContext, "xw5dVu/37eSxCrn1ftvGmOXtHkkOWocjzG88bldL3tE")

            odsay.requestSearchPubTransPath(
                mAppointmentData.startLng.toString(),
                mAppointmentData.startLat.toString(),
                mAppointmentData.longitude.toString(),
                mAppointmentData.latitude.toString(),
                null, null, null, object : OnResultCallbackListener {
                    override fun onSuccess(p0: ODsayData?, p1: API?) {
                        val jsonObj = p0!!.json
                        val resultObj = jsonObj.getJSONObject("result")
                        val pathArr = resultObj.getJSONArray("path")
                        val firstPath = pathArr.getJSONObject(0)
                        val infoObj = firstPath.getJSONObject("info")

                        val totalDistance = infoObj.getDouble("totalDistance") / 1000

                        Log.d("총 거리", totalDistance.toInt().toString())

                        val totalTime = infoObj.getInt("totalTime")

                        mInfoWindow.adapter = object : InfoWindow.DefaultTextAdapter(mContext) {
                            override fun getText(p0: InfoWindow): CharSequence {
                                return if(totalTime >= 60) {
                                    "${totalTime/60}시간 ${totalTime%60}분 소요"
                                } else {
                                    "${totalTime%60}분 소요"
                                }
                            }

                        }
                        mInfoWindow.open(arrivalMarker)

                        val subPathArr = firstPath.getJSONArray("subPath")

                        for(i in 0 until subPathArr.length()) {
                            val subPathObj = subPathArr.getJSONObject(i)

                            if(!subPathObj.isNull("passStopList")) {
                                val passStopList = subPathObj.getJSONObject("passStopList")
                                val stationsArr = passStopList.getJSONArray("stations")
                                for(j in 0 until stationsArr.length()) {
                                    val stationsObj = stationsArr.getJSONObject(j)
                                    val latLng = LatLng(stationsObj.getString("y").toDouble(),
                                        stationsObj.getString("x").toDouble())
                                    points.add(latLng)
                                }
                            }
                        }
                        when(totalDistance.toInt()) {
                            in 0 until 10 -> it.moveCamera(CameraUpdate.zoomTo(12.0))
                            in 10 until 30 -> it.moveCamera(CameraUpdate.zoomTo(10.0))
                            in 30 until 50 -> it.moveCamera(CameraUpdate.zoomTo(9.0))
                            else -> it.moveCamera(CameraUpdate.zoomTo(8.0))
                        }
                        points.add(arrival)


                        pathOverLay.coords = points
                        pathOverLay.map = it
                    }
                    override fun onError(p0: Int, p1: String?, p2: API?) {
                        Log.d("예상 시간 실패", p1!!)
                    }
                }
            )

        }
    }
}