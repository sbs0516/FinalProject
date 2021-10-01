package com.nepplus.finalproject.web

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.nepplus.finalproject.utils.ContextUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class ServerAPI {

    companion object {

        private val hostUrl = "https://keepthetime.xyz/api/docs/"

        private var retrofit: Retrofit? = null

        fun getRetrofit(context: Context): Retrofit {
            if(retrofit == null) {

                val interceptor = Interceptor {
                    with(it){
                        val newRequest = request().newBuilder()
                            .addHeader("X-Http-Token", ContextUtil.getToken(context))
//                            .addHeader("Connection", "close")
                            .build()

                        Log.d("서버 토큰값", ContextUtil.getToken(context))
                        proceed(newRequest)
                    }
                }
                val myClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

                // 기존 gson 에 시차 보정기를 보조 도구로 채택
                val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .registerTypeAdapter(Date::class.java, DateDeserializer()).create()

                retrofit =Retrofit.Builder().baseUrl(hostUrl).client(myClient)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build()
            }
            return retrofit!!
        }
    }
}