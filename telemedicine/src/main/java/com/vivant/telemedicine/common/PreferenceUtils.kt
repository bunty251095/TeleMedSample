package com.vivant.telemedicine.common

import android.content.SharedPreferences

class PreferenceUtils( private val sharedPreference: SharedPreferences ) {

    fun storePreference(key:String,value: String ) {
        sharedPreference.edit().putString(key,value).apply()
    }

    fun getPreference(key: String): String {
        return sharedPreference.getString(key, "")!!
    }

    fun storeBooleanPreference(key:String,value: Boolean ) {
        sharedPreference.edit().putBoolean(key,value).apply()
    }

    fun getBooleanPreference(key: String): Boolean {
        return sharedPreference.getBoolean(key, false)
    }

}