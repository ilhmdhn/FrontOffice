package com.ihp.frontoffice.helper

import android.content.Context
import android.content.SharedPreferences
import com.ihp.frontoffice.R

class PreferencesData(context: Context) {
    var sharedPref : SharedPreferences = context.getSharedPreferences("serviceTaxStyle",Context.MODE_PRIVATE)
    val editor = sharedPref.edit()

    fun setServiceStyle(code: Int){
        editor.putInt("serviceTaxStyle", code)
        editor.apply()
    }

    fun getServiceStyle(): Int{
        val styleCode = sharedPref.getInt("serviceTaxStyle", 3)
        return styleCode
    }
}