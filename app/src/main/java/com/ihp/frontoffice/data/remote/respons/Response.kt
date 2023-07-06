package com.ihp.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class Response(
	@SerializedName("state")
	var state: Boolean = false,
	@SerializedName("message")
	var message: String = "error",
	var isLoading: Boolean = true,
	var isBack: Boolean = false
)