package com.nepplus.finalproject

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "0733d4b66757d16104addf2378f4719d")
    }
}