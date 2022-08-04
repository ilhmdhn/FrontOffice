package com.ihp.frontoffice

import android.app.Application
import android.content.Context
import android.util.Log
import com.ihp.frontoffice.data.local.FrontOfficeDatabase
import android.widget.Toast
import com.ihp.frontoffice.data.entity.User
import com.ihp.frontoffice.helper.NetworkUtil
import java.lang.Exception

class MyApp : Application() {

    lateinit var baseUrl: String
    lateinit var userFo: User

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
        if ((wifiStatusNya == 0) or (wifiStatusNya == 2)) {
            return false
        } else {
            return true
        }
    }

    companion object {
        //init all global data
        @JvmField
        var frontOfficeDatabase: FrontOfficeDatabase? = null
        private val TAG = "MyApp"
    }
}