package com.ihp.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class InventoryPagingResponse(

	@field:SerializedName("data")
	val data: List<DataInventoryPaging>,

	@field:SerializedName("length")
	val length: Int,

	@field:SerializedName("state")
	val state: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataInventoryPaging(

	@field:SerializedName("inventory_global_id")
	val inventoryGlobalId: String,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("location")
	val location: Int,

	@field:SerializedName("inventory_code")
	val inventoryCode: String
)
