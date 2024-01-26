package com.ihp.frontoffice.helper

import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.ihp.frontoffice.R
import com.ihp.frontoffice.data.remote.respons.*
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*

class Printer58 {
    private var printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 49f, 32)

    fun disconnectPrinter(){
        printer.disconnectPrinter()
    }

    fun testPrint(context: Context){

        printer.disconnectPrinter()
        printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 49f, 32)
//        printer = EscPosPrinter(TcpConnection("192.168.1.222", 9100, 10), 203, 49f, 32)
        printer.printFormattedTextAndCut(
            "[L]\n" +
                    "[C]PRINTER TEST PAGE\n" +
                    "[C]Printer Work Normally\n" +
                    "[C]================================\n"+
                    "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(
                R.drawable.bhktv58, DisplayMetrics.DENSITY_DEFAULT))+"</img>\n"+
                    "[L]\n\n\n"
        )
//        Thread({
//            val printerz = EscPosPrinter(TcpConnection("192.168.1.222", 9100, 15), 203, 32f, 32)
//                try {
//                    printerz.printFormattedTextAndCut(
//                        "[L]\n" +
//                                "[C]PRINTER TEST PAGE\n" +
//                                "[C]Printer Work Normally\n" +
//                                "[C]================================\n"+
//                                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(R.drawable.bhktv58, DisplayMetrics.DENSITY_DEFAULT))+"</img>\n"+
//                                "[L]\n\n\n"
//                    )
//                }catch (e: Exception){
//                    Toasty.info(context, "Error "+e.toString(), Toasty.LENGTH_LONG, true).show()
//                }finally {
//                    printerz.disconnectPrinter()
//                }
//        }).start()
    }

    fun printBill(billData: xBillResponse?, user: String, context: Context, isCopies: Boolean =  false): Boolean {
        try {
            printer.disconnectPrinter()
            printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 49f, 32)
            val selling = StringBuilder()
            val transferRoom = StringBuilder()
            val copies = StringBuilder()
            var totalTransfer = 0
            val biayaLain = StringBuilder()
            val transferBill = StringBuilder()
            val uangMuka = StringBuilder()
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val currentDate = sdf.format(Date())
            val promoRoom = StringBuilder()
            var hiddenPromo: Boolean
            var promoAndCancel: Boolean


            if (isCopies){
                copies.append("[L]Copy\n")
//                copies.append("[R]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, getDrawable(context, R.drawable.copybnw))+"</img>\n")
            }

            if (billData?.data?.dataInvoice?.overpax !=null && billData.data.dataInvoice.overpax > 0){
                biayaLain.append("[L][R]Overpax [R]${utils.getCurrency(billData.data.dataInvoice.overpax.toLong())}\n")
            }

            if (billData?.data?.dataInvoice?.diskonKamar !=null && billData.data.dataInvoice.diskonKamar > 0){
                biayaLain.append("[L][R]Diskon Kamar [R](${utils.getCurrency(billData.data.dataInvoice.diskonKamar.toLong())})\n")
            }

            if (billData?.data?.dataInvoice?.surchargeKamar !=null && billData.data.dataInvoice.surchargeKamar > 0){
                biayaLain.append("[L][R]Surcharge Kamar [R](${utils.getCurrency(billData.data.dataInvoice.surchargeKamar.toLong())})\n")
            }

            if (billData?.data?.dataInvoice?.diskonPenjualan !=null && billData.data.dataInvoice.diskonPenjualan > 0){
                biayaLain.append("[L][R]Diskon Penjualan [R](${utils.getCurrency(billData.data.dataInvoice.diskonPenjualan.toLong())})\n")
            }

            if (billData?.data?.dataInvoice?.voucher !=null && billData.data.dataInvoice.voucher > 0){
                biayaLain.append("[L][R]Voucher [R](${utils.getCurrency(billData.data.dataInvoice.voucher.toLong())})\n")
            }

            if (billData?.data?.dataInvoice?.chargeLain !=null && billData.data.dataInvoice.chargeLain > 0){
                biayaLain.append("[L][R]Charge Lain [R](${utils.getCurrency(billData.data.dataInvoice.chargeLain.toLong())})\n")
            }

            if (billData?.data?.orderData != null) {
                selling.append("[L]Rincian Penjualan\n\n")
                for (i: Int in billData.data.orderData.indices) {
                    selling.append("[L]${billData.data.orderData[i]?.namaItem}\n")
                    selling.append("[L]  ${billData.data.orderData[i]?.jumlah} x ${utils.getCurrency(billData.data.orderData[i]?.harga?.toLong())} [R] ${utils.getCurrency(billData.data.orderData[i]?.total?.toLong())}\n")
                    hiddenPromo = false
                    if(!billData.data.cancelOrderData.isNullOrEmpty()){
                        for(cc: Int in billData.data.cancelOrderData.indices){
                            if(billData.data.orderData[i]?.orderCode == billData.data.cancelOrderData[cc]?.orderCode && billData.data.orderData[i]?.inventoryCode == billData.data.cancelOrderData[cc]?.inventoryCode){
                                if(billData.data.orderData[i]?.jumlah == billData.data.cancelOrderData[cc]?.jumlah){
                                    hiddenPromo = true
                                }
                                selling.append("[L]RETUR ${billData.data.cancelOrderData[cc]?.namaItem}\n")
                                selling.append("[L]  ${billData.data.cancelOrderData[cc]?.jumlah} x ${utils.getCurrency(billData.data.cancelOrderData[cc]?.harga?.toLong())}[R](${utils.getCurrency(billData.data.cancelOrderData[cc]?.total?.toLong())})\n")
                            }
                        }
                    }

                    if(!billData.data.promoOrderData.isNullOrEmpty()){
                        promoAndCancel = false
                        for(j: Int in billData.data.promoOrderData.indices){
                            if(billData.data.orderData[i]?.orderCode == billData.data.promoOrderData[j]?.orderCode && billData.data.orderData[i]?.inventoryCode == billData.data.promoOrderData[j]?.inventoryCode){
                                if(!hiddenPromo){
                                    if(!billData.data.cancelOrderData.isNullOrEmpty()){
                                        for(cp: Int in billData.data.cancelOrderData.indices){
                                            if(billData.data.promoOrderData[j]?.orderCode == billData.data.promoOrderCancel?.get(cp)?.orderCode && billData.data.promoOrderData[j]?.inventoryCode == billData.data.promoOrderCancel?.get(cp)?.inventoryCode){
                                                promoAndCancel = true
                                                selling.append("[L]${billData.data.promoOrderData[j]?.promoName} [R](${utils.getCurrency((((billData.data.promoOrderData[j]?.promoPrice)?.toLong() ?: 0) - ((billData.data.promoOrderCancel?.get(cp)?.promoPrice)?.toLong() ?: 0)))})\n")
                                            }
                                        }
                                    }
                                    if(!promoAndCancel){
                                        selling.append("[L]${billData.data.promoOrderData[j]?.promoName} [R](${utils.getCurrency(billData.data.promoOrderData[j]?.promoPrice?.toLong())})\n")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (!billData?.data?.transferListData.isNullOrEmpty()){
                transferRoom.append("\n[C][R]Transfer Ruangan\n")
                for(i in billData?.data?.transferListData?.indices!!){
                    transferRoom.append("[C][R]${billData.data.transferListData[i]?.room} [R]${utils.getCurrency(billData.data.transferListData[i]?.transferTotal?.toLong())}\n")
                    totalTransfer += billData.data.transferListData[i]?.transferTotal!!
                }
            }

            if (!billData?.data?.transferBillData.isNullOrEmpty()){
                for (i in billData?.data?.transferBillData?.indices!!){
                    transferBill.append(transferPrint(billData.data.transferBillData[i], user))
                }
            }

            if(billData?.data?.dataInvoice?.uangMuka!! > 0){
                uangMuka.append("\n[L][R]Uang Muka [R]${utils.getCurrency(billData.data.dataInvoice.uangMuka.toLong())}\n")
            }

            if(billData.data.dataInvoice.promo!! > 0){
                promoRoom.append("[L]PROMO[R](${utils.getCurrency(billData.data.dataInvoice.promo.toLong())})\n")
            }

            printer.printFormattedText(
                "" + copies +
                        "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(
                    R.drawable.bhktv58, DisplayMetrics.DENSITY_DEFAULT))+"</img>\n"+
                        "[C]${billData.data.dataOutlet?.namaOutlet}\n" +
                        "[C]${billData.data.dataOutlet?.alamatOutlet}\n" +
                        "[C]${billData.data.dataOutlet?.kota}\n" +
                        "[C]${billData.data.dataOutlet?.telepon}\n" +
                        "\n[C]<b>TAGIHAN</b>\n\n" +
                        "[L]Ruangan : ${billData.data.dataRoom?.ruangan}\n" +
                        "[L]Nama    : ${billData.data.dataRoom?.nama}\n" +
                        "[L]Tanggal : ${billData.data.dataRoom?.tanggal}\n" +
                        "\n" +
                        "[L]Sewa Ruangan\n" +
                        "[L]${billData?.data.dataRoom?.checkin} - ${billData.data.dataRoom?.checkout}" +
                        "[R]${utils.getCurrency(billData?.data?.dataInvoice.sewaRuangan?.toLong())}\n" +
                        promoRoom+
                        "\n" +
                        selling+
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
                        uangMuka+
                        "[R]-------------\n"+
                        "[L]Jumlah Bersih [R]${utils.getCurrency(billData?.data?.dataInvoice?.jumlahBersih?.toLong())}\n"+
                        "\n[C]<font size='wide'><b>Rp.${utils.getCurrency(billData?.data?.dataInvoice?.jumlahBersih?.toLong())}</b></font>\n"+
                        "\n[R]${currentDate} ${user}\n"+
                        transferBill
            )
            return true
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Mosok kene" + e.toString(), Toast.LENGTH_SHORT).show()
            Log.e("error", e.toString())
        }
        return false
    }

    fun printInvoice(invoiceData: xInvoiceResponse, context: Context, user: String, isCopies: Boolean = false): Boolean{

        try {
            printer.disconnectPrinter()
            printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 49f, 32)
//            printer = EscPosPrinter(TcpConnection("192.168.1.222", 9100), 203, 49f, 32)
            val selling = StringBuilder()
            val sdf = SimpleDateFormat("dd/M/yyyy HH:mm")
            val currentDate = sdf.format(Date())
            val transferRoom = StringBuilder()
            val paymentList = StringBuilder()
            val copies = StringBuilder()
            val biayaLain = StringBuilder()
            val transferBill = StringBuilder()
            val promoRoom = StringBuilder()
            val uangMuka = StringBuilder()
            var hiddenPromo: Boolean
            var promoAndCancel: Boolean

            val orderData = invoiceData.data?.orderData
            val cancelOrder = invoiceData.data?.cancelOrderData
            val promoOrder = invoiceData.data?.promoOrderData
            val promoAndCancelData = invoiceData.data?.promoOrderCancel

            if (invoiceData.data?.dataInvoice?.overpax !=null && invoiceData.data.dataInvoice.overpax > 0){
                biayaLain.append("[L][R]Overpax [R]${utils.getCurrency(invoiceData.data.dataInvoice.overpax.toLong())}\n")
            }

            if (invoiceData.data?.dataInvoice?.diskonKamar !=null && invoiceData.data.dataInvoice.diskonKamar > 0){
                biayaLain.append("[L][R]Diskon Kamar [R](${utils.getCurrency(invoiceData.data.dataInvoice.diskonKamar.toLong())})\n")
            }

            if (invoiceData.data?.dataInvoice?.surchargeKamar !=null && invoiceData.data.dataInvoice.surchargeKamar > 0){
                biayaLain.append("[L][R]Surcharge Kamar [R]${utils.getCurrency(invoiceData.data.dataInvoice.surchargeKamar.toLong())}\n")
            }

            if (invoiceData.data?.dataInvoice?.diskonPenjualan !=null && invoiceData.data.dataInvoice.diskonPenjualan > 0){
                biayaLain.append("[L][R]Diskon Penjualan [R](${utils.getCurrency(invoiceData.data.dataInvoice.diskonPenjualan.toLong())})\n")
            }

            if (invoiceData.data?.dataInvoice?.voucher !=null && invoiceData.data.dataInvoice.voucher > 0){
                biayaLain.append("[L][R]Voucher [R](${utils.getCurrency(invoiceData.data.dataInvoice.voucher.toLong())})\n")
            }

            if (invoiceData.data?.dataInvoice?.chargeLain !=null && invoiceData.data.dataInvoice.chargeLain > 0){
                biayaLain.append("[L][R]Charge Lain [R]${utils.getCurrency(invoiceData.data.dataInvoice.chargeLain.toLong())}\n")
            }

            if (isCopies){
                copies.append("[L]Copy\n")
//                copies.append("[R]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, getDrawable(context, R.drawable.copybnw))+"</img>\n")
            }

            if (orderData != null) {
                selling.append("[L]Rincian Penjualan\n\n")
                for (i: Int in orderData.indices) {
                    selling.append("[L]${orderData[i]?.namaItem}\n")
                    selling.append("[L]  ${orderData[i]?.jumlah} x ${utils.getCurrency(orderData[i]?.harga?.toLong())} [R] ${utils.getCurrency(orderData[i]?.total?.toLong())}\n")
                    hiddenPromo = false
                    if (!cancelOrder.isNullOrEmpty()) {
                        for (cc: Int in cancelOrder.indices) {
                            if (orderData[i]?.orderCode == cancelOrder[cc]?.orderCode && orderData[i]?.inventoryCode == cancelOrder[cc]?.inventoryCode) {
                                if (orderData[i]?.jumlah == cancelOrder[cc]?.jumlah) {
                                    hiddenPromo = true
                                }
                                selling.append("[L]RETUR ${cancelOrder[cc]?.namaItem}\n")
                                selling.append("[L]  ${cancelOrder[cc]?.jumlah} x ${utils.getCurrency(cancelOrder[cc]?.harga?.toLong())}[R](${utils.getCurrency(cancelOrder[cc]?.total?.toLong())})\n")
                            }
                        }
                    }

                    if (!promoOrder.isNullOrEmpty()) {
                        for (j: Int in promoOrder.indices) {
                            promoAndCancel = false
                            if (orderData[i]?.orderCode == promoOrder[j]?.orderCode && orderData[i]?.inventoryCode == promoOrder[j]?.inventoryCode) {
                                if (!hiddenPromo) {
                                    if (!cancelOrder.isNullOrEmpty()) {
                                        for (cp: Int in cancelOrder.indices) {
                                            if (promoOrder[j]?.orderCode == promoAndCancelData!![cp]?.orderCode && promoOrder[j]?.inventoryCode == promoAndCancelData[cp]?.inventoryCode) {
                                                promoAndCancel = true
                                                selling.append("[L]${promoOrder[j]?.promoName} [R](${utils.getCurrency((((promoOrder[j]?.promoPrice)?.toLong() ?: 0) - ((promoAndCancelData[cp]?.promoPrice)?.toLong() ?: 0)))})\n")
                                            }
                                        }
                                    }
                                    if (!promoAndCancel) {
                                        selling.append("[L]${promoOrder[j]?.promoName} [R](${utils.getCurrency(promoOrder[j]?.promoPrice?.toLong())})\n")
                                    }
                                }
                            }
                        }
                    }
                }

            }

            if (!invoiceData.data?.transferListData.isNullOrEmpty()){
                transferRoom.append("\n[R]Transfer Ruangan\n")
                for(i in invoiceData.data?.transferListData?.indices!!){
                    transferRoom.append("[C][R]${invoiceData.data.transferListData[i]?.room} [R]${utils.getCurrency(invoiceData.data.transferListData[i]?.transferTotal?.toLong())}\n")
                }
            }

            if (invoiceData.data?.paymentList != null){
                for (i in invoiceData.data.paymentList.indices){
                    paymentList.append("[C][R]${invoiceData.data.paymentList[i].payMethod} [R]${utils.getCurrency(invoiceData.data.paymentList[i].payValue?.toLong())}\n")
                }
            }

            if (!invoiceData.data?.transferBillData.isNullOrEmpty()){
                for(i in invoiceData.data?.transferBillData?.indices!!){
                    transferBill.append(transferPrint(invoiceData.data.transferBillData[i], user))
                }
            }

            if(invoiceData.data?.dataInvoice?.promo!!>0){
                "[L]PROMO[R](${utils.getCurrency(invoiceData.data.dataInvoice.promo.toLong())})\n"
            }

            if(invoiceData.data.dataInvoice.uangMuka!! >0){
                uangMuka.append("[L][R]UANG MUKA [R]${utils.getCurrency(invoiceData.data.dataInvoice.uangMuka.toLong())}\n")
            }


            printer.printFormattedText(
                "" + copies +
                        "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(
                    R.drawable.bhktv58, DisplayMetrics.DENSITY_DEFAULT))+"</img>\n"+
                        "[L]\n" +
                        "[C]${invoiceData.data.dataOutlet?.namaOutlet}\n"+
                        "[C]${invoiceData.data.dataOutlet?.alamatOutlet}\n"+
                        "[C]${invoiceData.data.dataOutlet?.kota}\n"+
                        "[C]${invoiceData.data.dataOutlet?.telepon}\n"+
                        "\n[C]<b>INVOICE</b>\n"+
                        "\n[L]Ruangan : ${invoiceData.data.dataRoom?.ruangan}"+
                        "\n[L]Nama    : ${invoiceData.data.dataRoom?.nama}"+
                        "\n[L]Tanggal : ${invoiceData.data.dataRoom?.tanggal}"+
                        "\n\n"+
                        "[L]Sewa Ruangan\n"+
                        "[L]${invoiceData.data?.dataRoom?.checkin} - ${invoiceData.data?.dataRoom?.checkout}" +
                        "[R]${utils.getCurrency(invoiceData.data?.dataInvoice?.sewaRuangan?.toLong())}\n" +
                        promoRoom+
                        selling+
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
                        uangMuka+
                        "[R]-------------\n"+
                        "[L]Jumlah Bersih [R]${utils.getCurrency(invoiceData.data?.dataInvoice?.jumlahBersih?.toLong())}\n\n"+
                        paymentList+
                        "[R]-------------\n"+
                        "[R]${utils.getCurrency(invoiceData.data?.paymentDetail?.payValue?.toLong())}\n\n"+
                        "[L]<font size='wide'><b>Kembali[R] ${utils.getCurrency(invoiceData.data?.paymentDetail?.changeValue?.toLong())}</b></font>\n"+
                        "\n[R]${currentDate} ${user}\n"+
                        transferBill
            )
            return true
        }catch(e: java.lang.Exception){
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            return false
        }
    }

    fun transferPrint(dataTransfer: xTransferBillDataItem?, user: String): String{

        val transferData: String

        val selling = StringBuilder()
        val transferRoom = StringBuilder()
        val copies = StringBuilder()
        val biayaLain = StringBuilder()
        val uangMuka = StringBuilder()
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm")
        val currentDate = sdf.format(Date())

        var promoAndCancel: Boolean
        var hiddenPromo: Boolean

        val orderData = dataTransfer?.orderData
        val cancelOrder = dataTransfer?.cancelOrderData
        val promoOrder = dataTransfer?.promoOrderData
        val promoAndCancelData = dataTransfer?.promoOrderCancel

        if (dataTransfer?.dataInvoice?.overpax !=null && dataTransfer.dataInvoice.overpax > 0){
            biayaLain.append("[L][R]Overpax [R]${utils.getCurrency(dataTransfer.dataInvoice.overpax.toLong())}\n")
        }

        if (dataTransfer?.dataInvoice?.diskonKamar !=null && dataTransfer.dataInvoice.diskonKamar > 0){
            biayaLain.append("[L][R]Diskon Kamar [R](${utils.getCurrency(dataTransfer.dataInvoice.diskonKamar.toLong())})\n")
        }

        if (dataTransfer?.dataInvoice?.surchargeKamar !=null && dataTransfer.dataInvoice.surchargeKamar > 0){
            biayaLain.append("[L][R]Surcharge Kamar [R](${utils.getCurrency(dataTransfer.dataInvoice.surchargeKamar.toLong())})\n")
        }

        if (dataTransfer?.dataInvoice?.diskonPenjualan !=null && dataTransfer.dataInvoice.diskonPenjualan > 0){
            biayaLain.append("[L][R]Diskon Penjualan [R](${utils.getCurrency(dataTransfer.dataInvoice.diskonPenjualan.toLong())})\n")
        }

        if (dataTransfer?.dataInvoice?.voucher !=null && dataTransfer.dataInvoice.voucher > 0){
            biayaLain.append("[L][R]Voucher [R](${utils.getCurrency(dataTransfer.dataInvoice.voucher.toLong())})\n")
        }

        if (dataTransfer?.dataInvoice?.chargeLain !=null && dataTransfer.dataInvoice.chargeLain > 0){
            biayaLain.append("[L][R]Charge Lain [R](${utils.getCurrency(dataTransfer.dataInvoice.chargeLain.toLong())})\n")
        }

        if (!orderData.isNullOrEmpty()) {
            selling.append("[L]Rincian Penjualan\n\n")
            for (i: Int in orderData.indices) {
                selling.append("[L]${orderData[i]?.namaItem}\n")
                selling.append("[L]  ${orderData[i]?.jumlah} x ${utils.getCurrency(orderData[i]?.harga?.toLong())} [R]${utils.getCurrency(orderData[i]?.total?.toLong())}\n")
                hiddenPromo = false
                if(!cancelOrder.isNullOrEmpty()){
                    for(c: Int in cancelOrder.indices){
                        if(orderData[i]?.orderCode == cancelOrder[c]?.orderCode && orderData[i]?.inventoryCode == cancelOrder[c]?.inventoryCode){
                            if(orderData[i]?.orderCode == cancelOrder[c]?.orderCode && orderData[i]?.inventoryCode == cancelOrder[c]?.inventoryCode && cancelOrder[c]?.jumlah == orderData[i]?.jumlah){
                                hiddenPromo = true
                            }
                            selling.append("[L]RETUR ${cancelOrder[c]?.namaItem}\n")
                            selling.append("  ${cancelOrder[c]?.jumlah} x ${utils.getCurrency(cancelOrder[c]?.harga?.toLong())}[R](${utils.getCurrency(cancelOrder[c]?.total?.toLong())})\n")
                        }
                    }
                }

                if(!promoOrder.isNullOrEmpty()){
                    promoAndCancel = false
                    for(p: Int in promoOrder.indices){
                        if(orderData[i]?.orderCode == promoOrder[p]?.orderCode && orderData[i]?.inventoryCode == promoOrder[p]?.inventoryCode){
                            if(!hiddenPromo){
                                if(!promoAndCancelData.isNullOrEmpty()){
                                    for(cp : Int in promoAndCancelData.indices){
                                        if(promoOrder[p]?.orderCode == promoAndCancelData[cp]?.orderCode && promoOrder[p]?.inventoryCode == promoAndCancelData[cp]?.inventoryCode){
                                            promoAndCancel = true
                                            selling.append("[L]${promoOrder[p]?.promoName}[R](${utils.getCurrency((promoAndCancelData[cp]?.promoPrice?.toLong()?.let { promoOrder[p]?.promoPrice?.toLong()?.minus(it) }))})\n")
                                        }
                                    }
                                }
                                if(!promoAndCancel){
                                    selling.append("[L]${promoOrder[p]?.promoName}[R](${utils.getCurrency(promoOrder[p]?.promoPrice?.toLong())})\n")
                                }
                            }
                        }
                    }
                }
            }
        }

        if((dataTransfer?.dataInvoice?.uangMuka ?: 0) > 0){
            uangMuka.append("\n[L][R]Uang Muka [R]${utils.getCurrency(dataTransfer?.dataInvoice?.uangMuka?.toLong())}\n")
        }

        transferData = "[L]\n\n\n\n\n" +
                copies+
                "[C]${dataTransfer?.dataOutlet?.namaOutlet}\n" +
                "[C]${dataTransfer?.dataOutlet?.alamatOutlet}\n" +
                "[C]${dataTransfer?.dataOutlet?.kota}\n" +
                "[C]${dataTransfer?.dataOutlet?.telepon}\n" +
                "\n[C]<b>Transfer Room</b>\n\n" +
                "[L]Ruangan : ${dataTransfer?.dataRoom?.ruangan}\n" +
                "[L]Nama    : ${dataTransfer?.dataRoom?.nama}\n" +
                "[L]Tanggal : ${dataTransfer?.dataRoom?.tanggal}\n" +
                "\n" +
                "[L]Sewa Ruangan\n" +
                "[L]${dataTransfer?.dataRoom?.checkin} - ${dataTransfer?.dataRoom?.checkout}" +
                "[R]${utils.getCurrency(dataTransfer?.dataInvoice?.sewaRuangan?.toLong())}\n" +
                "[L]PROMO[R](${utils.getCurrency(dataTransfer?.dataInvoice?.promo?.toLong())})\n" +
                "\n" +
                selling +
                "--------------------------------\n"+
                "[L]Jumlah Ruangan [R]${utils.getCurrency(dataTransfer?.dataInvoice?.jumlahRuangan?.toLong())}\n"+
                "[L]Jumlah Penjualan [R]${utils.getCurrency(dataTransfer?.dataInvoice?.jumlahPenjualan?.toLong())}\n"+
                "--------------------------------\n"+
                "[L][R]Jumlah [R]${utils.getCurrency(dataTransfer?.dataInvoice?.jumlah?.toLong())}\n"+
                "[L][R]Service [R]${utils.getCurrency(dataTransfer?.dataInvoice?.jumlahService?.toLong())}\n"+
                "[L][R]Pajak [R]${utils.getCurrency(dataTransfer?.dataInvoice?.jumlahPajak?.toLong())}\n"+
                biayaLain+
                "[R]-------------\n"+
                "[L][R]Total [R]${utils.getCurrency(dataTransfer?.dataInvoice?.jumlahTotal?.toLong())}\n"+
                transferRoom+
                uangMuka+
                "[R]-------------\n"+
                "[L]Jumlah Bersih [R]${utils.getCurrency(dataTransfer?.dataInvoice?.jumlahBersih?.toLong())}\n"+
                "\n[L]<font size='wide'><b>Rp.${utils.getCurrency(dataTransfer?.dataInvoice?.jumlahBersih?.toLong())}</b></font>\n"+
                "\n[R]${currentDate} ${user}\n"

        return transferData
    }


    fun printerKas(Shift: Int, data: DataStatusKas, chusr: String):Boolean{
        try {
            printer.disconnectPrinter()
            printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 49f, 32)
            val sdf = SimpleDateFormat("dd/M/yyyy HH:mm")
            val currentDate = sdf.format(Date())
            printer.printFormattedText(
                "\n\n[C]\n"+
                        "[C]${data.outletName}\n"+
                        "[C]${data.outletAddress}\n"+
                        "[C]${data.outletCity}\n"+
                        "[C]${data.outletPhone}\n"+
                        "\n"+
                        "[L]Shift $Shift[R]${data.tanggal}\n"+
                        "[L]Kamar[R]${utils.getCurrency(data.jumlahNilaiKamar)}\n"+
                        "[L]Makanan & Minuman[R]${utils.getCurrency(data.makananMinuman)}\n"+
                        "[L]UM Reservasi[R]${utils.getCurrency(data.totalHutangReservasi)}\n"+
                        "[L]Pendapatan Lain[R]${utils.getCurrency(data.jumlahPendapatanLain)}\n"+
                        "[R]---------------\n"+
                        "[L]Total[R]${utils.getCurrency(data.totalPenjualan)}\n"+
                        "[R]===============\n"+
                        "[L]Poin Membership[R]${utils.getCurrency(data.jumlahPembayaranPoinMembership)}\n"+
                        "[L]E Money[R]${utils.getCurrency(data.jumlahPembayaranEmoney)}\n"+
                        "[L]Transfer[R]${utils.getCurrency(data.jumlahPembayaranTransfer)}\n"+
                        "[L]Cash[R]${utils.getCurrency(data.jumlahPembayaranCash)}\n"+
                        "[L]Credit Card[R]${utils.getCurrency(data.jumlahPembayaranCreditCard)}\n"+
                        "[L]Debet Card[R]${utils.getCurrency(data.jumlahPembayaranDebetCard)}\n"+
                        "[L]Voucher[R]${utils.getCurrency(data.jumlahPembayaranVoucher)}\n"+
                        "[L]Piutang[R]${utils.getCurrency(data.jumlahPembayaranPiutang)}\n"+
                        "[L]Entertainment[R]${utils.getCurrency(data.jumlahPembayaranComplimentary)}\n"+
                        "[R]---------------\n"+
                        "[L]Setoran[R]${utils.getCurrency(data.totalPembayaran)}\n"+
                        "[L]Total[R]${utils.getCurrency(data.totalPenjualan)}\n"+
                        "\n[R]$currentDate $chusr\n"
            )
            return true
        }catch(error: java.lang.Exception){
            return false
        }
    }

    fun printerCheckinSlip(dataCheckin: CheckinSlipResponse, context: Context){
        try{
            printer.disconnectPrinter()
            printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 49f, 32)
            val sdf = SimpleDateFormat("dd/M/yyyy HH:mm")
            val currentDate = sdf.format(Date())
            val slip = dataCheckin.data

            printer.printFormattedText(
                "\n\n\n"+
                        "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, context.getResources().getDrawableForDensity(
                    R.drawable.bhktv58, DisplayMetrics.DENSITY_DEFAULT))+"</img>\n\n"+
                        "[C]${slip?.outletInfo?.namaOutlet}\n" +
                        "[C]${slip?.outletInfo?.alamatOutlet}\n" +
                        "[C]${slip?.outletInfo?.kota}\n" +
                        "[C]${slip?.outletInfo?.telepon}\n\n"+
                        "[C]<b>CHECK-IN SLIP</b>\n\n"+
                        "[L]Ruangan : <font size='wide'><b>${slip?.checkinDetail?.roomName}</b></font>\n\n" +
                        "[L]Nama              : ${slip?.checkinDetail?.name}\n" +
                        "[L]Jumlah Tamu       : ${slip?.checkinDetail?.pax}\n" +
                        "[L]Jenis Kamar       : ${slip?.checkinDetail?.roomType}\n" +
                        "[L]Jam Check-In      : ${slip?.checkinDetail?.checkinTime}\n" +
                        "[L]Lama Sewa         : ${slip?.checkinDetail?.checkinDuration} jam\n" +
                        "[L]Jumlah Biaya Sewa : ${utils.getCurrency(slip?.checkinDetail?.checkinFee?.toLong())}\n" +
                        "[L]Jam Checkout      : ${slip?.checkinDetail?.checkoutTime}\n" +
                        "[L]--------------------------------\n"+
                        "[C]<font size='wide'><b>PERNYATAAN</b></font>\n\n"+
                        "[C]Saya & rekan tidak  akan membawa\n" +
                        "[C]masuk atau  mengkonsumsi makanan\n" +
                        "[C]atau minuman yang  bukan berasal\n"+
                        "[C]dari  outlet  HAPPY  PUPPY  ini.\n" +
                        "[C]Apabila  terbukti kemudian, saya\n" +
                        "[C]bersedia  dikenakan denda sesuai\n"+
                        "[C]daftar yang berlaku\n\n"+
                        "[L](${slip?.checkinDetail?.name})\n"+
                        "[L](${slip?.checkinDetail?.phone})\n"+
                        "[R]${currentDate}\n\n"
            )
        }catch(error: java.lang.Exception){
            Toasty.error(context, "SLIP CHECKIN ERROR ${error}", Toasty.LENGTH_SHORT, true).show()
        }
    }

    fun printSlipOrder(roomCode: String, guestName: String, pax: String, solCode: String, user: String, listSol: List<DataListSol>){
        printer.disconnectPrinter()
        printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 49f, 32)
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm")
        val time = SimpleDateFormat("HH:mm")
        val currentDate = sdf.format(Date())
        val currentTime = time.format(Date())
        val isiSo = StringBuilder()

        for(so in listSol){
            isiSo.append("[L]<font size='wide'><b>${so.qty} ${so.name}</b></font>\n")
            if(!isiSo.isEmpty()){
                isiSo.append("[L]${so.note}\n")
            }
        }

        printer.printFormattedText(
            "\n\n\n"+
                    "[C]<b>SLIP ORDER</b>\n\n"+
                    "[L]Kamar    : <font size='wide'><b>$roomCode</b></font>\n"+
                    "[L]Jam      : <font size='wide'><b>$currentTime</b></font>\n"+
                    "[L]Nama     : $guestName\n"+
                    "[L]Jml Tamu : $pax\n"+
                    "[L]No Bukti : $solCode\n"+
                    "--------------------------------\n"+
                    isiSo+
                    "--------------------------------\n"+
                    "[R]Dibuat Oleh: $user\n"
        )
    }
}