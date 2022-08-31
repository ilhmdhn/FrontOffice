package com.ihp.frontoffice.data.remote.respons

import android.content.Context
import com.google.gson.annotations.SerializedName
import android.widget.Toast


open class BaseResponse {
    @SerializedName("state")
    var isOkay = false

    @SerializedName("length")
    var length = 0

    @SerializedName("message")
    var message: String? = null
    fun displayMessage(context: Context?) {
        if (!isOkay) {
            if (context != null){
                Toast.makeText(context, "Server Response : $message", Toast.LENGTH_SHORT).show()
            }
        }
    }
}