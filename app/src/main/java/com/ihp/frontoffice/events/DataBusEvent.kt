package com.ihp.frontoffice.events

object DataBusEvent {
    data class approvalResponse(
        val user: String,
        val password: String,
        val isApprove: Boolean,
    )

    data class OrderModel(
            var inventoryCode: String,
            var orderQty: Int,
            var orderNotes: String,
            var itemName: String,
            var itemPrice: Long,
            var itemLocation: String,
    )

    data class cancelOrder(
            var so: String,
            var inventoryCode: String,
            var qty: String,
            var rcp: String,
            var user: String,
            var android: String
    )

    data class revisiOrder(
            var so: String,
            var inventoryCode: String,
            var note: String,
            var qty: String,
            var qtyTemp: String,
            var rcp: String,
            var user: String,
            var android: String
    )

    data class soDo(
            var message: String
    )
}