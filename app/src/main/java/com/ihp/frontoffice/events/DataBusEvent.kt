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
}