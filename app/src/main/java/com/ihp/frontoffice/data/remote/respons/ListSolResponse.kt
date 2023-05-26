package com.ihp.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class ListSolResponse(

	@field:SerializedName("data")
	val data: List<DataListSol> = mutableListOf(),

	@field:SerializedName("state")
	val state: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class DataListSol(

	@field:SerializedName("note")
	val note: String,

	@field:SerializedName("qty")
	val qty: Int,

	@field:SerializedName("name")
	val name: String
)
