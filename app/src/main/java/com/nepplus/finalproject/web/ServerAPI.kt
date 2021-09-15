package com.nepplus.finalproject.web

import android.content.Context
import android.util.Log
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
                            .addHeader("X-Http-Token", ContextUtil.getToken(context)).build()

                        Log.d("서버 토큰값", ContextUtil.getToken(context))
                        proceed(newRequest)
                    }
                }
                val myClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

                retrofit =Retrofit.Builder().baseUrl(hostUrl).client(myClient)
                    .addConverterFactory(GsonConverterFactory.create()).build()
            }
            return retrofit!!
        }
    }
}