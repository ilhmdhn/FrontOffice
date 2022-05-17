package livs.code.frontoffice.helper

import java.text.NumberFormat
import java.util.*

object utils {
    fun getCurrency(nilai: Int?): String{
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        if(nilai == null){
            return  "0"
        }
        return formatRupiah.format(nilai).toString()
    }
}