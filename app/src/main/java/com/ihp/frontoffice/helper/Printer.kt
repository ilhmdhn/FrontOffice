package com.ihp.frontoffice.helper

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.ihp.frontoffice.data.remote.respons.DataPrintTransfer
import com.ihp.frontoffice.data.remote.respons.PrintBillDataResponse
import com.ihp.frontoffice.data.remote.respons.PrintInvoiceDataResponse
import com.ihp.frontoffice.data.repository.IhpRepository
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
class Printer {

    private var printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32)

    fun disconnectPrinter(){
        printer.disconnectPrinter()
    }

    fun testPrint(){
        printer.disconnectPrinter()
        printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32)
        printer.printFormattedText(
//                "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(printer, this.getApplicationContext().getResources().getDrawableForDensity(R.drawable.ic_baseline_fastfood_24, DisplayMetrics.DENSITY_MEDIUM))+"</img>\n" +
            "[L]\n" +
                    "[C]================================\n"+
                    "[C]<u><font size='big'>ORDER N°045</font></u>\n" +
                    "[L]\n" +
                    "[L]\n" +
                    "[L]<b>BEAUTIFUL SHIRT</b>[R]9.99e\n" +
                    "[L]  + Size : S\n" +
                    "[L]\n" +
                    "[L]<b>AWESOME HAT</b>[R]24.99e\n" +
                    "[L]  + Size : 57/58\n" +
                    "[L]\n" +
                    "[C]--------------------------------\n" +
                    "[R]TOTAL PRICE :[R]34.98e\n" +
                    "[R]TAX :[R]4.23e\n" +
                    "[L]\n" +
                    "[C]================================\n" +
                    "[L]\n" +
                    "[L]<font size='tall'>Customer :</font>\n" +
                    "[L]Raymond DUPONT\n" +
                    "[L]5 rue des girafes\n" +
                    "[L]31547 PERPETES\n" +
                    "[L]Tel : +33801201456\n" +
                    "[L]\n" +
                    "[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
                    "[C]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>"
        )
    }

    fun printBill(billData: PrintBillDataResponse?, user: String, context: Context, isCopies: Boolean =  false): Boolean {
        try {
            printer.disconnectPrinter()
            printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32)
            val selling = StringBuilder()
            val cancelOrder = StringBuilder()
            val promoFnB = StringBuilder()
            val transferRoom = StringBuilder()
            val copies = StringBuilder()
            var totalTransfer = 0
            val biayaLain = StringBuilder()
            val transferBill = StringBuilder()
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
            val currentDate = sdf.format(Date())

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
                for (i: Int in billData.data.orderData.indices) {
                    selling.append("[L]${billData.data.orderData[i]?.namaItem}\n")
                    selling.append("[L]${billData.data.orderData[i]?.jumlah} x ${utils.getCurrency(billData.data.orderData[i]?.harga?.toLong())} [R] ${utils.getCurrency(billData.data.orderData[i]?.total?.toLong())}\n")
                }
            }
            if (billData?.data?.cancelOrderData != null) {
                    cancelOrder.append("\n")
                for (i in billData.data.cancelOrderData.indices) {
                    cancelOrder.append("[L]RETUR ${billData.data.cancelOrderData[i]?.namaItem}\n")
                    cancelOrder.append("[L]${billData.data.cancelOrderData[i]?.jumlah} x ${utils.getCurrency(billData.data.cancelOrderData[i]?.harga?.toLong())}[R](${utils.getCurrency(billData.data.cancelOrderData[i]?.total?.toLong())})\n")
                }
            }

            if (billData?.data?.promoOrderData != null) {
                promoFnB.append("\n[L]${billData.data.promoOrderData.promo}[R](${utils.getCurrency(billData.data.promoOrderData.totalPromo?.toLong())})\n")
            }

            if (billData?.data?.transferListData != null){
                transferRoom.append("\n[C][R]Transfer Ruangan\n")
                for(i in billData.data.transferListData.indices){
                    transferRoom.append("[C][R]${billData.data.transferListData[i]?.room} [R]${utils.getCurrency(billData.data.transferListData[i]?.transferTotal?.toLong())}\n")
                    totalTransfer += billData.data.transferListData[i]?.transferTotal!!
                }
            }

            if (billData?.data?.dataBillTransferOnBIll != null){
                for (i in billData.data.dataBillTransferOnBIll.indices){
                    transferBill.append(transferPrint(billData.data.dataBillTransferOnBIll[i], user))
                }
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
                        "[L]PROMO[R](${utils.getCurrency(billData?.data?.dataInvoice?.promo?.toLong())})\n" +
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
                        "\n[R]${currentDate} ${user}"+
                        transferBill
            )
            return true
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, "Mosok kene" + e.toString(), Toast.LENGTH_SHORT).show()
            Log.e("error", e.toString())
        }
        return false
    }

    fun printInvoice(invoiceData: PrintInvoiceDataResponse, context: Context, user: String, isCopies: Boolean = false): Boolean{

        try {
            printer.disconnectPrinter()
            printer = EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(), 203, 48f, 32)
            val selling = StringBuilder()
            val cancelOrder = StringBuilder()
            val promoFnB = StringBuilder()
            val transferRoom = StringBuilder()
            val paymentList = StringBuilder()
            var totalTransfer = 0
            val copies = StringBuilder()
            val biayaLain = StringBuilder()
            val transferBill = StringBuilder()
            var totalPayment = 0

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

            if (invoiceData.data?.orderData != null){
                for (i in invoiceData.data.orderData.indices){
                    selling.append("[L]${invoiceData.data.orderData[i]?.namaItem}\n")
                    selling.append("[L]${invoiceData.data.orderData[i]?.jumlah} x ${utils.getCurrency(invoiceData.data.orderData[i]?.harga?.toLong())} [R] ${utils.getCurrency(invoiceData.data.orderData[i]?.total?.toLong())}\n")
                }
            }

            if (invoiceData.data?.cancelOrderData != null){
                cancelOrder.append("\n")
                for (i in invoiceData.data.cancelOrderData.indices){
                    cancelOrder.append("[L]Return ${invoiceData.data.cancelOrderData[i]?.namaItem}\n")
                    cancelOrder.append("[L]${invoiceData.data.cancelOrderData[i]?.jumlah} x ${utils.getCurrency(invoiceData.data.cancelOrderData[i]?.harga?.toLong())} [R](${utils.getCurrency(invoiceData.data.cancelOrderData[i]?.total?.toLong())})\n")
                }
            }

            if (invoiceData.data?.promoOrderData != null){
                promoFnB.append("\n[L]${invoiceData.data.promoOrderData.promo}[R](${invoiceData.data.promoOrderData.totalPromo})\n")
            }

            if (invoiceData.data?.transferListData != null){
                transferRoom.append("\n[C]Transfer Ruangan[R]\n")
                for(i in invoiceData.data.transferListData.indices){
                    transferRoom.append("[C][R]${invoiceData.data.transferListData[i]?.room} [R]${utils.getCurrency(invoiceData.data.transferListData[i]?.transferTotal?.toLong())}\n")
                    totalTransfer += invoiceData.data.transferListData[i]?.transferTotal!!
                }
            }

            if (invoiceData.data?.paymentData != null){
                for (i in invoiceData.data.paymentData.indices){
                    paymentList.append("[R]${invoiceData.data.paymentData[i]?.namaPayment} [R]${utils.getCurrency(invoiceData.data.paymentData[i]?.total?.toLong())}\n")
                    totalPayment+=invoiceData.data.paymentData[i]?.total!!
                }
            }

            if (invoiceData.data?.dataBillTransferOnInvoice != null){
                for(i in invoiceData.data.dataBillTransferOnInvoice.indices){
                    transferBill.append(transferPrint(invoiceData.data.dataBillTransferOnInvoice[i], user))
                }
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
                        "[L]Sewa Ruangan\n"+
                        "[L]${invoiceData.data?.dataRoom?.checkin} - ${invoiceData.data?.dataRoom?.checkout}" +
                        "[R]${utils.getCurrency(invoiceData.data?.dataInvoice?.sewaRuangan?.toLong())}\n" +
                        "[L]PROMO[R](${utils.getCurrency(invoiceData.data?.dataInvoice?.promo?.toLong())})\n" +
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
                        "[L]<font size='wide'><b>Kembali ${utils.getCurrency(totalPayment - jumlahBersih.toLong())}</b></font>"+
                        transferBill
            )
            return true
        }catch(e: java.lang.Exception){
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show()
            return false
        }
    }

    fun transferPrint(dataTransfer: DataPrintTransfer?, user: String): String{

        val transferData: String

        val selling = StringBuilder()
        val cancelOrder = StringBuilder()
        val promoFnB = StringBuilder()
        val transferRoom = StringBuilder()
        val copies = StringBuilder()
        var totalTransfer = 0
        val biayaLain = StringBuilder()
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
        val currentDate = sdf.format(Date())

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

        if (dataTransfer?.orderData != null) {
            for (i: Int in dataTransfer.orderData.indices) {
                selling.append("[L]${dataTransfer.orderData[i]?.namaItem}\n")
                selling.append("[L]${dataTransfer.orderData[i]?.jumlah} x ${utils.getCurrency(
                    dataTransfer.orderData[i]?.harga?.toLong())} [R] ${utils.getCurrency(
                    dataTransfer.orderData[i]?.total?.toLong())}\n")
            }
        }
        if (dataTransfer?.cancelOrderData != null) {
            cancelOrder.append("\n")
            for (i in dataTransfer.cancelOrderData.indices) {
                cancelOrder.append("[L]RETUR ${dataTransfer.cancelOrderData[i]?.namaItem}\n")
                cancelOrder.append("[L]${dataTransfer.cancelOrderData[i]?.jumlah} x ${utils.getCurrency(
                    dataTransfer.cancelOrderData[i]?.harga?.toLong())}[R](${utils.getCurrency(
                    dataTransfer.cancelOrderData[i]?.total?.toLong())})\n")
            }
        }

        if (dataTransfer?.promoOrderData != null) {
            promoFnB.append("\n[L]${dataTransfer.promoOrderData.promo}[R](${utils.getCurrency(dataTransfer.promoOrderData.totalPromo?.toLong())})\n")
        }

        if (dataTransfer?.transferListData != null){
            transferRoom.append("\n[C][R]Transfer Ruangan\n")
            for(i in dataTransfer.transferListData.indices){
                transferRoom.append("[C][R]${dataTransfer.transferListData[i]?.room} [R]${utils.getCurrency(dataTransfer.transferListData[i]?.transferTotal?.toLong())}\n")
                totalTransfer += dataTransfer.transferListData[i]?.transferTotal!!
            }
        }
        val jumlahBersih = totalTransfer + (if(dataTransfer?.dataInvoice?.jumlahTotal==null) 0 else dataTransfer.dataInvoice.jumlahTotal) - (if(dataTransfer?.dataInvoice?.uangMuka==null) 0 else dataTransfer.dataInvoice.uangMuka)

        transferData = "[L]\n\n\n\n\n" +
                copies+
                "[C]${dataTransfer?.dataOutlet?.namaOutlet}\n" +
                "[C]${dataTransfer?.dataOutlet?.alamatOutlet}\n" +
                "[C]${dataTransfer?.dataOutlet?.kota}\n" +
                "[C]${dataTransfer?.dataOutlet?.telepon}\n" +
                "\n[C]Transfer Room\n\n" +
                "[L]Ruangan : ${dataTransfer?.dataRoom?.ruangan}\n" +
                "[L]Nama    : ${dataTransfer?.dataRoom?.nama}\n" +
                "[L]Tanggal : ${dataTransfer?.dataRoom?.tanggal}\n" +
                "\n" +
                "[L]Sewa Ruangan\n" +
                "[L]${dataTransfer?.dataRoom?.checkin} - ${dataTransfer?.dataRoom?.checkout}" +
                "[R]${utils.getCurrency(dataTransfer?.dataInvoice?.sewaRuangan?.toLong())}\n" +
                "[L]PROMO[R](${utils.getCurrency(dataTransfer?.dataInvoice?.promo?.toLong())})\n" +
                "\n" +
                "[L]Rincian Penjualan\n" +
                selling +
                cancelOrder +
                promoFnB+
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
                "\n[L][R]Uang Muka [R]${utils.getCurrency(dataTransfer?.dataInvoice?.uangMuka?.toLong())}\n"+
                "[R]-------------\n"+
                "[L]Jumlah Bersih [R]${utils.getCurrency(jumlahBersih.toLong())}\n"+
                "\n[L]<font size='wide'><b>Rp.${utils.getCurrency(jumlahBersih.toLong())}</b></font>\n"+
                "\n[R]${currentDate} ${user}"

        return transferData
    }

    fun printerKas():Boolean{
        try {
            return true
        }catch(error: java.lang.Exception){
            return false
        }
    }
}