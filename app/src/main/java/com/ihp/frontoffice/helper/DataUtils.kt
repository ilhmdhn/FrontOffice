package com.ihp.frontoffice.helper

import android.util.Log
import com.google.gson.Gson
import com.ihp.frontoffice.data.entity.Room

class DataUtils {
    fun filterBilled(data: ArrayList<Room>): ArrayList<Room>{
        val listData = ArrayList<Room>()
        val gson = Gson()
        val json = gson.toJson(data)
        listData.addAll(data.filter{it.statusPrinter == "0"})
        return listData
    }
}