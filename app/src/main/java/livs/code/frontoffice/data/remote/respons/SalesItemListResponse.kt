package livs.code.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class SalesItemListResponse(

	@field:SerializedName("data")
	val data: List<SaleItemList>? = mutableListOf(),

	@field:SerializedName("length")
	val length: Int = 0,

	@field:SerializedName("state")
	val state: Boolean,

	@field:SerializedName("message")
	val message: String = ""
)

data class SaleItemList(

	@field:SerializedName("slip_order")
	val slipOrer: String = "",

	@field:SerializedName("DATE")
	val date: String = "",

	@field:SerializedName("Qty")
	val jumlah: Int = 0,

	@field:SerializedName("Total")
	val total: Int = 0,

	@field:SerializedName("nama_item")
	val namaItem: String = "",

	@field:SerializedName("inventory")
	val inventory: String = ""
)
