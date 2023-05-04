package com.ihp.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class OrderResponse(

		var isLoading: Boolean = true,

		@field:SerializedName("data")
	val data: List<DataOrderItem> = mutableListOf(),

		@field:SerializedName("length")
	val length: Int? = null,

		@field:SerializedName("state")
	val state: Boolean? = null,

		@field:SerializedName("message")
	val message: String? = null
)

data class DataOrderItem(

	@field:SerializedName("order_nilai_pajak")
	val orderNilaiPajak: Int? = null,

	@field:SerializedName("order_durasi_diterima")
	val orderDurasiDiterima: Int? = null,

	@field:SerializedName("order_notes")
	val orderNotes: String? = null,

	@field:SerializedName("order_qty_terkirim")
	val orderQtyTerkirim: Int? = null,

	@field:SerializedName("order_device")
	val orderDevice: String? = null,

	@field:SerializedName("order_user")
	val orderUser: String? = null,

	@field:SerializedName("order_quantity_temp")
	val orderQuantityTemp: Int? = null,

	@field:SerializedName("order_date_terkirim")
	val orderDateTerkirim: String? = null,

	@field:SerializedName("order_urutan")
	val orderUrutan: Int? = null,

	@field:SerializedName("order_qty_belum_terkirim")
	val orderQtyBelumTerkirim: Int? = null,

	@field:SerializedName("order_printed")
	val orderPrinted: Int? = null,

	@field:SerializedName("order_date_diterima")
	val orderDateDiterima: String? = null,

	@field:SerializedName("order_diskon_per_item")
	val orderDiskonPerItem: Int? = null,

	@field:SerializedName("order_room_rcp")
	val orderRoomRcp: String? = null,

	@field:SerializedName("order_nilai_service")
	val orderNilaiService: Int? = null,

	@field:SerializedName("order_price_include_service_pajak")
	val orderPriceIncludeServicePajak: Int? = null,

	@field:SerializedName("order_inventory_nama")
	val orderInventoryNama: String? = null,

	@field:SerializedName("order_price")
	val orderPrice: Int? = null,

	@field:SerializedName("order_total_diskon")
	val orderTotalDiskon: Int? = null,

	@field:SerializedName("order_qty_diterima")
	val orderQtyDiterima: Int? = null,

	@field:SerializedName("order_total_durasi")
	val orderTotalDurasi: Int? = null,

	@field:SerializedName("order_sol")
	val orderSol: String? = null,

	@field:SerializedName("order_date")
	val orderDate: String? = null,

	@field:SerializedName("order_durasi_terkirim")
	val orderDurasiTerkirim: Int? = null,

	@field:SerializedName("order_durasi_awal")
	val orderDurasiAwal: Int? = null,

	@field:SerializedName("order_inventory_id_global")
	val orderInventoryIdGlobal: String? = null,

	@field:SerializedName("order_quantity")
	val orderQuantity: Int? = null,

	@field:SerializedName("order_qty_cancel")
	val orderQtyCancel: Int? = null,

	@field:SerializedName("order_state")
	val orderState: String? = null,

	@field:SerializedName("order_inventory")
	val orderInventory: String? = null
)
