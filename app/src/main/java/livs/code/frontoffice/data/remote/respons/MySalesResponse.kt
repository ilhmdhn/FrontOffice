package livs.code.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class MySalesResponse(

	@field:SerializedName("data")
	val data: List<DataItemSales>,

	@field:SerializedName("length")
	val length: Int,

	@field:SerializedName("state")
	val state: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataItemSales(

	@field:SerializedName("display_waktu")
	val displayWaktu: String,

	@field:SerializedName("total")
	val total: Long

)
