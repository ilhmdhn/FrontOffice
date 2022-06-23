package livs.code.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class SaleItemsbyNameResponse(

	@field:SerializedName("data")
	val data: List<SalesItembyName>,

	@field:SerializedName("length")
	val length: Int? = null,

	@field:SerializedName("state")
	val state: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class SalesItembyName(

	@field:SerializedName("jumlah")
	val jumlah: Int? = null,

	@field:SerializedName("nama_item")
	val namaItem: String? = null,

	@field:SerializedName("tanggal")
	val tanggal: String? = null,

	@field:SerializedName("so")
	val so: String? = null,

	@field:SerializedName("room")
	val room: String? = null
)
