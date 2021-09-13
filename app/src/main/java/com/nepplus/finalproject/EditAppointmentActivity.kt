package com.nepplus.finalproject

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.nepplus.finalproject.databinding.ActivityEditAppointmentBinding
import java.text.SimpleDateFormat
import java.util.*

class EditAppointmentActivity : BaseActivity() {

    lateinit var binding:ActivityEditAppointmentBinding

    val mSelectedDateTime = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_appointment)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {

        binding.pickDateTxt.setOnClickListener {

            val dateSetListener = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                    mSelectedDateTime.set(p1, p2, p3)

                    val simpleDateFormat = SimpleDateFormat("yyyy. M. dd (E)")
                    binding.pickDateTxt.text = simpleDateFormat.format(mSelectedDateTime.time)
                    binding.pickDateTxt.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                }
            }
            val datePickerDialog = DatePickerDialog(mContext, dateSetListener,
                mSelectedDateTime.get(Calendar.YEAR),
                mSelectedDateTime.get(Calendar.MONTH),
                mSelectedDateTime.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()

        } // 완료

        binding.pickTimeTxt.setOnClickListener {

            val timeSetListener = object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                    mSelectedDateTime.set(Calendar.HOUR_OF_DAY, p1)
                    mSelectedDateTime.set(Calendar.MINUTE, p2)

                    val simpleDateFormat = SimpleDateFormat("a h:mm")
                    binding.pickTimeTxt.text = simpleDateFormat.format(mSelectedDateTime.time)
                    binding.pickTimeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                }
            }
            TimePickerDialog(mContext, timeSetListener,
                mSelectedDateTime.get(Calendar.HOUR_OF_DAY),
                mSelectedDateTime.get(Calendar.MINUTE), false).show()

        } // 완료

        binding.searchBtn.setOnClickListener {  }

        binding.reEditBtn.setOnClickListener {

            binding.appointmentEdt.text.clear()
            binding.pickDateTxt.text = "날짜 선택"
            binding.pickDateTxt.setTextColor(ContextCompat.getColor(mContext, R.color.black))
            binding.pickTimeTxt.text = "시간 선택"
            binding.pickTimeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.black))
            binding.placeEdt.text.clear()

        } // 완료

        binding.okBtn.setOnClickListener {

            if(binding.pickDateTxt.text == "날짜 선택") {
                Toast.makeText(mContext, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
                binding.pickDateTxt.setTextColor(ContextCompat.getColor(mContext, R.color.datetime_red))
                return@setOnClickListener
            }
            if(binding.pickTimeTxt.text == "시간 선택") {
                Toast.makeText(mContext, "시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
                binding.pickTimeTxt.setTextColor(ContextCompat.getColor(mContext, R.color.datetime_red))
                return@setOnClickListener
            }

            val alertDialog = AlertDialog.Builder(mContext)
            alertDialog.setTitle(binding.appointmentEdt.text.toString())
            alertDialog.setMessage("${binding.pickDateTxt.text.toString()} ${binding.pickTimeTxt.text}, " +
                    "${binding.placeEdt.text} 에서 만나는 게 맞나요?")
            alertDialog.setPositiveButton("맞아요", DialogInterface.OnClickListener { dialogInterface, i ->

                finish()
            })
            alertDialog.setNegativeButton("틀려요", null)
            alertDialog.show()

        }

    }

    override fun setValues() {

    }
}