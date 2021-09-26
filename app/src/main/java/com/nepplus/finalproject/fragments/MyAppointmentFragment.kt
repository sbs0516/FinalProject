package com.nepplus.finalproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.nepplus.finalproject.R
import com.nepplus.finalproject.databinding.FragmentMyAppointmentBinding

class MyAppointmentFragment: BaseFragment() {

    lateinit var binding: FragmentMyAppointmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_appointment, container, false)
        return binding.root
    }

    override fun setupEvents() {

    }

    override fun setValues() {

    }
}