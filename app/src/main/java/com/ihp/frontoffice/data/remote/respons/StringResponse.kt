package com.ihp.frontoffice.data.remote.respons

import com.google.gson.annotations.SerializedName

data class StringResponse (
    @field:SerializedName("data")
    val data: String = "",

    @field:SerializedName("state")
    val state: Boolean = false,

    @field:SerializedName("message")
    val message: String = ""
)