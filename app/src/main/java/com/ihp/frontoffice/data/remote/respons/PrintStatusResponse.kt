package com.ihp.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class PrintStatusResponse(

	@field:SerializedName("data")
	val data: PrintStatusResult = PrintStatusResult(),

	@field:SerializedName("state")
	val state: Boolean = false,

	@field:SerializedName("message")
	val message: String = "gagal"
)

data class PrintStatusResult(

	@field:SerializedName("print")
	val print: String = "no data"
)
