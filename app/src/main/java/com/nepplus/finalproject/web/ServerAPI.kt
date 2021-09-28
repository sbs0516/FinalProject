package com.nepplus.finalproject.web

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.nepplus.finalproject.utils.ContextUtil
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServerAPI {

    companion object {

        private val hostUrl = "http://3.36.146.152"

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

                val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
                retrofit =Retrofit.Builder().baseUrl(hostUrl).client(myClient)
                    .addConverterFactory(GsonConverterFactory.create(gson)).build()
            }
            return retrofit!!
        }
    }
}