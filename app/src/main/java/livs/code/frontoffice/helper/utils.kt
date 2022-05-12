package livs.code.frontoffice.helper

import java.text.NumberFormat
import java.util.*

object utils {
    fun getCurrency(nilai: Int): String{
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(nilai).toString()
    }
}