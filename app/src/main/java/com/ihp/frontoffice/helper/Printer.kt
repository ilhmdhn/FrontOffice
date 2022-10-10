package com.ihp.frontoffice.helper

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.remote.respons.PrintBillDataResponse
import com.ihp.frontoffice.data.remote.respons.PrintInvoiceDataResponse
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class Printer {

    fun printBill(billData: PrintBillDataResponse?, user: String, context: Context, isCopies: Boolean =  false): Boolean {
        try {
            val printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32)
            val selling = StringBuilder()
            val cancelOrder = StringBuilder()
            val promoFnB = StringBuilder()
            val transferRoom = StringBuilder()
            val copies = StringBuilder()
            var totalTransfer = 0
            val biayaLain = StringBuilder()
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
            val currentDate = sdf.format(Date())

            if (isCopies){
                copies.append("[R]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, getDrawable(context, R.drawable.copybnw))+"</img>\n")
            }else{
                copies.append("")
            }

            if (billData?.data?.dataInvoice?.overpax !=null && billData.data.dataInvoice.overpax > 0){
                biayaLain.append("[L][R]Overpax [R]${utils.getCurrency(billData.data.dataInvoice.overpax.toLong())}\n")
            }

            if (billData?.data?.dataInvoice?.diskonKamar !=null && billData.data.dataInvoice.diskonKamar > 0){
                biayaLain.append("[L][R]Diskon Kamar [R]${utils.getCurrency(billData.data.dataInvoice.diskonKamar.toLong())}\n")
            }

            if (billData?.data?.dataInvoice?.surchargeKamar !=null && billData.data.dataInvoice.surchargeKamar > 0){
                biayaLain.append("[L][R]Surcharge Kamar [R]${utils.getCurrency(billData.data.dataInvoice.surchargeKamar.toLong())}\n")
            }

            if (billData?.data?.dataInvoice?.diskonPenjualan !=null && billData.data.dataInvoice.diskonPenjualan > 0){
                biayaLain.append("[L][R]Diskon Penjualan [R]${utils.getCurrency(billData.data.dataInvoice.diskonPenjualan.toLong())}\n")
            }

            if (billData?.data?.dataInvoice?.voucher !=null && billData.data.dataInvoice.voucher > 0){
                biayaLain.append("[L][R]Voucher [R]${utils.getCurrency(billData.data.dataInvoice.voucher.toLong())}\n")
            }

            if (billData?.data?.dataInvoice?.chargeLain !=null && billData.data.dataInvoice.chargeLain > 0){
                biayaLain.append("[L][R]Charge Lain [R]${utils.getCurrency(billData.data.dataInvoice.chargeLain.toLong())}\n")
            }

            if (billData?.data?.orderData != null) {
                for (i: Int in billData.data.orderData.indices) {
                    selling.append("[L]${billData.data.orderData[i]?.namaItem}\n")
                    selling.append("[L]${billData.data.orderData[i]?.jumlah} x ${utils.getCurrency(billData.data.orderData[i]?.harga?.toLong())} [R] ${utils.getCurrency(billData.data.orderData[i]?.total?.toLong())}\n")
                }
            } else {
                selling.append("")
            }
            if (billData?.data?.cancelOrderData != null) {
                    cancelOrder.append("\n")
                for (i in billData.data.cancelOrderData.indices) {
                    cancelOrder.append("[L]RETURN ${billData.data.cancelOrderData[i]?.namaItem}\n")
                    cancelOrder.append("[L]${billData.data.cancelOrderData[i]?.jumlah} x ${utils.getCurrency(billData.data.cancelOrderData[i]?.harga?.toLong())}[R]${utils.getCurrency(billData.data.cancelOrderData[i]?.total?.toLong())}\n")
                }
            } else {
                cancelOrder.append("")
            }

            if (billData?.data?.promoOrderData != null) {
                promoFnB.append("\n[L]${billData.data.promoOrderData.promo}\n")
            } else {
                promoFnB.append("")
            }

            if (billData?.data?.transferListData != null){
                transferRoom.append("\n[C][R]Transfer Ruangan\n")
                for(i in billData.data.transferListData.indices){
                    transferRoom.append("[C][R]${billData.data.transferListData[i]?.room} [R]${utils.getCurrency(billData.data.transferListData[i]?.transferTotal?.toLong())}\n")
                    totalTransfer += billData.data.transferListData[i]?.transferTotal!!
                }
            }else{
                transferRoom.append("")
            }
            val jumlahBersih = totalTransfer + (if(billData?.data?.dataInvoice?.jumlahTotal==null) 0 else billData.data.dataInvoice.jumlahTotal) - (if(billData?.data?.dataInvoice?.uangMuka==null) 0 else billData.data.dataInvoice.uangMuka)
            printer.printFormattedText(
                "[L]\n" +
                        copies+
                        "[C]${billData?.data?.dataOutlet?.namaOutlet}\n" +
                        "[C]${billData?.data?.dataOutlet?.alamatOutlet}\n" +
                        "[C]${billData?.data?.dataOutlet?.kota}\n" +
                        "[C]${billData?.data?.dataOutlet?.telepon}\n" +
                        "\n[C]TAGIHAN\n\n" +
                        "[L]Ruangan : ${billData?.data?.dataRoom?.ruangan}\n" +
                        "[L]Nama    : ${billData?.data?.dataRoom?.nama}\n" +
                        "[L]Tanggal : ${billData?.data?.dataRoom?.tanggal}\n" +
                        "\n" +
                        "[L]Sewa Ruangan\n" +
                        "[L]${billData?.data?.dataRoom?.checkin} - ${billData?.data?.dataRoom?.checkout}" +
                        "[R]${utils.getCurrency(billData?.data?.dataInvoice?.sewaRuangan?.toLong())}\n" +
                        "[L]PROMO[R]${utils.getCurrency(billData?.data?.dataInvoice?.promo?.toLong())}\n" +
                        "\n" +
                        "[L]Rincian Penjualan\n" +
                        selling +
                        cancelOrder +
                        promoFnB+
                        "--------------------------------\n"+
                        "[L]Jumlah Ruangan [R]${utils.getCurrency(billData?.data?.dataInvoice?.jumlahRuangan?.toLong())}\n"+
                        "[L]Jumlah Penjualan [R]${utils.getCurrency(billData?.data?.dataInvoice?.jumlahPenjualan?.toLong())}\n"+
                        "--------------------------------\n"+
                        "[L][R]Jumlah [R]${utils.getCurrency(billData?.data?.dataInvoice?.jumlah?.toLong())}\n"+
                        "[L][R]Service [R]${utils.getCurrency(billData?.data?.dataInvoice?.jumlahService?.toLong())}\n"+
                        "[L][R]Pajak [R]${utils.getCurrency(billData?.data?.dataInvoice?.jumlahPajak?.toLong())}\n"+
                        biayaLain+
                        "[R]-------------\n"+
                        "[L][R]Total [R]${utils.getCurrency(billData?.data?.dataInvoice?.jumlahTotal?.toLong())}\n"+
                        transferRoom+
                        "\n[L][R]Uang Muka [R]${utils.getCurrency(billData?.data?.dataInvoice?.uangMuka?.toLong())}\n"+
                        "[R]-------------\n"+
                        "[L]Jumlah Bersih [R]${utils.getCurrency(jumlahBersih.toLong())}\n"+
                        "\n[L]<font size='wide'><b>Rp.${utils.getCurrency(jumlahBersih.toLong())}</b></font>\n"+
                        "\n[R]${currentDate} ${user}"
            )
            printer.disconnectPrinter()
            return true
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Mosok kene" + e.toString(), Toast.LENGTH_SHORT).show()
            Log.e("error", e.toString())
        }
        return false
    }

    fun printInvoice(invoiceData: PrintInvoiceDataResponse, context: Context, isCopies: Boolean = false): Boolean{
        try {
            val printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32)
            val selling = StringBuilder()
            val cancelOrder = StringBuilder()
            val promoFnB = StringBuilder()
            val transferRoom = StringBuilder()
            val paymentList = StringBuilder()
            var totalTransfer = 0
            val copies = StringBuilder()
            val biayaLain = StringBuilder()
            var totalPayment = 0

            if (invoiceData.data?.dataInvoice?.overpax !=null && invoiceData.data.dataInvoice.overpax > 0){
                biayaLain.append("[L][R]Overpax [R]${utils.getCurrency(invoiceData.data.dataInvoice.overpax.toLong())}\n")
            }

            if (invoiceData.data?.dataInvoice?.diskonKamar !=null && invoiceData.data.dataInvoice.diskonKamar > 0){
                biayaLain.append("[L][R]Diskon Kamar [R]${utils.getCurrency(invoiceData.data.dataInvoice.diskonKamar.toLong())}\n")
            }

            if (invoiceData.data?.dataInvoice?.surchargeKamar !=null && invoiceData.data.dataInvoice.surchargeKamar > 0){
                biayaLain.append("[L][R]Surcharge Kamar [R]${utils.getCurrency(invoiceData.data.dataInvoice.surchargeKamar.toLong())}\n")
            }

            if (invoiceData.data?.dataInvoice?.diskonPenjualan !=null && invoiceData.data.dataInvoice.diskonPenjualan > 0){
                biayaLain.append("[L][R]Diskon Penjualan [R]${utils.getCurrency(invoiceData.data.dataInvoice.diskonPenjualan.toLong())}\n")
            }

            if (invoiceData.data?.dataInvoice?.voucher !=null && invoiceData.data.dataInvoice.voucher > 0){
                biayaLain.append("[L][R]Voucher [R]${utils.getCurrency(invoiceData.data.dataInvoice.voucher.toLong())}\n")
            }

            if (invoiceData.data?.dataInvoice?.chargeLain !=null && invoiceData.data.dataInvoice.chargeLain > 0){
                biayaLain.append("[L][R]Charge Lain [R]${utils.getCurrency(invoiceData.data.dataInvoice.chargeLain.toLong())}\n")
            }

            if (isCopies){
                copies.append("[R]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, getDrawable(context, R.drawable.copybnw))+"</img>\n")
            }

            if (invoiceData.data?.orderData != null){
                for (i in invoiceData.data.orderData.indices){
                    selling.append("[L]${invoiceData.data.orderData[i]?.namaItem}\n")
                    selling.append("[L]${invoiceData.data.orderData[i]?.jumlah} x ${utils.getCurrency(invoiceData.data.orderData[i]?.harga?.toLong())} [R] ${utils.getCurrency(invoiceData.data.orderData[i]?.total?.toLong())}\n")
                }
            }else{
                    selling.append("")
            }

            if (invoiceData.data?.cancelOrderData != null){
                cancelOrder.append("\n")
                for (i in invoiceData.data.cancelOrderData.indices){
                    cancelOrder.append("[L]Return ${invoiceData.data.cancelOrderData[i]?.namaItem}\n")
                    cancelOrder.append("[L]${invoiceData.data.cancelOrderData[i]?.jumlah} x ${utils.getCurrency(invoiceData.data.cancelOrderData[i]?.harga?.toLong())} [R]${utils.getCurrency(invoiceData.data.cancelOrderData[i]?.total?.toLong())}\n")
                }
            } else{
                cancelOrder.append("")
            }

            if (invoiceData.data?.promoOrderData != null){
                promoFnB.append("\n[L]${invoiceData.data.promoOrderData.promo}\n")
            }else{
                promoFnB.append("")
            }

            if (invoiceData.data?.transferListData != null){
                transferRoom.append("\n[C]Transfer Ruangan[R]\n")
                for(i in invoiceData.data.transferListData.indices){
                    transferRoom.append("[C][R]${invoiceData.data.transferListData[i]?.room} [R]${utils.getCurrency(invoiceData.data.transferListData[i]?.transferTotal?.toLong())}\n")
                    totalTransfer += invoiceData.data.transferListData[i]?.transferTotal!!
                }
            } else{
                transferRoom.append("")
            }

            if (invoiceData.data?.paymentData != null){
                for (i in invoiceData.data.paymentData.indices){
                    paymentList.append("[R]${invoiceData.data.paymentData[i]?.namaPayment} [R]${utils.getCurrency(invoiceData.data.paymentData[i]?.total?.toLong())}\n")
                    totalPayment+=invoiceData.data.paymentData[i]?.total!!
                }
            }else{
                paymentList.append("")
            }
            val jumlahBersih = totalTransfer + (if(invoiceData.data?.dataInvoice?.jumlahTotal==null) 0 else invoiceData.data.dataInvoice.jumlahTotal)
            printer.printFormattedText(
                "[L]\n"+
                        copies+
                        "[C]${invoiceData.data?.dataOutlet?.namaOutlet}\n"+
                        "[C]${invoiceData.data?.dataOutlet?.alamatOutlet}\n"+
                        "[C]${invoiceData.data?.dataOutlet?.kota}\n"+
                        "[C]${invoiceData.data?.dataOutlet?.telepon}\n"+
                        "\n[C]INVOICE\n"+
                        "\n[L]Ruangan : ${invoiceData.data?.dataRoom?.ruangan}"+
                        "\n[L]Nama    : ${invoiceData.data?.dataRoom?.nama}"+
                        "\n[L]Tanggal : ${invoiceData.data?.dataRoom?.tanggal}"+
                        "\n\n"+
                        "[L]Rincian Penjualan\n"+
                        selling+
                        cancelOrder+
                        promoFnB+
                        "--------------------------------\n"+
                        "[L]Jumlah Ruangan [R]${utils.getCurrency(invoiceData.data?.dataInvoice?.jumlahRuangan?.toLong())}\n"+
                        "[L]Jumlah Penjualan [R]${utils.getCurrency(invoiceData.data?.dataInvoice?.jumlahPenjualan?.toLong())}\n"+
                        "--------------------------------\n"+
                        "[L][R]Jumlah [R]${utils.getCurrency(invoiceData.data?.dataInvoice?.jumlah?.toLong())}\n"+
                        "[L][R]Service [R]${utils.getCurrency(invoiceData.data?.dataInvoice?.jumlahService?.toLong())}\n"+
                        "[L][R]Pajak [R]${utils.getCurrency(invoiceData.data?.dataInvoice?.jumlahPajak?.toLong())}\n"+
                        biayaLain+
                        "[R]-------------\n"+
                        "[L][R]Total [R]${utils.getCurrency(invoiceData.data?.dataInvoice?.jumlahTotal?.toLong())}\n"+
                        transferRoom+
                        "[R]-------------\n"+
                        "[R]Jumlah Bersih [R]${utils.getCurrency(jumlahBersih.toLong())}\n\n"+
                        paymentList+
                        "[R]-------------\n"+
                        "[L][R]Kembali[R]${utils.getCurrency(totalPayment - jumlahBersih.toLong())}"
            )
            printer.disconnectPrinter()
            return true
        }catch(e: java.lang.Exception){
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            return false
        }
    }

}