package com.nepplus.finalproject.datas

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class UserData(
    var id: Int,
    var provider: String,
    var email: String,
    @SerializedName("ready_minute") var readyMinute: Int,
    var nick_name: String,
    @SerializedName("profile_img") var profileImg: String,
    @SerializedName("arrived_at") var arrivedAt: Date?,
    @SerializedName("nick_name") var nickName: String): Serializable {
}