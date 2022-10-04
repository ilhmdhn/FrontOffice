package com.ihp.frontoffice.helper

import java.text.NumberFormat
import java.util.*

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
}