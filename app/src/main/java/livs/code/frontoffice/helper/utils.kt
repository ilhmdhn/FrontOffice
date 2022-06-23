package livs.code.frontoffice.helper

import java.text.NumberFormat
import java.util.*

object utils {
    fun getCurrency(nilai: Long?): String{
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        if(nilai == null){
            return  "null"
        }
        return formatRupiah.format(nilai).toString()
    }
}