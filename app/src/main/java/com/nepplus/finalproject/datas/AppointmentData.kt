package com.nepplus.finalproject.datas

import android.util.Log
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AppointmentData(
    var id: Int,
    @SerializedName("user_id")
    var userId: Int,
    var title: String,
    var datetime: Date,
    @SerializedName("start_place")
    var startPlace: String,
    @SerializedName("start_latitude")
    var startLat: Double,
    @SerializedName("start_longitude")
    var startLng: Double,
    var place: String,
    var latitude: Double,
    var longitude: Double,
    var user: UserData,
    @SerializedName("invited_friends")
    var invitedFriends: ArrayList<UserData>,
    @SerializedName("invited_appointments")
    var invitedAppointments: ArrayList<AppointmentData>
) {


}