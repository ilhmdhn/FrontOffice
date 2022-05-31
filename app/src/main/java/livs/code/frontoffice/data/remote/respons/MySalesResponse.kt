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

	@field:SerializedName("tax_penjualan")
	val taxPenjualan: Long,

	@field:SerializedName("display_waktu")
	val displayWaktu: String,

	@field:SerializedName("total_penjualan")
	val totalPenjualan: Long,

	@field:SerializedName("charge_penjualan")
	val chargePenjualan: Long,

	@field:SerializedName("service_penjualan")
	val servicePenjualan: Long,

	@field:SerializedName("discount_penjualan")
	val discountPenjualan: Long
)
