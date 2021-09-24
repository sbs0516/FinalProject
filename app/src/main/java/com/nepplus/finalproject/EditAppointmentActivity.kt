package com.nepplus.finalproject

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.nepplus.finalproject.databinding.ActivityEditAppointmentBinding
import com.nepplus.finalproject.datas.BasicResponse
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appointment)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {

        val editTextOriginBack = binding.emptyEdt.background

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

        } // 작성

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

        } // 작성

        binding.searchBtn.setOnClickListener {  }

        binding.reEditBtn.setOnClickListener {

            binding.appointmentEdt.text.clear()
            binding.appointmentEdt.clearFocus()

            binding.pickDateTxt.text = "날짜 선택"
            binding.pickDateTxt.setTextColor(ContextCompat.getColor(mContext, R.color.black))
            binding.pickDateTxt.setBackgroundResource(0)

            binding.pickTimeTxt.text = "시간 선택"
            binding.pickTimeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.black))
            binding.pickTimeTxt.setBackgroundResource(0)

            binding.placeEdt.text.clear()
            binding.placeEdt.clearFocus()

            mSelectedDateTime = Calendar.getInstance()

        } // 작성

        binding.okBtn.setOnClickListener {

            if(!validation()) {
                return@setOnClickListener
            }

            val inputTitle = binding.appointmentEdt.text.toString()

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val inputDateTime = simpleDateFormat.format(mSelectedDateTime.time)

            val inputPlace = binding.placeEdt.text.toString()

            // lat 과 long 은 임시 하드코딩
            mSelectedLat = 37.111
            mSelectedLng = 128.111

            val alertDialog = AlertDialog.Builder(mContext)
            alertDialog.setTitle(binding.appointmentEdt.text.toString())

            // 메시지 색깔 변경 - using Html 인데, 잘 모르니까 일단 체크만 해두자
            alertDialog.setMessage("${binding.pickDateTxt.text} ${binding.pickTimeTxt.text}, " +
                    "${binding.placeEdt.text} 에서 만나는 게 맞나요?")
            alertDialog.setPositiveButton("맞아요", DialogInterface.OnClickListener { dialogInterface, i ->

                apiService.postRequestAppointment(
                    inputTitle, inputDateTime, inputPlace, mSelectedLat, mSelectedLng).enqueue(object : Callback<BasicResponse> {
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if(response.isSuccessful) {
                            val basicResponse = response.body()!!
                            Toast.makeText(mContext, "약속을 등록했습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            val basicResponse = response.errorBody()!!.string()
                            Log.d("에러", JSONObject(basicResponse).toString())
                        }

                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    }

                })

            })
            alertDialog.setNegativeButton("틀려요", null)
            alertDialog.show()


        } // 작성 중

    }

    override fun setValues() {

    }

    fun validation(): Boolean {

        if(binding.appointmentEdt.text.toString() == "") {
            Toast.makeText(mContext, "제목을 작성해주세요.", Toast.LENGTH_SHORT).show()
//            binding.appointmentEdt.setTextColor(ContextCompat.getColor(mContext, R.color.datetime_red))
            binding.appointmentEdt.setBackgroundResource(R.drawable.underline_red_rect)
            return false
        }
        if(binding.pickDateTxt.text == "날짜 선택") {
            Toast.makeText(mContext, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
            binding.pickDateTxt.setTextColor(ContextCompat.getColor(mContext, R.color.datetime_red))
            binding.pickDateTxt.setBackgroundResource(R.drawable.underline_red_rect)
            return false
        }
        if(binding.pickTimeTxt.text == "시간 선택") {
            Toast.makeText(mContext, "시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
            binding.pickTimeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.datetime_red))
            binding.pickTimeTxt.setBackgroundResource(R.drawable.underline_red_rect)
            return false
        }
        if(binding.placeEdt.text.toString() == "") {
            Toast.makeText(mContext, "장소를 등록해주세요.", Toast.LENGTH_SHORT).show()
//            binding.placeEdt.setTextColor(ContextCompat.getColor(mContext, R.color.datetime_red))
            binding.placeEdt.setBackgroundResource(R.drawable.underline_red_rect)
            return false
        }
        return true
    }

}