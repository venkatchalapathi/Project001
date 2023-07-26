package com.example.project001

import android.content.Context
import android.content.SharedPreferences

object PreferenceManager {

    private val USERNAME = "username"
    private val USERTYPE = "usertype"
    private val PREF = "PROJECT001_PREFERENCES"

    fun saveUser(context: Context,userName:String,userType:String){
        val preference = context.getSharedPreferences(PREF,Context.MODE_PRIVATE)
        val editor = preference.edit()

        editor.putString(USERNAME,userName)
        editor.putString(USERTYPE,userType)
        editor.apply()
    }

    fun getUserName(context: Context) : String{
        val preference = context.getSharedPreferences(PREF,Context.MODE_PRIVATE)
        return preference.getString(USERNAME,"")!!
    }


    fun getUserType(context: Context) : String{
        val preference = context.getSharedPreferences(PREF,Context.MODE_PRIVATE)
        return preference.getString(USERTYPE,"")!!
    }


}