package com.ihp.frontoffice.data.remote.respons

data class OrderResponse(
        val data: List<DataOrderItem> = mutableListOf(),
        val length: Int? = null,
        val state: Boolean? = null,
        val message: String? = null,
        var isLoading: Boolean = true
)

data class DataOrderItem(
    val orderNilaiPajak: Int? = null,
    val orderDurasiDiterima: Int? = null,
    val orderNotes: String? = null,
    val orderQtyTerkirim: Int? = null,
    val orderDevice: String? = null,
    val orderUser: String? = null,
    val orderQuantityTemp: Int? = null,
    val orderDateTerkirim: String? = null,
    val orderUrutan: Int? = null,
    val orderQtyBelumTerkirim: Int? = null,
    val orderPrinted: Int? = null,
    val orderDateDiterima: String? = null,
    val orderDiskonPerItem: Int? = null,
    val orderRoomRcp: String? = null,
    val orderNilaiService: Int? = null,
    val orderPriceIncludeServicePajak: Int? = null,
    val orderInventoryNama: String? = null,
    val orderPrice: Int? = null,
    val orderTotalDiskon: Int? = null,
    val orderQtyDiterima: Int? = null,
    val orderTotalDurasi: Int? = null,
    val orderSol: String? = null,
    val orderDate: String? = null,
    val orderDurasiTerkirim: Int? = null,
    val orderDurasiAwal: Int? = null,
    val orderInventoryIdGlobal: String? = null,
    val orderQuantity: Int? = null,
    val orderQtyCancel: Int? = null,
    val orderState: String? = null,
    val orderInventory: String? = null
)