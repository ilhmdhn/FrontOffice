package com.ihp.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class CancelItemsResponse(

	@field:SerializedName("state")
	val state: Boolean? = null,


	@field:SerializedName("data")
	val data: List<DataCancelItems>,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataCancelItems(

	@field:SerializedName("jumlah_item")
	val jumlahItem: Int? = null,

	@field:SerializedName("jam")
	val jam: String? = null,

	@field:SerializedName("nama_item")
	val namaItem: String? = null,

	@field:SerializedName("kamar")
	val kamar: String? = null,

	@field:SerializedName("nama_tamu")
	val namaTamu: String? = null,

	@field:SerializedName("chusr")
	val chusr: String? = null
)
