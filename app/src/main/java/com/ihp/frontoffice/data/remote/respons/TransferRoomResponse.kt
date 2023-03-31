package com.ihp.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class TransferRoomResponse(

	@field:SerializedName("data")
	val data: tfData? = null,

	@field:SerializedName("state")
	val state: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class tfData(

	@field:SerializedName("kode_rcp")
	val kodeRcp: String? = null,

	@field:SerializedName("room")
	val room: String? = null
)
