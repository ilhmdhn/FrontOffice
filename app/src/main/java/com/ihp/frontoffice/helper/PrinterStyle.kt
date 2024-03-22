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
        val amountResult = StringBuilder()
        amountResult.append("[L][R]Jumlah [R]${utils.getCurrency(data.amount.toLong())}\n")
        amountResult.append("[L][R]Service FnB ${data.fnbServicePercent}%[R]${utils.getCurrency(data.fnbServiceValue.toLong())}\n")
        amountResult.append("[L][R]Service Room ${data.roomServicePercent}%[R]${utils.getCurrency(data.roomServiceValue.toLong())}\n")
        amountResult.append("[L][R]Pajak FnB ${data.fnbTaxPercent}%[R]${utils.getCurrency(data.fnbTaxValue.toLong())}\n")
        amountResult.append("[L][R]Pajak Room ${data.roomTaxPercent}%[R]${utils.getCurrency(data.roomTaxValue.toLong())}\n")
        amountResult.append(data.otherCharge)
        amountResult.append("[R]-------------\n")
        amountResult.append("[L][R]Total [R]${utils.getCurrency(data.total.toLong())}\n")
        amountResult.append(data.transfer)
        amountResult.append(data.downPayment)
        amountResult.append("[R]-------------\n")
        amountResult.append("[L][R]Jumlah Bersih [R]${utils.getCurrency(data.netAmount.toLong())}\n")
        return amountResult
    }

    fun serviceTaxDua(data: ServiceTaxModel): StringBuilder{
        val amountResult = StringBuilder()
        val totalServicePercent = data.fnbServicePercent + data.roomServicePercent
        val totalTaxPercent = data.fnbTaxPercent + data.roomTaxPercent
        val totalServiceValue = data.fnbServiceValue + data.roomServiceValue
        val totalTaxValue = data.fnbTaxValue + data.roomTaxValue

        amountResult.append("[L][R]Jumlah [R]${utils.getCurrency(data.amount.toLong())}\n")
        amountResult.append("[L][R]Service ${totalServicePercent}%[R]${utils.getCurrency(totalServiceValue.toLong())}\n")
        amountResult.append("[L][R]Pajak ${totalTaxPercent}%[R]${utils.getCurrency(totalTaxValue.toLong())}\n")
        amountResult.append(data.otherCharge)
        amountResult.append("[R]-------------\n")
        amountResult.append("[L][R]Total [R]${utils.getCurrency(data.total.toLong())}\n")
        amountResult.append(data.transfer)
        amountResult.append(data.downPayment)
        amountResult.append("[R]-------------\n")
        amountResult.append("[L][R]Jumlah Bersih [R]${utils.getCurrency(data.netAmount.toLong())}\n")

        return amountResult
    }

    fun serviceTaxTiga(data: ServiceTaxModel): StringBuilder{
        val amountResult = StringBuilder()
        val totalServiceValue = data.fnbServiceValue + data.roomServiceValue
        val totalTaxValue = data.fnbTaxValue + data.roomTaxValue
        amountResult.append("[L][R]Jumlah [R]${utils.getCurrency(data.amount.toLong())}\n")
        amountResult.append("[L][R]Jumlah Service [R]${utils.getCurrency(totalServiceValue.toLong())}\n")
        amountResult.append("[L][R]Jumlah Pajak [R]${utils.getCurrency(totalTaxValue.toLong())}\n")
        amountResult.append(data.otherCharge)
        amountResult.append("[R]-------------\n")
        amountResult.append("[L][R]Total [R]${utils.getCurrency(data.total.toLong())}\n")
        amountResult.append(data.transfer)
        amountResult.append(data.downPayment)
        amountResult.append("[R]-------------\n")
        amountResult.append("[L][R]Jumlah Bersih [R]${utils.getCurrency(data.netAmount.toLong())}\n")
        return amountResult
    }

    fun serviceTaxEmpat(data: ServiceTaxModel): StringBuilder{
        val amountResult = StringBuilder()
        if(data.transfer.isNotEmpty() || data.downPayment.isNotEmpty()){
            amountResult.append("[L][R]Total [R]${utils.getCurrency(data.total.toLong())}\n")
            amountResult.append(data.transfer)
            amountResult.append(data.downPayment)
            amountResult.append("[R]-------------\n")
        }
        amountResult.append("[L][R]Jumlah Bersih [R]${utils.getCurrency(data.netAmount.toLong())}\n")
        return amountResult
    }

    fun serviceTaxLima(data: ServiceTaxModel): StringBuilder{
        val amountResult = StringBuilder()
        if(data.transfer.isNotEmpty() || data.downPayment.isNotEmpty()){
            amountResult.append("[L][R]Total [R]${utils.getCurrency(data.total.toLong())}\n")
            amountResult.append(data.transfer)
            amountResult.append(data.downPayment)
            amountResult.append("[R]-------------\n")
        }
        amountResult.append("[L][R]Jumlah Bersih [R]${utils.getCurrency(data.netAmount.toLong())}\n")
        amountResult.append("[L][R](Termasuk jasa pelayanan dan pajak)\n")
        return amountResult
    }

    fun serviceTaxEnam(data: ServiceTaxModel): StringBuilder{
        val amountResult = StringBuilder()
        amountResult.append("[L][R]Jumlah [R]${utils.getCurrency(data.amount.toLong())}\n")
        amountResult.append("[L][R]Service FnB ${data.fnbServicePercent}%[R]${utils.getCurrency(data.fnbServiceValue.toLong())}\n")
        amountResult.append("[L][R]Service Room ${data.roomServicePercent}%[R]${utils.getCurrency(data.roomServiceValue.toLong())}\n")
        amountResult.append(data.otherCharge)
        amountResult.append("[R]-------------\n")
        amountResult.append("[L][R]Total [R]${utils.getCurrency(data.total.toLong())}\n")
        amountResult.append(data.transfer)
        amountResult.append(data.downPayment)
        amountResult.append("[R]-------------\n")
        amountResult.append("[L][R]Jumlah Bersih [R]${utils.getCurrency(data.netAmount.toLong())}\n")
        return amountResult
    }

    fun serviceTaxTuju(data: ServiceTaxModel): StringBuilder{
        val amountResult = StringBuilder()
        amountResult.append("[L][R]Jumlah [R]${utils.getCurrency(data.amount.toLong())}\n")
        amountResult.append("[L][R]Pajak FnB ${data.fnbTaxPercent}%[R]${utils.getCurrency(data.fnbTaxValue.toLong())}\n")
        amountResult.append("[L][R]Pajak Room ${data.roomTaxPercent}%[R]${utils.getCurrency(data.roomTaxValue.toLong())}\n")
        amountResult.append(data.otherCharge)
        amountResult.append("[R]-------------\n")
        amountResult.append("[L][R]Total [R]${utils.getCurrency(data.total.toLong())}\n")
        amountResult.append(data.transfer)
        amountResult.append(data.downPayment)
        amountResult.append("[R]-------------\n")
        amountResult.append("[L][R]Jumlah Bersih [R]${utils.getCurrency(data.netAmount.toLong())}\n")
        return amountResult
    }
}