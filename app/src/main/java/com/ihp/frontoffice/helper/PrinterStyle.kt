package com.ihp.frontoffice.helper
import android.content.Context
import com.ihp.frontoffice.data.model.ServiceTaxModel

object PrinterStyle {

    fun serviceTaxStyle(context: Context, data: ServiceTaxModel): StringBuilder{
        val resultServiceTax: StringBuilder
        val preferencesData = PreferencesData(context)
        val styleCode = preferencesData.getServiceStyle()
        when(styleCode){
            1 -> resultServiceTax = serviceTaxSatu(data)
            2 -> resultServiceTax = serviceTaxDua(data)
            3 -> resultServiceTax = serviceTaxTiga(data)
            4 -> resultServiceTax = serviceTaxEmpat(data)
            5 -> resultServiceTax = serviceTaxLima(data)
            6 -> resultServiceTax = serviceTaxEnam(data)
            7 -> resultServiceTax = serviceTaxTuju(data)
            else -> resultServiceTax = serviceTaxTiga(data)
        }
        return resultServiceTax
    }

    fun serviceTaxSatu(data: ServiceTaxModel): StringBuilder{
        val taxServiceResult = StringBuilder()
        taxServiceResult.append("[L][R]Service FnB ${data.fnbServicePercent}% [R]${utils.getCurrency(data.fnbServiceValue.toLong())}\n")
        taxServiceResult.append("[L][R]Service Room ${data.roomServicePercent}% [R]${utils.getCurrency(data.roomServiceValue.toLong())}\n")
        taxServiceResult.append("[L][R]Pajak FnB ${data.fnbTaxPercent}% [R]${utils.getCurrency(data.fnbTaxValue.toLong())}\n")
        taxServiceResult.append("[L][R]Pajak Room ${data.roomTaxPercent}% [R]${utils.getCurrency(data.roomTaxValue.toLong())}\n")
        return taxServiceResult
    }

    fun serviceTaxDua(data: ServiceTaxModel): StringBuilder{
        val taxServiceResult = StringBuilder()
        val totalServicePercent = data.fnbServicePercent + data.roomServicePercent
        val totalTaxPercent = data.fnbTaxPercent + data.roomTaxPercent
        val totalServiceValue = data.fnbServiceValue + data.roomServiceValue
        val totalTaxValue = data.fnbTaxValue + data.roomTaxValue

        taxServiceResult.append("[L][R]Jumlah Service ${totalServicePercent}% [R]${utils.getCurrency(totalServiceValue.toLong())}\n")
        taxServiceResult.append("[L][R]Jumlah Pajak ${totalTaxPercent}% [R]${utils.getCurrency(totalTaxValue.toLong())}\n")
        return taxServiceResult
    }

    fun serviceTaxTiga(data: ServiceTaxModel): StringBuilder{
        val taxServiceResult = StringBuilder()
        val totalServiceValue = data.fnbServiceValue + data.roomServiceValue
        val totalTaxValue = data.fnbTaxValue + data.roomTaxValue

        taxServiceResult.append("[L][R]Jumlah Service [R]${utils.getCurrency(totalServiceValue.toLong())}\n")
        taxServiceResult.append("[L][R]Jumlah Pajak [R]${utils.getCurrency(totalTaxValue.toLong())}\n")
        return taxServiceResult
    }

    fun serviceTaxEmpat(data: ServiceTaxModel): StringBuilder{
        val taxServiceResult = StringBuilder()
        taxServiceResult.append("\n")
        return taxServiceResult
    }

    fun serviceTaxLima(data: ServiceTaxModel): StringBuilder{
        val taxServiceResult = StringBuilder()
        taxServiceResult.append("[C]Rincian diatas termasuk jasa pelayanan dan pajak\n")
        return taxServiceResult
    }

    fun serviceTaxEnam(data: ServiceTaxModel): StringBuilder{
        val taxServiceResult = StringBuilder()
        taxServiceResult.append("[L][R]Service FnB ${data.fnbServicePercent}% [R]${utils.getCurrency(data.fnbServiceValue.toLong())}\n")
        taxServiceResult.append("[L][R]Service Room ${data.roomServicePercent}% [R]${utils.getCurrency(data.roomServiceValue.toLong())}\n")
        return taxServiceResult
    }

    fun serviceTaxTuju(data: ServiceTaxModel): StringBuilder{
        val taxServiceResult = StringBuilder()
        taxServiceResult.append("[L][R]Pajak FnB ${data.fnbTaxPercent}% [R]${utils.getCurrency(data.fnbTaxValue.toLong())}\n")
        taxServiceResult.append("[L][R]Pajak Room ${data.roomTaxPercent}% [R]${utils.getCurrency(data.roomTaxValue.toLong())}\n")
        return taxServiceResult
    }
}