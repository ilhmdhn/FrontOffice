package com.ihp.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class xBillResponse(

    @field:SerializedName("data")
    val data: xData? = null,

    @field:SerializedName("state")
    val state: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class xCancelOrderDataItem(

    @field:SerializedName("order_code")
    val orderCode: String? = null,

    @field:SerializedName("cancel_order_code")
    val cancelOrderCode: String? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("inventory_code")
    val inventoryCode: String? = null,

    @field:SerializedName("jumlah")
    val jumlah: Int? = null,

    @field:SerializedName("nama_item")
    val namaItem: String? = null
)

data class xPromoOrderDataItem(

    @field:SerializedName("order_code")
    val orderCode: String? = null,

    @field:SerializedName("promo_name")
    val promoName: String? = null,

    @field:SerializedName("promo_price")
    val promoPrice: Int? = null,

    @field:SerializedName("inventory_code")
    val inventoryCode: String? = null
)

data class xDataInvoice(

    @field:SerializedName("sewa_ruangan")
    val sewaRuangan: Int? = null,

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

    @field:SerializedName("uang_muka")
    val uangMuka: Int? = null,

    @field:SerializedName("promo")
    val promo: Int? = null,

    @field:SerializedName("jumlah_pajak")
    val jumlahPajak: Int? = null,

    @field:SerializedName("room_service")
    val roomService: Int? = null,

    @field:SerializedName("room_tax")
    val roomTax: Int? = null,

    @field:SerializedName("fnb_service")
    val fnbService: Int? = null,

    @field:SerializedName("fnb_tax")
    val fnbTax: Int? = null,

    @field:SerializedName("transfer")
    val transfer: String? = null,

    @field:SerializedName("jumlah_total")
    val jumlahTotal: Int? = null,

    @field:SerializedName("jumlah")
    val jumlah: Int? = null,

    @field:SerializedName("jumlah_bersih")
    val jumlahBersih: Int? = null,

    @field:SerializedName("status_print")
    val statusPrint: String? = null,

    @field:SerializedName("jumlah_penjualan")
    val jumlahPenjualan: Int? = null
)

data class xData(

    @field:SerializedName("transferBillData")
    val transferBillData: List<xTransferBillDataItem?>? = null,

    @field:SerializedName("cancelOrderData")
    val cancelOrderData: List<xCancelOrderDataItem?>? = null,

    @field:SerializedName("dataRoom")
    val dataRoom: xDataRoom? = null,

    @field:SerializedName("transferListData")
    val transferListData: List<xTransferListDataItem?>? = null,

    @field:SerializedName("orderData")
    val orderData: List<xOrderDataItem?>? = null,

    @field:SerializedName("promoOrderCancel")
    val promoOrderCancel: List<xPromoOrderCancelItem?>? = null,

    @field:SerializedName("promoOrderData")
    val promoOrderData: List<xPromoOrderDataItem?>? = null,

    @field:SerializedName("dataOutlet")
    val dataOutlet: xDataOutlet? = null,

    @field:SerializedName("dataInvoice")
    val dataInvoice: xDataInvoice? = null,

    @field:SerializedName("service_percent")
    val serviceTax: taxServiceModel
)

data class xDataOutlet(

    @field:SerializedName("kota")
    val kota: String? = null,

    @field:SerializedName("telepon")
    val telepon: String? = null,

    @field:SerializedName("alamat_outlet")
    val alamatOutlet: String? = null,

    @field:SerializedName("nama_outlet")
    val namaOutlet: String? = null
)

data class xTransferListDataItem(

    @field:SerializedName("transferTotal")
    val transferTotal: Int? = null,

    @field:SerializedName("room")
    val room: String? = null
)

data class xTransferBillDataItem(

    @field:SerializedName("cancelOrderData")
    val cancelOrderData: List<xCancelOrderDataItem?>? = null,

    @field:SerializedName("dataRoom")
    val dataRoom: xDataRoom? = null,

    @field:SerializedName("orderData")
    val orderData: List<xOrderDataItem?>? = null,

    @field:SerializedName("promoOrderCancel")
    val promoOrderCancel: List<xPromoOrderCancelItem?>? = null,

    @field:SerializedName("promoOrderData")
    val promoOrderData: List<xPromoOrderDataItem?>? = null,

    @field:SerializedName("dataOutlet")
    val dataOutlet: xDataOutlet? = null,

    @field:SerializedName("dataInvoice")
    val dataInvoice: xDataInvoice? = null,

    @field:SerializedName("service_percent")
    val serviceTax: taxServiceModel
)

data class xOrderDataItem(

    @field:SerializedName("order_code")
    val orderCode: String? = null,

    @field:SerializedName("total")
    val total: Int? = null,

    @field:SerializedName("jumlah")
    val jumlah: Int? = null,

    @field:SerializedName("harga")
    val harga: Int? = null,

    @field:SerializedName("nama_item")
    val namaItem: String? = null,

    @field:SerializedName("inventory_code")
    val inventoryCode: String? = null
)

data class xPromoOrderCancelItem(

    @field:SerializedName("order_code")
    val orderCode: String? = null,

    @field:SerializedName("order_cancel_code")
    val orderCancelCode: String? = null,

    @field:SerializedName("promo_name")
    val promoName: String? = null,

    @field:SerializedName("promo_price")
    val promoPrice: Int? = null,

    @field:SerializedName("inventory_code")
    val inventoryCode: String? = null
)

data class xDataRoom(

    @field:SerializedName("ruangan")
    val ruangan: String? = null,

    @field:SerializedName("room_code")
    val roomCode: String? = null,

    @field:SerializedName("Checkout")
    val checkout: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("Checkin")
    val checkin: String? = null,

    @field:SerializedName("tanggal")
    val tanggal: String? = null
)
data class taxServiceModel(

    @field:SerializedName("service_room_percent")
    val roomServicePercent: Int = 0,

    @field:SerializedName("tax_room_percent")
    val roomTaxPercent: Int = 0,

    @field:SerializedName("service_fnb_percent")
    val fnbServicePercent: Int = 0,

    @field:SerializedName("tax_fnb_percent")
    val fnbTaxPercent: Int = 0,
)
