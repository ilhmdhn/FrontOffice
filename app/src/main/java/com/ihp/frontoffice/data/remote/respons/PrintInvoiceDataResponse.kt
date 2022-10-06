package com.ihp.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class PrintInvoiceDataResponse(

    @field:SerializedName("data")
    val data: DataPrintInvoice? = null,

    @field:SerializedName("state")
    val state: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class CancelOrderDataItemInvoice(

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("jumlah")
    val jumlah: Int? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("nama_item")
    val namaItem: String? = null
)

data class DataRoom(

    @field:SerializedName("ruangan")
    val ruangan: String? = null,

    @field:SerializedName("Checkout")
    val checkout: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("Checkin")
    val checkin: String? = null,

    @field:SerializedName("tanggal")
    val tanggal: String? = null
)

data class DataInvoice(

    @field:SerializedName("sewa_ruangan")
    val sewaRuangan: Double? = null,

    @field:SerializedName("surcharge_kamar")
    val surchargeKamar: Int? = null,

    @field:SerializedName("diskon_kamar")
    val diskonKamar: Int? = null,

    @field:SerializedName("diskon_penjualan")
    val diskonPenjualan: Int? = null,

    @field:SerializedName("voucher")
    val voucher: Int? = null,

    @field:SerializedName("charge_lain")
    val chargeLain: Int? = null,

    @field:SerializedName("overpax")
    val overpax: Int? = null,

    @field:SerializedName("jumlah_ruangan")
    val jumlahRuangan: Int? = null,

    @field:SerializedName("jumlah_service")
    val jumlahService: Int? = null,

    @field:SerializedName("promo")
    val promo: Double? = null,

    @field:SerializedName("jumlah_pajak")
    val jumlahPajak: Int? = null,

    @field:SerializedName("transfer")
    val transfer: String? = null,

    @field:SerializedName("jumlah_total")
    val jumlahTotal: Int? = null,

    @field:SerializedName("jumlah")
    val jumlah: Int? = null,

    @field:SerializedName("jumlah_bersih")
    val jumlahBersih: Int? = null,

    @field:SerializedName("jumlah_penjualan")
    val jumlahPenjualan: Int? = null
)

data class DataPrintInvoice(

    @field:SerializedName("cancelOrderData")
    val cancelOrderData: List<CancelOrderDataItemInvoice?>? = null,

    @field:SerializedName("dataRoom")
    val dataRoom: DataRoom? = null,

    @field:SerializedName("paymentData")
    val paymentData: List<PaymentDataItem?>? = null,

    @field:SerializedName("orderData")
    val orderData: List<OrderDataItemInvoice?>? = null,

    @field:SerializedName("promoOrderData")
    val promoOrderData: PromoOrderDataInvoice? = null,

    @field:SerializedName("dataOutlet")
    val dataOutlet: DataOutletInvoice? = null,

    @field:SerializedName("dataInvoice")
    val dataInvoice: DataInvoice? = null,
    
    @field:SerializedName("transferListData")
    val transferListData: List<TransferListDataItemInvoice?>? = null,
)

data class TransferListDataItemInvoice(

    @field:SerializedName("transferTotal")
    val transferTotal: Int? = null,

    @field:SerializedName("room")
    val room: String? = null
)

data class PromoOrderDataInvoice(
    @field:SerializedName("promo")
    val promo: String? = null,

    @field:SerializedName("total_promo")
    val totalPromo: Int? = null,
)

data class DataOutletInvoice(

    @field:SerializedName("kota")
    val kota: String? = null,

    @field:SerializedName("telepon")
    val telepon: String? = null,

    @field:SerializedName("alamat_outlet")
    val alamatOutlet: String? = null,

    @field:SerializedName("nama_outlet")
    val namaOutlet: String? = null
)

data class PaymentDataItem(

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("nama_payment")
    val namaPayment: String? = null
)

data class OrderDataItemInvoice(

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("jumlah")
    val jumlah: Int? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("nama_item")
    val namaItem: String? = null
)
