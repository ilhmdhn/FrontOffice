package com.ihp.frontoffice.helper

import com.ihp.frontoffice.data.entity.Room

class DataUtils {
    fun filterBilled(data: ArrayList<Room>): ArrayList<Room>{
        val listData = ArrayList<Room>()
        listData.addAll(data.filter{it.statusPrinter == "0"})
        return listData
    }
}