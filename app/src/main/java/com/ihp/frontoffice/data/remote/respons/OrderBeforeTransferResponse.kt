package com.ihp.frontoffice.data.remote.respons
import com.google.gson.annotations.SerializedName

data class OrderBeforeTransferResponse(

	@field:SerializedName("data")
	val data: List<OklBeforeTransfer>,

	@field:SerializedName("length")
	val length: Int,

	@field:SerializedName("state")
	val state: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class OklBeforeTransfer(

	@field:SerializedName("order_code")
	val orderCode: String,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("service")
	val service: Int,

	@field:SerializedName("discount")
	val discount: Int,

	@field:SerializedName("okd")
	val okd: List<OkdBeforeTransferItem> = mutableListOf(),

	@field:SerializedName("tax")
	val tax: Int,

	@field:SerializedName("room")
	val room: String
)

data class OkdBeforeTransferItem(

	@field:SerializedName("order_code")
	val orderCode: String,

	@field:SerializedName("slip_order_code")
	val soCode: String,

	@field:SerializedName("note")
	val note: String,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("qty")
	val qty: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("inventory_code")
	val inventoryCode: String
)