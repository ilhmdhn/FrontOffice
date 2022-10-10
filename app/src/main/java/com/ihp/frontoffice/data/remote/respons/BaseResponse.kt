package com.ihp.frontoffice.data.remote.respons

import android.content.Context
import com.google.gson.annotations.SerializedName
import android.widget.Toast
import es.dmoral.toasty.Toasty


open class BaseResponse {
    @SerializedName("state")
    var isOkay = false

    @SerializedName("length")
    var length = 0

    @SerializedName("message")
    var message: String? = null
    fun displayMessage(context: Context?) {
        if (!isOkay) {
            if (context != null) {
                Toasty.warning(context, "Server Response : $message", Toast.LENGTH_SHORT, true).show()
            }
        }
    }
}