package com.ihp.frontoffice.helper

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.ihp.frontoffice.data.remote.respons.PrintBillDataResponse
import com.ihp.frontoffice.events.EventsWrapper.PrintBillInvoice
import java.text.SimpleDateFormat
import java.util.*

class Printer {
    @SuppressLint("SimpleDateFormat")
    fun printBill(billData: PrintBillDataResponse?, user: String, context: Context): Boolean {
        try {
            val selling = StringBuilder()
            val cancelOrder = StringBuilder()
            val promoFnB = StringBuilder()
            val transferRoom = StringBuilder()
            var totalTransfer: Double = 0.0
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
            val currentDate = sdf.format(Date())
            val printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32)

            if (billData?.data?.orderData != null) {
                for (i: Int in billData.data.orderData.indices) {
                    selling.append("[L]${billData.data.orderData[i]?.namaItem}\n")
                    selling.append("[L]${billData.data.orderData[i]?.jumlah} x ${billData.data.orderData[i]?.harga} [R] ${billData.data.orderData[i]?.total}\n")
                }
            } else {
                selling.append("")
            }

            if (billData?.data?.cancelOrderData != null) {
                    cancelOrder.append("\n")
                for (i in billData.data.cancelOrderData.indices) {
                    cancelOrder.append("[L]RETURN ${billData.data.cancelOrderData[i]?.namaItem}\n")
                    cancelOrder.append("[L]${billData.data.cancelOrderData[i]?.jumlah} x ${billData.data.cancelOrderData[i]?.harga} [R] ${billData.data.cancelOrderData[i]?.total}\n")
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
                    transferRoom.append("[C][R]${billData.data.transferListData[i]?.room} [R]${billData.data.transferListData[i]?.transferTotal}\n")
                    totalTransfer += billData.data.transferListData[i]?.transferTotal!!
                }
            }else{
                promoFnB.append("")
            }
            val jumlahBersih = totalTransfer + (if(billData?.data?.dataInvoice?.jumlahTotal==null) 0 else billData.data.dataInvoice.jumlahTotal) - (if(billData?.data?.dataInvoice?.uangMuka==null) 0 else billData.data.dataInvoice.uangMuka)
            printer.printFormattedText(
                "[L]\n" +
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
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Mosok kene" + e.toString(), Toast.LENGTH_SHORT).show()
            Log.e("error", e.toString())
        }
        return true
    }

//    fun printInvoice(invoiceData: )
}