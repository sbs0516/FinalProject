package com.nepplus.finalproject.utils

import android.content.Context

class ContextUtil {

    companion object {

        private val prefName = "FinalProjectPref"

        private val TOKEN = "token"

        fun setToken(context: Context, token: String) {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putString(TOKEN, token)
        }
        fun getToken(context: Context): String {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getString(TOKEN, "")!!
        }

    }

}