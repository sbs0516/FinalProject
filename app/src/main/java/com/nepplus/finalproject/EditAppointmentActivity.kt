package com.nepplus.finalproject

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.nepplus.finalproject.adapters.EditAppointmentSpinnerAdapter
import com.nepplus.finalproject.databinding.ActivityEditAppointmentBinding
import com.nepplus.finalproject.datas.BasicResponse
import com.nepplus.finalproject.datas.PlaceData
import com.nepplus.finalproject.utils.ContextUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EditAppointmentActivity : BaseActivity() {

    lateinit var binding:ActivityEditAppointmentBinding

    var mSelectedDateTime = Calendar.getInstance()

    var mSelectedLat = 0.0
    var mSelectedLng = 0.0

    val mMyPlaceList = ArrayList<PlaceData>()

    var mNaverMap: NaverMap? = null

    var selectedMarker = Marker()

    lateinit var mDepartPlace: PlaceData

    lateinit var mSpinnerAdapter: EditAppointmentSpinnerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appointment)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {

        binding.departureSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                mDepartPlace = mMyPlaceList[p2]

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        binding.helperTxt.setOnTouchListener { view, motionEvent ->
            binding.editScrollView.requestDisallowInterceptTouchEvent(true)
            // ?????? ???????????? ?????????? X -> ?????? ????????? ?????? ????????? ?????? ??????
            return@setOnTouchListener false
        }

        binding.pickDateTxt.setOnClickListener {

            val dateSetListener = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                    mSelectedDateTime.set(p1, p2, p3)

                    val simpleDateFormat = SimpleDateFormat("yyyy. M. dd (E)")
                    binding.pickDateTxt.text = simpleDateFormat.format(mSelectedDateTime.time)
                    binding.pickDateTxt.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                    binding.pickDateTxt.setBackgroundResource(0)
                }
            }

            DatePickerDialog(mContext, dateSetListener, mSelectedDateTime.get(Calendar.YEAR),
                mSelectedDateTime.get(Calendar.MONTH),
                mSelectedDateTime.get(Calendar.DAY_OF_MONTH)).show()

        } // ??????

        binding.pickTimeTxt.setOnClickListener {

            val timeSetListener = object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                    mSelectedDateTime.set(Calendar.HOUR_OF_DAY, p1)
                    mSelectedDateTime.set(Calendar.MINUTE, p2)

                    val simpleDateFormat = SimpleDateFormat("a h:mm")
                    binding.pickTimeTxt.text = simpleDateFormat.format(mSelectedDateTime.time)
                    binding.pickTimeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                    binding.pickTimeTxt.setBackgroundResource(0)
                }
            }
            TimePickerDialog(mContext, timeSetListener,
                mSelectedDateTime.get(Calendar.HOUR_OF_DAY),
                mSelectedDateTime.get(Calendar.MINUTE), false).show()

        } // ??????

        binding.searchBtn.setOnClickListener {  }

        binding.reEditBtn.setOnClickListener {

            binding.appointmentEdt.text.clear()
            binding.appointmentEdt.clearFocus()

            binding.pickDateTxt.text = "?????? ??????"
            binding.pickDateTxt.setTextColor(ContextCompat.getColor(mContext, R.color.black))
            binding.pickDateTxt.setBackgroundResource(0)

            binding.pickTimeTxt.text = "?????? ??????"
            binding.pickTimeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.black))
            binding.pickTimeTxt.setBackgroundResource(0)

            binding.placeEdt.text.clear()
            binding.placeEdt.clearFocus()

            mSelectedDateTime = Calendar.getInstance()

        } // ??????

        binding.okBtn.setOnClickListener {

            if(!validation()) {
                return@setOnClickListener
            }

            val inputTitle = binding.appointmentEdt.text.toString()

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val inputDateTime = simpleDateFormat.format(mSelectedDateTime.time)

            val inputPlace = binding.placeEdt.text.toString()

            val alertDialog = AlertDialog.Builder(mContext)
            alertDialog.setTitle(binding.appointmentEdt.text.toString())

            // ????????? ?????? ?????? - using Html ??????, ??? ???????????? ?????? ????????? ?????????
            alertDialog.setMessage("${binding.pickDateTxt.text} ${binding.pickTimeTxt.text}, " +
                    "${binding.placeEdt.text} ?????? ????????? ??? ??????????")
            alertDialog.setPositiveButton("?????????", DialogInterface.OnClickListener { dialogInterface, i ->

                apiService.postRequestAppointment(
                    inputTitle, inputDateTime, mDepartPlace.name, mDepartPlace.latitude,
                    mDepartPlace.longitude, inputPlace, mSelectedLat, mSelectedLng).enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if(response.isSuccessful) {
                            val basicResponse = response.body()!!
                            Toast.makeText(mContext, "????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            val basicResponse = response.errorBody()!!.string()
                            Log.d("??????", JSONObject(basicResponse).toString())
                        }

                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    }

                })

            })
            alertDialog.setNegativeButton("?????????", null)
            alertDialog.show()


        } // ?????? ???

    }

    override fun setValues() {

        barTitleTxt.text = "?????? ????????????"

        mSpinnerAdapter = EditAppointmentSpinnerAdapter(mContext, R.layout.departure_list_item, mMyPlaceList)
        binding.departureSpinner.adapter = mSpinnerAdapter

        apiService.getRequestMyPlace().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful) {
                    val basicResponse = response.body()!!

                    mMyPlaceList.clear()
                    mMyPlaceList.addAll(basicResponse.data.places)

                    mSpinnerAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
            }

        })

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

                selectedMarker.position = LatLng(mSelectedLat, mSelectedLng)
                selectedMarker.map = it

            }

        }
    }

    fun validation(): Boolean {

        if(binding.appointmentEdt.text.toString() == "") {
            Toast.makeText(mContext, "????????? ??????????????????.", Toast.LENGTH_SHORT).show()
//            binding.appointmentEdt.setTextColor(ContextCompat.getColor(mContext, R.color.datetime_red))
            binding.appointmentEdt.setBackgroundResource(R.drawable.underline_red_rect)
            return false
        }
        if(binding.pickDateTxt.text == "?????? ??????") {
            Toast.makeText(mContext, "????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            binding.pickDateTxt.setTextColor(ContextCompat.getColor(mContext, R.color.datetime_red))
            binding.pickDateTxt.setBackgroundResource(R.drawable.underline_red_rect)
            return false
        }
        if(binding.pickTimeTxt.text == "?????? ??????") {
            Toast.makeText(mContext, "????????? ??????????????????.", Toast.LENGTH_SHORT).show()
            binding.pickTimeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.datetime_red))
            binding.pickTimeTxt.setBackgroundResource(R.drawable.underline_red_rect)
            return false
        }
        if(binding.placeEdt.text.toString() == "") {
            Toast.makeText(mContext, "????????? ??????????????????.", Toast.LENGTH_SHORT).show()
//            binding.placeEdt.setTextColor(ContextCompat.getColor(mContext, R.color.datetime_red))
            binding.placeEdt.setBackgroundResource(R.drawable.underline_red_rect)
            return false
        }
        return true
    }

}