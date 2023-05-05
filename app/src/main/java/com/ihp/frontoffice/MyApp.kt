package com.ihp.frontoffice

import android.app.Application
import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.ihp.frontoffice.data.entity.User
import com.ihp.frontoffice.data.local.FrontOfficeDatabase
import com.ihp.frontoffice.helper.NetworkUtil

class MyApp : Application() {

    lateinit var baseUrl: String
    var userFo: User = User()

    override fun onCreate() {
        super.onCreate()
        frontOfficeDatabase = FrontOfficeDatabase.getInstance(this.applicationContext)
        setBaseUrl()
        setUserLogin()
        if (!isWifiConnect(applicationContext)) {
            Toast.makeText(applicationContext, "Tidak ada koneksi wifi ", Toast.LENGTH_LONG).show()
        }
    }

    private fun setUserLogin() {
        try {
            userFo = frontOfficeDatabase!!.userDao().userLogin
        } catch (x: Exception) {
            Log.e(TAG, x.message!!)
        }
    }

    fun setBaseUrl() {
        baseUrl = try {
            val cfg = frontOfficeDatabase!!.configDao().lastConfig
            cfg.baseURL
        } catch (x: Exception) {
            Log.e(TAG, x.message!!)
            Toast.makeText(this.applicationContext,"Konfigurasi server belum ada",Toast.LENGTH_SHORT).show()
            ""
        }
    }

    override fun toString(): String {
        return "FrontOffice"
    }

    fun isWifiConnect(ctx: Context?): Boolean {
        val wifiStatusNya = NetworkUtil.getConnectivityStatusString(ctx)
        return !((wifiStatusNya == 0) or (wifiStatusNya == 2))
    }

    fun getDeviceName(): String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else capitalize(manufacturer) + " " + model
    }
    private fun capitalize(str: String): String? {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true
        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(c.uppercaseChar())
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }
        return phrase.toString()
    }
    companion object {
        //init all global data
        @JvmField
        var frontOfficeDatabase: FrontOfficeDatabase? = null
        private val TAG = "MyApp"
    }
}