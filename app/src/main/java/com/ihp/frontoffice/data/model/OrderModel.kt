package com.ihp.frontoffice.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderModel(
        var inventoryCode: String,
        var orderQty: Int,
        var orderNotes: String,
        var itemName: String,
): Parcelable