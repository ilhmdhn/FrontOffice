package com.ihp.frontoffice.helper

import com.ihp.frontoffice.data.entity.Room
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

object utils {
    fun getCurrency(nilai: Long?): String{
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        if(nilai == null){
            return  "null"
        }
        formatRupiah.maximumFractionDigits = 0 //hilangkan 2 koma paling belakang
        formatRupiah.format(nilai).replace(" ".toRegex(), "")
        return formatRupiah.format(nilai).replace("Rp".toRegex(), "")
//        return formatRupiah.format(nilai).toString()
    }

    fun filterBilled(arrayListData: ArrayList<Room>): ArrayList<Room>{
        val listData = ArrayList<Room>()
        listData.addAll(arrayListData.filter{it.statusPrinter == "0"})
        return listData
    }
}