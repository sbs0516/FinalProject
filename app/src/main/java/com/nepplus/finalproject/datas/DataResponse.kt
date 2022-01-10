package com.nepplus.finalproject.datas

class DataResponse(
    var user: UserData,
    var token: String,
    var places: ArrayList<PlaceData>,
    var appointment: AppointmentData,
    var appointments: ArrayList<AppointmentData>) {
}