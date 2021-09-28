package com.nepplus.finalproject.web

import com.nepplus.finalproject.datas.BasicResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ServerAPIService {

    @FormUrlEncoded
    @PUT("/user")
    fun putRequestSignUp(
        @Field("email") email: String,
        @Field("password") pw: String,
        @Field("nick_name") nickname: String): Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user")
    fun postRequestSignIn(
        @Field("email") email: String,
        @Field("password") pw: String): Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user/social")
    fun postRequestSocialLogin(
        @Field("provider") provider: String,
        @Field("uid") uid: String,
        @Field("nick_name") name: String): Call<BasicResponse>

    @FormUrlEncoded
    @POST("/appointment")
    fun postRequestAppointment(
        @Field("title") title: String,
        @Field("datetime") dateTime: String,
        @Field("start_place") startPlace: String,
        @Field("start_latitude") startLatitude: Double,
        @Field("start_longitude") startLongitude: Double,
        @Field("place") place: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double,
//        @Field("friend_list") friendList: String
        ): Call<BasicResponse>

    @GET("/user")
    fun getRequestMyInfo(): Call<BasicResponse>

    @GET("/user/place")
    fun getRequestMyPlace(): Call<BasicResponse>

    @GET("/appointment")
    fun getRequestMyAppointment(): Call<BasicResponse>

    @FormUrlEncoded
    @PATCH("/user")
    fun patchRequestNickName(
        @Field("field") field: String,
        @Field("value") value: String): Call<BasicResponse>

    @FormUrlEncoded
    @PATCH("/user")
    fun patchRequestReadyTime(
        @Field("field") field: String,
        @Field("value") value: String): Call<BasicResponse>

    @Multipart
    @PUT("/user/image")
    fun postRequestProfileImg(@Part profile_image: MultipartBody.Part): Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user/place")
    fun postRequestMyDeparture(
        @Field("name") name: String,
        @Field("latitude") latitude: Double,
        @Field("longitude") longitude: Double,
        @Field("is_primary") isPrimary: Boolean): Call<BasicResponse>

    @FormUrlEncoded
    @PATCH("/user/password")
    fun patchRequestChangePassword(
        @Field("current_password") current_password: String,
        @Field("new_password") new_password: String): Call<BasicResponse>

    @DELETE("/user")
    fun deleteRequestMember(
        @Query("text") agree: String): Call<BasicResponse>

    @GET("/user/check")
    fun getRequestDuplicationCheck(
        @Query("type") type: String,
        @Query("value") value: String): Call<BasicResponse>

}