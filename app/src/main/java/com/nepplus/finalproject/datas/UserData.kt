package com.nepplus.finalproject.datas

import com.google.gson.annotations.SerializedName

class UserData(
    var id: Int,
    var provider: String,
    var email: String,
    @SerializedName("ready_minute")
    var readyMinute: Int,
    var nick_name: String,
    @SerializedName("profile_img")
    var profileImg: String) {
}